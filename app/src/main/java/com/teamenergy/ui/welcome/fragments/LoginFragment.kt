package com.teamenergy.ui.welcome.fragments

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.teamenergy.R
import com.teamenergy.databinding.FragmentLoginBinding
import com.teamenergy.databinding.FragmentSignUpBinding
import com.teamenergy.teamenergy.BaseEnergyViewModel
import com.teamenergy.ui.EnergyApplication.Companion.loginDataLiveData
import com.teamenergy.ui.EnergyApplication.Companion.masterDataLiveData
import com.teamenergy.ui.base.utils.JsonBuilder
import com.teamenergy.ui.base.utils.setClickableText
import com.teamenergy.ui.base.utils.spannableColor
import com.teamenergy.ui.base.utils.spannableUnderline
import com.teamenergy.ui.home.HomeActivity
import com.teamenergy.ui.welcome.WelcomeActivity
import org.json.JSONObject
import org.koin.android.ext.android.inject

class LoginFragment : Fragment() {

    private var binding: FragmentLoginBinding? = null
    private val viewModel by inject<BaseEnergyViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.getMasterData()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        // Inflate the layout for this fragment
        binding = FragmentLoginBinding.inflate(inflater, container, false).apply {
            lifecycleOwner = viewLifecycleOwner
            fragment = this@LoginFragment
        }
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews()
        setupListeners()
        observeLiveData()
    }

    @SuppressLint("SetTextI18n")
    private fun setupViews() {
        val signUp = resources.getString(R.string.sign_up)
        binding?.signUpTextView?.text = "${resources.getString(R.string.do_not_have_an_account)} $signUp"
        val text = binding?.signUpTextView?.text.toString()
        val startIndex = text.indexOf(signUp)
        val endIndex = startIndex + signUp.length
        val spanText = text.spannableUnderline(startIndex, endIndex).toString()

        binding?.signUpTextView?.setClickableText(text, startIndex, endIndex, R.color.slate_color) {
            findNavController().navigate(R.id.action_global_signUpFragment)
        }
    }

    private fun setupListeners() {
        binding?.loginButton?.setOnClickListener {
            if (!binding?.loginEditText?.text.toString().isNullOrEmpty() && !binding?.passwordEditText?.text.toString().isNullOrEmpty()) {
//                val jsonData = JsonBuilder(
//                    "userName" to  binding?.loginEditText?.text.toString(),
//                    "password" to  binding?.passwordEditText?.text.toString()
//                )
                val jsonData = JsonObject()
                jsonData.add("userName", JsonParser.parseString(binding?.loginEditText?.text.toString()))
                jsonData.add("password", JsonParser.parseString(binding?.passwordEditText?.text.toString()))
                binding?.progressBarLayout?.visibility = View.VISIBLE
                viewModel.login(jsonData)
            }
        }
    }

    private fun observeLiveData() {
        viewModel.loginLiveData.observe(viewLifecycleOwner) { loginData ->
            binding?.progressBarLayout?.visibility = View.GONE
            loginData?.data ?: return@observe
            loginDataLiveData.value = loginData
            startActivity(Intent(requireActivity(), HomeActivity::class.java))
            requireActivity().finish()
        }
        viewModel.loginErrorLiveData.observe(viewLifecycleOwner) { errorMessage ->
            binding?.progressBarLayout?.visibility = View.GONE
            errorMessage ?: return@observe
            if (!errorMessage.isNullOrEmpty()) {
                Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_SHORT).show()
                viewModel.resetLoginErrorLiveData()
            }
        }
        viewModel.getMasterLiveData.observe(viewLifecycleOwner) { masterData ->
            masterData ?: return@observe
            masterDataLiveData.value = masterData
        }
    }
}