package com.teamenergy.ui.home

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.IdRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.PermissionChecker
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.gson.JsonObject
import com.google.zxing.integration.android.IntentIntegrator
import com.teamenergy.R
import com.teamenergy.databinding.ActivityHomeBinding
import com.teamenergy.teamenergy.BaseEnergyViewModel
import com.teamenergy.ui.EnergyApplication.Companion.qrScannerResultLiveData
import com.teamenergy.ui.account.AccountFragment
import com.teamenergy.ui.base.utils.openQRScanner
import com.teamenergy.ui.base.utils.setupWithNavController
import com.teamenergy.ui.card.CardFragment
import com.teamenergy.ui.notification.NotificationFragment
import com.teamenergy.ui.scanner.ScannerFragment
import com.yandex.mapkit.MapKitFactory
import org.koin.android.ext.android.inject
import java.util.*

class HomeActivity : AppCompatActivity() {

    private var binding: ActivityHomeBinding? = null
    private val viewModel by inject<BaseEnergyViewModel>()
    private lateinit var bottomNavigationView: BottomNavigationView
    private val mapFragment = MapFragment()
    private val cardFragment = CardFragment()
    private val scannerFragment = ScannerFragment()
    private val notificationFragment = NotificationFragment()
    private val accountFragment = AccountFragment()
    private var scannerContents: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_home)
        binding?.activity = this@HomeActivity
        setContentView(binding?.root)
        observeLiveData()
        bottomNavigationView = binding?.bottomNavigationView!!
        setFragment(mapFragment)
        setupBottomNavigationView()
    }

    private fun setupBottomNavigationView(){
        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_graph_map -> setFragment(mapFragment)
                R.id.nav_graph_card -> setFragment(cardFragment)
                R.id.nav_graph_scanner -> onQrScannerClick()
                R.id.nav_graph_notification -> setFragment(notificationFragment)
                R.id.nav_graph_account -> setFragment(accountFragment)
            }
            true
        }
    }

    private fun setFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.nav_host_container, fragment)
            commit()
        }
    }

    @SuppressLint("MissingSuperCall")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        qrScannerResultLiveData.value = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
    }

    private fun observeLiveData() {
        qrScannerResultLiveData.observe(this) {
            if (!it?.contents.isNullOrEmpty()) {
                scannerContents = it?.contents
                viewModel.startTransaction(JsonObject().apply {
                    addProperty("connectorId", scannerContents)
                })
            }else{
                bottomNavigationView.selectedItemId = R.id.nav_graph_map
                setFragment(mapFragment)
            }
        }
    }

    private fun onQrScannerClick() {
        if (hasCameraPermission() == PermissionChecker.PERMISSION_GRANTED) {
            openQRScanner("Scan a barcode")
        } else {
            requestPermissionLauncher.launch(android.Manifest.permission.CAMERA)
        }
    }

    private fun hasCameraPermission() = ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA)

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                openQRScanner("Scan a barcode")
            } else {
                Log.i("Permission: ", "Denied")
                bottomNavigationView.selectedItemId = R.id.nav_graph_map
                setFragment(mapFragment)
            }
        }
}