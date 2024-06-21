package magym.robobt.feature.control.presentation.ui.util

import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.awaitLongPressOrCancellation
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.ui.input.pointer.AwaitPointerEventScope
import androidx.compose.ui.input.pointer.PointerEvent
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.PointerEventTimeoutCancellationException
import androidx.compose.ui.input.pointer.PointerId
import androidx.compose.ui.input.pointer.PointerInputChange
import androidx.compose.ui.input.pointer.PointerInputScope
import androidx.compose.ui.input.pointer.changedToUpIgnoreConsumed
import androidx.compose.ui.input.pointer.isOutOfBounds
import androidx.compose.ui.util.fastAll
import androidx.compose.ui.util.fastAny
import androidx.compose.ui.util.fastFirstOrNull

/**
 * Fork of [detectDragGestures]
 */
internal suspend fun PointerInputScope.detectPressGestures(
    onDown: () -> Unit = {},
    onUp: () -> Unit = {},
) {
    awaitEachGesture {
        val down = awaitFirstDown(requireUnconsumed = false)
        val press = awaitPressOrCancellation(down.id)

        if (press != null) {
            onDown.invoke()
            awaitUp(down.id, onUp)
        }
    }
}

/**
 * Fork of [androidx.compose.foundation.gestures.awaitDragOrUp]
 */
private suspend fun AwaitPointerEventScope.awaitUp(
    pointerId: PointerId,
    onUp: () -> Unit,
) {
    var pointer = pointerId
    while (true) {
        val event = awaitPointerEvent()
        val upEvent = event.changes.fastFirstOrNull { it.id == pointer } ?: return
        if (upEvent.changedToUpIgnoreConsumed()) {
            val otherDown = event.changes.fastFirstOrNull { it.pressed }
            if (otherDown == null) {
                // This is the last "up"
                onUp()
                return
            } else {
                pointer = otherDown.id
            }
        }
    }
}

/**
 * Fork of [awaitLongPressOrCancellation]
 */
private suspend fun AwaitPointerEventScope.awaitPressOrCancellation(
    pointerId: PointerId
): PointerInputChange? {
    if (currentEvent.isPointerUp(pointerId)) {
        return null // The pointer has already been lifted, so the press is cancelled.
    }

    val initialDown =
        currentEvent.changes.fastFirstOrNull { it.id == pointerId } ?: return null

    var press: PointerInputChange? = null
    var currentDown = initialDown
    return try {
        withTimeout(10) {
            // wait for first tap up or press
            var finished = false
            while (!finished) {
                val event = awaitPointerEvent(PointerEventPass.Main)
                if (event.changes.fastAll { it.changedToUpIgnoreConsumed() }) {
                    // All pointers are up
                    finished = true
                }

                if (
                    event.changes.fastAny {
                        it.isConsumed || it.isOutOfBounds(size, extendedTouchPadding)
                    }
                ) {
                    finished = true // Canceled
                }

                // Check for cancel by position consumption. We can look on the Final pass of
                // the existing pointer event because it comes after the Main pass we checked
                // above.
                val consumeCheck = awaitPointerEvent(PointerEventPass.Final)
                if (consumeCheck.changes.fastAny { it.isConsumed }) {
                    finished = true
                }
                if (event.isPointerUp(currentDown.id)) {
                    val newPressed = event.changes.fastFirstOrNull { it.pressed }
                    if (newPressed != null) {
                        currentDown = newPressed
                        press = currentDown
                    } else {
                        // should technically never happen as we checked it above
                        finished = true
                    }
                    // Pointer (id) stayed down.
                } else {
                    press = event.changes.fastFirstOrNull { it.id == currentDown.id }
                }
            }
        }
        null
    } catch (_: PointerEventTimeoutCancellationException) {
        press ?: initialDown
    }
}

/**
 * Fork of [androidx.compose.foundation.gestures.isPointerUp]
 */
private fun PointerEvent.isPointerUp(pointerId: PointerId): Boolean {
    return changes.fastFirstOrNull { it.id == pointerId }?.pressed != true
}