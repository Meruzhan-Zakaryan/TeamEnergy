package com.teamenergy.ui.account

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.teamenergy.R
import com.teamenergy.databinding.FragmentAccountBinding
import com.teamenergy.databinding.FragmentLoginBinding
import com.teamenergy.teamenergy.BaseEnergyViewModel
import com.teamenergy.ui.EnergyApplication.Companion.loginDataLiveData
import com.teamenergy.ui.base.utils.SharedPreferenceUtils
import com.teamenergy.ui.home.HomeActivity
import com.teamenergy.ui.welcome.WelcomeActivity
import org.koin.android.ext.android.inject


class AccountFragment : Fragment() {

    private var binding: FragmentAccountBinding? = null
    private val viewModel by inject<BaseEnergyViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentAccountBinding.inflate(inflater, container, false).apply {
            lifecycleOwner = viewLifecycleOwner
            fragment = this@AccountFragment
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
        (requireActivity() as HomeActivity).setBottomVisibility(true)
    }

    private fun setupListeners() {
        binding?.logoutButton?.setOnClickListener {
            // viewModel.logout()
            binding?.progressBarLayout?.visibility = View.VISIBLE
            SharedPreferenceUtils().removeUserData(requireActivity())
            loginDataLiveData.value = null
            binding?.progressBarLayout?.visibility = View.GONE
            startActivity(Intent(requireActivity(), WelcomeActivity::class.java))
            requireActivity().finish()
        }

    }

    private fun observeLiveData() {
        loginDataLiveData.observe(viewLifecycleOwner) { userData ->
            binding?.usernameTextView?.text = userData?.data?.userName
            binding?.mailTextView?.text = userData?.data?.email
            binding?.phoneTextView?.text = userData?.data?.mobile
            binding?.carVendorTextView?.text = userData?.data?.carsList?.get(0)?.carVendor
            binding?.carModelTextView?.text = userData?.data?.carsList?.get(0)?.carModel
        }

        viewModel.loginLiveData.observe(viewLifecycleOwner) {
            SharedPreferenceUtils().removeUserData(requireActivity())
            loginDataLiveData.value = null
        }
    }
}