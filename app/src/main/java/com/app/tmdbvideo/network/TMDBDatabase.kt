package com.app.tmdbvideo.network

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.app.tmdbvideo.model.DataConverter
import com.app.tmdbvideo.model.MovieResponse
import com.app.tmdbvideo.model.TvResponse


@Database(entities = [TvResponse::class,MovieResponse::class], version = 1, exportSchema = false)
@TypeConverters(DataConverter::class)
abstract class TMDBDatabase : RoomDatabase() {

    abstract fun tvSeriesDao() : TvSeriesDao

    abstract fun movieDao() : MoviesDao

    companion object{
        private var INSTANCE : TMDBDatabase? =null

        fun getInstance(context: Context): TMDBDatabase {
            synchronized(this){
                var instance = INSTANCE
                if(instance==null){
                    instance = Room.databaseBuilder(context.applicationContext,TMDBDatabase::class.java,"TMDB_database")
                        .fallbackToDestructiveMigration()
                        .allowMainThreadQueries()
                        .build()
                    INSTANCE = instance
                }
                return  instance
            }
        }
    }
}