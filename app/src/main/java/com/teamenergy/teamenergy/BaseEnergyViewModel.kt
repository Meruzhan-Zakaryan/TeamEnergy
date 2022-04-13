package com.teamenergy.teamenergy

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.JsonObject
import com.teamenergy.proxy.network.masterData.ChargerDto
import com.teamenergy.proxy.network.masterData.LoginDto
import com.teamenergy.proxy.network.masterData.MasterDto
import com.teamenergy.proxy.network.masterData.UserDto
import com.teamenergy.teamenergy.net.ApiResultCallback
import com.teamenergy.teamenergy.repository.BaseEnergyRepository
import com.teamenergy.ui.EnergyApplication.Companion.getCoroutineContext
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import org.json.JSONObject

class BaseEnergyViewModel(private val baseEnergyRepository: BaseEnergyRepository) : ViewModel() {
    val errorMessages = MutableLiveData<String?>()
    var job: Job? = null

    private val _getMasterLiveData = MutableLiveData<MasterDto?>()
    val getMasterLiveData: LiveData<MasterDto?>
        get() = _getMasterLiveData


    fun getMasterData() {
        viewModelScope.launch(getCoroutineContext()) {
            baseEnergyRepository.getMasterData(object : ApiResultCallback<MasterDto?> {
                override fun onSuccess(response: MasterDto?) {
                    _getMasterLiveData.value = response
                }

                override fun onError(response: String) {

                }
            })
        }
    }

    private val _loginLiveData = MutableLiveData<LoginDto?>()
    val loginLiveData: LiveData<LoginDto?>
        get() = _loginLiveData

    private val _loginErrorLiveData = MutableLiveData<String?>()
    val loginErrorLiveData: LiveData<String?>
        get() = _loginErrorLiveData

    fun resetLoginErrorLiveData() {
        _loginErrorLiveData.value = null
    }


    fun login(loginData: JsonObject) {
        viewModelScope.launch(getCoroutineContext()) {
            baseEnergyRepository.login(object : ApiResultCallback<LoginDto?> {
                override fun onSuccess(response: LoginDto?) {
                    _loginLiveData.value = response
                }

                override fun onError(response: String) {
                    _loginErrorLiveData.value = response
                }
            }, loginData = loginData)
        }
    }

    private val _registerLiveData = MutableLiveData<UserDto?>()
    val registerLiveData: LiveData<UserDto?>
        get() = _registerLiveData

    private val _registerErrorLiveData = MutableLiveData<String?>()
    val registerErrorLiveData: LiveData<String?>
        get() = _registerErrorLiveData

    fun resetErrorLivedata() {
        _registerErrorLiveData.value = null
    }

    fun register(registerData: JsonObject) {
        viewModelScope.launch(getCoroutineContext()) {
            baseEnergyRepository.register(object : ApiResultCallback<UserDto?> {
                override fun onSuccess(response: UserDto?) {
                    _registerLiveData.value = response
                }

                override fun onError(response: String) {
                    _registerErrorLiveData.value = response
                }
            }, registerData = registerData)
        }
    }

    private val _verifyPhoneNumberLiveData = MutableLiveData<JsonObject?>()
    val verifyPhoneNumberLiveData: LiveData<JsonObject?>
        get() = _verifyPhoneNumberLiveData

    private val _verifyPhoneNumberErrorLiveData = MutableLiveData<String?>()
    val verifyPhoneNumberErrorLiveData: LiveData<String?>
        get() = _verifyPhoneNumberErrorLiveData

    fun resetVerifyPhoneNumberErrorLivedata() {
        _verifyPhoneNumberErrorLiveData.value = null
    }

    fun verifyPhoneNumber(verifyPhoneNumberRequest: JsonObject) {
        viewModelScope.launch(getCoroutineContext()) {
            baseEnergyRepository.verifyPhoneNumber(object : ApiResultCallback<JsonObject?> {
                override fun onSuccess(response: JsonObject?) {
                    _verifyPhoneNumberLiveData.value = response
                }

                override fun onError(response: String) {
                    _verifyPhoneNumberErrorLiveData.value = response
                }
            }, verifyPhoneNumberRequest)
        }
    }

    private val _resetVerifyCodeLiveData = MutableLiveData<JsonObject?>()
    val resetVerifyCodeLiveData: LiveData<JsonObject?>
        get() = _resetVerifyCodeLiveData

    private val _resetVerifyCodeErrorLiveData = MutableLiveData<String?>()
    val resetVerifyCodeErrorLiveData: LiveData<String?>
        get() = _resetVerifyCodeErrorLiveData

    fun deleteResetVerifyCodeErrorLivedata() {
        _resetVerifyCodeErrorLiveData.value = null
    }

    fun resetVerifyCode(resetVerifyCodeRequest: JsonObject) {
        viewModelScope.launch(getCoroutineContext()) {
            baseEnergyRepository.resetVerifyCode(object : ApiResultCallback<JsonObject?> {
                override fun onSuccess(response: JsonObject?) {
                    _resetVerifyCodeLiveData.value = response
                }

                override fun onError(response: String) {
                    _resetVerifyCodeErrorLiveData.value = response
                }
            }, resetVerifyCodeRequest)
        }
    }

    private val _getAllChargersLiveData = MutableLiveData<ChargerDto?>()
    val getAllChargersLiveData: LiveData<ChargerDto?>
        get() = _getAllChargersLiveData

    private val _getAllChargersErrorLiveData = MutableLiveData<String?>()
    val getAllChargersErrorLiveData: LiveData<String?>
        get() = _getAllChargersErrorLiveData

    fun resetGetAllChargersErrorLivedata() {
        _getAllChargersErrorLiveData.value = null
    }

    fun getAllChargers(getAllChargersRequest: JsonObject) {
        viewModelScope.launch(getCoroutineContext()) {
            baseEnergyRepository.getAllChargers(object : ApiResultCallback<ChargerDto?> {
                override fun onSuccess(response: ChargerDto?) {
                    _getAllChargersLiveData.value = response
                }

                override fun onError(response: String) {
                    _getAllChargersErrorLiveData.value = response
                }
            }, getAllChargersRequest)
        }
    }

    private val _startTransactionLiveData = MutableLiveData<JsonObject?>()
    val startTransactionLiveData: LiveData<JsonObject?>
        get() = _startTransactionLiveData

    fun startTransaction(getAllChargersRequest: JsonObject) {
        viewModelScope.launch(getCoroutineContext()) {
            baseEnergyRepository.startTransaction(object : ApiResultCallback<JsonObject?> {
                override fun onSuccess(response: JsonObject?) {
                    _startTransactionLiveData.value = response
                }

                override fun onError(response: String) {
                    _getAllChargersErrorLiveData.value = response
                }
            }, getAllChargersRequest)
        }
    }
}