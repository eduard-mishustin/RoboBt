@file:OptIn(ExperimentalComposeUiApi::class)

package magym.robobt.feature.control.presentation.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import magym.robobt.common.ui.theme.PreviewTheme
import magym.robobt.common.ui.theme.ScreenPreview
import magym.robobt.feature.control.presentation.ui.state.ControlUiState
import magym.robobt.feature.control.presentation.ui.util.detectPressGestures

@Composable
internal fun ControlScreen(
    state: ControlUiState,
    onChangeControlModeClick: () -> Unit = {},
    onTopLeftButtonDown: () -> Unit = {},
    onTopLeftButtonUp: () -> Unit = {},
    onTopRightButtonDown: () -> Unit = {},
    onTopRightButtonUp: () -> Unit = {},
    onBottomLeftButtonDown: () -> Unit = {},
    onBottomLeftButtonUp: () -> Unit = {},
    onBottomRightButtonDown: () -> Unit = {},
    onBottomRightButtonUp: () -> Unit = {},
) {
    when (state) {
        is ControlUiState.Accelerometer -> AccelerometerControl(
            state = state,
            onChangeControlModeClicked = onChangeControlModeClick
        )

        is ControlUiState.Manual -> ManualControl(
            state = state,
            onTopLeftButtonDown = onTopLeftButtonDown,
            onTopLeftButtonUp = onTopLeftButtonUp,
            onTopRightButtonDown = onTopRightButtonDown,
            onTopRightButtonUp = onTopRightButtonUp,
            onBottomLeftButtonDown = onBottomLeftButtonDown,
            onBottomLeftButtonUp = onBottomLeftButtonUp,
            onBottomRightButtonDown = onBottomRightButtonDown,
            onBottomRightButtonUp = onBottomRightButtonUp,
        )
    }
}

@Composable
internal fun AccelerometerControl(
    state: ControlUiState.Accelerometer,
    onChangeControlModeClicked: () -> Unit,
) {
    Column {
        Text(
            modifier = Modifier.padding(32.dp),
            fontSize = 32.sp,
            text = state.leftMotor.toString() + " : " + state.rightMotor.toString()
        )

        Text(
            modifier = Modifier.padding(32.dp),
            fontSize = 32.sp,
            text = state.weather
        )

        Button(
            modifier = Modifier.padding(16.dp),
            onClick = onChangeControlModeClicked
        ) {
            Text(text = "Change control mode")
        }
    }
}

@Composable
internal fun ManualControl(
    state: ControlUiState.Manual,
    onTopLeftButtonDown: () -> Unit,
    onTopLeftButtonUp: () -> Unit,
    onTopRightButtonDown: () -> Unit,
    onTopRightButtonUp: () -> Unit,
    onBottomLeftButtonDown: () -> Unit,
    onBottomLeftButtonUp: () -> Unit,
    onBottomRightButtonDown: () -> Unit,
    onBottomRightButtonUp: () -> Unit,
) {
    Column {
        Text(
            modifier = Modifier.padding(32.dp),
            fontSize = 32.sp,
            text = state.weather
        )

        Spacer(modifier = Modifier.weight(1f))

        Row {
            ControlButton(onDown = onTopLeftButtonDown, onUp = onTopLeftButtonUp, text = "↑")
            Spacer(modifier = Modifier.weight(1f))
            ControlButton(onDown = onTopRightButtonDown, onUp = onTopRightButtonUp, text = "↑")
        }

        Row {
            ControlButton(onDown = onBottomLeftButtonDown, onUp = onBottomLeftButtonUp, text = "↓")
            Spacer(modifier = Modifier.weight(1f))
            ControlButton(onDown = onBottomRightButtonDown, onUp = onBottomRightButtonUp, text = "↓")
        }
    }
}

@Composable
private fun ControlButton(onDown: () -> Unit, onUp: () -> Unit, text: String) {
    Button(
        modifier = Modifier
            .padding(16.dp)
            .pointerInput(Unit) {
                detectPressGestures(
                    onDown = onDown,
                    onUp = onUp,
                )
            },
        onClick = {}
    ) {
        Text(
            modifier = Modifier.padding(32.dp),
            text = text,
            fontSize = 48.sp,
        )
    }
}

@ScreenPreview
@Composable
private fun ControlScreenAccelerometerPreview() = PreviewTheme {
    ControlScreen(ControlUiState.Accelerometer(leftMotor = 0, rightMotor = 0, "27 °C"))
}

@ScreenPreview
@Composable
private fun ControlScreenManualPreview() = PreviewTheme {
    ControlScreen(ControlUiState.Manual("27 °C, 30%"))
}