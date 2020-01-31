package com.example.newyorklist

//import android.R

import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.newyorklist.data.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch


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
class MainActivity : AppCompatActivity() {

    private var job: Job = Job()

    val ioScope = CoroutineScope(Dispatchers.IO + job)
    val uiScope = CoroutineScope(Dispatchers.Main + job)

    var recyclerView: RecyclerView? = null
    var recyclerViewAdapter: RecyclerViewAdapter? = null
    var listReviews = StateSave.reviews
    val data = StateSave.api
    var isLoading = false

    val db = DatabaseHandler(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        recyclerView = findViewById(R.id.recyclerView)
        initAdapter()
        initScrollListener()
        if (listReviews.size > 0) {//значение больше нуля значит мы восстановили стейт. промотаем список до нужной позиции (smoothScrollToPosition не работает)
            recyclerView?.scrollToPosition(StateSave.scrollPosition)
        }
        val search: SearchView = findViewById(R.id.search)

        search.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                data.setQuery(query)
                listReviews.clear()
                loadMore(true)
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
//                data.setQuery(newText)
//                listReviews.clear()
//                loadMore(true)
                return false
            }
        })
    }

    private fun initAdapter() {
        recyclerViewAdapter = RecyclerViewAdapter(listReviews)
        recyclerView!!.adapter = recyclerViewAdapter
    }

    private fun initScrollListener() {
        recyclerView!!.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val linearLayoutManager = recyclerView.layoutManager as LinearLayoutManager
                StateSave.scrollPosition =
                    linearLayoutManager.findFirstCompletelyVisibleItemPosition()
                if (!isLoading && data.getHasMore()) {
                    if (linearLayoutManager.findLastCompletelyVisibleItemPosition() == listReviews.size - 1 && listReviews.size > 1) {
                        loadMore(false)
                        isLoading = true
                    }
                }
            }
        })
    }

    private fun loadMore(newList: Boolean = false) {
        if (newList) {
            recyclerView?.post {
                recyclerViewAdapter?.notifyDataSetChanged()
            }
        }
        listReviews.add(Review())
        recyclerView?.post {
            recyclerViewAdapter?.notifyItemInserted(listReviews.size)
        }
        val scrollPosition: Int = listReviews.size
        val handler = Handler()
        handler.postDelayed({
            ioScope.launch {
                val result = data.loadData()
                if (newList) {
                    listReviews.clear()
                } else {
                    listReviews.removeAt(listReviews.size - 1)
                }
                val reviews = db.addReviewsInDB(db, result.results, listReviews)
                uiScope.launch {
                    listReviews = reviews
                    recyclerView?.post {
                        recyclerViewAdapter?.notifyItemRangeChanged(
                            scrollPosition - 1,
                            listReviews.size - scrollPosition
                        )
                    }
                    isLoading = false
                }
            }
        }, 0)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        val search: SearchView = findViewById(R.id.search)
        val searchText = search.query
        outState.putCharSequence("searchText", searchText)
        StateSave.reviews = listReviews

    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {//я не понимаю почему, но при поворотах экрана и при изменении экрана этот метод просто не вызывается
        super.onRestoreInstanceState(savedInstanceState)
        val userText = savedInstanceState.getCharSequence("searchText")
        val search: SearchView = findViewById(R.id.search)
        search.setQuery(userText, false)
//        listReviews = StateSave.reviews
    }

    override fun onPause() {
        super.onPause()
        StateSave.reviews = listReviews
    }

    override fun onResume() {
        super.onResume()
    }
}
