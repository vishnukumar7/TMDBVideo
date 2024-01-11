package com.app.tmdbvideo.network

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.app.tmdbvideo.model.TrendingTvResponse
import com.app.tmdbvideo.model.TvResultsItem

@Dao
interface TvSeriesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertTv(popularTable: TrendingTvResponse)

    @Query("select * from tv_series_table")
    fun getAllTvList() : LiveData<List<TrendingTvResponse>>

    @Query("select * from tv_series_table where page=:page and type=:type")
    fun findPageType(page : Int,type : String) : List<TrendingTvResponse>

    @Query("select * from tv_series_table where page=:page")
    fun findPage(page : Int) : List<TrendingTvResponse>

    @Query("delete from tv_series_table")
    fun deleteAll()
}