package com.teamenergy.ui.scanner

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.content.PermissionChecker
import androidx.navigation.fragment.findNavController
import com.google.gson.JsonObject
import com.teamenergy.R
import com.teamenergy.databinding.FragmentNotificationBinding
import com.teamenergy.databinding.FragmentScannerBinding
import com.teamenergy.teamenergy.BaseEnergyViewModel
import com.teamenergy.ui.EnergyApplication
import com.teamenergy.ui.base.utils.openQRScanner
import com.teamenergy.ui.home.HomeActivity
import org.koin.android.ext.android.inject


class ScannerFragment : Fragment() {

    private var binding: FragmentScannerBinding? = null
    private val viewModel by inject<BaseEnergyViewModel>()
    private var scannerContents: String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentScannerBinding.inflate(inflater, container, false).apply {
            lifecycleOwner = viewLifecycleOwner
            fragment = this@ScannerFragment
        }
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onQrScannerClick()
        setupViews()
        setupListeners()
        observeLiveData()

    }

    private fun setupViews(){
        (requireActivity() as HomeActivity).setBottomVisibility(true)
    }

    private fun setupListeners(){

    }

    private fun observeLiveData(){
        EnergyApplication.qrScannerResultLiveData.observe(requireActivity()) {
            if (!it?.contents.isNullOrEmpty()) {
                scannerContents = it?.contents
                viewModel.startTransaction(JsonObject().apply {
                    addProperty("connectorId", scannerContents)
                })
            }
//            else{
//                findNavController().navigate(ScannerFragmentDirections.actionGlobalMapFragment())
//                //setFragment(mapFragment)
//            }
        }
    }

    private fun onQrScannerClick() {
        if (hasCameraPermission() == PermissionChecker.PERMISSION_GRANTED) {
            openQRScanner("Scan a barcode")
        } else {
            requestPermissionLauncher.launch(android.Manifest.permission.CAMERA)
        }
    }

    private fun hasCameraPermission() = ContextCompat.checkSelfPermission(requireContext(), android.Manifest.permission.CAMERA)

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                openQRScanner("Scan a barcode")
            } else {
                Log.i("Permission: ", "Denied")
//                findNavController().navigate(ScannerFragmentDirections.actionGlobalMapFragment())
                //setFragment(mapFragment)
            }
        }
}