package com.teamenergy.ui.filter

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import com.google.gson.JsonObject
import com.teamenergy.R
import com.teamenergy.databinding.FragmentAccountBinding
import com.teamenergy.databinding.FragmentFilterBinding
import com.teamenergy.proxy.domain.ConnectorType
import com.teamenergy.proxy.domain.Status
import com.teamenergy.teamenergy.BaseEnergyViewModel
import com.teamenergy.ui.EnergyApplication.Companion.masterDataLiveData
import com.teamenergy.ui.home.HomeActivity
import org.koin.android.ext.android.inject

class FilterFragment : Fragment() {

    private var binding: FragmentFilterBinding? = null
    private val viewModel by inject<BaseEnergyViewModel>()
    private var connectorType: ConnectorType? = null
    private var statusType: Status? = null
    private val portTypeAdapter = PortTypeAdapter { connectorType ->
        this.connectorType = connectorType
    }
    private val statusAdapter = ChargerStatusAdapter {statusType->
        this.statusType = statusType
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.getMasterData()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFilterBinding.inflate(inflater, container, false).apply {
            lifecycleOwner = viewLifecycleOwner
            fragment = this@FilterFragment
        }
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews()
        setupListeners()
        observeLiveData()
    }

    private fun setupViews() {
        (requireActivity() as HomeActivity).setBottomVisibility(false)
        binding?.chargerStatusRecyclerView?.adapter = statusAdapter
        binding?.connectorTypeRecyclerView?.adapter = portTypeAdapter

    }

    private fun setupListeners() {
        binding?.toolbar?.setNavigationOnClickListener {
            findNavController().navigate(R.id.nav_graph_map)
        }
        binding?.toFilterButton?.setOnClickListener {
            if (statusType?.key != null && connectorType?.key != null) {
                val jsonObject = JsonObject()
                jsonObject.addProperty("status", statusType?.key)
                jsonObject.addProperty("connectorTypeId", connectorType?.key)
                viewModel.getAllChargers(jsonObject)
                findNavController().navigate(FilterFragmentDirections.actionGlobalMapFragment(false))
            } else {
                Toast.makeText(requireContext(), "Part of details is missing", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun observeLiveData() {
        viewModel.getMasterLiveData.observe(viewLifecycleOwner) { masterDto ->
            binding?.progressBarLayout?.visibility = View.GONE
            masterDto?.statusList ?: return@observe
            masterDto.connectorTypes ?: return@observe
            statusAdapter.update(masterDto.statusList)
            portTypeAdapter.update(masterDto.connectorTypes)
        }
        masterDataLiveData.observe(viewLifecycleOwner) { masterDto ->
            masterDto.statusList
            masterDto.connectorTypes
        }
        viewModel.getAllChargersLiveData.observe(viewLifecycleOwner) {

        }
    }
}