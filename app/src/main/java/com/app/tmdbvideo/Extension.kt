package com.app.tmdbvideo

import androidx.compose.runtime.snapshots.SnapshotStateList
import com.app.tmdbvideo.model.HomePageModel

object Extension {

    fun String.toPosterUrl() = "https://image.tmdb.org/t/p/w342$this"

    fun String.toBackdropUrl() = "https://image.tmdb.org/t/p/original$this"

    fun String.toProfilePhotoUrl() = "https://image.tmdb.org/t/p/w185$this"

    fun String.toOriginalUrl() = "https://image.tmdb.org/t/p/original$this"

    fun String.toImdbProfileUrl() = "https://www.imdb.com/name/$this"

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

}
