package wardani.dika.gitsearch.api

import android.content.Context
import com.franmontiel.persistentcookiejar.PersistentCookieJar
import com.franmontiel.persistentcookiejar.cache.SetCookieCache
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.concurrent.TimeUnit

object ApiClient {
    private const val BASE_URL = "https://api.github.com/"
    private const val CONNECTION_TIME_OUT = 20L
    private val rtoUnit = TimeUnit.SECONDS

    @Volatile
    private var client: Retrofit? = null

    private fun createHeader(context: Context): OkHttpClient {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        val cookieJar = PersistentCookieJar(SetCookieCache(), SharedPrefsCookiePersistor(context))

        return OkHttpClient.Builder()
            .connectTimeout(CONNECTION_TIME_OUT, rtoUnit)
            .addInterceptor(interceptor)
            .cookieJar(cookieJar)
            .build()
    }

    private fun createClient(context: Context): Retrofit {
        var tempRetrofit = client

        return if (tempRetrofit == null) {
            tempRetrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(createHeader(context))
                .addConverterFactory(ScalarsConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()

            client = tempRetrofit
            tempRetrofit
        } else {
            tempRetrofit
        }
    }

    fun<T> createEndPoint(context: Context, apiInterfaceClass: Class<T>): T {
        return createClient(context).create(apiInterfaceClass)
    }
}