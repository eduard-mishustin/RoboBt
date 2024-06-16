package magym.robobt.common.ui.theme

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.graphics.Color

val LocalRoboPalette = compositionLocalOf<RoboPalette> { error("Not provided") }

@Suppress("PropertyName")
@Immutable
interface RoboPalette {

    val TextPrimary: Color
    val TextSecondary: Color
    val TextAccent: Color
    val TextNegative: Color

    val BackgroundPrimary: Color
    val BackgroundSecondary: Color
    val BackgroundAccentPrimary: Color

    val OutlineSecondary: Color

    object Light : RoboPalette {

        override val TextPrimary: Color = CuiColorToken.Black1
        override val TextSecondary: Color = CuiColorToken.Grey5
        override val TextAccent: Color = CuiColorToken.Orange2
        override val TextNegative: Color = CuiColorToken.Red1

        override val BackgroundPrimary: Color = CuiColorToken.White1
        override val BackgroundSecondary: Color = CuiColorToken.Grey1
        override val BackgroundAccentPrimary: Color = CuiColorToken.Orange1

        override val OutlineSecondary: Color = CuiColorToken.Grey2
    }

    object Dark : RoboPalette {

        override val TextPrimary: Color = CuiColorToken.White2
        override val TextSecondary: Color = CuiColorToken.Brown1
        override val TextAccent: Color = CuiColorToken.Orange1
        override val TextNegative: Color = CuiColorToken.Red1

        override val BackgroundPrimary: Color = CuiColorToken.Black2
        override val BackgroundSecondary: Color = CuiColorToken.Brown2
        override val BackgroundAccentPrimary: Color = CuiColorToken.Orange1

        override val OutlineSecondary: Color = CuiColorToken.Brown4
    }
}

object CuiColorToken {
    val Orange1 = Color(0xFF549BF1)
    val Orange2 = Color(0xFFDB4819)

    val White1 = Color(0xFFFFFFFF)
    val White2 = Color(0xFFE4E9F1)

    val Black1 = Color(0xFF222B39)
    val Black2 = Color(0xFF1E1A19)

    val Grey1 = Color(0xFFF5F5F5)
    val Grey2 = Color(0xFFE6E6E6)
    val Grey5 = Color(0xFF656160)

    val Brown1 = Color(0xFFB2A095)
    val Brown2 = Color(0xFF2F2A2A)
    val Brown4 = Color(0xFF383838)
    val Red1 = Color(0xFFE4382A)
}