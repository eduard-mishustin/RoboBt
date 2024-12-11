package magym.robobt.video_stream

import okhttp3.OkHttpClient
import org.koin.dsl.module
import java.util.concurrent.TimeUnit

val videoStreamModule = module {
    single<VideoStreamRepository> {
        val client = OkHttpClient.Builder()
            .connectTimeout(5, TimeUnit.MINUTES)
            .readTimeout(5, TimeUnit.MINUTES)
            .writeTimeout(5, TimeUnit.MINUTES)
            .build()

        VideoStreamRepositoryImpl(client)
    }
}