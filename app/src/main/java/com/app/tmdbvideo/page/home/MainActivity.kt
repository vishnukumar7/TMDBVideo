package com.app.tmdbvideo.page.home

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SearchBar
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
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
import com.app.tmdbvideo.model.MovieResultsItem
import com.app.tmdbvideo.model.TvResultsItem
import com.app.tmdbvideo.page.DetailMovieScreenPage
import com.app.tmdbvideo.page.DetailTvScreenPage
import com.app.tmdbvideo.page.SearchScreenPage
import com.app.tmdbvideo.page.ViewAllMovie
import com.app.tmdbvideo.page.ViewAllTv
import com.app.tmdbvideo.page.ViewModelClassFactory
import com.app.tmdbvideo.ui.theme.TMDBVideoTheme
import com.app.tmdbvideo.ui.theme.topBarColor
import com.app.tmdbvideo.util.AppConstant

class MainActivity : ComponentActivity() {
    private lateinit var mainViewModel: MainViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val modelFactory = ViewModelClassFactory(application)
        mainViewModel = ViewModelProvider(this,modelFactory)[MainViewModel::class.java]
        setContent {
            TMDBVideoTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Color.Black
                ) {
                    ScreenHome(mainViewModel = mainViewModel, this)
                }
            }
        }
    }
}

@Composable
fun ScreenHome(mainViewModel: MainViewModel,activity: ComponentActivity,startPosition : String = Routes.HomeViewScreen.routes){
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = startPosition){
        composable(Routes.HomeViewScreen.routes){
            HomePageView(mainViewModel = mainViewModel,activity,navController)
        }

        composable("${Routes.DetailMovieViewScreen.routes}/{movieId}"){
            val movieId = it.arguments?.getString("movieId")
            movieId?.let {
                DetailMovieScreenPage(activity = activity,movieId,navController)
            }
        }

        composable("${Routes.DetailTvViewScreen.routes}/{seriesId}"){
            val seriesId = it.arguments?.getString("seriesId")
            seriesId?.let {
                DetailTvScreenPage(activity = activity,seriesId,navController)
            }
        }

        composable("${Routes.ViewAllTVScreen.routes}/{type}"){
            val typeCategories= it.arguments?.getString("type")
            typeCategories?.let {
                ViewAllTv(mainViewModel = mainViewModel, type = typeCategories, navController = navController)
            }
        }

        composable("${Routes.ViewAllMovieScreen.routes}/{type}"){
            val typeCategories= it.arguments?.getString("type")
            typeCategories?.let {
                ViewAllMovie(mainViewModel = mainViewModel, type = typeCategories, navController = navController)
            }
        }

        composable(Routes.SearchFilterOption.routes){
            SearchScreenPage(activity,navController)
        }


    }
}

@Composable
fun HomePageView1(mainViewModel: MainViewModel,activity: ComponentActivity,navController: NavHostController){
    val lifeCycleOwner = LocalLifecycleOwner.current
    val lifeCycleState by lifeCycleOwner.lifecycle.currentStateFlow.collectAsState()
    val homeList by mainViewModel.homeList.observeAsState(emptyList())
    LaunchedEffect(Unit){
        when(lifeCycleState){
            Lifecycle.State.RESUMED -> {
                mainViewModel.getHomeData()
            }

            else -> {

            }
        }
    }

    if (homeList.isEmpty()) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Loading...", textAlign = TextAlign.Center, color = Color.White, fontSize = 22.sp)
        }
    } else {
       Box {
          Column {
              TopAppBar(elevation = 4.dp, title = {}, backgroundColor = topBarColor, actions = {
                  IconButton(onClick = {
                      navController.navigate(Routes.SearchFilterOption.routes)
                  }) {
                      Icon(Icons.Filled.Search,null, tint = Color.White)
                  }
              })
              Box(modifier = Modifier.padding(horizontal = 10.dp, vertical = 10.dp)) {
                  LazyColumn {
                      items(items = homeList) {pageModel->
                          Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween){
                              Text(text = pageModel.name, fontWeight = FontWeight.Bold, color = Color.White, textAlign = TextAlign.Start)
                              ClickableText(text = AnnotatedString(text = "View All"), style = TextStyle(textAlign = TextAlign.End, color = Color.Red), onClick = {
                                  if(pageModel.name==AppConstant.HOME_HEADER_POPULAR_MOVIE || pageModel.name==AppConstant.HOME_HEADER_TOP_RATED_MOVIE){
                                      navController.navigate("${Routes.ViewAllMovieScreen.routes}/{type}".replace(oldValue = "{type}", newValue = pageModel.name))
                                  }else{
                                      navController.navigate("${Routes.ViewAllTVScreen.routes}/{type}".replace(oldValue = "{type}", newValue = pageModel.name))
                                  }
                              })
                          }
                          Spacer(modifier = Modifier.height(5.dp))
                          if(pageModel.listItem.isNotEmpty()){
                              if(pageModel.listItem[0] is TvResultsItem){
                                  RowViewTv(listItem = pageModel.listItem as List<TvResultsItem>, navController = navController)
                              }else{
                                  RowViewMovie(listItem = pageModel.listItem as List<MovieResultsItem>, navController = navController)
                              }
                          }
                          Spacer(modifier = Modifier.height(20.dp))
                      }
                  }
              }
          }
       }
    }
}

