package com.example.newyorklist

//import android.R

import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.newyorklist.data.DatabaseHandler
import com.example.newyorklist.data.LoadDataFromUrl
import com.example.newyorklist.data.NewYorkJson
import kotlinx.coroutines.*
import com.example.newyorklist.data.Review

import java.util.*
import kotlin.coroutines.CoroutineContext


/*
кхм...итак здрасте. В прошлый раз попытка выполнить тестовый провалилась. Я уперся в то, что не смог сделать автоподгрузку в RecyclerView. И в попытках ее прикрутить превратил приложение в полное гавно. куча разных модулей, куча закомментированного кода...треш короче. Это очередная попытка сделать это. Начнем от противного - сделать таки автоподгрузку. И я ее таки сделал!
 */
/*
 *
Тестовое задание для Android-разработчика.
Требуется создать приложение для поиска и просмотра обзоров по фильмам, работающее под ОС Android, начиная с версии 5.0 (API v.21).
Приложение состоит из двух экранов: 1 - список с кратким описанием отзывов и полем для поиска, 2 - экран с отзывом.
Данные нужно получать с New York Times Api, обращаясь к reviews/search.json.
Сценарий работы:
На первом экране в тулбаре находится поле для ввода поискового запроса. По мере ввода текста на сервер автоматически отправляется запрос на поиск обзоров. Результаты запроса помещаются в БД и отображаются на экране в списке.
При прокручивании списка вниз должна подгружаться следующая страница данных из api (используя параметр offset)
Каждый элемент списка содержит изображение, заголовок, кнопку "Подробнее". При нажатии на кнопку "Подробнее" открывается экран с подробным описанием отзыва.
Экран с подробным описанием отзыва получает id обзора с предыдущего экрана и достает информацию из БД по этому id. В UI необходимо отобразить изображение, заголовок, текст обзора, дату публикации.
Детали:

Запрос к API должен выполняться со случайным замедлением (с точки зрения пользователя) на время от 0 до 5 секунд
Приложение должно продолжать нормально работать и сохранять состояние UI при повороте экрана
Обращение к сети и БД в UI потоке запрещены
Архитектура приложения, дизайн, обработка ошибок получения данных - на Ваш вкус
Разрешено использование любых инструментов/библиотек
Код должен быть аккуратным и понятным другим людям с Вашей точки зрения
Код проекта должен быть расположен на github в приватном или публичном доступе. Приветствуется наличие нескольких коммитов в процессе работы с нормальным описанием.
api-key: fzrw2QrRVsQcUEXhTQCz2qYWFjPV8XAs
secret: HTfeigAHETiypjDE
api-id: efb5ab20-6535-45da-92d3-694955cef14c
https://api.nytimes.com/svc/movies/v2/reviews/search.json?query=godfather&api-key=fzrw2QrRVsQcUEXhTQCz2qYWFjPV8XAs
 */
class MainActivity : AppCompatActivity(), CoroutineScope {

    private var job: Job = Job()

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }

    var recyclerView: RecyclerView? = null
    var recyclerViewAdapter: RecyclerViewAdapter? = null
//    var rowsArrayList: ArrayList<String?> = ArrayList()
    var rowsArrayList = mutableListOf<Review>()
    val data = LoadDataFromUrl()
    var isLoading = false

    val db = DatabaseHandler(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        recyclerView = findViewById(R.id.recyclerView)
        val search: SearchView = findViewById(R.id.search)
        search.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {

                data.setQuery(query)
                rowsArrayList.clear()
                launch {
                    val result = data.loadData()
                    val reviews = addReviewsInDB(db, result.results, rowsArrayList)
                    rowsArrayList = reviews
//                    populateData(0)
                    initAdapter()
                    initScrollListener()
                }

                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                return false
            }
        })


    }

    suspend fun populateData(i: Int) = coroutineScope {
        launch {
            var j=i
            while (j < i+10) {
                rowsArrayList.add(Review("Item $j", "date", "text", "img"))
                j++
            }
        }
    }

    private fun initAdapter() {
        recyclerViewAdapter = RecyclerViewAdapter(rowsArrayList)
        recyclerView!!.adapter = recyclerViewAdapter
    }

    private fun initScrollListener() {
        recyclerView!!.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val linearLayoutManager =
                    recyclerView.layoutManager as LinearLayoutManager?
                if (!isLoading) {
                    if (linearLayoutManager != null && linearLayoutManager.findLastCompletelyVisibleItemPosition() == rowsArrayList.size - 1) { //bottom of list!
                        loadMore()
                        isLoading = true
                    }
                }
            }
        })
    }

    private fun loadMore() {
        rowsArrayList.add(Review())

        recyclerView!!.post {
            // There is no need to use notifyDataSetChanged()
            recyclerViewAdapter!!.notifyItemInserted(rowsArrayList.size - 1)
        }

        recyclerViewAdapter!!.notifyItemInserted(rowsArrayList.size - 1)
        val handler = Handler()
        handler.postDelayed(Runnable {
            rowsArrayList.removeAt(rowsArrayList.size - 1)
            val scrollPosition: Int = rowsArrayList.size
            recyclerViewAdapter!!.notifyItemRemoved(scrollPosition)
            launch {
                populateData(scrollPosition)
                recyclerView!!.post {
                    recyclerViewAdapter!!.notifyDataSetChanged()
                }
            }

            isLoading = false
        }, 2000)
    }

    fun addReviewsInDB(
        db: DatabaseHandler,
        reviews: List<NewYorkJson.Result>,
        out: MutableList<Review>
    ): MutableList<Review> {
        for (review in reviews) {
            if (!db.searchReviewByName(review.display_title)) {
                if (review.multimedia != null) {
                    out.add(
                        Review(
                            db.addReview(
                                review.display_title,
                                review.publication_date,
                                review.summary_short,
                                review.multimedia.src
                            ),
                            review.display_title,
                            review.publication_date,
                            review.summary_short,
                            review.multimedia.src
                        )
                    )
                } else {
                    out.add(
                        Review(
                            db.addReview(
                                review.display_title,
                                review.publication_date,
                                review.summary_short
                            ),
                            review.display_title,
                            review.publication_date,
                            review.summary_short,
                            ""
                        )
                    )
                }
            } else {
                out.add(db.getReviewByName(review.display_title))
            }
        }
        return out
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        Log.i("INFO ", "onSaveInstanceState")
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        Log.i("INFO ", "onRestoreInstanceState")
    }
}
