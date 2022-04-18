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
import com.teamenergy.databinding.FragmentResetPasswordVerifyCodeBinding
import com.teamenergy.databinding.FragmentVerifyPhoneNumberBinding
import com.teamenergy.teamenergy.BaseEnergyViewModel
import com.teamenergy.ui.EnergyApplication
import com.teamenergy.ui.EnergyApplication.Companion.userDataLiveData
import com.teamenergy.ui.base.utils.setClickableText
import com.teamenergy.ui.base.utils.spannableUnderline
import com.teamenergy.ui.selectCar.SelectCarFragmentArgs
import org.koin.android.ext.android.inject

class ResetPasswordVerifyCodeFragment : Fragment() {

    private var binding: FragmentResetPasswordVerifyCodeBinding? = null
    private val viewModel by inject<BaseEnergyViewModel>()
    private val args by navArgs<ResetPasswordVerifyCodeFragmentArgs>()
    private lateinit var phoneNumber: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        phoneNumber = args.number
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentResetPasswordVerifyCodeBinding.inflate(inflater, container, false).apply {
            lifecycleOwner = viewLifecycleOwner
            fragment = this@ResetPasswordVerifyCodeFragment
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
        val resetCode = resources.getString(R.string.resent_code)
        binding?.resetPasswordTextView?.text = "${resources.getString(R.string.can_not_receive_sms)} $resetCode"
        val text = binding?.resetPasswordTextView?.text.toString()
        val startIndex = text.indexOf(resetCode)
        val endIndex = startIndex + resetCode.length
        val spanText = text.spannableUnderline(startIndex, endIndex).toString()

        binding?.resetPasswordTextView?.setClickableText(text, startIndex, endIndex, R.color.slate_color) {
            binding?.progressBarLayout?.visibility = View.VISIBLE
            viewModel.sendPasswordResetCode(JsonObject().apply {
                addProperty("mobile", phoneNumber)
            })
        }
    }

    private fun setupListeners() {
        binding?.toolbar?.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
        binding?.pinView?.setPinViewEventListener { pinview, fromUser ->
            findNavController().navigate(ResetPasswordVerifyCodeFragmentDirections.actionGlobalResetPasswordFragment(phoneNumber, pinview.value))
        }
    }

    private fun observeLiveData() {
        viewModel.sendPasswordResetCodeErrorLiveData.observe(viewLifecycleOwner) { error ->
            error ?: return@observe
            Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT).show()
            viewModel.removeResetPasswordErrorLivedata()
        }
    }
}