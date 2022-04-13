package com.teamenergy.ui.selectCar

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.teamenergy.databinding.FragmentSelectCarBinding
import com.teamenergy.proxy.model.ModelTypeEnum
import com.teamenergy.teamenergy.BaseEnergyViewModel
import com.teamenergy.ui.EnergyApplication.Companion.masterDataLiveData
import org.koin.android.ext.android.inject

class SelectCarFragment : Fragment() {

    private var binding: FragmentSelectCarBinding? = null
    private val viewModel by inject<BaseEnergyViewModel>()
    private val args by navArgs<SelectCarFragmentArgs>()
    private val carTypeAdapter = CarTypeAdapter { item ->
        findNavController().previousBackStackEntry?.savedStateHandle?.set(CAR_RESULT_CODE, item)
        // setFragmentResult(CAR_RESULT_CODE, bundleOf("item" to item))
        findNavController().navigateUp()
    }
    private val modelTypeAdapter = ModelTypeAdapter { item ->
        findNavController().previousBackStackEntry?.savedStateHandle?.set(MODEL_RESULT_CODE, item)
        // setFragmentResult(CAR_RESULT_CODE, bundleOf("item" to item))
        findNavController().navigateUp()
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentSelectCarBinding.inflate(inflater, container, false).apply {
            lifecycleOwner = viewLifecycleOwner
            fragment = this@SelectCarFragment
        }
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews()
        setupListeners()
    }

    private fun setupViews() {
        when (args.type) {
            ModelTypeEnum.CAR -> {
                binding?.recyclerView?.adapter = carTypeAdapter
                carTypeAdapter.update(masterDataLiveData.value?.carList)
            }
            ModelTypeEnum.MODEL -> {
                binding?.recyclerView?.adapter = modelTypeAdapter
                modelTypeAdapter.update(args.carData?.models)
            }

        }
    }

    private fun setupListeners() {
        binding?.toolbar?.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun observeLiveData() {

    }

    companion object {
        const val CAR_RESULT_CODE = "car_result_code"
        const val MODEL_RESULT_CODE = "model_result_code"
    }

}