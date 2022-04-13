package com.teamenergy.ui

import android.app.Application
import android.content.res.Configuration
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.lifecycle.MutableLiveData
import com.google.zxing.integration.android.IntentResult
import com.teamenergy.proxy.network.masterData.LoginDto
import com.teamenergy.proxy.network.masterData.MasterDto
import com.teamenergy.proxy.network.masterData.UserDto
import com.teamenergy.teamenergy.di.appModule
import com.teamenergy.teamenergy.net.BaseCoroutineExceptionHandler
import com.teamenergy.ui.di.energyModule
import com.yandex.mapkit.MapKitFactory
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidFileProperties
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import java.util.*

class EnergyApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger(Level.ERROR)
            androidContext(this@EnergyApplication)
            androidFileProperties()
            modules(appModule(), energyModule)
        }
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
    }

    companion object {
        private val TAG = EnergyApplication::class.java.simpleName

        lateinit var locales: List<Locale>
        lateinit var defaultLocale: Locale

        val networkStateLiveData: MutableLiveData<Boolean> = MutableLiveData()
        val masterDataLiveData: MutableLiveData<MasterDto> = MutableLiveData()
        val loginDataLiveData: MutableLiveData<LoginDto> = MutableLiveData()
        val userDataLiveData: MutableLiveData<UserDto> = MutableLiveData()
        val qrScannerResultLiveData: MutableLiveData<IntentResult?> = MutableLiveData()

        var isLastNetworkStateWasConnected: Boolean? = null
        var lastNoInternetShownToastTime: Long? = null
        const val SHOW_NO_INTERNET_CONNECTION_POPUP_TIME_RANGE = 4000

        fun getCoroutineContext() = Dispatchers.Main + SupervisorJob() + BaseCoroutineExceptionHandler(
            CoroutineExceptionHandler
        )
    }
}