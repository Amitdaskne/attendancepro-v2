package com.amit.attendancepro.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.BlurredEdgeTreatment
import androidx.compose.ui.draw.blur
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.amit.attendancepro.ui.theme.BgAccent
import com.amit.attendancepro.ui.theme.BgDeep
import com.amit.attendancepro.ui.theme.BgMid
import com.amit.attendancepro.ui.theme.BlobBlue
import com.amit.attendancepro.ui.theme.BlobPurple
import com.amit.attendancepro.ui.theme.BlobTeal

@Composable
fun BoxScope.GlassBackground() {
    Box(
        Modifier
            .fillMaxSize()
            .background(
                Brush.linearGradient(
                    listOf(BgDeep, BgMid, BgAccent),
                    start = Offset.Zero,
                    end = Offset.Infinite
                )
            )
    ) {
        Blob(BlobPurple, 320.dp, Alignment.TopStart, (-70).dp, 60.dp)
        Blob(BlobTeal, 280.dp, Alignment.CenterEnd, 100.dp, (-40).dp)
        Blob(BlobBlue, 260.dp, Alignment.BottomStart, (-50).dp, (-70).dp)
    }
}

@Composable
private fun BoxScope.Blob(
    color: Color, size: Dp, align: Alignment, offsetX: Dp, offsetY: Dp
) {
    Box(
        Modifier
            .align(align)
            .offset(x = offsetX, y = offsetY)
            .size(size)
            .blur(100.dp, edgeTreatment = BlurredEdgeTreatment.Unbounded)
            .background(color.copy(alpha = 0.35f), CircleShape)
    )
}
