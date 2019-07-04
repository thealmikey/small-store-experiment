package com.example.myapplication.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.myapplication.database.dao.BlogDao
import com.example.myapplication.model.Blog
import com.example.myapplication.util.DateTypeConverter

@Database(
    entities = arrayOf(
        Blog::class
    ),
    version = 1,
    exportSchema = false
)
@TypeConverters(DateTypeConverter::class)
abstract class MyAppDatabase : RoomDatabase() {
    abstract fun blogDao(): BlogDao
}