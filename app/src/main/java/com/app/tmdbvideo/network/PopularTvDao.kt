package com.app.tmdbvideo.network

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.app.tmdbvideo.model.TvResponse

@Dao
interface TvSeriesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertTv(popularTable: TvResponse)

    @Query("select * from tv_series_table")
    fun getAllTvList() : LiveData<List<TvResponse>>

    @Query("select * from tv_series_table where page=:page and type=:type")
    fun findPageType(page : Int,type : String) : List<TvResponse>

    @Query("select * from tv_series_table where page=:page")
    fun findPage(page : Int) : List<TvResponse>

    @Query("delete from tv_series_table")
    fun deleteAll()
}