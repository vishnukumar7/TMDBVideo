package com.app.tmdbvideo.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.google.gson.annotations.SerializedName

@Entity(tableName = "movie_table")
data class MovieResponse(

	@PrimaryKey(autoGenerate = true)
	var id: Int=0,

	@ColumnInfo
	@field:SerializedName("page")
	val page: Int,

	@ColumnInfo(name = "total_pages")
	@field:SerializedName("total_pages")
	var totalPages: Int,


	@ColumnInfo
	@TypeConverters(DataConverter::class)
	@field:SerializedName("results")
	var results: List<MovieResultsItem>,

	@ColumnInfo(name = "total_results")
	@field:SerializedName("total_results")
	var totalResults: Int,

	@ColumnInfo
	var type : String =""
)

data class MovieResultsItem(

	@field:SerializedName("overview")
	val overview: String,

	@field:SerializedName("original_language")
	val originalLanguage: String,

	@field:SerializedName("original_title")
	val originalTitle: String,

	@field:SerializedName("video")
	val video: Boolean,

	@field:SerializedName("title")
	val title: String,

	@TypeConverters(DataConverter::class)
	@field:SerializedName("genre_ids")
	val genreIds: List<Int>,

	@field:SerializedName("poster_path")
	val posterPath: String,

	@field:SerializedName("backdrop_path")
	val backdropPath: String,

	@field:SerializedName("release_date")
	val releaseDate: String,

	@field:SerializedName("popularity")
	val popularity: Double,

	@field:SerializedName("vote_average")
	val voteAverage: Double,

	@field:SerializedName("id")
	val id: Int,

	@field:SerializedName("adult")
	val adult: Boolean,

	@field:SerializedName("vote_count")
	val voteCount: Int
)
