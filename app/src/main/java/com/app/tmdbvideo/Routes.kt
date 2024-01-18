package com.app.tmdbvideo

sealed class Routes(val routes : String){
    data object DetailMovieViewScreen : Routes("DetailMovie")
    data object DetailTvViewScreen : Routes("DetailTv")
    data object HomeViewScreen : Routes("Home")
    data object SearchFilterOption : Routes("search")
    data object ViewAllTVScreen : Routes("ViewAllTv")
    data object ViewAllMovieScreen : Routes("ViewAllMovie")


}
