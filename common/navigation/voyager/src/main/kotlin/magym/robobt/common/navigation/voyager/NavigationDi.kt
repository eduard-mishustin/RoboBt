package magym.robobt.common.navigation.voyager

import magym.robobt.common.navigation.api.Router
import magym.robobt.common.navigation.voyager.impl.AndroidApplicationExitProvider
import magym.robobt.common.navigation.voyager.impl.ApplicationExitProvider
import magym.robobt.common.navigation.voyager.impl.NavigatorHolder
import magym.robobt.common.navigation.voyager.impl.RouterImpl
import magym.robobt.common.navigation.voyager.impl.result.NavigationResultEventBus
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val navigationModule = module {
	singleOf(::RouterImpl) bind Router::class
	singleOf(::AndroidApplicationExitProvider) bind ApplicationExitProvider::class
	singleOf(::NavigationResultEventBus)
	singleOf(::NavigatorHolder)
}