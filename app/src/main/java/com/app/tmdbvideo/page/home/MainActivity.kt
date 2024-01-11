package com.app.tmdbvideo.page.home

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.Button
import androidx.compose.material.ExperimentalMaterialApi
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextLayoutResult
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
import com.app.tmdbvideo.Extension.toBackdropUrl
import com.app.tmdbvideo.Extension.toPosterUrl
import com.app.tmdbvideo.R
import com.app.tmdbvideo.Routes
import com.app.tmdbvideo.model.MovieResultsItem
import com.app.tmdbvideo.model.TvResultsItem
import com.app.tmdbvideo.page.DetailMovieScreenPage
import com.app.tmdbvideo.page.DetailTvScreenPage
import com.app.tmdbvideo.page.detail.DetailViewModel
import com.app.tmdbvideo.ui.theme.TMDBVideoTheme

class MainActivity : ComponentActivity() {
    private lateinit var mainViewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val modelFactory = MainViewModelFactory(application)
        mainViewModel =ViewModelProvider(this,modelFactory).get(MainViewModel::class.java)
        setContent {
            TMDBVideoTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Color.Black
                ) {
                    ScreenHome(mainViewModel = mainViewModel)
                }
            }
        }
    }
}

@Composable
fun ScreenHome(mainViewModel: MainViewModel,startPosition : String = Routes.HomeViewScreen.routes){
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = startPosition){
        composable(Routes.HomeViewScreen.routes){
            homePageView(mainViewModel = mainViewModel,navController)
        }

        composable("${Routes.DetailMovieViewScreen.routes}/{movieId}"){
            val movieId = it.arguments?.getString("movieId")
            movieId?.let {
                DetailMovieScreenPage(mainViewModel = mainViewModel,movieId,navController)
            }
        }

        composable("${Routes.DetailTvViewScreen.routes}/{seriesId}"){
            val seriesId = it.arguments?.getString("seriesId")
            seriesId?.let {
                DetailTvScreenPage(mainViewModel = mainViewModel,seriesId,navController)
            }
        }

    }
}

@Composable
fun homePageView(mainViewModel: MainViewModel,navController: NavHostController){
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
        Box(modifier = Modifier.padding(horizontal = 10.dp, vertical = 10.dp)) {
            LazyColumn {
                items(items = homeList) {
                    Row(horizontalArrangement = Arrangement.SpaceEvenly){
                        Text(text = it.name, fontWeight = FontWeight.Bold, color = Color.White, textAlign = TextAlign.Start, softWrap = true)
                        ClickableText(text = AnnotatedString(text = "View All"), style = TextStyle(textAlign = TextAlign.End, color = Color.Red), softWrap = true, onClick = {
                            navController.navigate(Routes.ViewAllScreen.routes)
                        })
                    }
                    Spacer(modifier = Modifier.height(5.dp))
                    if(it.listItem.isNotEmpty()){
                        if(it.listItem[0] is TvResultsItem){
                            rowViewTv(listItem = it.listItem as List<TvResultsItem>, navController = navController)
                        }else{
                            rowViewMovie(listItem = it.listItem as List<MovieResultsItem>, navController = navController)
                        }
                    }
                    Spacer(modifier = Modifier.height(20.dp))
                }
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun rowViewTv(listItem : List<TvResultsItem> , navController: NavHostController){
    LazyRow {
        items(items = listItem) {
            Card(modifier = Modifier.padding(5.dp), shape = CardDefaults.outlinedShape, onClick = {
                navController.navigate("${Routes.DetailTvViewScreen.routes}/{seriesId}".replace(oldValue = "{seriesId}", newValue = it.id.toString()))
            }) {
                Column {
                    viewAsyncImage(
                        model = it.posterPath.toPosterUrl(),
                        contentDescription = it.name
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun rowViewMovie(listItem : List<MovieResultsItem> , navController: NavHostController){
    LazyRow {
        items(items = listItem) {
            Card(modifier = Modifier.padding(5.dp), shape = CardDefaults.outlinedShape, onClick = {
                navController.navigate("${Routes.DetailMovieViewScreen.routes}/{movieId}".replace(oldValue = "{movieId}", newValue = it.id.toString()))
            }) {
                Column {
                    viewAsyncImage(
                        model = it.posterPath.toPosterUrl(),
                        contentDescription = it.title
                    )
                }
            }
        }
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun gridViewTvSeries(listItem : List<TvResultsItem> , navController: NavHostController){
    LazyVerticalGrid(columns = GridCells.Fixed(2)){
        items(listItem.size){
            Card(modifier = Modifier.padding(5.dp), shape = CardDefaults.outlinedShape, onClick = {
                //navController.navigate(Routes.DetailTvViewScreen.routes)
            }) {
                Column{
                    viewAsyncImage(model = listItem[it].posterPath.toBackdropUrl(), contentDescription = listItem[it].name)
                }
            }
        }
    }
}

@Composable
fun viewAsyncImage(model : String,contentDescription : String){
    AsyncImage(
        model = model,
        contentDescription = contentDescription,
        placeholder = painterResource(id = R.drawable.placeholder_image),
        error = painterResource(id = R.drawable.error_image),
        contentScale = ContentScale.FillBounds, modifier = Modifier
            .width(150.dp)
            .height(150.dp)
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