package com.app.tmdbvideo.page.home

import android.app.Application
import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import com.app.tmdbvideo.model.HomePageModel
import com.app.tmdbvideo.model.MovieResponse
import com.app.tmdbvideo.model.TrendingTvResponse
import com.app.tmdbvideo.model.TvResultsItem
import com.app.tmdbvideo.network.ApiInterface
import com.app.tmdbvideo.network.RetrofitClient
import com.app.tmdbvideo.network.TMDBDatabase
import com.app.tmdbvideo.responsitory.TvRepository
import com.app.tmdbvideo.util.AppConstant
import com.google.gson.Gson
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel(private val application: Application) : ViewModel() {

    private val tvRepository: TvRepository

    init {
        val tmdb = TMDBDatabase.getInstance(application)
        tvRepository = TvRepository(tmdb)

    }

    private val _trendingTvList = MutableLiveData<List<TvResultsItem>>()
    val trendingTvList: LiveData<List<TvResultsItem>> = _trendingTvList

    private val _homeList = MutableLiveData<List<HomePageModel>>()
    val homeList: LiveData<List<HomePageModel>> = _homeList

    private val homePageModelList = mutableStateListOf<HomePageModel>()

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage

    private val _refreshState = MutableLiveData<Boolean>()
    val refreshState: LiveData<Boolean> = _refreshState


    var currentPage = 1

    fun refreshData() {
        _refreshState.value = true
        getPopularTvList()
    }

    fun getHomeData() {
        if(AppConstant.networkCheck(application)){
            getPopularTvList()
        }else{
            homePageModelList.clear()
            tvRepository.findTvSeriesPage(1)
            tvRepository.findMoviePage(1)
            viewModelScope.launch {
                tvRepository.searchOneTvResult.asFlow().collect{
                    val topRatedTvSeriesPage = it.filter { it.type==AppConstant.TOP_RATED_TV }
                    val popularTvSeriesPage = it.filter { it.type==AppConstant.POPULAR_TV }
                    homePageModelList.add(0,HomePageModel("Popular Tv",popularTvSeriesPage))
                    homePageModelList.add(1,HomePageModel("Top Rated Tv",topRatedTvSeriesPage))
                    _homeList.postValue(homePageModelList)
                }

                /*tvRepository.searchOneMovieResult.asFlow().collect{
                    val topRatedMoviePage = it.filter { it.type==AppConstant.TOP_RATED_MOVIE } //"Top Rated Movies"
                    val popularMoviePage = it.filter { it.type==AppConstant.POPULAR_MOVIE }// "Popular Movies"
                    homePageModelList.add(3, HomePageModel("Top Rated Movies",topRatedMoviePage))
                    homePageModelList.add(2, HomePageModel("Popular Movies",popularMoviePage))
                    _homeList.postValue(homePageModelList)
                }*/
            }
        }
    }

    fun getPopularTvList(page: Int = 1) {
        homePageModelList.clear()
        viewModelScope.launch {
            val apiService = RetrofitClient.buildService(ApiInterface::class.java)
            val call = apiService.getPopularTv(page = page.toString())
            call.enqueue(object : Callback<TrendingTvResponse> {
                override fun onResponse(
                    call: Call<TrendingTvResponse>,
                    response: Response<TrendingTvResponse>
                ) {
                    _refreshState.value = false
                    if (response.isSuccessful && response.body() != null) {
                        tvRepository.deleteAll()
                        val responseData = response.body()!!
                        _trendingTvList.value = responseData.results
                        homePageModelList.add(HomePageModel("Popular Tv", responseData.results))
                        responseData.type = AppConstant.POPULAR_TV
                        _homeList.value = homePageModelList
                        tvRepository.insertTv(responseData)
                        getTopRatedTvList()
                        Log.e("TAG", "onResponse: ${Gson().toJson(response.body())}")

                    }
                }

                override fun onFailure(call: Call<TrendingTvResponse>, t: Throwable) {
                    _refreshState.value = false
                    _errorMessage.value = t.message.toString()
                }

            })
        }
    }

    fun getTopRatedTvList(page: Int = 1) {
        viewModelScope.launch {
            val apiService = RetrofitClient.buildService(ApiInterface::class.java)
            val call = apiService.getTopRatedTv(page = page.toString())
            call.enqueue(object : Callback<TrendingTvResponse> {
                override fun onResponse(
                    call: Call<TrendingTvResponse>,
                    response: Response<TrendingTvResponse>
                ) {
                    _refreshState.value = false
                    if (response.isSuccessful && response.body() != null) {
                        val responseData = response.body()!!
                        homePageModelList.add(
                            HomePageModel(
                                "Top Rated Tv",
                                responseData.results
                            )
                        )
                        responseData.type = AppConstant.TOP_RATED_TV
                        tvRepository.insertTv(responseData)
                        _homeList.value = homePageModelList
                        getPopularMovieList()
                        Log.e("TAG", "onResponse: ${Gson().toJson(response.body())}")

                    }
                }

                override fun onFailure(call: Call<TrendingTvResponse>, t: Throwable) {
                    _refreshState.value = false
                    _errorMessage.value = t.message.toString()
                }

            })

        }
    }

    fun getPopularMovieList(page: Int = 1) {
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
                        val responseData = response.body()!!
                        homePageModelList.add(
                            HomePageModel(
                                "Popular Movies",
                                responseData.results
                            )
                        )
                        responseData.type = AppConstant.POPULAR_MOVIE
                        _homeList.value = homePageModelList
                        tvRepository.insertMovie(responseData)
                        getTopRatedMovieList()
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

    fun getTopRatedMovieList(page: Int = 1) {
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
                        val responseData = response.body()!!
                        homePageModelList.add(
                            HomePageModel(
                                "Top Rated Movies",
                                responseData.results
                            )
                        )
                        _homeList.value = homePageModelList
                        responseData.type = AppConstant.TOP_RATED_MOVIE
                        tvRepository.insertMovie(responseData)
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

class MainViewModelFactory(private var application: Application) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            return MainViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel Class")
    }
}