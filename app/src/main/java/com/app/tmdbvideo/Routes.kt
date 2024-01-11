package com.app.tmdbvideo

sealed class Routes(val routes : String){
    object DetailMovieViewScreen : Routes("DetailMovie")
    object DetailTvViewScreen : Routes("DetailTv")
    object HomeViewScreen : Routes("Home")
    object SearchFilterOption : Routes("search")
    object ViewAllTVScreen : Routes("ViewAllTv")
    object ViewAllMovieScreen : Routes("ViewAllMovie")
}
