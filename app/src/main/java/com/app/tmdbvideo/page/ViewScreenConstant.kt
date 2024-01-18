package com.app.tmdbvideo.page

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.material.TabRow
import androidx.compose.material.TabRowDefaults
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.app.tmdbvideo.Extension.toBackdropUrl
import com.app.tmdbvideo.model.CollectionResponse
import com.app.tmdbvideo.page.home.ViewAsyncImage
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.PagerState
import com.google.accompanist.pager.pagerTabIndicatorOffset
import kotlinx.coroutines.launch


data class ChildLayout(
    val contentType: String = "",
    val content: @Composable (item: Any?) -> Unit = {},
    val items: List<Any> = emptyList()
)

@Composable
fun VerticalScrollLayout(
    modifier: Modifier = Modifier,
    vararg childLayouts: ChildLayout
) {
    LazyColumn(modifier = modifier.fillMaxSize()) {
        childLayouts.forEach { child ->
            loadItems(child)
        }
    }
}

private fun LazyListScope.loadItems(childLayout: ChildLayout) {
    items(items = childLayout.items) { item ->
        childLayout.content(item)
    }
}
@OptIn(ExperimentalPagerApi::class)
@Composable
fun Tabs(pagerState : PagerState){
    val list = listOf("Related Movies")
    val scope = rememberCoroutineScope()

    TabRow(selectedTabIndex = pagerState.currentPage, backgroundColor = Color.Black, contentColor = Color.White, indicator = { tabPositions ->
        TabRowDefaults.Indicator(
            Modifier.pagerTabIndicatorOffset(pagerState, tabPositions),
            height = 2.dp,
            color = Color.White
        )
    }){
        list.forEachIndexed { index, s ->
            Tab(selected = pagerState.currentPage==index, text = { Text(text = list[index], color = if(pagerState.currentPage==index) Color.White else Color.Gray) }, onClick = {
                scope.launch {
                    pagerState.animateScrollToPage(index)
                }
            })
        }
    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun TabContent(pagerState: PagerState,pageDetails : CollectionResponse,screenWidth : Int){
    /*HorizontalPager(state = pagerState) {page ->
        when(page){
            0 -> TabContentScreen(data = "No List",pageDetails,screenWidth)
        }
    }*/
    TabContentScreen(data = "No List",pageDetails,screenWidth)
}

@Composable
fun TabContentScreen(data : String,pageDetails: CollectionResponse,screenWidth: Int){
    LazyVerticalGrid(modifier = Modifier.fillMaxSize(), columns = GridCells.Fixed(3)) {
        items(items = pageDetails.parts){
            ViewAsyncImage(model = it.backdropPath.toBackdropUrl(), contentDescription = it.title, modifier = Modifier
                .width((screenWidth/3).dp)
                .height((screenWidth/3).dp))
        }
    }
}