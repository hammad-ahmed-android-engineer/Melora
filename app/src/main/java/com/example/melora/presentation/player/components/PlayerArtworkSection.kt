package com.example.melora.presentation.player.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.melora.core.designsystem.components.ArtworkImage
import com.example.melora.core.designsystem.AppDimensions
import com.example.melora.core.designsystem.dimensions

@Composable
fun PlayerArtworkSection(
    artworkUri: String?,
    scale: Float,
    isLandscape: Boolean,
    modifier: Modifier = Modifier,
    cornerRadius: Dp = 14.dp
) {
    Box(
        modifier = modifier
            .then(
                if (isLandscape) Modifier.fillMaxHeight().aspectRatio(1f)
                else Modifier.fillMaxWidth().aspectRatio(1f).padding(horizontal = MaterialTheme.dimensions.spacing.small)
            ),
        contentAlignment = Alignment.Center
    ) {
        // Floating ambient subtle glow
        Box(
            modifier = Modifier
                .fillMaxWidth(0.94f)
                .aspectRatio(1f)
                .scale(scale)
                .shadow(
                    elevation = AppDimensions.elevations.high,
                    shape = RoundedCornerShape(cornerRadius),
                    clip = false
                )
                .background(
                    color = MaterialTheme.colorScheme.primary.copy(alpha = 0.20f),
                    shape = RoundedCornerShape(cornerRadius)
                )
        )

        ArtworkImage(
            artworkUri = artworkUri,
            size = if (isLandscape) 240.dp else 320.dp,
            cornerRadius = cornerRadius,
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f)
                .scale(scale)
                .clip(RoundedCornerShape(cornerRadius))
                .shadow(
                    elevation = AppDimensions.elevations.medium,
                    shape = RoundedCornerShape(cornerRadius)
                )
        )
    }
}