@Composable
fun HomePageView(mainViewModel: MainViewModel,activity: ComponentActivity,navController: NavHostController){
    val lifeCycleOwner = LocalLifecycleOwner.current
    val lifeCycleState by lifeCycleOwner.lifecycle.currentStateFlow.collectAsState()
    val homeList by mainViewModel.homeList.observeAsState(emptyList())
    LaunchedEffect(Unit){
        when(lifeCycleState){
            Lifecycle.State.RESUMED -> {
                mainViewModel.getHomeData()
            }

            else -> {

            }
        }
    }

    Box {
        Column {
            TopAppBar(elevation = 4.dp, title = {}, backgroundColor = topBarColor, actions = {
                IconButton(onClick = {
                    navController.navigate(Routes.SearchFilterOption.routes)
                }) {
                    Icon(Icons.Filled.Search,null, tint = Color.White)
                }
            })
            if (homeList.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("Loading...", textAlign = TextAlign.Center, color = Color.White, fontSize = 22.sp)
                }
            } else {
                Box(modifier = Modifier.padding(horizontal = 10.dp, vertical = 10.dp)) {
                    LazyColumn {
                        items(items = homeList) {pageModel->
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween){
                                Text(text = pageModel.name, fontWeight = FontWeight.Bold, color = Color.White, textAlign = TextAlign.Start)
                                ClickableText(text = AnnotatedString(text = "View All"), style = TextStyle(textAlign = TextAlign.End, color = Color.Red), onClick = {
                                    if(pageModel.name==AppConstant.HOME_HEADER_POPULAR_MOVIE || pageModel.name==AppConstant.HOME_HEADER_TOP_RATED_MOVIE){
                                        navController.navigate("${Routes.ViewAllMovieScreen.routes}/{type}".replace(oldValue = "{type}", newValue = pageModel.name))
                                    }else{
                                        navController.navigate("${Routes.ViewAllTVScreen.routes}/{type}".replace(oldValue = "{type}", newValue = pageModel.name))
                                    }
                                })
                            }
                            Spacer(modifier = Modifier.height(5.dp))
                            if(pageModel.listItem.isNotEmpty()){
                                if(pageModel.listItem[0] is TvResultsItem){
                                    RowViewTv(listItem = pageModel.listItem as List<TvResultsItem>, navController = navController)
                                }else{
                                    RowViewMovie(listItem = pageModel.listItem as List<MovieResultsItem>, navController = navController)
                                }
                            }
                            Spacer(modifier = Modifier.height(20.dp))
                        }
                    }
                }
            }
        }
    }


}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RowViewTv(listItem : List<TvResultsItem> , navController: NavHostController){
    LazyRow {
        items(items = listItem) {
            Card(modifier = Modifier.padding(5.dp), shape = CardDefaults.outlinedShape, onClick = {
                navController.navigate("${Routes.DetailTvViewScreen.routes}/{seriesId}".replace(oldValue = "{seriesId}", newValue = it.id.toString()))
            }) {
                Column {
                    ViewAsyncImage(
                        model = it.posterPath.toPosterUrl(),
                        contentDescription = it.name, modifier = Modifier
                            .width(150.dp)
                            .height(150.dp)
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RowViewMovie(listItem : List<MovieResultsItem> , navController: NavHostController){
    LazyRow {
        items(items = listItem) {
            Card(modifier = Modifier.padding(5.dp), shape = CardDefaults.outlinedShape, onClick = {
                navController.navigate("${Routes.DetailMovieViewScreen.routes}/{movieId}".replace(oldValue = "{movieId}", newValue = it.id.toString()))
            }) {
                Column {
                    ViewAsyncImage(
                        model = it.posterPath.toPosterUrl(),
                        contentDescription = it.title, modifier = Modifier
                            .width(150.dp)
                            .height(150.dp)
                    )
                }
            }
        }
    }
}





@Composable
fun ViewAsyncImage(model : String,contentDescription : String,modifier: Modifier){
    Log.e("TAG", "ViewAsyncImage: $model", )
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