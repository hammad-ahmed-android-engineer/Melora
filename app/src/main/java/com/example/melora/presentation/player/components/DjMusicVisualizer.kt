package com.example.melora.presentation.player.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.melora.core.designsystem.dimensions
import kotlin.math.sin

/**
 * Animated DJ-style music visualizer spectrum with glowing frequency bars
 * reacting dynamically when audio is playing.
 */
@Composable
fun DjMusicVisualizer(
    isPlaying: Boolean,
    modifier: Modifier = Modifier,
    barCount: Int = 26,
    maxHeight: Dp = 38.dp,
    minHeight: Dp = MaterialTheme.dimensions.spacing.tiny
) {
    val infiniteTransition = rememberInfiniteTransition(label = "DjVisualizerTransition")

    // Rhythmic DJ beat cycle
    val beatProgress by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = (2 * Math.PI).toFloat(),
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1100, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "DjBeatCycle"
    )

    // Secondary harmonic phase for varied multi-frequency wave
    val harmonicProgress by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = (4 * Math.PI).toFloat(),
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1800, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "DjHarmonicCycle"
    )

    // Animated master intensity smoothly ramping up/down when play/pause toggles
    val masterIntensity = remember { Animatable(if (isPlaying) 1f else 0f) }
    LaunchedEffect(isPlaying) {
        masterIntensity.animateTo(
            targetValue = if (isPlaying) 1f else 0f,
            animationSpec = tween(durationMillis = 400, easing = FastOutSlowInEasing)
        )
    }

    val primaryColor = MaterialTheme.colorScheme.primary
    val secondaryColor = MaterialTheme.colorScheme.secondary
    val tertiaryColor = MaterialTheme.colorScheme.tertiary

    val gradientBrush = Brush.verticalGradient(
        colors = listOf(
            tertiaryColor,
            primaryColor,
            secondaryColor.copy(alpha = 0.7f)
        )
    )

    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(MaterialTheme.dimensions.corners.large))
            .background(MaterialTheme.colorScheme.surfaceContainerHighest.copy(alpha = 0.35f))
            .padding(horizontal = 14.dp, vertical = 6.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(maxHeight),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Bottom
        ) {
            for (i in 0 until barCount) {
                // Calculate pseudo-harmonic audio frequency amplitude for DJ equalizer look
                val normalizedIndex = i.toFloat() / barCount.toFloat()
                
                // Bass frequencies (left), Mid-range (center), High treble (right)
                val baseWave = sin(beatProgress + normalizedIndex * 4.8f)
                val harmonicWave = sin(harmonicProgress + normalizedIndex * 9.2f)
                val frequencyWeight = if (normalizedIndex < 0.35f) {
                    // Heavy bass punch on the lower bands
                    1.1f + 0.3f * sin(beatProgress * 2f + normalizedIndex * 2f)
                } else if (normalizedIndex < 0.75f) {
                    // Rich mid-range melody
                    0.95f + 0.25f * sin(harmonicProgress + normalizedIndex * 5f)
                } else {
                    // Crisp treble sparkle
                    0.85f + 0.35f * sin(beatProgress * 3f + normalizedIndex * 8f)
                }

                val combinedWave = ((baseWave * 0.6f + harmonicWave * 0.4f) + 1f) / 2f
                val activeHeightFraction = (combinedWave * frequencyWeight).coerceIn(0.12f, 1.0f)
                val currentFraction = (activeHeightFraction * masterIntensity.value).coerceAtLeast(0.08f)
                val currentBarHeight = minHeight + (maxHeight - minHeight) * currentFraction

                Box(
                    modifier = Modifier
                        .width(3.5.dp)
                        .height(currentBarHeight)
                        .clip(CircleShape)
                        .background(gradientBrush)
                )
            }
        }
    }
}
