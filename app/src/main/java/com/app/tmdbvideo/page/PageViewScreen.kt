package com.app.tmdbvideo.page

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.lazy.items
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.app.tmdbvideo.Extension.toBackdropUrl
import com.app.tmdbvideo.Extension.toOriginalUrl
import com.app.tmdbvideo.Extension.toPosterUrl
import com.app.tmdbvideo.R
import com.app.tmdbvideo.Routes
import com.app.tmdbvideo.page.detail.DetailViewModel
import com.app.tmdbvideo.page.home.MainViewModel
import com.app.tmdbvideo.page.home.viewAsyncImage
import com.app.tmdbvideo.util.AppConstant

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailTvScreenPage(detailViewModel: DetailViewModel,movieId : String, navController: NavHostController){
    val imageList by detailViewModel.imageList.observeAsState()
    val lifeCycleOwner = LocalLifecycleOwner.current
    val lifeCycleState by lifeCycleOwner.lifecycle.currentStateFlow.collectAsState()
    LaunchedEffect(Unit){
        when(lifeCycleState){
            Lifecycle.State.RESUMED -> {

            }

            else -> {

            }
        }
        detailViewModel.apiCall(movieId)
    }


    if(imageList==null){
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Loading...", textAlign = TextAlign.Center)
        }
    }else{
        Box(modifier = Modifier.fillMaxSize()){
            LazyRow {
                items(items = imageList!!.backdrops) {
                    Card(modifier = Modifier.padding(5.dp), shape = CardDefaults.outlinedShape) {
                        Column {
                            viewAsyncImage(
                                model = it.filePath.toOriginalUrl(),
                                contentDescription = it.filePath, modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }
                }
            }
        }
    }
}


@Composable
fun DetailMovieScreenPage(detailViewModel: DetailViewModel,movieId : String, navController: NavHostController){

}


@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun ViewAllTv(mainViewModel: MainViewModel,type: String, navController: NavHostController) {
    val tvList by mainViewModel.trendingTvList.observeAsState(emptyList())
    val context = LocalConfiguration.current
    val lifeCycleOwner = LocalLifecycleOwner.current
    val lifeCycleState by lifeCycleOwner.lifecycle.currentStateFlow.collectAsState()
    val size = (context.screenWidthDp / 2)
    val gridState = rememberLazyGridState()
    LaunchedEffect(Unit) {

        when(lifeCycleState){
            Lifecycle.State.STARTED -> {
                if(type==AppConstant.HOME_HEADER_POPULAR_TV)
                    mainViewModel.getPopularTvList(search = AppConstant.TAG_VIEW_ALL)
                else mainViewModel.getTopRatedTvList(search = AppConstant.TAG_VIEW_ALL)
            }

            else -> {

            }
        }
    }

    if (tvList.isEmpty()) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Loading...", textAlign = TextAlign.Center)
        }
    } else {
        Box(modifier = Modifier.fillMaxSize())
        LazyVerticalGrid(columns = GridCells.Fixed(2), state = gridState) {
            items(tvList.size) {
                Card(modifier = Modifier.padding(5.dp), shape = CardDefaults.outlinedShape, onClick = {
                    navController.navigate(Routes.DetailTvViewScreen.routes)
                }) {
                    Column{
                        viewAsyncImage(model = tvList[it].posterPath.toBackdropUrl(), contentDescription = tvList[it].name, modifier = Modifier
                            .width(size.dp)
                            .height((size + 100).dp))
                    }
                }

            }
            if(gridState.firstVisibleItemIndex+10>tvList.size && mainViewModel.apiPage==mainViewModel.currentPage){
                if(type==AppConstant.HOME_HEADER_POPULAR_TV)
                    mainViewModel.getPopularTvList(search = AppConstant.TAG_VIEW_ALL, page = mainViewModel.currentPage+1)
                else mainViewModel.getTopRatedTvList(search = AppConstant.TAG_VIEW_ALL, page = mainViewModel.currentPage+1)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun ViewAllMovie(mainViewModel: MainViewModel,type: String, navController: NavHostController) {
    val tvList by mainViewModel.trendingTvList.observeAsState(emptyList())
    val lifeCycleOwner = LocalLifecycleOwner.current
    val lifeCycleState by lifeCycleOwner.lifecycle.currentStateFlow.collectAsState()
    val context = LocalConfiguration.current
    val size = (context.screenWidthDp / 2) - 5
    LaunchedEffect(Unit) {
        when(lifeCycleState){
            Lifecycle.State.STARTED -> {
                if(type==AppConstant.HOME_HEADER_POPULAR_MOVIE)
                    mainViewModel.getPopularMovieList()
                else mainViewModel.getTopRatedMovieList()
            }

            else -> {

            }
        }
    }

    if (tvList.isEmpty()) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Loading...", textAlign = TextAlign.Center)
        }
    } else {
        Box(modifier = Modifier.fillMaxSize())
        LazyVerticalGrid(columns = GridCells.Fixed(2)) {
            items(tvList.size) {
                Card(modifier = Modifier.padding(5.dp), shape = CardDefaults.outlinedShape, onClick = {
                    navController.navigate(Routes.DetailTvViewScreen.routes)
                }) {
                    Column{
                        AsyncImage(
                            model = tvList[it].posterPath.toBackdropUrl(),
                            contentDescription = tvList[it].name,
                            placeholder = painterResource(id = R.drawable.placeholder_image),
                            error = painterResource(id = R.drawable.error_image),
                            contentScale = ContentScale.FillBounds
                        )
                    }
                }

            }
        }
    }
}

