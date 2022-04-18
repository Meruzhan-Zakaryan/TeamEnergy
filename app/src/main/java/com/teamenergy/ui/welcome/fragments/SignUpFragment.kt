package com.teamenergy.ui.welcome.fragments

import android.annotation.SuppressLint
import android.content.res.ColorStateList
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.gson.JsonObject
import com.teamenergy.R
import com.teamenergy.databinding.FragmentSignUpBinding
import com.teamenergy.proxy.domain.Car
import com.teamenergy.proxy.domain.CarModel
import com.teamenergy.proxy.model.ModelTypeEnum
import com.teamenergy.proxy.network.masterData.CarDto
import com.teamenergy.proxy.network.masterData.CarModelDto
import com.teamenergy.teamenergy.BaseEnergyViewModel
import com.teamenergy.ui.EnergyApplication.Companion.masterDataLiveData
import com.teamenergy.ui.EnergyApplication.Companion.userDataLiveData
import com.teamenergy.ui.base.utils.setClickableText
import com.teamenergy.ui.base.utils.spannableUnderline
import com.teamenergy.ui.selectCar.SelectCarFragment
import org.koin.android.ext.android.inject


class SignUpFragment : Fragment() {

    private var binding: FragmentSignUpBinding? = null
    private val viewModel by inject<BaseEnergyViewModel>()
    private var selectedCar: Car? = null
    private var selectedModel: CarModel? = null
    private var emailCheck: Boolean = false
    private var phoneCheck: Boolean = false
    private var usernameCheck: Boolean = false
    private var registerRequest = JsonObject()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.getMasterData()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSignUpBinding.inflate(inflater, container, false).apply {
            lifecycleOwner = viewLifecycleOwner
            fragment = this@SignUpFragment
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
        val termsAndConditions = resources.getString(R.string.terms_and_conditions)
        binding?.termsAndConditionsTextView?.text = "${resources.getString(R.string.i_agree_with)} $termsAndConditions"
        val text = binding?.termsAndConditionsTextView?.text.toString()
        val startIndex = text.indexOf(termsAndConditions)
        val endIndex = startIndex + termsAndConditions.length
        val spanText = text.spannableUnderline(startIndex, endIndex).toString()

        binding?.termsAndConditionsTextView?.setClickableText(text, startIndex, endIndex, R.color.slate_color) {
            //findNavController().navigate(R.id.action_global_signUpFragment)
        }

    }

    private fun setupListeners() {
        binding?.toolbar?.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
        binding?.carTypetextView?.setOnClickListener {
            findNavController().navigate(SignUpFragmentDirections.actionGlobalSelectCarFragment(ModelTypeEnum.CAR, null))
        }
        binding?.carModelTypeTextView?.setOnClickListener {
            findNavController().navigate(SignUpFragmentDirections.actionGlobalSelectCarFragment(ModelTypeEnum.MODEL, selectedCar))
        }
        binding?.loginButton?.setOnClickListener {
            if (
                binding?.nameEditText?.text.toString().isNotEmpty() &&
                binding?.passwordEditText?.text.toString().isNotEmpty() &&
                binding?.phoneEditText?.text.toString().isNotEmpty() &&
                binding?.emailEditText?.text.toString().isNotEmpty() &&
                !selectedCar?.carVendorId?.equals("Car")!! &&
                !selectedModel?.carModelId?.equals("Model")!!
            ) {
                binding?.progressBarLayout?.visibility = View.VISIBLE
                registerRequest.addProperty("userName", binding?.nameEditText?.text.toString())
                registerRequest.addProperty("password", binding?.passwordEditText?.text.toString())
                registerRequest.addProperty("mobile", binding?.phoneEditText?.text.toString())
                registerRequest.addProperty("email", binding?.emailEditText?.text.toString())
                registerRequest.addProperty("carVendorId", selectedCar?.carVendorId)
                registerRequest.addProperty("carModelId", selectedModel?.carModelId)

                viewModel.checkPhone(binding?.phoneEditText?.text.toString())
                viewModel.checkEmail(binding?.emailEditText?.text.toString())
                viewModel.checkUsername(binding?.nameEditText?.text.toString())
            } else {
                binding?.progressBarLayout?.visibility = View.GONE
                Toast.makeText(requireContext(), "Invalid field", Toast.LENGTH_SHORT).show()
            }
        }
    }

    @SuppressLint("ResourceAsColor")
    private fun observeLiveData() {
        viewModel.getMasterLiveData.observe(viewLifecycleOwner) {
            masterDataLiveData.value = it
        }
        findNavController().currentBackStackEntry?.savedStateHandle?.getLiveData<Car?>(SelectCarFragment.CAR_RESULT_CODE)?.observe(viewLifecycleOwner) { carData ->
            selectedCar = carData
            binding?.carTypetextView?.setTextColor(R.color.black_color_alpha87)
            binding?.carTypetextView?.text = carData?.name
        }

        findNavController().currentBackStackEntry?.savedStateHandle?.getLiveData<CarModel?>(SelectCarFragment.MODEL_RESULT_CODE)?.observe(viewLifecycleOwner) { carModelData ->
            selectedModel = carModelData
            binding?.carModelTypeTextView?.setTextColor(R.color.black_color_alpha87)
            binding?.carModelTypeTextView?.text = carModelData.name
        }
        viewModel.registerLiveData.observe(viewLifecycleOwner) { registerData ->
            binding?.progressBarLayout?.visibility = View.GONE
            registerData ?: return@observe
            userDataLiveData.value = registerData
            findNavController().navigate(SignUpFragmentDirections.actionGlobalVerifyPhoneNumberFragment())
        }

        viewModel.registerErrorLiveData.observe(viewLifecycleOwner) { error ->
            binding?.progressBarLayout?.visibility = View.GONE
            error ?: return@observe
            Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT).show()
            viewModel.resetErrorLivedata()
        }

        viewModel.checkEmailLiveData.observe(viewLifecycleOwner) {response->
            binding?.progressBarLayout?.visibility = View.GONE
            response?:return@observe
            if (response == "available") {
                emailCheck = true
                if (emailCheck && phoneCheck && usernameCheck) {
                    binding?.progressBarLayout?.visibility = View.VISIBLE
                    viewModel.register(registerRequest)
                }
            } else {
                Toast.makeText(requireContext(), "Email $response", Toast.LENGTH_SHORT).show()
            }
            viewModel.removeCheckEmailLiveData()
        }
        viewModel.checkUsernameLiveData.observe(viewLifecycleOwner) {response->
            binding?.progressBarLayout?.visibility = View.GONE
            response?:return@observe
            if (response == "available") {
                usernameCheck = true
                if (emailCheck && phoneCheck && usernameCheck) {
                    binding?.progressBarLayout?.visibility = View.VISIBLE
                    viewModel.register(registerRequest)
                }
            } else {
                Toast.makeText(requireContext(), "Username $response", Toast.LENGTH_SHORT).show()
            }
            viewModel.removeCheckUsernameLiveData()
        }
        viewModel.checkPhoneLiveData.observe(viewLifecycleOwner) {response->
            binding?.progressBarLayout?.visibility = View.GONE
            response?:return@observe
            if (response == "available") {
                emailCheck = true
                if (emailCheck && phoneCheck && usernameCheck) {
                    binding?.progressBarLayout?.visibility = View.VISIBLE
                    viewModel.register(registerRequest)
                }
            } else {
                Toast.makeText(requireContext(), "Phone $response", Toast.LENGTH_SHORT).show()
            }
            viewModel.removeCheckPhoneData()
        }
    }
}