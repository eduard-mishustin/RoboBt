package magym.robobt.web

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module

val webRepositoryModule = module {
    single<WebRepository> {
        WebRepositoryImpl(
            okHttpClient = OkHttpClient(),
            scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
        )
    }

    single {
        val logging = HttpLoggingInterceptor().apply {
            setLevel(HttpLoggingInterceptor.Level.BODY)
        }

        OkHttpClient.Builder()
            .addInterceptor(logging)
            .build()
    }
}