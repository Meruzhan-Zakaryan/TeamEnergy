package com.teamenergy.ui.launch

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import androidx.databinding.DataBindingUtil
import com.teamenergy.R
import com.teamenergy.databinding.ActivityLaunchBinding
import com.teamenergy.ui.welcome.WelcomeActivity

class LaunchActivity : AppCompatActivity() {

    private var binding: ActivityLaunchBinding? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_launch)
        Handler().postDelayed({
            startActivity(Intent(this@LaunchActivity, WelcomeActivity::class.java))
            finish()
        },2000)
    }
}