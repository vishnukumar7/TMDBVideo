package com.app.tmdbvideo.page

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.app.tmdbvideo.page.detail.DetailViewModel
import com.app.tmdbvideo.page.home.MainViewModel

class ViewModelClassFactory(private var application: Application) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(MainViewModel::class.java) -> {
                MainViewModel(application) as T
            }

            modelClass.isAssignableFrom(DetailViewModel::class.java) -> {
                DetailViewModel(application) as T
            }

            modelClass.isAssignableFrom(SearchViewModel::class.java) -> {
                SearchViewModel(application) as T
            }

            else -> throw IllegalArgumentException("Unknown ViewModel Class")
        }
    }
}