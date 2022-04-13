package com.teamenergy.ui.verify

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.google.gson.JsonObject
import com.teamenergy.R
import com.teamenergy.databinding.FragmentSignUpBinding
import com.teamenergy.databinding.FragmentVerifyPhoneNumberBinding
import com.teamenergy.teamenergy.BaseEnergyViewModel
import com.teamenergy.ui.EnergyApplication.Companion.userDataLiveData
import com.teamenergy.ui.base.utils.setClickableText
import com.teamenergy.ui.base.utils.spannableUnderline
import com.teamenergy.ui.home.HomeActivity
import com.teamenergy.ui.welcome.WelcomeActivity
import org.koin.android.ext.android.inject


class VerifyPhoneNumberFragment : Fragment() {


    private var binding: FragmentVerifyPhoneNumberBinding? = null
    private val viewModel by inject<BaseEnergyViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentVerifyPhoneNumberBinding.inflate(inflater, container, false).apply {
            lifecycleOwner = viewLifecycleOwner
            fragment = this@VerifyPhoneNumberFragment
        }
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews()
        observeLiveData()
    }

    private fun setupViews() {
        binding?.pinView?.setPinViewEventListener { pinview, fromUser ->
            binding?.progressBarLayout?.visibility = View.VISIBLE
            viewModel.verifyPhoneNumber(JsonObject().apply {
                addProperty("userId", userDataLiveData.value?.data?.userId)
                addProperty("code", pinview.value)
            })
        }
        val resetCode = resources.getString(R.string.resent_code)
        binding?.resetPasswordTextView?.text = "${resources.getString(R.string.can_not_receive_sms)} $resetCode"
        val text = binding?.resetPasswordTextView?.text.toString()
        val startIndex = text.indexOf(resetCode)
        val endIndex = startIndex + resetCode.length
        val spanText = text.spannableUnderline(startIndex, endIndex).toString()

        binding?.resetPasswordTextView?.setClickableText(text, startIndex, endIndex, R.color.slate_color) {
            binding?.progressBarLayout?.visibility = View.VISIBLE
            viewModel.resetVerifyCode(JsonObject().apply {
                addProperty("mobile", userDataLiveData.value?.data?.mobile)
            })
        }
    }

    private fun observeLiveData() {
        viewModel.verifyPhoneNumberLiveData.observe(viewLifecycleOwner) { verifyData ->
            binding?.progressBarLayout?.visibility = View.GONE
            verifyData ?: return@observe
            startActivity(Intent(requireActivity(), HomeActivity::class.java))
        }
        viewModel.verifyPhoneNumberErrorLiveData.observe(viewLifecycleOwner) { error ->
            binding?.progressBarLayout?.visibility = View.GONE
            error ?: return@observe
            Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT).show()
            viewModel.resetVerifyPhoneNumberErrorLivedata()
        }
        viewModel.resetVerifyCodeLiveData.observe(viewLifecycleOwner) { resetPassword ->
            binding?.progressBarLayout?.visibility = View.GONE

        }
        viewModel.resetVerifyCodeErrorLiveData.observe(viewLifecycleOwner) { error ->
            binding?.progressBarLayout?.visibility = View.GONE
            error ?: return@observe
            Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT).show()
            viewModel.deleteResetVerifyCodeErrorLivedata()
        }
    }
}