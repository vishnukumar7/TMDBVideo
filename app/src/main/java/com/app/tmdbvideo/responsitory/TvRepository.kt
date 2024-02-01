package com.app.tmdbvideo.responsitory

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.app.tmdbvideo.model.MovieResponse
import com.app.tmdbvideo.model.TvResponse
import com.app.tmdbvideo.network.MoviesDao
import com.app.tmdbvideo.network.TMDBDatabase
import com.app.tmdbvideo.network.TvSeriesDao
import com.app.tmdbvideo.util.AppConstant
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class TvRepository(tmdb: TMDBDatabase) {

    private val tvSeriesDao: TvSeriesDao = tmdb.tvSeriesDao()

    private val moviesDao : MoviesDao = tmdb.movieDao()

    val allTvData : LiveData<List<TvResponse>> = tvSeriesDao.getAllTvList()

    val allMovieData : LiveData<List<MovieResponse>> = moviesDao.getAllMovieList()

    var searchOneTvResult = MutableLiveData<List<TvResponse>>()
    var searchOneMovieResult = MutableLiveData<List<MovieResponse>>()



    private val coroutineScope = CoroutineScope(Dispatchers.Main)

    fun insertTv(newResponse: TvResponse){
        coroutineScope.launch {
            val oldResponse = tvSeriesDao.findPageType(newResponse.page,newResponse.type)
            if (oldResponse.isEmpty()){
                tvSeriesDao.insertTv(newResponse)
            }else{
                oldResponse[0].results=newResponse.results
                oldResponse[0].totalPages=newResponse.totalPages
                oldResponse[0].totalResults=newResponse.totalResults
                tvSeriesDao.insertTv(oldResponse[0])
            }
        }
    }

    fun insertMovie(newResponse: MovieResponse){
        coroutineScope.launch {
            val oldResponse = moviesDao.findMovieType(newResponse.page,newResponse.type)
            if (oldResponse.isEmpty()){
                moviesDao.insertMovie(newResponse)
            }else{
                oldResponse[0].results=newResponse.results
                oldResponse[0].totalPages=newResponse.totalPages
                oldResponse[0].totalResults=newResponse.totalResults
                moviesDao.insertMovie(oldResponse[0])
            }
        }
    }

    fun deleteAll(){
        coroutineScope.launch {
            tvSeriesDao.deleteAll()
        }
    }

    fun findTvSeriesPage(page : Int,type: String=""){
        coroutineScope.launch {
            searchOneTvResult.postValue(asyncFindTvSeries(page = page, type = type).await())
        }
    }

    fun findMoviePage(page: Int,type: String=""){
        coroutineScope.launch {
            searchOneMovieResult.postValue(asyncFindMovie(page = page,type= type).await())
        }
    }

    private fun asyncFindTvSeries(page: Int,type : String) : Deferred<List<TvResponse>> = coroutineScope.async(Dispatchers.IO) {
        if(type.isEmpty())
            return@async tvSeriesDao.findPage(page = page)
        else return@async tvSeriesDao.findPageType(page = page, type = type)
    }

    private fun asyncFindMovie(page: Int,type: String) : Deferred<List<MovieResponse>> = coroutineScope.async(Dispatchers.IO) {
        if(type.isEmpty()){
            return@async moviesDao.findMovie(page = page)
        }else {
            return@async moviesDao.findMovieType(page = page, type = type)
        }
    }

}