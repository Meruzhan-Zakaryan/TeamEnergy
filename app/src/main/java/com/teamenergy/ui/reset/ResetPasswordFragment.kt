package com.teamenergy.ui.reset

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.gson.JsonObject
import com.teamenergy.R
import com.teamenergy.databinding.FragmentResetPasswordBinding
import com.teamenergy.databinding.FragmentResetPasswordVerifyCodeBinding
import com.teamenergy.teamenergy.BaseEnergyViewModel
import org.koin.android.ext.android.inject


class ResetPasswordFragment : Fragment() {

    private var binding: FragmentResetPasswordBinding? = null
    private val viewModel by inject<BaseEnergyViewModel>()
    private val args by navArgs<ResetPasswordFragmentArgs>()
    private lateinit var phoneNumber: String
    private lateinit var verifyCode: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        phoneNumber = args.number
        verifyCode = args.code
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentResetPasswordBinding.inflate(inflater, container, false).apply {
            lifecycleOwner = viewLifecycleOwner
            fragment = this@ResetPasswordFragment
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
        binding?.resetPasswordButton?.setOnClickListener {
            binding?.progressBarLayout?.visibility = View.VISIBLE
            val newPassword = binding?.passwordEditText?.text.toString()
            val confirmNewsPassword = binding?.confirmPasswordEditText?.text.toString()
            if (newPassword.isNotEmpty() && confirmNewsPassword.isNotEmpty() && newPassword == confirmNewsPassword) {
                val jsonObject = JsonObject()
                jsonObject.addProperty("mobile", phoneNumber)
                jsonObject.addProperty("code", verifyCode)
                jsonObject.addProperty("newPassword", newPassword)
                jsonObject.addProperty("confirmPassword", confirmNewsPassword)
                viewModel.resetPassword(jsonObject)
            } else {
                binding?.progressBarLayout?.visibility = View.GONE
                Toast.makeText(requireContext(), "Incorrect Password", Toast.LENGTH_SHORT).show()
            }
        }
        binding?.loginButton?.setOnClickListener {
            findNavController().navigate(ResetPasswordFragmentDirections.actionGlobalLoginFragment())
        }
    }

    private fun observeLiveData() {
        viewModel.resetPasswordLiveData.observe(viewLifecycleOwner) {
            binding?.progressBarLayout?.visibility = View.GONE
            binding?.successLayout?.visibility = View.VISIBLE
        }

        viewModel.resetPasswordErrorLiveData.observe(viewLifecycleOwner) { errorMessage ->
            errorMessage ?: return@observe
            binding?.progressBarLayout?.visibility = View.GONE
            Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_SHORT).show()
            viewModel.removeResetPasswordErrorLivedata()
        }
    }
}