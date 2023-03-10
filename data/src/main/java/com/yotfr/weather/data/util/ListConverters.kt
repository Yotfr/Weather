package com.yotfr.weather.data.util

import androidx.room.TypeConverter
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types

/**
 * [ListConverters] is a class that contains type converters for room.
 * This class was created in order to avoid additional mapping or complex database structure.
 * Since the type of a large number of dto's fields is: List<T>. We can just convert this list
 * to Json and and vice versa
 */
class ListConverters {

    private val moshi = Moshi.Builder().build()

    @TypeConverter
    fun fromStringList(value: List<String>): String {
        val type = Types.newParameterizedType(List::class.java, String::class.java)
        val jsonAdapter: JsonAdapter<List<String>> = moshi.adapter(
            type
        )
        return jsonAdapter.toJson(value)
    }

    @TypeConverter
    fun toStringList(value: String): List<String>? {
        val type = Types.newParameterizedType(List::class.java, String::class.java)
        val jsonAdapter: JsonAdapter<List<String>> = moshi.adapter(
            type
        )
        return jsonAdapter.fromJson(value)
    }

    @TypeConverter
    fun fromIntList(value: List<Int>): String {
        val type = Types.newParameterizedType(List::class.java, Int::class.javaObjectType)
        val jsonAdapter: JsonAdapter<List<Int>> = moshi.adapter(
            type
        )
        return jsonAdapter.toJson(value)
    }

    @TypeConverter
    fun toIntList(value: String): List<Int>? {
        val type = Types.newParameterizedType(List::class.java, Int::class.javaObjectType)
        val jsonAdapter: JsonAdapter<List<Int>> = moshi.adapter(
            type
        )
        return jsonAdapter.fromJson(value)
    }

    @TypeConverter
    fun fromDoubleList(value: List<Double>): String {
        val type = Types.newParameterizedType(List::class.java, Double::class.javaObjectType)
        val jsonAdapter: JsonAdapter<List<Double>> = moshi.adapter(
            type
        )
        return jsonAdapter.toJson(value)
    }

    @TypeConverter
    fun toDoubleList(value: String): List<Double>? {
        val type = Types.newParameterizedType(List::class.java, Double::class.javaObjectType)
        val jsonAdapter: JsonAdapter<List<Double>> = moshi.adapter(
            type
        )
        return jsonAdapter.fromJson(value)
    }
}
