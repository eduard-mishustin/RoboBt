package magym.robobt.common.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.unit.dp

@Composable
fun RoboTheme(
    content: @Composable () -> Unit,
) {
    val isDarkTheme = isSystemInDarkTheme()

    val colorScheme = if (isDarkTheme) DarkAndroidColorScheme else LightAndroidColorScheme
    val roboPalette = if (isDarkTheme) RoboPalette.Dark else RoboPalette.Light

    MaterialTheme(
        colorScheme = colorScheme,
        shapes = RoboShapes,
        content = {
            CompositionLocalProvider(
                LocalRoboPalette provides roboPalette,
                LocalContentColor provides MaterialTheme.colorScheme.onBackground,
                content = content,
            )
        },
    )
}

private val RoboShapes = Shapes(
    extraSmall = RoundedCornerShape(16.dp),
    small = RoundedCornerShape(16.dp),
    medium = RoundedCornerShape(16.dp),
    large = RoundedCornerShape(24.dp),
    extraLarge = RoundedCornerShape(24.dp),
)

private val LightAndroidColorScheme = lightColorScheme(
    primary = RoboPalette.Light.BackgroundAccentPrimary,
    onPrimary = RoboPalette.Light.BackgroundPrimary,
    secondary = RoboPalette.Light.TextSecondary,
    onSecondary = RoboPalette.Light.TextPrimary,
    background = RoboPalette.Light.BackgroundPrimary,
    onBackground = RoboPalette.Light.TextPrimary,
    surface = RoboPalette.Light.BackgroundPrimary,
    onSurface = RoboPalette.Light.TextPrimary,
    error = RoboPalette.Light.TextNegative,
    onError = RoboPalette.Light.BackgroundPrimary,
    primaryContainer = RoboPalette.Light.BackgroundAccentPrimary,
    onPrimaryContainer = RoboPalette.Light.BackgroundPrimary,
    outlineVariant = RoboPalette.Light.OutlineSecondary,
)

private val DarkAndroidColorScheme = darkColorScheme(
    primary = RoboPalette.Dark.BackgroundAccentPrimary,
    onPrimary = RoboPalette.Dark.BackgroundPrimary,
    secondary = RoboPalette.Dark.TextSecondary,
    onSecondary = RoboPalette.Dark.TextPrimary,
    background = RoboPalette.Dark.BackgroundPrimary,
    onBackground = RoboPalette.Dark.TextPrimary,
    surface = RoboPalette.Dark.BackgroundPrimary,
    onSurface = RoboPalette.Dark.TextPrimary,
    error = RoboPalette.Dark.TextNegative,
    onError = RoboPalette.Dark.BackgroundPrimary,
    primaryContainer = RoboPalette.Dark.BackgroundAccentPrimary,
    onPrimaryContainer = RoboPalette.Dark.BackgroundPrimary,
    outlineVariant = RoboPalette.Dark.OutlineSecondary,
)