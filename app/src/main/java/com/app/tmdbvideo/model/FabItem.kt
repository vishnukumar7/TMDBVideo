package com.app.tmdbvideo.model

import androidx.compose.ui.graphics.painter.Painter

data class FabItem (
    val icon : Painter,
    val label : String,
    val onFabItemClicked: () -> Unit
)

enum class MultiState {
    COLLAPSED,EXPANDED
}