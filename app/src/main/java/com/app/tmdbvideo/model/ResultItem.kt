package com.app.tmdbvideo.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.google.gson.annotations.SerializedName


@Entity(tableName = "tv_series_table")
data class TvResponse(

    @PrimaryKey(autoGenerate = true)
    var id: Int=0,

    @ColumnInfo(name = "page")
    @field:SerializedName("page")
    var page: Int=-1,

    @ColumnInfo(name = "total_pages")
    @field:SerializedName("total_pages")
    var totalPages: Int=0,


    @TypeConverters(DataConverter::class)
    @ColumnInfo(name = "tv_result")
    @field:SerializedName("results")
    var results: List<ResultItem> = ArrayList(),

    @ColumnInfo(name = "total_results")
    @field:SerializedName("total_results")
    var totalResults: Int=0,

    @ColumnInfo
    var type : String =""
)

@Entity(tableName = "movie_table")
data class MovieResponse(

    @PrimaryKey(autoGenerate = true)
    var id: Int=0,

    @ColumnInfo
    @field:SerializedName("page")
    var page: Int=-1,

    @ColumnInfo(name = "total_pages")
    @field:SerializedName("total_pages")
    var totalPages: Int =0 ,


    @ColumnInfo
    @TypeConverters(DataConverter::class)
    @field:SerializedName("results")
    var results: List<ResultItem> = ArrayList(),

    @ColumnInfo(name = "total_results")
    @field:SerializedName("total_results")
    var totalResults: Int = 0,

    @ColumnInfo
    var type : String =""
)
data class ResultItem(
    @field:SerializedName("overview")
    var overview: String="",

    @field:SerializedName("original_language")
    var originalLanguage: String="",

    @field:SerializedName("original_title")
    var originalTitleMovie: String="",

    @field:SerializedName("video")
    var videoMovie: Boolean=false,

    @field:SerializedName("title")
    var titleMovie: String="",

    @TypeConverters(DataConverter::class)
    @field:SerializedName("genre_ids")
    var genreIds: List<Int> = ArrayList(),

    @field:SerializedName("poster_path")
    var posterPath: String="",

    @field:SerializedName("backdrop_path")
    var backdropPath: String?="",

    @field:SerializedName("release_date")
    var releaseDateMovie: String ="",

    @field:SerializedName("popularity")
    var popularity: Double = 0.0,

    @field:SerializedName("vote_average")
    var voteAverage: Double=0.0,

    @field:SerializedName("id")
    var id: Int= -1,

    @field:SerializedName("adult")
    var adult: Boolean=false,

    @field:SerializedName("vote_count")
    var voteCount: Int = -1,

    @field:SerializedName("first_air_date")
    var firstAirDateTv: String="",

    @TypeConverters(DataConverter::class)
    @field:SerializedName("origin_country")
    var originCountryTv: List<String> = ArrayList(),

    @field:SerializedName("media_type")
    var mediaType: String = "",

    @field:SerializedName("original_name")
    var originalNameTv: String = "",

    @field:SerializedName("name")
    var nameTv: String="",

)
