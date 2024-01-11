package com.app.tmdbvideo.network

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.app.tmdbvideo.model.MovieResponse

@Dao
interface MoviesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertMovie(newValue : MovieResponse)

    @Query("select * from movie_table")
    fun getAllMovieList() : LiveData<List<MovieResponse>>

    @Query("select * from movie_table where page=:page and type=:type")
    fun findMovieType(page : Int,type : String) : List<MovieResponse>

    @Query("select * from movie_table where page=:page")
    fun findMovie(page : Int) : List<MovieResponse>

    @Query("delete from movie_table")
    fun deleteAll()
}