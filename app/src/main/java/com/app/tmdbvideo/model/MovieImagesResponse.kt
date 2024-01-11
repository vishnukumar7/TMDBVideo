package com.app.tmdbvideo.model

import com.google.gson.annotations.SerializedName

data class MovieImagesResponse(

	@field:SerializedName("backdrops")
	val backdrops: List<BackdropsMovieItem>,

	@field:SerializedName("posters")
	val posters: List<PostersMovieItem>,

	@field:SerializedName("id")
	val id: Int,

	@field:SerializedName("logos")
	val logos: List<LogosMovieItem>
)

data class LogosMovieItem(

	@field:SerializedName("aspect_ratio")
	val aspectRatio: Any,

	@field:SerializedName("file_path")
	val filePath: String,

	@field:SerializedName("vote_average")
	val voteAverage: Int,

	@field:SerializedName("width")
	val width: Int,

	@field:SerializedName("iso_639_1")
	val iso6391: String,

	@field:SerializedName("vote_count")
	val voteCount: Int,

	@field:SerializedName("height")
	val height: Int
)

data class BackdropsMovieItem(

	@field:SerializedName("aspect_ratio")
	val aspectRatio: Any,

	@field:SerializedName("file_path")
	val filePath: String,

	@field:SerializedName("vote_average")
	val voteAverage: Any,

	@field:SerializedName("width")
	val width: Int,

	@field:SerializedName("iso_639_1")
	val iso6391: Any,

	@field:SerializedName("vote_count")
	val voteCount: Int,

	@field:SerializedName("height")
	val height: Int
)

data class PostersMovieItem(

	@field:SerializedName("aspect_ratio")
	val aspectRatio: Any,

	@field:SerializedName("file_path")
	val filePath: String,

	@field:SerializedName("vote_average")
	val voteAverage: Any,

	@field:SerializedName("width")
	val width: Int,

	@field:SerializedName("iso_639_1")
	val iso6391: String,

	@field:SerializedName("vote_count")
	val voteCount: Int,

	@field:SerializedName("height")
	val height: Int
)
