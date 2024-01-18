package com.app.tmdbvideo.model

import com.google.gson.annotations.SerializedName

data class CollectionResponse(

	@field:SerializedName("backdrop_path")
	val backdropPath: String,

	@field:SerializedName("overview")
	val overview: String,

	@field:SerializedName("name")
	val name: String,

	@field:SerializedName("parts")
	val parts: List<PartsItem>,

	@field:SerializedName("id")
	val id: Int,

	@field:SerializedName("poster_path")
	val posterPath: String
)

data class PartsItem(

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

	@field:SerializedName("genre_ids")
	val genreIds: List<Int>,

	@field:SerializedName("poster_path")
	val posterPath: String,

	@field:SerializedName("backdrop_path")
	val backdropPath: String,

	@field:SerializedName("media_type")
	val mediaType: String,

	@field:SerializedName("release_date")
	val releaseDate: String,

	@field:SerializedName("popularity")
	val popularity: Any,

	@field:SerializedName("vote_average")
	val voteAverage: Any,

	@field:SerializedName("id")
	val id: Int,

	@field:SerializedName("adult")
	val adult: Boolean,

	@field:SerializedName("vote_count")
	val voteCount: Int
)

