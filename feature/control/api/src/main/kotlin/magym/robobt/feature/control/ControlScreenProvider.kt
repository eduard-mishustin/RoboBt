package magym.robobt.feature.control

import magym.robobt.common.navigation.api.RoboScreen

fun interface ControlScreenProvider {

    operator fun invoke(): RoboScreen
}