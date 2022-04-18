package com.teamenergy.ui.launch

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import androidx.databinding.DataBindingUtil
import com.teamenergy.R
import com.teamenergy.databinding.ActivityLaunchBinding
import com.teamenergy.ui.EnergyApplication
import com.teamenergy.ui.EnergyApplication.Companion.loginDataLiveData
import com.teamenergy.ui.base.utils.SharedPreferenceUtils
import com.teamenergy.ui.home.HomeActivity
import com.teamenergy.ui.welcome.WelcomeActivity

class LaunchActivity : AppCompatActivity() {

    private var binding: ActivityLaunchBinding? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_launch)
        Handler().postDelayed({
            checkState()
        }, 2000)
    }

    fun checkState() {
        val userData = SharedPreferenceUtils().getUserData(this)
        if (userData != null) {
            loginDataLiveData.value = userData
            startActivity(Intent(this@LaunchActivity, HomeActivity::class.java))
            finish()
        } else {
            startActivity(Intent(this@LaunchActivity, WelcomeActivity::class.java))
            finish()
        }
    }
}