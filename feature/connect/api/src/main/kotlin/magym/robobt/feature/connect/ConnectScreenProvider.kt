package magym.robobt.feature.connect

import magym.robobt.common.navigation.api.RoboScreen

fun interface ConnectScreenProvider {

    operator fun invoke(): RoboScreen
}