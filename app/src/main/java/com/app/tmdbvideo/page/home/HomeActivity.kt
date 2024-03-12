package com.app.tmdbvideo.page.home

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.AppBarDefaults
import androidx.compose.material.BottomAppBar
import androidx.compose.material.FabPosition
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.TopAppBar
import androidx.compose.material.contentColorFor
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.sharp.ArrowBack
import androidx.compose.material.primarySurface
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.app.tmdbvideo.Extension.toPosterUrl
import com.app.tmdbvideo.R
import com.app.tmdbvideo.Routes
import com.app.tmdbvideo.model.FabItem
import com.app.tmdbvideo.model.ResultItem
import com.app.tmdbvideo.page.FloatActionButtonWithSubItem
import com.app.tmdbvideo.page.SearchScreenPage
import com.app.tmdbvideo.page.ViewAllMovie
import com.app.tmdbvideo.page.ViewAllTv
import com.app.tmdbvideo.page.ViewModelClassFactory
import com.app.tmdbvideo.page.detail.DetailMovieScreenPage
import com.app.tmdbvideo.page.detail.DetailTvScreenPage
import com.app.tmdbvideo.ui.theme.TMDBVideoTheme
import com.app.tmdbvideo.ui.theme.topBarColor
import com.app.tmdbvideo.util.AppConstant
import com.app.tmdbvideo.util.AppConstant.HEADER_HOME
import com.app.tmdbvideo.util.Home
import com.app.tmdbvideo.util.MyBottomAppBar

class HomeActivity : ComponentActivity() {
    private lateinit var homeViewModel: HomeViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val modelFactory = ViewModelClassFactory(application)
        homeViewModel = ViewModelProvider(this, modelFactory)[HomeViewModel::class.java]
        setContent {
            TMDBVideoTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Color.Black
                ) {
                    ScreenHome(homeViewModel = homeViewModel, this)
                }
            }
        }
    }
}

@Composable
fun ScreenHome(
    homeViewModel: HomeViewModel,
    activity: ComponentActivity,
    startPosition: String = Routes.HomeViewScreen.routes
) {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = startPosition) {
        composable(Routes.HomeViewScreen.routes) {
            HomePageView(homeViewModel = homeViewModel, activity, navController)
        }

        composable(Routes.MovieListScreen.routes) {
            HomePageView(homeViewModel = homeViewModel, activity, navController)
        }

        composable(Routes.TvSeriesListScreen.routes) {
            HomePageView(homeViewModel = homeViewModel, activity, navController)
        }

        composable("${Routes.DetailMovieViewScreen.routes}/{movieId}") {
            val movieId = it.arguments?.getString("movieId")
            movieId?.let {
                DetailMovieScreenPage(activity = activity, movieId, navController)
            }
        }

        composable("${Routes.DetailTvViewScreen.routes}/{seriesId}") {
            val seriesId = it.arguments?.getString("seriesId")
            seriesId?.let {
                DetailTvScreenPage(activity = activity, seriesId, navController)
            }
        }

        composable("${Routes.ViewAllTVScreen.routes}/{type}") {
            val typeCategories = it.arguments?.getString("type")
            typeCategories?.let {
                ViewAllTv(
                    homeViewModel = homeViewModel,
                    type = typeCategories,
                    navController = navController
                )
            }
        }

        composable("${Routes.ViewAllMovieScreen.routes}/{type}") {
            val typeCategories = it.arguments?.getString("type")
            typeCategories?.let {
                ViewAllMovie(
                    homeViewModel = homeViewModel,
                    type = typeCategories,
                    navController = navController
                )
            }
        }

        composable(Routes.SearchFilterOption.routes) {
            SearchScreenPage(activity, navController)
        }

        composable(Routes.ComingSoonScreen.routes){
            ComingSoonPage(activity,navController)
        }


    }
}

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun ComingSoonPage(activity: ComponentActivity, navController: NavHostController) {
    Scaffold(
        scaffoldState = rememberScaffoldState(),
        /*floatingActionButton = {
            FloatingActionBarCompose {
                homeViewModel.defaultTypeList = it
                homeViewModel.getHomeData(it)
            }
        },*/
        bottomBar = {
            BottomBarCompose(navController)
        },
        //  isFloatingActionButtonDocked = true,
        backgroundColor = Color.Black,
        // floatingActionButtonPosition = FabPosition.End,
        topBar = { TopBarCompose(HEADER_HOME, navController) }
    ) {

    }
}

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun HomePageView(
    homeViewModel: HomeViewModel,
    activity: ComponentActivity,
    navController: NavHostController,
) {
    val lifeCycleOwner = LocalLifecycleOwner.current
    val lifeCycleState by lifeCycleOwner.lifecycle.currentStateFlow.collectAsState()
    val homeList by homeViewModel.homeList.observeAsState(emptyList())
    LaunchedEffect(Unit) {
        when (lifeCycleState) {
            Lifecycle.State.RESUMED -> {
                homeViewModel.getHomeData(homeViewModel.defaultTypeList)
            }

            else -> {

            }
        }
    }

    Scaffold(
        scaffoldState = rememberScaffoldState(),
        /*floatingActionButton = {
            FloatingActionBarCompose {
                homeViewModel.defaultTypeList = it
                homeViewModel.getHomeData(it)
            }
        },*/
        bottomBar = {
            BottomBarCompose(navController)
        },
      //  isFloatingActionButtonDocked = true,
        backgroundColor = Color.Black,
       // floatingActionButtonPosition = FabPosition.End,
        topBar = { TopBarCompose(HEADER_HOME, navController) }
    ) {
        if (homeList.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(
                    "Loading...",
                    textAlign = TextAlign.Center,
                    color = Color.White,
                    fontSize = 22.sp
                )
            }
        } else {
            Box(modifier = Modifier.padding(horizontal = 10.dp, vertical = 10.dp)) {
                LazyColumn {
                    items(items = homeList) { pageModel ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = pageModel.name,
                                fontWeight = FontWeight.Bold,
                                color = Color.White,
                                textAlign = TextAlign.Start
                            )
                            ClickableText(
                                text = AnnotatedString(text = "View All"),
                                style = TextStyle(
                                    textAlign = TextAlign.End,
                                    color = Color.Red
                                ),
                                onClick = {
                                    if (pageModel.name == AppConstant.HOME_HEADER_POPULAR_MOVIE || pageModel.name == AppConstant.HOME_HEADER_TOP_RATED_MOVIE) {
                                        navController.navigate(
                                            "${Routes.ViewAllMovieScreen.routes}/{type}".replace(
                                                oldValue = "{type}",
                                                newValue = pageModel.name
                                            )
                                        )
                                    } else {
                                        navController.navigate(
                                            "${Routes.ViewAllTVScreen.routes}/{type}".replace(
                                                oldValue = "{type}",
                                                newValue = pageModel.name
                                            )
                                        )
                                    }
                                })
                        }
                        Spacer(modifier = Modifier.height(5.dp))
                        if (pageModel.listItem.isNotEmpty()) {
                            RowView(
                                listItem = pageModel.listItem,
                                navController = navController
                            )
                        }
                        Spacer(modifier = Modifier.height(20.dp))
                    }
                }
            }
        }
    }

}

