package com.example.newyorklist

//import android.R

import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.util.*


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

    var recyclerView: RecyclerView? = null
    var recyclerViewAdapter: RecyclerViewAdapter? = null
    var rowsArrayList: ArrayList<String?> = ArrayList()

    var isLoading = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        recyclerView = findViewById(R.id.recyclerView)
        populateData()
        initAdapter()
        initScrollListener()
    }

    private fun populateData() {
        var i = 0
        while (i < 10) {
            rowsArrayList.add("Item $i")
            i++
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
        rowsArrayList.add(null)

        recyclerView!!.post {
            // There is no need to use notifyDataSetChanged()
            recyclerViewAdapter!!.notifyItemInserted(rowsArrayList.size - 1)
        }

//        recyclerViewAdapter!!.notifyItemInserted(rowsArrayList.size - 1)
        val handler = Handler()
        handler.postDelayed(Runnable {
            rowsArrayList.removeAt(rowsArrayList.size - 1)
            val scrollPosition: Int = rowsArrayList.size
            recyclerViewAdapter!!.notifyItemRemoved(scrollPosition)
            var currentSize = scrollPosition
            val nextLimit = currentSize + 10
            while (currentSize - 1 < nextLimit) {
                rowsArrayList.add("Item $currentSize")
                currentSize++
            }
            recyclerViewAdapter!!.notifyDataSetChanged()
            isLoading = false
        }, 2000)
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
