package com.teamenergy.teamenergy.net

import android.util.Log
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlin.coroutines.CoroutineContext

class BaseCoroutineExceptionHandler (
    override val key: CoroutineContext.Key<*>,
    private val resultCallBack: ApiResultCallback<*>? = null
) : CoroutineExceptionHandler {
    override fun handleException(context: CoroutineContext, exception: Throwable) {
        try {
            Log.e("Global error", exception.localizedMessage ?: " ")
            exception.localizedMessage?.let{resultCallBack?.onError(it)}
        } catch (ex: Exception) {
            Log.e("Coroutine Exception", ex.localizedMessage ?: " ")
        }
    }
}