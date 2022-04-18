package com.teamenergy.ui.chargerInfo

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.teamenergy.R
import com.teamenergy.databinding.FragmentChargerInfoBinding
import com.teamenergy.databinding.FragmentMapBinding
import com.teamenergy.proxy.domain.ChargerItem
import com.teamenergy.teamenergy.BaseEnergyViewModel
import com.teamenergy.ui.home.HomeActivity
import com.teamenergy.ui.selectCar.SelectCarFragment
import com.teamenergy.ui.selectCar.SelectCarFragmentArgs
import com.yandex.mapkit.user_location.UserLocationLayer
import org.koin.android.ext.android.inject


class ChargerInfoFragment : Fragment() {

    private var binding: FragmentChargerInfoBinding? = null
    private val viewModel by inject<BaseEnergyViewModel>()
    private val args by navArgs<ChargerInfoFragmentArgs>()
    private lateinit var chargerItem: ChargerItem
    private val adapter = ConnectorAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        chargerItem = args.charger
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentChargerInfoBinding.inflate(inflater, container, false).apply {
            lifecycleOwner = viewLifecycleOwner
            fragment = this@ChargerInfoFragment
        }
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
        setupListeners()
        observeLiveData()
    }

    private fun setupView() {
        (requireActivity() as HomeActivity).setBottomVisibility(false)
        binding?.connectorRecyclerView?.adapter = adapter
        adapter.update(chargerItem.connectors)
        binding?.toolbarTitle?.text = chargerItem.name
        binding?.chargerName?.text = chargerItem.name
        binding?.chargerAddress?.text = chargerItem.address
        binding?.numberTextView?.text = chargerItem.phone

    }

    private fun setupListeners() {
        binding?.directionButton?.setOnClickListener {
            findNavController().navigate(ChargerInfoFragmentDirections.actionGlobalFullScreenMapFragment(chargerItem))
        }
        binding?.toolbar?.setNavigationOnClickListener {
            findNavController().navigateUp()
        }

    }

    private fun observeLiveData() {

    }

    companion object {
        const val START_DIRECTION = "start_direction"
    }
}