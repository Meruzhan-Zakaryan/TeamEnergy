package com.teamenergy.ui.reset

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.teamenergy.databinding.FragmentForgotPasswordBinding
import com.teamenergy.teamenergy.BaseEnergyViewModel
import org.koin.android.ext.android.inject


class ForgotPasswordFragment : Fragment() {

    private var binding: FragmentForgotPasswordBinding? = null
    private val viewModel by inject<BaseEnergyViewModel>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentForgotPasswordBinding.inflate(inflater, container, false).apply {
            lifecycleOwner = viewLifecycleOwner
            fragment = this@ForgotPasswordFragment
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

    }

    private fun setupListeners() {
        binding?.toolbar?.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
        binding?.continueButton?.setOnClickListener {
            val phoneNumber = binding?.phoneEditText?.text.toString()
            if (phoneNumber.isNotEmpty()) {
                binding?.progressBarLayout?.visibility = View.VISIBLE
                val jsonData = JsonObject()
                jsonData.addProperty("mobile", phoneNumber)
                viewModel.sendPasswordResetCode(jsonData)
            }
        }
    }

    private fun observeLiveData() {
        viewModel.sendPasswordResetCodeLiveData.observe(viewLifecycleOwner) { resetPasswordData ->
            binding?.progressBarLayout?.visibility = View.GONE
            findNavController().navigate(ForgotPasswordFragmentDirections.actionGlobalResetPasswordVerifyCodeFragment(binding?.phoneEditText?.text.toString()))
        }
        viewModel.sendPasswordResetCodeErrorLiveData.observe(viewLifecycleOwner) { error ->
            error ?: return@observe
            binding?.progressBarLayout?.visibility = View.GONE
            Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT).show()
            viewModel.removeSendPasswordResetCodeErrorLivedata()
        }
    }
}