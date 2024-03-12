package com.app.tmdbvideo.page

import android.annotation.SuppressLint
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavHostController
import com.app.tmdbvideo.Extension.optString
import com.app.tmdbvideo.Extension.toBackdropUrl
import com.app.tmdbvideo.page.home.ViewAsyncImage

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
