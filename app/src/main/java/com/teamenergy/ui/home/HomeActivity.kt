package com.teamenergy.ui.home

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.PermissionChecker
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.navigation.NavController
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
import org.koin.android.ext.android.inject

class HomeActivity : AppCompatActivity() {

    private var binding: ActivityHomeBinding? = null
    private val viewModel by inject<BaseEnergyViewModel>()
    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var navController: NavController
    private var currentNavController: LiveData<NavController>? = null
    private val mapFragment = MapFragment()
    private val cardFragment = CardFragment()
    private val scannerFragment = ScannerFragment()
    private val notificationFragment = NotificationFragment()
    private val accountFragment = AccountFragment()
    private var scannerContents: String? = null

    @RequiresApi(Build.VERSION_CODES.S)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_home)
        binding?.activity = this@HomeActivity
        setContentView(binding?.root)
        observeLiveData()
        bottomNavigationView = binding?.bottomNavigationView!!
//        navController = findNavController(R.id.nav_host_container)
//        setupWithNavController(bottomNavigationView,navController)
//        setFragment(mapFragment)
//        setupBottomNavigationView()
        setupBottomNavigationBar()
     //   setupBottomNavigationView()
    }

    @RequiresApi(Build.VERSION_CODES.S)
    private fun setupBottomNavigationBar() {

        val navGraphIds = listOf(
            R.navigation.nav_graph_map,
            R.navigation.nav_graph_card,
            R.navigation.nav_graph_scanner,
            R.navigation.nav_graph_notification,
            R.navigation.nav_graph_account
        )

            val controller: LiveData<NavController> = bottomNavigationView.setupWithNavController(
                navGraphIds = navGraphIds,
                fragmentManager = supportFragmentManager,
                containerId = R.id.nav_host_container,
                intent = intent
            )
//        navController.addOnDestinationChangedListener { _, destination, _ ->
//            when (destination.id){
//                R.id.filterFragment-> bottomNavigationView.visibility = View.GONE
//                else->{
//                    bottomNavigationView.visibility  =View.VISIBLE
//                }
//            }
//        }
            currentNavController = controller

    }

    override fun onSupportNavigateUp(): Boolean {
        return currentNavController?.value?.navigateUp() ?: false
    }

    fun setBottomVisibility(visibility:Boolean){
        binding?.bottomNavigationView?.isVisible = visibility
    }

    private fun setupBottomNavigationView(){
        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
//                R.id.nav_graph_map -> setFragment(mapFragment)
//                R.id.nav_graph_card -> setFragment(cardFragment)
                R.id.nav_graph_scanner -> onQrScannerClick()
//                R.id.nav_graph_notification -> setFragment(notificationFragment)
//                R.id.nav_graph_account -> setFragment(accountFragment)
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
                //setFragment(mapFragment)
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
                //setFragment(mapFragment)
            }
        }
}