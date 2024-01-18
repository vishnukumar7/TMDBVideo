package com.app.tmdbvideo.page.detail

import android.app.Application
import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.tmdbvideo.model.CollectionResponse
import com.app.tmdbvideo.model.DetailImageResponse
import com.app.tmdbvideo.model.MovieDetailResponse
import com.app.tmdbvideo.network.ApiInterface
import com.app.tmdbvideo.network.RetrofitClient
import com.app.tmdbvideo.network.TMDBDatabase
import com.app.tmdbvideo.responsitory.TvRepository
import com.google.gson.Gson
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailViewModel(private val application: Application) : ViewModel() {

    private val tvRepository: TvRepository
    val TAG="DetailViewModel"
    init {
        val tmdb = TMDBDatabase.getInstance(application)
        tvRepository = TvRepository(tmdb)
    }

    private val detailTypeList = mutableStateListOf<Any>()

    private val _detailPage = MutableLiveData<List<Any>>()
    val detailPage : LiveData<List<Any>> = _detailPage


    fun apiCall(movieID : String){
        Log.e(TAG, "apiCall: $movieID", )
        viewModelScope.launch {
            val apiService = RetrofitClient.buildService(ApiInterface::class.java)
            val call = apiService.getMoviesImage(movieID)
            call.enqueue(object : Callback<DetailImageResponse> {
                override fun onResponse(
                    call: Call<DetailImageResponse>,
                    response: Response<DetailImageResponse>
                ) {
                    if(response.isSuccessful && response.body()!=null){
                        detailTypeList.clear()
                        detailTypeList.add(response.body()!!)
                        _detailPage.value=detailTypeList
                        detailsPageApi(movieID)
                        Log.e(TAG, "onResponse: ${Gson().toJson(response.body())}", )
                    }
                }

                override fun onFailure(call: Call<DetailImageResponse>, t: Throwable) {

                }

            })
        }
    }

    private fun detailsPageApi(movieID: String){
        viewModelScope.launch {
            val apiInterface = RetrofitClient.buildService(ApiInterface::class.java)
            val call = apiInterface.getMovieDetail(movieID)
            call.enqueue(object : Callback<MovieDetailResponse> {
                override fun onResponse(
                    call: Call<MovieDetailResponse>,
                    response: Response<MovieDetailResponse>
                ) {
                    if(response.isSuccessful && response.body()!=null){
                        val responseData = response.body()!!
                        detailTypeList.add(responseData)
                        _detailPage.value=detailTypeList
                        responseData.belongsToCollection?.let {
                            getCollection(collectionId = it.id.toString())
                        }
                        Log.e(TAG, "onResponse: ${Gson().toJson(response.body())}", )
                    }
                }

                override fun onFailure(call: Call<MovieDetailResponse>, t: Throwable) {

                }

            })
        }
    }

    private fun getCollection(collectionId : String){
        viewModelScope.launch {
            val apiInterface = RetrofitClient.buildService(ApiInterface::class.java)
            val call = apiInterface.getCollection(collectionId)
            call.enqueue(object : Callback<CollectionResponse> {
                override fun onResponse(
                    call: Call<CollectionResponse>,
                    response: Response<CollectionResponse>
                ) {
                    if(response.isSuccessful && response.body()!=null){
                        detailTypeList.add(response.body()!!)
                        _detailPage.value=detailTypeList
                        Log.e(TAG, "onResponse: ${Gson().toJson(response.body())}", )
                    }
                }

                override fun onFailure(call: Call<CollectionResponse>, t: Throwable) {

                }

            })
        }
    }

}