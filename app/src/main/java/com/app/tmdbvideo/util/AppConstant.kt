package com.app.tmdbvideo.util

import android.content.Context
import android.net.ConnectivityManager
import android.util.DebugUtils
import android.util.Log
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.toLowerCase
import androidx.compose.ui.text.withStyle
import java.util.Locale

object AppConstant  {
    //Tv Series
    const val TOP_RATED_TV="top_rated_tv"
    const val POPULAR_TV="popular_tv"
    const val ON_THE_AIR_TV="on_the_air_tv"
    const val AIRING_TODAY_TV="airing_today_tv"
    const val TRENDING_BY_DAY_TV="trending_by_day_tv"
    const val TRENDING_BY_WEEK_TV="trending_by_week_tv"

    //Movies
    const val TOP_RATED_MOVIE="top_rated_movie"
    const val POPULAR_MOVIE="popular_movie"
    const val NOW_PLAYING_MOVIE="now_playing_movie"
    const val UP_COMING_MOVIE="up_coming_movie"
    const val TRENDING_BY_DAY_MOVIE="trending_by_day_movie"
    const val TRENDING_BY_WEEK_MOVIE="trending_by_week_movie"


    const val TV_SERIES="tv_series"
    const val MOVIES="movies"

    //tv series header
    const val HOME_HEADER_POPULAR_TV="Popular Tv"
    const val HOME_HEADER_TOP_RATED_TV="Top Rated Tv"
    const val HEADER_ON_THE_AIR_TV="On The Air"
    const val HEADER_AIRING_TODAY_TV="Airing Today"
    const val HEADER_TRENDING_BY_DAY_TV="Trending By Day"
    const val HEADER_TRENDING_BY_WEEK_TV="Trending By Week"

    //movie header
    const val HOME_HEADER_POPULAR_MOVIE="Popular Movies"
    const val HOME_HEADER_TOP_RATED_MOVIE="Top Rated Movies"
    const val HEADER_NOW_PLAYING_MOVIE="Now Playing"
    const val HEADER_UP_COMING_MOVIE="Up Coming"
    const val HEADER_TRENDING_BY_DAY_MOVIE="Trending By Day"
    const val HEADER_TRENDING_BY_WEEK_MOVIE="Trending By Week"

    const val TAG_VIEW_ALL="view_all"

    fun networkCheck(context: Context): Boolean {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = cm.activeNetworkInfo
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting
    }


    fun buildAnnotatedStringWithColors(text: String,searchText : String) : AnnotatedString{
        val builder  = AnnotatedString.Builder()
        if(text.lowercase().contains(searchText.lowercase())){
            val firstIndex = text.lowercase().indexOf(searchText.lowercase())
            Log.e("TAG", "buildAnnotatedStringWithColors: text ::::: $text and search text ::::::: $searchText", )
            if(firstIndex!=0){
                val first = text.substring(0,firstIndex)
                Log.e("TAG", "buildAnnotatedStringWithColors:first:::::::: $first", )
                builder.withStyle(style = SpanStyle(color = Color.White)){
                    append(first)
                }
            }
            builder.withStyle(style = SpanStyle(color = Color.Red)){
                append(searchText)
            }
            if(firstIndex!=text.length-1){
                Log.e("TAG", "buildAnnotatedStringWithColors: first index ::::::::: $firstIndex and search ::::: $searchText and  search text ::::::: ${searchText.length} and text :::::: $text amd length of text ::::::::: ${text.length}", )
                val last = text.substring(firstIndex+searchText.length,text.length)
                Log.e("TAG", "buildAnnotatedStringWithColors: last ::::::::: $last", )
                builder.withStyle(style = SpanStyle(color = Color.White)){
                    append(last)
                }
            }
        }else{
            builder.withStyle(style = SpanStyle(color = Color.White)){
                append(text)
            }
        }

        return builder.toAnnotatedString()

    }


}

class ColorsTransFormation(private val searchText: String) : VisualTransformation{
    override fun filter(text: AnnotatedString): TransformedText {
        return TransformedText(AppConstant.buildAnnotatedStringWithColors(text.toString(),searchText), OffsetMapping.Identity)
    }

}