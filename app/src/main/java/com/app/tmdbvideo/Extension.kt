package com.app.tmdbvideo

import androidx.compose.runtime.snapshots.SnapshotStateList
import com.app.tmdbvideo.model.HomePageModel
import com.app.tmdbvideo.model.MovieResponse
import com.app.tmdbvideo.model.ResultItem
import com.app.tmdbvideo.model.TvResponse

object Extension {

    fun String.toPosterUrl() = "https://image.tmdb.org/t/p/w342$this"

    fun String.toBackdropUrl() = "https://image.tmdb.org/t/p/original$this"

    fun String.toProfilePhotoUrl() = "https://image.tmdb.org/t/p/w185$this"

    fun String.toOriginalUrl() = "https://image.tmdb.org/t/p/original$this"

    fun String.toImdbProfileUrl() = "https://www.imdb.com/name/$this"

    fun String?.optString(replaceText : String="") = this ?: replaceText

    fun  SnapshotStateList<HomePageModel>.addValue(index: Int, homePageModel: HomePageModel) {
        if(this.isEmpty()){
            this.add(homePageModel)
        }else{
            val data=this.toList().filter { it.name==homePageModel.name }
            if(data.isEmpty()){
                this.add(index,homePageModel)
            }
        }
    }

    fun List<ResultItem>.addMediaType() : List<ResultItem>{
        this.forEach {
            it.mediaType = if(it.titleMovie.isNotEmpty()) "Movie" else if(it.nameTv.isNotEmpty()) "tv" else ""
        }
        return this
    }

    fun TvResponse.addMediaType() : TvResponse{
        this.results.forEach {
            it.mediaType = if(it.titleMovie.isNotEmpty()) "Movie" else if(it.nameTv.isNotEmpty()) "tv" else ""
        }
        return this
    }

    fun MovieResponse.addMediaType() : MovieResponse{
        this.results.forEach {
            it.mediaType = if(it.titleMovie.isNotEmpty()) "Movie" else if(it.nameTv.isNotEmpty()) "tv" else ""
        }
        return this
    }

}
