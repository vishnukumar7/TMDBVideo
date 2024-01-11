package com.app.tmdbvideo.page

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.app.tmdbvideo.Extension.toBackdropUrl
import com.app.tmdbvideo.R
import com.app.tmdbvideo.Routes
import com.app.tmdbvideo.page.home.MainViewModel

@Composable
fun DetailTvScreenPage(mainViewModel: MainViewModel,movieId : String, navController: NavHostController){

}


@Composable
fun DetailMovieScreenPage(mainViewModel: MainViewModel,movieId : String, navController: NavHostController){

}


@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun viewAllTv(mainViewModel: MainViewModel, navController: NavHostController) {
    val tvList by mainViewModel.trendingTvList.observeAsState(emptyList())
    val context = LocalConfiguration.current
    val size = (context.screenWidthDp / 2) - 5
    LaunchedEffect(Unit) {
        mainViewModel.getHomeData()
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

