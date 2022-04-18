package com.teamenergy.ui.card

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.teamenergy.R
import com.teamenergy.databinding.FragmentCardBinding
import com.teamenergy.databinding.FragmentScannerBinding
import com.teamenergy.teamenergy.BaseEnergyViewModel
import com.teamenergy.ui.home.HomeActivity
import org.koin.android.ext.android.inject


class CardFragment : Fragment() {

    private var binding: FragmentCardBinding? = null
    private val viewModel by inject<BaseEnergyViewModel>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentCardBinding.inflate(inflater, container, false).apply {
            lifecycleOwner = viewLifecycleOwner
            fragment = this@CardFragment
        }
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (requireActivity() as HomeActivity).setBottomVisibility(true)
    }
}