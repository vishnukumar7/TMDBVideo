package com.app.tmdbvideo.page

import android.annotation.SuppressLint
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.app.tmdbvideo.Extension.optString
import com.app.tmdbvideo.Extension.toBackdropUrl
import com.app.tmdbvideo.Extension.toOriginalUrl
import com.app.tmdbvideo.R
import com.app.tmdbvideo.Routes
import com.app.tmdbvideo.model.CollectionResponse
import com.app.tmdbvideo.model.DetailImageResponse
import com.app.tmdbvideo.model.MovieDetailResponse
import com.app.tmdbvideo.page.detail.DetailViewModel
import com.app.tmdbvideo.page.home.MainViewModel
import com.app.tmdbvideo.page.home.ViewAsyncImage
import com.app.tmdbvideo.util.AppConstant
import com.google.accompanist.pager.ExperimentalPagerApi

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailTvScreenPage(
    activity: ComponentActivity,
    movieId: String,
    navController: NavHostController
) {

}

@Composable
fun DetailMovieScreenPageNew(
    detailViewModel: DetailViewModel,
    movieId: String,
    navController: NavHostController
) {
    val imageList by detailViewModel.detailPage.observeAsState(emptyList())
    val lifeCycleOwner = LocalLifecycleOwner.current
    val lifeCycleState by lifeCycleOwner.lifecycle.currentStateFlow.collectAsState()
    LaunchedEffect(Unit) {
        when (lifeCycleState) {
            Lifecycle.State.RESUMED -> {

            }

            else -> {

            }
        }
        detailViewModel.apiCall(movieId)
    }

    if (imageList.isEmpty()) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Loading...", textAlign = TextAlign.Center)
        }
    } else {

    }
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


@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun ViewAllTv(mainViewModel: MainViewModel, type: String, navController: NavHostController) {
    val tvList by mainViewModel.trendingTvList.observeAsState(emptyList())
    val context = LocalConfiguration.current
    val lifeCycleOwner = LocalLifecycleOwner.current
    val lifeCycleState by lifeCycleOwner.lifecycle.currentStateFlow.collectAsState()
    val size = (context.screenWidthDp / 2)
    val gridState = rememberLazyGridState()
    LaunchedEffect(Unit) {

        when (lifeCycleState) {
            Lifecycle.State.STARTED -> {
                if (type == AppConstant.HOME_HEADER_POPULAR_TV)
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
            if (gridState.firstVisibleItemIndex + 10 > tvList.size && mainViewModel.apiPage == mainViewModel.currentPage) {
                if (type == AppConstant.HOME_HEADER_POPULAR_TV)
                    mainViewModel.getPopularTvList(
                        search = AppConstant.TAG_VIEW_ALL,
                        page = mainViewModel.currentPage + 1
                    )
                else mainViewModel.getTopRatedTvList(
                    search = AppConstant.TAG_VIEW_ALL,
                    page = mainViewModel.currentPage + 1
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun ViewAllMovie(mainViewModel: MainViewModel, type: String, navController: NavHostController) {
    val tvList by mainViewModel.trendingTvList.observeAsState(emptyList())
    val lifeCycleOwner = LocalLifecycleOwner.current
    val lifeCycleState by lifeCycleOwner.lifecycle.currentStateFlow.collectAsState()
    val context = LocalConfiguration.current
    val size = (context.screenWidthDp / 2) - 5
    LaunchedEffect(Unit) {
        when (lifeCycleState) {
            Lifecycle.State.STARTED -> {
                if (type == AppConstant.HOME_HEADER_POPULAR_MOVIE)
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


@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun SearchScreenPage(activity: ComponentActivity, navController: NavHostController) {
    val searchViewModel = ViewModelProvider(
        activity,
        ViewModelClassFactory(activity.application)
    )[SearchViewModel::class.java]
    val keyboardController = LocalSoftwareKeyboardController.current
    val queryResult by searchViewModel.searchQueryResult.observeAsState("")
    val searchResultList by searchViewModel.searchResultList.observeAsState(emptyList())

    Box {
        Column {
            TopAppBar(
                title = { Text(text = "") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = null,
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = Color.Black)
            )
            SearchBar(
                query = queryResult,
                onQueryChange = { searchViewModel.searchQueryChange(it) },
                onSearch = { keyboardController?.hide() },
                colors = SearchBarDefaults.colors(
                    containerColor = Color.Black,
                    dividerColor = Color.White,
                    inputFieldColors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White,
                        focusedIndicatorColor = Color.White,
                        disabledIndicatorColor = Color.White,
                        unfocusedIndicatorColor = Color.White,
                        cursorColor = Color.Red,
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White
                    )
                ),
                active = true,
                onActiveChange = {},
                trailingIcon = {
                    if (queryResult.isNotEmpty()) {
                        IconButton(onClick = { searchViewModel.searchQueryChange("") }) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                tint = Color.White,
                                contentDescription = null
                            )
                        }
                    }
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = null,
                        tint = Color.White
                    )
                },
                placeholder = {
                    Text(
                        text = "Search Movies",
                        color = Color.LightGray,
                        fontSize = 16.sp
                    )
                }) {

                if (searchResultList.isNotEmpty()) {
                    LazyColumn {
                        items(items = searchResultList) { item ->
                            Column {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    ViewAsyncImage(
                                        model = item.backdropPath.optString().toBackdropUrl(),
                                        contentDescription = item.titleMovie,
                                        modifier = Modifier
                                            .height(75.dp)
                                            .width(75.dp)
                                    )
                                    Spacer(modifier = Modifier.width(20.dp))
                                    TextField(
                                        value = item.titleMovie,
                                        onValueChange = { item.titleMovie = it },
                                       /* visualTransformation = ColorsTransFormation(queryResult),*/
                                        colors = TextFieldDefaults.colors(
                                            focusedContainerColor = Color.Black,
                                            unfocusedContainerColor = Color.Black,
                                            focusedIndicatorColor = Color.Black,
                                            focusedLabelColor = Color.Black
                                        ), readOnly = true
                                    )
                                }
                                Spacer(modifier = Modifier.height(10.dp))
                                Divider(color = Color.DarkGray)
                                Spacer(modifier = Modifier.height(10.dp))
                            }
                        }
                    }
                }
            }
        }
    }
    BackHandler {
        navController.popBackStack()
    }
}
