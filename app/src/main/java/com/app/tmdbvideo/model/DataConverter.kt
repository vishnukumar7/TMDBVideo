package com.app.tmdbvideo.model

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class DataConverter {

    @TypeConverter
    fun fromTvResultTableList(resultList : List<TvResultsItem>?	): String? {
        if(resultList==null)
            return null
        val gson = Gson()
        val type = object : TypeToken<List<TvResultsItem>>(){}.type
        return gson.toJson(resultList,type)
    }

    @TypeConverter
    fun toTvResultTableList(tvResultTableString : String?) : List<TvResultsItem>{
        if(tvResultTableString==null)
            return ArrayList()
        val gson = Gson()
        val type = object : TypeToken<List<TvResultsItem>>(){}.type
        return gson.fromJson(tvResultTableString,type)
    }

    @TypeConverter
    fun fromMovieResultTableList(resultList : List<MovieResultsItem>?	): String? {
        if(resultList==null)
            return null
        val gson = Gson()
        val type = object : TypeToken<List<MovieResultsItem>>(){}.type
        return gson.toJson(resultList,type)
    }

    @TypeConverter
    fun toMovieResultTableList(tvResultTableString : String?) : List<MovieResultsItem>{
        if(tvResultTableString==null)
            return ArrayList()
        val gson = Gson()
        val type = object : TypeToken<List<MovieResultsItem>>(){}.type
        return gson.fromJson(tvResultTableString,type)
    }

    @TypeConverter
    fun fromListJsonInt(result : List<Int>?) : String?{
        if(result==null)
            return null
        val gson = Gson()
        val type = object : TypeToken<List<Int>>(){}.type
        return gson.toJson(result,type)

    }

    @TypeConverter
    fun toListJsonInt(result : String?) : List<Int>{
        if(result==null)
            return ArrayList()
        val gson = Gson()
        val type = object : TypeToken<List<Int>>(){}.type
        return gson.fromJson(result,type)
    }

    @TypeConverter
    fun fromListJsonString(result : List<String>?) : String?{
        if(result==null)
            return null
        val gson = Gson()
        val type = object : TypeToken<List<String>>(){}.type
        return gson.toJson(result,type)

    }

    @TypeConverter
    fun toListJsonString(result : String?) : List<String>{
        if(result==null)
            return ArrayList()
        val gson = Gson()
        val type = object : TypeToken<List<String>>(){}.type
        return gson.fromJson(result,type)
    }

}