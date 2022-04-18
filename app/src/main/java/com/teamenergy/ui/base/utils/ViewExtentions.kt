package com.teamenergy.ui.base.utils

import android.app.Activity
import android.view.View
import android.widget.AdapterView
import android.widget.Spinner
import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.google.zxing.integration.android.IntentIntegrator
import com.teamenergy.ui.base.CaptureActivityPortrait

fun Spinner.onItemSelected(
    onNothingSelected: (AdapterView<*>?) -> Unit = {},
    onItemSelected: (AdapterView<*>?, View?, Int, Long) -> Unit
) {
    this.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
        override fun onNothingSelected(p0: AdapterView<*>?) {
            onNothingSelected(p0)
        }

        override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
            onItemSelected(p0, p1, p2, p3)
        }

    }
}

fun Spinner.selectedPosition(): Int = if (this.selectedItemPosition == -1) 0 else this.selectedItemPosition

fun Fragment.openQRScanner(title: String) {
    val intentIntegrator = IntentIntegrator(requireActivity())
    intentIntegrator.setPrompt(title)
    intentIntegrator.setCameraId(0)
    intentIntegrator.setOrientationLocked(true)
    intentIntegrator.captureActivity = CaptureActivityPortrait::class.java
    intentIntegrator.setBeepEnabled(true)
    intentIntegrator.initiateScan()
}

fun Activity.openQRScanner(title: String) {
    val intentIntegrator = IntentIntegrator(this)
    intentIntegrator.setPrompt(title)
    intentIntegrator.setCameraId(0)
    intentIntegrator.setOrientationLocked(true)
    intentIntegrator.captureActivity = CaptureActivityPortrait::class.java
    intentIntegrator.setBeepEnabled(true)
    intentIntegrator.initiateScan()
}

fun NavController.clearBackStack() = popBackStack(graph.startDestination, false)