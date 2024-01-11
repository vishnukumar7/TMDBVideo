package com.app.tmdbvideo.page.detail

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.tmdbvideo.model.DetailImageResponse
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

    private val _imagesList = MutableLiveData<DetailImageResponse>()
    val imageList : LiveData<DetailImageResponse> = _imagesList


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
                        _imagesList.value=response.body()
                        Log.e(TAG, "onResponse: ${Gson().toJson(response.body())}", )
                    }
                }

                override fun onFailure(call: Call<DetailImageResponse>, t: Throwable) {

                }

            })
        }
    }

}