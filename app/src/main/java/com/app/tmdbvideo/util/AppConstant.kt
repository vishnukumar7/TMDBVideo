package com.app.tmdbvideo.util

import android.content.Context
import android.net.ConnectivityManager

object AppConstant  {

    const val POPULAR_MOVIE="popular_movie"
    const val POPULAR_TV="popular_tv"
    const val TOP_RATED_MOVIE="top_rated_movie"
    const val TOP_RATED_TV="top_rated_tv"

    const val TV_SERIES="tv_series"
    const val MOVIES="movies"

    fun networkCheck(context: Context): Boolean {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = cm.activeNetworkInfo
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting
    }


}