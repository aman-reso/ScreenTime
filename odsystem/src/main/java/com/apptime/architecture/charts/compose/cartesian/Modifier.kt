package com.telekom.odsystem.charts.compose.cartesian

import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.pointerInput
import com.telekom.odsystem.charts.compose.common.detectZoomGestures
import com.telekom.odsystem.charts.core.common.Point

private const val BASE_SCROLL_ZOOM_DELTA = 0.1f

private fun Offset.toPoint() = Point(x, y)

internal fun Modifier.pointerInput(
    scrollState: VicoScrollState,
    onPointerPositionChange: ((Point?) -> Unit)?,
    onZoom: ((Float, Offset) -> Unit)?,
    consumeMoveEvents: Boolean,
) =
    scrollable(
        state = scrollState.scrollableState,
        orientation = Orientation.Horizontal,
        enabled = scrollState.scrollEnabled,
        reverseDirection = true,
    )
        .pointerInput(onZoom, onPointerPositionChange) {
            awaitPointerEventScope {
                while (true) {
                    val event = awaitPointerEvent()
                    when {
                        event.type == PointerEventType.Scroll && scrollState.scrollEnabled && onZoom != null ->
                            onZoom(
                                1 - event.changes.first().scrollDelta.y * BASE_SCROLL_ZOOM_DELTA,
                                event.changes.first().position,
                            )

                        onPointerPositionChange == null -> continue
                        event.type == PointerEventType.Press ->
                            onPointerPositionChange(event.changes.first().position.toPoint())

                        event.type == PointerEventType.Release -> onPointerPositionChange(null)
                        event.type == PointerEventType.Move && !scrollState.scrollEnabled -> {
                            val changes = event.changes.first()
                            if (consumeMoveEvents) changes.consume()
                            onPointerPositionChange(changes.position.toPoint())
                        }
                    }
                }
            }
        }
        .then(
            if (scrollState.scrollEnabled && onZoom != null) {
                Modifier.pointerInput(onPointerPositionChange, onZoom) {
                    detectZoomGestures { centroid, zoom ->
                        onPointerPositionChange?.invoke(null)
                        onZoom(zoom, centroid)
                    }
                }
            } else {
                Modifier
            }
        )
