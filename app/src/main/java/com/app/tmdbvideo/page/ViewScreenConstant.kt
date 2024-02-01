package com.app.tmdbvideo.page

import androidx.compose.animation.core.Transition
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.TabRow
import androidx.compose.material.TabRowDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.scale
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.dp
import com.app.tmdbvideo.Extension.toBackdropUrl
import com.app.tmdbvideo.model.CollectionResponse
import com.app.tmdbvideo.model.FabItem
import com.app.tmdbvideo.model.MultiState
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
fun Tabs(pagerState: PagerState) {
    val list = listOf("Related Movies")
    val scope = rememberCoroutineScope()

    TabRow(
        selectedTabIndex = pagerState.currentPage,
        backgroundColor = Color.Black,
        contentColor = Color.White,
        indicator = { tabPositions ->
            TabRowDefaults.Indicator(
                Modifier.pagerTabIndicatorOffset(pagerState, tabPositions),
                height = 2.dp,
                color = Color.White
            )
        }) {
        list.forEachIndexed { index, s ->
            Tab(
                selected = pagerState.currentPage == index,
                text = {
                    Text(
                        text = list[index],
                        color = if (pagerState.currentPage == index) Color.White else Color.Gray
                    )
                },
                onClick = {
                    scope.launch {
                        pagerState.animateScrollToPage(index)
                    }
                })
        }
    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun TabContent(pagerState: PagerState, pageDetails: CollectionResponse, screenWidth: Int) {
    /*HorizontalPager(state = pagerState) {page ->
        when(page){
            0 -> TabContentScreen(data = "No List",pageDetails,screenWidth)
        }
    }*/
    TabContentScreen(data = "No List", pageDetails, screenWidth)
}

@Composable
fun TabContentScreen(data: String, pageDetails: CollectionResponse, screenWidth: Int) {
    LazyVerticalGrid(modifier = Modifier.fillMaxSize(), columns = GridCells.Fixed(3)) {
        items(items = pageDetails.parts) {
            ViewAsyncImage(
                model = it.backdropPath.toBackdropUrl(),
                contentDescription = it.title,
                modifier = Modifier
                    .width((screenWidth / 3).dp)
                    .height((screenWidth / 3).dp)
            )
        }
    }
}

@Composable
fun FloatActionButtonWithSubItem(
    fabIcon: Painter,
    items: List<FabItem>,
    showLabels: Boolean = false,
    onStateChanged: ((state: MultiState) -> Unit)? = null
) {
    var currentState by remember { mutableStateOf(MultiState.COLLAPSED) }
    val stateTransition: Transition<MultiState> =
        updateTransition(targetState = currentState, label = null)

    val stateChange: () -> Unit = {
        currentState =
            if (stateTransition.currentState == MultiState.COLLAPSED) MultiState.EXPANDED else MultiState.COLLAPSED
        onStateChanged?.invoke(currentState)
    }

    val modifier = if(currentState ==   MultiState.EXPANDED)
        Modifier
            .fillMaxSize()
            .clickable(indication = null,
                interactionSource = remember { MutableInteractionSource() }) {
                currentState = MultiState.COLLAPSED
            } else Modifier.fillMaxSize()
    Column(
        horizontalAlignment = Alignment.End,
        verticalArrangement = Arrangement.Bottom,
    ) {
        items.forEach {
            SmallFloatingActionBarRow(item = it, stateTransition = stateTransition, showLabels = showLabels)
            Spacer(modifier = Modifier.height(20.dp))
        }
        FloatingActionButton(onClick = { stateChange() }, shape =  RoundedCornerShape(16.dp), containerColor = Color.Blue) {
            Icon(painter = fabIcon, contentDescription = null, tint = Color.White,modifier = Modifier.size(16.dp))
        }
    }
   /* Box(modifier = modifier, contentAlignment = Alignment.BottomEnd){
        Box(modifier = Modifier
            .fillMaxWidth()
            .height(400.dp), contentAlignment = Alignment.BottomEnd){
            if(currentState==MultiState.EXPANDED){
                Canvas(modifier = Modifier
                    .fillMaxSize()
                    .graphicsLayer {
                        scaleX = 2.2f
                        scaleY = 2.1f
                    }) {
                    translate(150f, top = 300f) {
                        scale(5f) {}
                        drawCircle(color = Color.Red, radius = 200.dp.toPx())
                    }
                }
            }
            Column(
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.Bottom,
            ) {
                items.forEach {
                    SmallFloatingActionBarRow(item = it, stateTransition = stateTransition, showLabels = showLabels)
                    Spacer(modifier = Modifier.height(20.dp))
                }
                FloatingActionButton(onClick = { stateChange() }, shape = CircleShape, containerColor = Color.Blue) {
                    Icon(painter = fabIcon, contentDescription = null, tint = Color.White)
                }
            }
        }
    }*/


}

@Composable
fun SmallFloatingActionBarRow(
    item: FabItem,
    showLabels: Boolean,
    stateTransition: Transition<MultiState>
) {
    val alpha: Float by stateTransition.animateFloat(
        transitionSpec = {
            tween(durationMillis = 50)
        }, label = ""
    ) { state ->
        if (state == MultiState.COLLAPSED) 0F else 1F
    }

    val scale: Float by stateTransition.animateFloat(
        label = ""
    ) { state ->
        if (state == MultiState.COLLAPSED) 0F else 1F
    }

    Row(
        verticalAlignment = Alignment.CenterVertically, modifier = Modifier
            .alpha(animateFloatAsState(targetValue = alpha, label = "").value)
            .scale(animateFloatAsState(targetValue = scale, label = "").value)
    ) {
        if (showLabels) {
            Text(text = item.label, color = Color.White, modifier = Modifier
                .padding(start = 5.dp, end = 5.dp, top = 5.dp, bottom = 5.dp)
                .clickable(onClick = { item.onFabItemClicked() })
            )
        }
        SmallFloatingActionButton(
            onClick = { item.onFabItemClicked() },
            containerColor = MaterialTheme.colors.secondary,
            contentColor = Color.White
        ) {
            Icon(painter = item.icon, contentDescription = item.label, modifier = Modifier.size(14.dp))
        }
    }
}