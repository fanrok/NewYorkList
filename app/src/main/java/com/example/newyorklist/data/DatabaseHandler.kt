package com.example.newyorklist.data

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper


class DatabaseHandler(
    context: Context
) :
    SQLiteOpenHelper(
        context, DATABASE_NAME,
        null, DATABASE_VERSION
    ) {
    companion object {
        private val DATABASE_VERSION = 1
        private val DATABASE_NAME = "reviewsManager"
        private val TABLE_REVIEWS = "reviews"
        private val KEY_ID = "id"
        private val KEY_NAME = "name"
        private val KEY_DATE = "date"
        private val KEY_TEXT = "text"
        private val KEY_IMG = "img"
    }

    override fun onCreate(db: SQLiteDatabase) {//событие при первом запуске приложения - инициализируем бд
        val CREATE_REVIEW_TABLE = ("CREATE TABLE " + TABLE_REVIEWS + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + KEY_NAME + " TEXT,"
                + KEY_DATE + " TEXT," + KEY_TEXT + " TEXT," + KEY_IMG + " TEXT" + ")")
        db.execSQL(CREATE_REVIEW_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_REVIEWS")

        onCreate(db)
    }

    /**
     * добавляем отзыв в бд
     */
    fun addReview(name: String, date: String, text: String, img: String?): Long {
        val db = this.writableDatabase
        val id: Long
        val values = ContentValues()
        values.put(KEY_NAME, name)
        values.put(KEY_DATE, date)
        values.put(KEY_TEXT, text)
        values.put(KEY_IMG, img)

        id = db.insert(TABLE_REVIEWS, null, values)
        db.close()
        return id
    }

    fun addReview(name: String, date: String, text: String): Long {
        val db = this.writableDatabase
        val id: Long
        val values = ContentValues()
        values.put(KEY_NAME, name)
        values.put(KEY_DATE, date)
        values.put(KEY_TEXT, text)

        id = db.insert(TABLE_REVIEWS, null, values)
        db.close()
        return id
    }

    fun searchReviewByName(name: String): Boolean {
        val db = this.readableDatabase
        val cursor = db.query(
            TABLE_REVIEWS, arrayOf(KEY_ID), "$KEY_NAME=?",
            arrayOf(name), null, null, null, null
        )
        if (cursor.getCount() != 0) {
            cursor.close()
            return true
        }
        cursor.close()
        return false
    }

    fun getReviewById(id: Int): Review {
        val db = this.readableDatabase

        val cursor = db.query(
            TABLE_REVIEWS, arrayOf(KEY_ID, KEY_NAME, KEY_DATE, KEY_TEXT, KEY_IMG), "$KEY_ID=?",
            arrayOf(id.toString()), null, null, null, null
        )

        cursor?.moveToFirst()

        val out = Review(
            Integer.parseInt(cursor!!.getString(0)).toLong(),
            cursor.getString(1),
            cursor.getString(2),
            cursor.getString(3),
            cursor.getString(4)
        )
        db.close()
        cursor.close()
        return out
    }

    fun getReviewByName(name: String): Review {
        val db = this.readableDatabase

        val cursor = db.query(
            TABLE_REVIEWS, arrayOf(KEY_ID, KEY_NAME, KEY_DATE, KEY_TEXT, KEY_IMG), "$KEY_NAME=?",
            arrayOf(name), null, null, null, null
        )

        cursor?.moveToFirst()
        val out = Review(
            Integer.parseInt(cursor!!.getString(0)).toLong(),
            cursor.getString(1),
            cursor.getString(2),
            cursor.getString(3),
            cursor.getString(4)
        )
        db.close()
        cursor.close()
        return out
    }
}