package com.example.newyorklist.data.db.migrations

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

class Migration {
    companion object {
        val MIGRATION_1_2: Migration = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE reviews ADD COLUMN link TEXT DEFAULT \"\" NOT NULL")
            }
        }
    }
}