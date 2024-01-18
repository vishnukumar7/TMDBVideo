package com.app.tmdbvideo.page

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.app.tmdbvideo.model.MovieResponse
import com.app.tmdbvideo.model.MovieResultsItem
import com.app.tmdbvideo.network.ApiInterface
import com.app.tmdbvideo.network.RetrofitClient
import com.app.tmdbvideo.util.LogcatUtils
import com.google.gson.Gson
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SearchViewModel(application: Application) : AndroidViewModel(application) {

    private val TAG= "SearchViewModel"
    private val _searchQueryResult = MutableLiveData<String>()
    val searchQueryResult : LiveData<String> = _searchQueryResult

    private val _searchResultList = MutableLiveData<List<MovieResultsItem>>()

    val searchResultList : LiveData<List<MovieResultsItem>> = _searchResultList

    fun searchQueryChange(newQuery : String) {
        _searchQueryResult.value=newQuery
        searchApiCall(newQuery)
    }

    private fun searchApiCall(newQuery: String){
        viewModelScope.launch {
            val apiInterface = RetrofitClient.buildService(ApiInterface::class.java)
            val call = apiInterface.getSearchMovie(query = newQuery)
            call.enqueue(object : Callback<MovieResponse> {
                override fun onResponse(
                    call: Call<MovieResponse>,
                    response: Response<MovieResponse>
                ) {
                    if (response.isSuccessful && response.body()!=null){
                        LogcatUtils.e(TAG, "onResponse: ${Gson().toJson(response.body())}", )
                        response.body()?.results?.let {
                            _searchResultList.postValue(it)
                        }
                    }
                }

                override fun onFailure(call: Call<MovieResponse>, t: Throwable) {

                }

            })
        }
    }
}