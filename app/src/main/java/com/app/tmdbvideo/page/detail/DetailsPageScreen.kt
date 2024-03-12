package com.app.tmdbvideo.page.detail

import androidx.activity.ComponentActivity
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavHostController
import com.app.tmdbvideo.Extension.toOriginalUrl
import com.app.tmdbvideo.model.CollectionResponse
import com.app.tmdbvideo.model.DetailImageResponse
import com.app.tmdbvideo.model.MovieDetailResponse
import com.app.tmdbvideo.page.TabContent
import com.app.tmdbvideo.page.Tabs
import com.app.tmdbvideo.page.ViewModelClassFactory
import com.app.tmdbvideo.page.home.ViewAsyncImage
import com.google.accompanist.pager.ExperimentalPagerApi

@Composable
fun DetailTvScreenPage(
    activity: ComponentActivity,
    movieId: String,
    navController: NavHostController
) {

}



@OptIn(ExperimentalFoundationApi::class, ExperimentalPagerApi::class)
@Composable
fun DetailMovieScreenPage(
    activity: ComponentActivity,
    movieId: String,
    navController: NavHostController
) {
    val modelFactory = ViewModelClassFactory(activity.application)
    val detailViewModel: DetailViewModel =
        ViewModelProvider(activity, modelFactory)[DetailViewModel::class.java]
    val imageList by detailViewModel.detailPage.observeAsState(emptyList())
    val lifeCycleOwner = LocalLifecycleOwner.current
    val lifeCycleState by lifeCycleOwner.lifecycle.currentStateFlow.collectAsState()
    val context = LocalConfiguration.current
    LaunchedEffect(Unit) {
        when (lifeCycleState) {
            Lifecycle.State.RESUMED -> {

            }

            else -> {

            }
        }
        detailViewModel.apiCall(movieId)
    }

    val pagerState = com.google.accompanist.pager.rememberPagerState(pageCount = 1)


    if (imageList.isEmpty()) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Loading...", textAlign = TextAlign.Center)
        }
    } else {
        LazyColumn {
            items(items = imageList) { pageModel ->
                if (pageModel is DetailImageResponse) {
                    HorizontalPager(
                        state = rememberPagerState { pageModel.backdrops.size },
                        pageSpacing = 0.dp,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(300.dp),
                        contentPadding = PaddingValues(0.dp),
                        pageSize = PageSize.Fill,
                    ) {
                        Card(
                            modifier = Modifier.padding(5.dp),
                            shape = CardDefaults.outlinedShape
                        ) {
                            Column {
                                ViewAsyncImage(
                                    model = pageModel.backdrops[it].filePath.toOriginalUrl(),
                                    contentDescription = pageModel.backdrops[it].filePath,
                                    modifier = Modifier.fillMaxWidth()
                                )
                            }
                        }
                    }
                } else if (pageModel is MovieDetailResponse) {
                    Box(modifier = Modifier.padding(10.dp)) {
                        Column {
                            Text(text = pageModel.title, fontSize = 18.sp, color = Color.White)
                            Spacer(modifier = Modifier.height(10.dp))
                            Text(
                                text = pageModel.releaseDate.split('-')[0],
                                fontSize = 14.sp,
                                color = Color.White
                            )
                            Spacer(modifier = Modifier.height(10.dp))
                            Text(text = pageModel.overview, fontSize = 12.sp, color = Color.White)
                        }
                    }
                }/*else if(pageModel is CollectionResponse){
                   Column {
                       Tabs(pagerState = pagerState)
                       TabContent(pagerState = pagerState,pageModel,context.screenWidthDp)
                   }
               }*/

            }
            item {
                val colllectionList = imageList.filterIsInstance<CollectionResponse>()
                if (colllectionList.isNotEmpty()) {
                    Column {
                        Tabs(pagerState = pagerState)
                        TabContent(
                            pagerState = pagerState,
                            colllectionList[0],
                            context.screenWidthDp
                        )
                    }
                }
            }
        }
    }
}