package com.example.myapplication.util


import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type
import java.util.*

class DateTypeConverter {
    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return if (value == null) null else Date(value)
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time
    }
}

class JsonListStringConverter:JsonListConverter<String>()


open class JsonListConverter<T>{

    fun <T>listAdapter(): Type {
       return object:TypeToken<List<T>>(){}.type
    }

    @TypeConverter
    fun toJSON(list: List<T>?): String? = Gson().toJson(list,listAdapter<T>())

    @TypeConverter
    fun fromJson(list: String): List<T>? {
       return Gson().fromJson(list,listAdapter<T>())
    }
}
