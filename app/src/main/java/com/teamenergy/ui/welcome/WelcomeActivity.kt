package com.teamenergy.ui.welcome

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.teamenergy.R
import com.teamenergy.teamenergy.BaseEnergyViewModel

class WelcomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)
       // var model = ViewModelProvider(this)[BaseEnergyViewModel::class.java]
    }
}