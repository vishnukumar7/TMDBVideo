package com.app.tmdbvideo.page

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material.Scaffold
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
import androidx.compose.ui.graphics.Color
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
import com.app.tmdbvideo.R
import com.app.tmdbvideo.Routes
import com.app.tmdbvideo.page.home.HomeViewModel
import com.app.tmdbvideo.page.home.TopBarCompose
import com.app.tmdbvideo.page.home.ViewAsyncImage
import com.app.tmdbvideo.util.AppConstant

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ViewAllTv(homeViewModel: HomeViewModel, type: String, navController: NavHostController) {
    val tvList by homeViewModel.trendingTvList.observeAsState(emptyList())
    val context = LocalConfiguration.current
    val lifeCycleOwner = LocalLifecycleOwner.current
    val lifeCycleState by lifeCycleOwner.lifecycle.currentStateFlow.collectAsState()
    val size = (context.screenWidthDp / 2)
    val gridState = rememberLazyGridState()
    LaunchedEffect(Unit) {

        when (lifeCycleState) {
            Lifecycle.State.STARTED -> {
                if (type == AppConstant.HOME_HEADER_POPULAR_TV)
                    homeViewModel.getPopularTvList(search = AppConstant.TAG_VIEW_ALL)
                else homeViewModel.getTopRatedTvList(search = AppConstant.TAG_VIEW_ALL)
            }

            else -> {

            }
        }
    }

    Scaffold(
        backgroundColor = Color.Black,
        topBar = {
        TopBarCompose(title = type, navController)
        }
    ) {
        if (tvList.isEmpty()) {
            Box(
                modifier = Modifier
                    .padding(it)
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text("Loading...", textAlign = TextAlign.Center)
            }
        } else {
            Box(modifier = Modifier
                .padding(it)
                .fillMaxSize())
            LazyVerticalGrid(columns = GridCells.Fixed(2), state = gridState) {
                items(tvList.size) {
                    Card(
                        modifier = Modifier.padding(5.dp),
                        shape = CardDefaults.outlinedShape,
                        onClick = {
                            navController.navigate(Routes.DetailTvViewScreen.routes)
                        }) {
                        Column {
                            ViewAsyncImage(
                                model = tvList[it].posterPath.toBackdropUrl(),
                                contentDescription = tvList[it].nameTv,
                                modifier = Modifier
                                    .width(size.dp)
                                    .height((size + 100).dp)
                            )
                        }
                    }

                }
                if (gridState.firstVisibleItemIndex + 10 > tvList.size && homeViewModel.apiPage == homeViewModel.currentPage) {
                    if (type == AppConstant.HOME_HEADER_POPULAR_TV)
                        homeViewModel.getPopularTvList(
                            search = AppConstant.TAG_VIEW_ALL,
                            page = homeViewModel.currentPage + 1
                        )
                    else homeViewModel.getTopRatedTvList(
                        search = AppConstant.TAG_VIEW_ALL,
                        page = homeViewModel.currentPage + 1
                    )
                }
            }
        }
    }
}

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ViewAllMovie(homeViewModel: HomeViewModel, type: String, navController: NavHostController) {
    val tvList by homeViewModel.trendingTvList.observeAsState(emptyList())
    val lifeCycleOwner = LocalLifecycleOwner.current
    val lifeCycleState by lifeCycleOwner.lifecycle.currentStateFlow.collectAsState()
    val context = LocalConfiguration.current
    val size = (context.screenWidthDp / 2) - 5
    LaunchedEffect(Unit) {
        when (lifeCycleState) {
            Lifecycle.State.STARTED -> {
                if (type == AppConstant.HOME_HEADER_POPULAR_MOVIE)
                    homeViewModel.getPopularMovieList()
                else homeViewModel.getTopRatedMovieList()
            }

            else -> {

            }
        }
    }

    Scaffold(
        backgroundColor = Color.Black,
        topBar = {
            TopBarCompose(title = type, navController)
        }
    ) {
        if (tvList.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Loading...", textAlign = TextAlign.Center)
            }
        } else {
            Box(modifier = Modifier.fillMaxSize())
            LazyVerticalGrid(columns = GridCells.Fixed(2)) {
                items(tvList.size) {
                    Card(
                        modifier = Modifier.padding(5.dp),
                        shape = CardDefaults.outlinedShape,
                        onClick = {
                            navController.navigate(Routes.DetailTvViewScreen.routes)
                        }) {
                        Column {
                            AsyncImage(
                                model = tvList[it].posterPath.toBackdropUrl(),
                                contentDescription = tvList[it].nameTv,
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


}
