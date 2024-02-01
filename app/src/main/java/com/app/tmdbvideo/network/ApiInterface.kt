package com.app.tmdbvideo.network

import com.app.tmdbvideo.model.CollectionResponse
import com.app.tmdbvideo.model.DetailImageResponse
import com.app.tmdbvideo.model.GenreResponse
import com.app.tmdbvideo.model.MovieDetailResponse
import com.app.tmdbvideo.model.MovieResponse
import com.app.tmdbvideo.model.SearchCollectionResponse
import com.app.tmdbvideo.model.TvDetailResponse
import com.app.tmdbvideo.model.TvResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiInterface {

    @GET("3/tv/popular")
    fun getPopularTv(@Query("language") language : String="en-US",@Query("page")page : String) : Call<TvResponse>

    @GET("3/tv/on_the_air")
    fun getOnTheAirTv(@Query("language") language : String="en-US",@Query("page")page : String) : Call<TvResponse>

    @GET("3/tv/airing_today")
    fun getAiringTodayTv(@Query("language") language : String="en-US",@Query("page")page : String) : Call<TvResponse>

    @GET("3/tv/top_rated")
    fun getTopRatedTv(@Query("language") language : String="en-US",@Query("page")page : String) : Call<TvResponse>


    @GET("3/trending/tv/day")
    fun getTrendingTvByDay(@Query("language") language : String="en-US") : Call<TvResponse>

    @GET("3/trending/tv/week")
    fun getTrendingTvByWeek(@Query("language") language : String="en-US") : Call<TvResponse>

    @GET("3/movie/now_playing")
    fun getNowPlayingMovie(@Query("language") language : String="en-US",@Query("page")page : String) : Call<MovieResponse>

    @GET("3/movie/popular")
    fun getPopularMovie(@Query("language") language : String="en-US",@Query("page")page : String) : Call<MovieResponse>



    @GET("3/movie/top_rated")
    fun getTopRatedMovie(@Query("language") language : String="en-US",@Query("page")page : String) : Call<MovieResponse>

    @GET("3/movie/upcoming")
    fun getUpcomingMovie(@Query("language") language : String="en-US",@Query("page")page : String) : Call<MovieResponse>


    @GET("3/trending/movie/day")
    fun getTrendingMovieByDay(@Query("language") language : String="en-US") : Call<TvResponse>

    @GET("3/trending/movie/week")
    fun getTrendingMovieByWeek(@Query("language") language : String="en-US") : Call<TvResponse>

    @GET("3/trending/all/day")
    fun getTrendingByDay(@Query("language") language : String="en-US") : Call<TvResponse>

    @GET("3/trending/all/week")
    fun getTrendingByWeek(@Query("language") language : String="en-US") : Call<TvResponse>

    @GET("3/tv/{series_d}/images")
    fun getSeriesImage(@Path("series_d") seriesId : String) : Call<DetailImageResponse>

    @GET("3/movie/{movie_id}/images")
    fun getMoviesImage(@Path("movie_id") movieId : String) : Call<DetailImageResponse>

    @GET("3/tv/{series_id}")
    fun getTvDetail(@Path("series_id") seriesId : String) : Call<TvDetailResponse>

    @GET("3/movie/{movie_id}")
    fun getMovieDetail(@Path("movie_id") movieId : String) : Call<MovieDetailResponse>

    @GET("3/collection/{collection_id}")
    fun getCollection(@Path("collection_id")collectionId : String) : Call<CollectionResponse>

    @GET("3/search/collection")
    fun getSearchCollection(@Query("query")query : String,@Query("include_adult") adult : Boolean=false,@Query("language")language : String="en-US",@Query("page") page : Int =1) : Call<SearchCollectionResponse>

    @GET("3/search/movie")
    fun getSearchMovie(@Query("query")query : String,@Query("include_adult") adult : Boolean=false,@Query("language")language : String="en-US",@Query("region")region : String="US",@Query("page") page : Int =1) : Call<MovieResponse>


    @GET("3/genre/movie/list")
    fun getGenresMovieList(@Query("language")language : String="en-US") : Call<GenreResponse>

    @GET("3/genre/tv/list")
    fun getGenresTvList(@Query("language")language : String="en-US") : Call<GenreResponse>


}