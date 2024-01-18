package com.app.tmdbvideo.network

import com.app.tmdbvideo.model.CollectionResponse
import com.app.tmdbvideo.model.DetailImageResponse
import com.app.tmdbvideo.model.MovieDetailResponse
import com.app.tmdbvideo.model.MovieResponse
import com.app.tmdbvideo.model.SearchCollectionResponse
import com.app.tmdbvideo.model.TrendingTvResponse
import com.app.tmdbvideo.model.TvDetailResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiInterface {

    @GET("3/tv/popular")
    fun getPopularTv(@Query("language") language : String="en-US",@Query("page")page : String) : Call<TrendingTvResponse>

    @GET("3/tv/top_rated")
    fun getTopRatedTv(@Query("language") language : String="en-US",@Query("page")page : String) : Call<TrendingTvResponse>

    @GET("3/movie/popular")
    fun getPopularMovie(@Query("language") language : String="en-US",@Query("page")page : String) : Call<MovieResponse>

    @GET("3/movie/top_rated")
    fun getTopRatedMovie(@Query("language") language : String="en-US",@Query("page")page : String) : Call<MovieResponse>

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
}