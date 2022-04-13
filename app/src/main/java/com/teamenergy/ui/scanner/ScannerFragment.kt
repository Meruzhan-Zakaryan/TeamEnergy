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
import com.teamenergy.R
import com.teamenergy.databinding.FragmentNotificationBinding
import com.teamenergy.databinding.FragmentScannerBinding
import com.teamenergy.teamenergy.BaseEnergyViewModel
import com.teamenergy.ui.base.utils.openQRScanner
import org.koin.android.ext.android.inject


class ScannerFragment : Fragment() {

    private var binding: FragmentScannerBinding? = null
    private val viewModel by inject<BaseEnergyViewModel>()


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

    }



}