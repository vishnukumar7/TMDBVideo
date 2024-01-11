package com.app.tmdbvideo.model

import com.google.gson.annotations.SerializedName

data class DetailImageResponse(

	@field:SerializedName("backdrops")
	val backdrops: List<BackdropsItem>,

	@field:SerializedName("posters")
	val posters: List<PostersItem>,

	@field:SerializedName("id")
	val id: Int,

	@field:SerializedName("logos")
	val logos: List<LogosItem>
)

data class LogosItem(

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

data class PostersItem(

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

data class BackdropsItem(

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
