package com.app.tmdbvideo.page.home

import android.app.Application
import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import com.app.tmdbvideo.Extension.addMediaType
import com.app.tmdbvideo.Extension.addValue
import com.app.tmdbvideo.model.HomePageModel
import com.app.tmdbvideo.model.MovieResponse
import com.app.tmdbvideo.model.ResultItem
import com.app.tmdbvideo.model.ResultsItem
import com.app.tmdbvideo.model.TvResponse
import com.app.tmdbvideo.network.ApiInterface
import com.app.tmdbvideo.network.RetrofitClient
import com.app.tmdbvideo.network.TMDBDatabase
import com.app.tmdbvideo.responsitory.TvRepository
import com.app.tmdbvideo.util.AppConstant
import com.google.gson.Gson
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel(private val application: Application) : AndroidViewModel(application) {

    private val tvRepository: TvRepository
val TAG="MainViewModel"
    init {
        val tmdb = TMDBDatabase.getInstance(application)
        tvRepository = TvRepository(tmdb)
    }



    private val _trendingTvList = MutableLiveData<List<ResultItem>>()
    val trendingTvList: LiveData<List<ResultItem>> = _trendingTvList

    private val resultListItem = mutableStateListOf<ResultItem>()

    private val _searchMovieList = MutableLiveData<List<ResultItem>>()
    val searchMovieList: LiveData<List<ResultItem>> = _searchMovieList


    private val _homeList = MutableLiveData<List<HomePageModel>>()
    val homeList: LiveData<List<HomePageModel>> = _homeList

    private val homePageModelList = mutableStateListOf<HomePageModel>()

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage

    private val _refreshState = MutableLiveData<Boolean>()
    val refreshState: LiveData<Boolean> = _refreshState


    var currentPage = 1
    var apiPage =1

    fun refreshData() {
        _refreshState.value = true
        getPopularTvList()
    }

    fun getHomeData(typeList : String="Home") {
        if(AppConstant.networkCheck(application)){
            getPopularTvList()
        }else{
            homePageModelList.clear()

            viewModelScope.launch {
                tvRepository.searchOneTvResult.asFlow().collect{
                    val topRatedTvSeriesPage = it.filter { it.type==AppConstant.TOP_RATED_TV }
                    val popularTvSeriesPage = it.filter { it.type==AppConstant.POPULAR_TV }
                    homePageModelList.addValue(0,HomePageModel(AppConstant.HOME_HEADER_POPULAR_TV,popularTvSeriesPage[0].results))
                    homePageModelList.addValue(1,HomePageModel(AppConstant.TOP_RATED_TV,topRatedTvSeriesPage[0].results))
                    _homeList.postValue(homePageModelList)
                 //   tvRepository.findMoviePage(1)
                }

                tvRepository.searchOneMovieResult.asFlow().collect{
                    val topRatedMoviePage = it.filter { it.type==AppConstant.TOP_RATED_MOVIE } //"Top Rated Movies"
                    val popularMoviePage = it.filter { it.type==AppConstant.POPULAR_MOVIE }// "Popular Movies"
                    Log.e(TAG, "getHomeData: topRatedMoviePage::::: ${Gson().toJson(topRatedMoviePage)}", )
                    Log.e(TAG, "getHomeData: popularMoviePage :::::::::: ${Gson().toJson(popularMoviePage)}", )
                    homePageModelList.addValue(0, HomePageModel(AppConstant.HOME_HEADER_TOP_RATED_MOVIE,topRatedMoviePage[0].results))
                    homePageModelList.addValue(1, HomePageModel(AppConstant.HOME_HEADER_POPULAR_MOVIE,popularMoviePage[0].results))
                    _homeList.postValue(homePageModelList)
                }
            }
            tvRepository.findMoviePage(1)

        }
    }

    fun getOnTheAirTvList(page: Int = 1,search: String = "",typeList: String="") {
        apiPage=page
        viewModelScope.launch {
            val apiService = RetrofitClient.buildService(ApiInterface::class.java)

            val call = apiService.getOnTheAirTv(page = page.toString())
            call.enqueue(object : Callback<TvResponse> {
                override fun onResponse(
                    call: Call<TvResponse>,
                    response: Response<TvResponse>
                ) {
                    _refreshState.value = false
                    if (response.isSuccessful && response.body() != null) {
                        tvRepository.deleteAll()
                        val responseData = response.body()!!.addMediaType()
                        responseData.type = AppConstant.ON_THE_AIR_TV
                        tvRepository.insertTv(responseData)
                        if(search.isEmpty()){
                            homePageModelList.clear()
                            homePageModelList.add(HomePageModel(AppConstant.HOME_HEADER_POPULAR_TV, responseData.results))
                            _homeList.value = homePageModelList
                            getTopRatedTvList()
                        }else{
                            currentPage=responseData.page
                            if(page==1) {

                                resultListItem.clear()
                                resultListItem.addAll(responseData.results)
                            }
                            else{
                                resultListItem.addAll(responseData.results)
                            }
                            _trendingTvList.value = resultListItem
                        }
                        Log.e("TAG", "onResponse: ${Gson().toJson(response.body())}")

                    }
                }

                override fun onFailure(call: Call<TvResponse>, t: Throwable) {
                    _refreshState.value = false
                    _errorMessage.value = t.message.toString()
                }

            })
        }
    }

    fun getAiringTodayTvList(page: Int = 1,search: String = "",typeList: String="") {
        apiPage=page
        viewModelScope.launch {
            val apiService = RetrofitClient.buildService(ApiInterface::class.java)

            val call = apiService.getPopularTv(page = page.toString())
            call.enqueue(object : Callback<TvResponse> {
                override fun onResponse(
                    call: Call<TvResponse>,
                    response: Response<TvResponse>
                ) {
                    _refreshState.value = false
                    if (response.isSuccessful && response.body() != null) {
                        tvRepository.deleteAll()
                        val responseData = response.body()!!.addMediaType()
                        responseData.type = AppConstant.POPULAR_TV
                        tvRepository.insertTv(responseData)
                        if(search.isEmpty()){
                            homePageModelList.clear()
                            homePageModelList.add(HomePageModel(AppConstant.HOME_HEADER_POPULAR_TV, responseData.results))
                            _homeList.value = homePageModelList
                            getTopRatedTvList()
                        }else{
                            currentPage=responseData.page
                            if(page==1) {

                                resultListItem.clear()
                                resultListItem.addAll(responseData.results)
                            }
                            else{
                                resultListItem.addAll(responseData.results)
                            }
                            _trendingTvList.value = resultListItem
                        }
                        Log.e("TAG", "onResponse: ${Gson().toJson(response.body())}")

                    }
                }

                override fun onFailure(call: Call<TvResponse>, t: Throwable) {
                    _refreshState.value = false
                    _errorMessage.value = t.message.toString()
                }

            })
        }
    }

    fun getTrendingTvByDayList(page: Int = 1,search: String = "",typeList: String="") {
        apiPage=page
        viewModelScope.launch {
            val apiService = RetrofitClient.buildService(ApiInterface::class.java)

            val call = apiService.getPopularTv(page = page.toString())
            call.enqueue(object : Callback<TvResponse> {
                override fun onResponse(
                    call: Call<TvResponse>,
                    response: Response<TvResponse>
                ) {
                    _refreshState.value = false
                    if (response.isSuccessful && response.body() != null) {
                        tvRepository.deleteAll()
                        val responseData = response.body()!!.addMediaType()
                        responseData.type = AppConstant.POPULAR_TV
                        tvRepository.insertTv(responseData)
                        if(search.isEmpty()){
                            homePageModelList.clear()
                            homePageModelList.add(HomePageModel(AppConstant.HOME_HEADER_POPULAR_TV, responseData.results))
                            _homeList.value = homePageModelList
                            getTopRatedTvList()
                        }else{
                            currentPage=responseData.page
                            if(page==1) {

                                resultListItem.clear()
                                resultListItem.addAll(responseData.results)
                            }
                            else{
                                resultListItem.addAll(responseData.results)
                            }
                            _trendingTvList.value = resultListItem
                        }
                        Log.e("TAG", "onResponse: ${Gson().toJson(response.body())}")

                    }
                }

                override fun onFailure(call: Call<TvResponse>, t: Throwable) {
                    _refreshState.value = false
                    _errorMessage.value = t.message.toString()
                }

            })
        }
    }

    fun getTrendingTvByWeekList(page: Int = 1,search: String = "",typeList: String="") {
        apiPage=page
        viewModelScope.launch {
            val apiService = RetrofitClient.buildService(ApiInterface::class.java)

            val call = apiService.getPopularTv(page = page.toString())
            call.enqueue(object : Callback<TvResponse> {
                override fun onResponse(
                    call: Call<TvResponse>,
                    response: Response<TvResponse>
                ) {
                    _refreshState.value = false
                    if (response.isSuccessful && response.body() != null) {
                        tvRepository.deleteAll()
                        val responseData = response.body()!!.addMediaType()
                        responseData.type = AppConstant.POPULAR_TV
                        tvRepository.insertTv(responseData)
                        if(search.isEmpty()){
                            homePageModelList.clear()
                            homePageModelList.add(HomePageModel(AppConstant.HOME_HEADER_POPULAR_TV, responseData.results))
                            _homeList.value = homePageModelList
                            getTopRatedTvList()
                        }else{
                            currentPage=responseData.page
                            if(page==1) {

                                resultListItem.clear()
                                resultListItem.addAll(responseData.results)
                            }
                            else{
                                resultListItem.addAll(responseData.results)
                            }
                            _trendingTvList.value = resultListItem
                        }
                        Log.e("TAG", "onResponse: ${Gson().toJson(response.body())}")

                    }
                }

                override fun onFailure(call: Call<TvResponse>, t: Throwable) {
                    _refreshState.value = false
                    _errorMessage.value = t.message.toString()
                }

            })
        }
    }

    fun getTopRatedTvList(page: Int = 1,search: String="",typeList: String="") {
        apiPage=page
        viewModelScope.launch {
            val apiService = RetrofitClient.buildService(ApiInterface::class.java)
            val call = apiService.getTopRatedTv(page = page.toString())
            call.enqueue(object : Callback<TvResponse> {
                override fun onResponse(
                    call: Call<TvResponse>,
                    response: Response<TvResponse>
                ) {
                    _refreshState.value = false
                    if (response.isSuccessful && response.body() != null) {
                        val responseData = response.body()!!.addMediaType()
                        responseData.type = AppConstant.TOP_RATED_TV
                        tvRepository.insertTv(responseData)
                        if(search.isEmpty()){
                            homePageModelList.add(
                                HomePageModel(
                                    AppConstant.TOP_RATED_TV,
                                    responseData.results
                                )
                            )
                            _homeList.value = homePageModelList
                            getPopularMovieList()
                        }else{
                            currentPage=responseData.page
                            if(page==1) {
                                resultListItem.clear()
                                resultListItem.addAll(responseData.results)
                            }
                            else{
                                resultListItem.addAll(responseData.results)
                            }
                            _trendingTvList.value=resultListItem
                        }
                        Log.e("TAG", "onResponse: ${Gson().toJson(response.body())}")

                    }
                }

                override fun onFailure(call: Call<TvResponse>, t: Throwable) {
                    _refreshState.value = false
                    _errorMessage.value = t.message.toString()
                }

            })

        }
    }


    fun getPopularTvList(page: Int = 1,search: String = "",typeList: String="") {
        apiPage=page
        viewModelScope.launch {
            val apiService = RetrofitClient.buildService(ApiInterface::class.java)

            val call = apiService.getPopularTv(page = page.toString())
            call.enqueue(object : Callback<TvResponse> {
                override fun onResponse(
                    call: Call<TvResponse>,
                    response: Response<TvResponse>
                ) {
                    _refreshState.value = false
                    if (response.isSuccessful && response.body() != null) {
                        tvRepository.deleteAll()
                        val responseData = response.body()!!.addMediaType()
                        responseData.type = AppConstant.POPULAR_TV
                        tvRepository.insertTv(responseData)
                        if(search.isEmpty()){
                            homePageModelList.clear()
                            homePageModelList.add(HomePageModel(AppConstant.HOME_HEADER_POPULAR_TV, responseData.results))
                            _homeList.value = homePageModelList
                            getTopRatedTvList()
                        }else{
                            currentPage=responseData.page
                            if(page==1) {

                                resultListItem.clear()
                                resultListItem.addAll(responseData.results)
                            }
                            else{
                                resultListItem.addAll(responseData.results)
                            }
                            _trendingTvList.value = resultListItem
                        }
                        Log.e("TAG", "onResponse: ${Gson().toJson(response.body())}")

                    }
                }

                override fun onFailure(call: Call<TvResponse>, t: Throwable) {
                    _refreshState.value = false
                    _errorMessage.value = t.message.toString()
                }

            })
        }
    }

    fun getPopularMovieList(page: Int = 1,search: String="",typeList: String="") {
        apiPage=page
        viewModelScope.launch {
            val apiService = RetrofitClient.buildService(ApiInterface::class.java)
            val call = apiService.getPopularMovie(page = page.toString())
            call.enqueue(object : Callback<MovieResponse> {
                override fun onResponse(
                    call: Call<MovieResponse>,
                    response: Response<MovieResponse>
                ) {
                    _refreshState.value = false
                    if (response.isSuccessful && response.body() != null) {
                        val responseData = response.body()!!.addMediaType()
                        responseData.type = AppConstant.POPULAR_MOVIE
                        tvRepository.insertMovie(responseData)
                       if(search.isEmpty()){
                           homePageModelList.add(
                               HomePageModel(
                                   AppConstant.HOME_HEADER_POPULAR_MOVIE,
                                   responseData.results
                               )
                           )
                           _homeList.value = homePageModelList
                           getTopRatedMovieList()
                       }else{
                           currentPage=responseData.page
                           if(page==1) {
                               resultListItem.clear()
                               resultListItem.addAll(responseData.results)
                           }
                           else{
                               resultListItem.addAll(responseData.results)
                           }
                           _searchMovieList.value=resultListItem
                       }
                        Log.e("TAG", "onResponse: ${Gson().toJson(responseData)}")

                    }
                }

                override fun onFailure(call: Call<MovieResponse>, t: Throwable) {
                    _refreshState.value = false
                    _errorMessage.value = t.message.toString()
                }

            })

        }
    }

    fun getTopRatedMovieList(page: Int = 1,search: String="",typeList: String="") {
        apiPage=page
        viewModelScope.launch {
            val apiService = RetrofitClient.buildService(ApiInterface::class.java)
            val call = apiService.getTopRatedMovie(page = page.toString())
            call.enqueue(object : Callback<MovieResponse> {
                override fun onResponse(
                    call: Call<MovieResponse>,
                    response: Response<MovieResponse>
                ) {
                    _refreshState.value = false
                    if (response.isSuccessful && response.body() != null) {
                        val responseData = response.body()!!.addMediaType()
                        responseData.type = AppConstant.TOP_RATED_MOVIE
                        tvRepository.insertMovie(responseData)
                        if(search.isEmpty()){
                            homePageModelList.add(
                                HomePageModel(
                                    AppConstant.HOME_HEADER_TOP_RATED_MOVIE,
                                    responseData.results
                                )
                            )
                            _homeList.value = homePageModelList
                        }else{
                            currentPage=responseData.page
                            if(page==1) {
                                resultListItem.clear()
                                resultListItem.addAll(responseData.results)
                            }
                            else{
                                resultListItem.addAll(responseData.results)
                            }
                            _searchMovieList.value=resultListItem
                        }
                        Log.e("TAG", "onResponse: ${Gson().toJson(responseData)}")

                    }
                }

                override fun onFailure(call: Call<MovieResponse>, t: Throwable) {
                    _refreshState.value = false
                    _errorMessage.value = t.message.toString()
                }
            })
        }
    }


}