@Composable
fun FloatingActionBarCompose(onFabItemClicked: (String) -> Unit) {
    FloatActionButtonWithSubItem(
        fabIcon = painterResource(id = R.drawable.filter), items = arrayListOf(
            FabItem(painterResource(id = R.drawable.movies), label = "Movies", onFabItemClicked = {
                onFabItemClicked.invoke("Movie")
            }),
            FabItem(painterResource(id = R.drawable.series), label = "Tv", onFabItemClicked = {
                onFabItemClicked.invoke("Tv")
            })
        ), showLabels = true
    )
}

@Composable
fun TopBarCompose(title: String, navController: NavHostController? = null) {
    TopAppBar(
        title = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
            ) {
                if (title != "Home") {
                    IconButton(onClick = { navController?.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Sharp.ArrowBack,
                            contentDescription = "back",
                            tint = Color.White, modifier = Modifier.fillMaxHeight()
                        )
                    }
                    Spacer(modifier = Modifier.width((-10).dp))
                }
                Box(
                    contentAlignment = Alignment.Center, modifier = Modifier
                        .fillMaxHeight()
                        .fillMaxWidth()
                ) {
                    Text(
                        text = title,
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                    )
                }

            }
        },
        backgroundColor = topBarColor,
        actions = {
            if (title == "Home") {
                IconButton(onClick = {
                    navController?.navigate(Routes.SearchFilterOption.routes)
                }) {
                    Icon(Icons.Filled.Search, null, tint = Color.White)
                }
            }
        })
}

@Composable
fun BottomBarCompose(navController: NavHostController) {
    MyBottomAppBar(cutoutShape = RectangleShape, backgroundColor = Color.Black) {
        NavigationItem(
            navController = navController,
            routesString = Routes.HomeViewScreen.routes,
            label = "Home",
            icon = Home
        )
        NavigationItem(
            navController = navController,
            routesString = Routes.ComingSoonScreen.routes,
            label = "Home", painterIcon = painterResource(id = R.drawable.coming_soon)
        )
    }
}

@Composable
fun NavigationItem(
    navController: NavHostController,
    routesString: String,
    label: String,
    icon: ImageVector? = null,
    painterIcon: Painter? = null
) {
    IconButton(onClick = { navController.navigate(routesString) }) {
        Column(verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
            if (icon != null)
                Icon(imageVector = icon, contentDescription = label, tint = Color.White)
            else if (painterIcon != null)
                Icon(painter = painterIcon, contentDescription = label, tint = Color.White)
            Text(text = label, color = Color.White, fontSize = 16.sp)
        }
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RowView(listItem: List<ResultItem>, navController: NavHostController) {
    LazyRow {
        items(items = listItem) {
            Card(modifier = Modifier.padding(5.dp), shape = CardDefaults.outlinedShape, onClick = {
                navController.navigate(
                    "${Routes.DetailMovieViewScreen.routes}/{movieId}".replace(
                        oldValue = "{movieId}",
                        newValue = it.id.toString()
                    )
                )
            }) {
                Column {
                    ViewAsyncImage(
                        model = it.posterPath.toPosterUrl(),
                        contentDescription = if (it.mediaType.equals(
                                "movie",
                                true
                            )
                        ) it.titleMovie else it.nameTv, modifier = Modifier
                            .width(150.dp)
                            .height(150.dp)
                    )
                }
            }
        }
    }
}


@Composable
fun ViewAsyncImage(model: String, contentDescription: String, modifier: Modifier) {
    Log.e("TAG", "ViewAsyncImage: $model")
    AsyncImage(
        model = model,
        contentDescription = contentDescription,
        placeholder = painterResource(id = R.drawable.placeholder_image),
        error = painterResource(id = R.drawable.error_image),
        contentScale = ContentScale.FillBounds, modifier = modifier
    )
}


@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    TMDBVideoTheme {
        Greeting("Android")
    }
}