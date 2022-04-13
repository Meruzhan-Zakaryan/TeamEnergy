package com.teamenergy.teamenergy.net

import androidx.annotation.Keep
import com.google.gson.GsonBuilder
import kotlinx.coroutines.*
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

inline fun <reified T> createWebService(okHttpClient: OkHttpClient) = createWebService<T>(okHttpClient, "https://teamenergywebapi.azurewebsites.net/")

inline fun <reified T> createWebService(okHttpClient: OkHttpClient, baseUrl: String): T {
    val retrofit = Retrofit.Builder()
        .baseUrl(baseUrl)
        .client(okHttpClient)
        .addConverterFactory(GsonCreatorHelper.gsonForApiRequest).build()
//        .addCallAdapterFactory(RxJava2CallAdapterFactory.create()).build()
    return retrofit.create(T::class.java)
}

fun createOkHttpClient(): OkHttpClient = OkHttpClient.Builder()
    .connectTimeout(60L, TimeUnit.SECONDS)
    .readTimeout(60L, TimeUnit.SECONDS)
    .addInterceptor(
        HttpLoggingInterceptor()
            .apply {
                level =
//                    if (BaseConfigProperties.isEnableHttpLogging())
                    HttpLoggingInterceptor.Level.BODY
//                else HttpLoggingInterceptor.Level.NONE
            }
    )
    .build()


object GsonCreatorHelper {
    val gsonForApiRequest: GsonConverterFactory = GsonConverterFactory.create(
        GsonBuilder()
            .setLenient()
            .enableComplexMapKeySerialization()
            .create()
    )
}

suspend fun <T> coroutineResponseWithContext(
    resultCallBack: ApiResultCallback<T?>,
    //isShowLoader: Boolean = true,
    apiFunction: suspend () -> Response<T>
) {
    withContext(
        Dispatchers.IO + BaseCoroutineExceptionHandler(
            CoroutineExceptionHandler,
            resultCallBack
        )
    ) {
        val response = apiFunction()
        val responseBody = response.body()

        GlobalScope.launch(
            Dispatchers.Main + BaseCoroutineExceptionHandler(CoroutineExceptionHandler, resultCallBack)
        ) {
            if (response.isSuccessful) {
                resultCallBack.onSuccess(responseBody)
            } else {
                resultCallBack.onError(response.message())
            }

        }
    }
}

@Keep
interface ApiResultCallback<T> {
    fun onSuccess(response: T)

    @Deprecated("Use onError(status: Status.Failure) instead.")
    fun onError(response: String)

    /**
     * @return true if handled
     */
    // fun onError(status: Status.Failure): Boolean = false

    fun onNotHandledError(error: Any? = null) {


    }
}