package com.android.purebilibili.core.ui.animation

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.tween
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.graphics.graphicsLayer
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * 瀑布流/交错式入场动画
 * Staggered entrance animation modifier.
 *
 * @param index The index of the item in the list.
 * @param visible Whether the item should be visible.
 * @param offsetDistance The initial Y offset distance.
 */
fun Modifier.staggeredEntrance(
    index: Int,
    visible: Boolean,
    offsetDistance: Float = 50f
): Modifier = composed {
    val alpha = remember { Animatable(0f) }
    val translationY = remember { Animatable(offsetDistance) }
    val scale = remember { Animatable(0.94f) }

    LaunchedEffect(visible) {
        if (visible) {
            // Delay based on index for the staggered effect
            delay(index * 35L)

            // Parallel animations
            launch {
                alpha.animateTo(
                    targetValue = 1f,
                    animationSpec = tween(
                        durationMillis = 300,
                        easing = CubicBezierEasing(0.2f, 0.8f, 0.2f, 1.0f) // Ease-out
                    )
                )
            }
            launch {
                translationY.animateTo(
                    targetValue = 0f,
                    animationSpec = tween(
                        durationMillis = 450,
                        easing = CubicBezierEasing(0.18f, 0.8f, 0.2f, 1.0f) // Fast-out, slow-in
                    )
                )
            }
            launch {
                scale.animateTo(
                    targetValue = 1f,
                    animationSpec = tween(
                        durationMillis = 400,
                        easing = CubicBezierEasing(0.2f, 0.8f, 0.2f, 1.0f)
                    )
                )
            }
        }
    }

    this.graphicsLayer {
        this.alpha = alpha.value
        this.translationY = translationY.value * density // Convert dp-like float to pixels if needed, but here we treat input as px or user dp
        this.scaleX = scale.value
        this.scaleY = scale.value
    }
}
