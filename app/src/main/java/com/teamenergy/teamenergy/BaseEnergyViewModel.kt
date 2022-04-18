package com.teamenergy.teamenergy

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.JsonObject
import com.teamenergy.proxy.domain.Charger
import com.teamenergy.proxy.domain.Master
import com.teamenergy.proxy.mapper.ChargerMapper
import com.teamenergy.proxy.mapper.MasterMapper
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

    private val _getMasterLiveData = MutableLiveData<Master?>()
    val getMasterLiveData: LiveData<Master?>
        get() = _getMasterLiveData


    fun getMasterData() {
        viewModelScope.launch(getCoroutineContext()) {
            baseEnergyRepository.getMasterData(object : ApiResultCallback<MasterDto?> {
                override fun onSuccess(response: MasterDto?) {
                    _getMasterLiveData.value = response?.let(MasterMapper.masterMapper)
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

    private val _getAllChargersLiveData = MutableLiveData<Charger?>()
    val getAllChargersLiveData: LiveData<Charger?>
        get() = _getAllChargersLiveData

    fun removeGetAllChargersLiveData() {
        _getAllChargersLiveData.value = null
    }

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
                    _getAllChargersLiveData.value = response?.let(ChargerMapper.chargerMapper)
//                    _getAllChargersLiveData.value = response
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

    private val _sendPasswordResetCodeLiveData = MutableLiveData<JsonObject?>()
    val sendPasswordResetCodeLiveData: LiveData<JsonObject?>
        get() = _sendPasswordResetCodeLiveData

    private val _sendPasswordResetCodeErrorLiveData = MutableLiveData<String?>()
    val sendPasswordResetCodeErrorLiveData: LiveData<String?>
        get() = _sendPasswordResetCodeErrorLiveData

    fun removeSendPasswordResetCodeErrorLivedata() {
        _sendPasswordResetCodeErrorLiveData.value = null
    }

    fun sendPasswordResetCode(resetPasswordRequest: JsonObject) {
        viewModelScope.launch(getCoroutineContext()) {
            baseEnergyRepository.sendPasswordResetCode(object : ApiResultCallback<JsonObject?> {
                override fun onSuccess(response: JsonObject?) {
                    _sendPasswordResetCodeLiveData.value = response
                }

                override fun onError(response: String) {
                    _sendPasswordResetCodeErrorLiveData.value = response
                }
            }, resetPasswordRequest)
        }
    }


    private val _resetPasswordLiveData = MutableLiveData<JsonObject?>()
    val resetPasswordLiveData: LiveData<JsonObject?>
        get() = _resetPasswordLiveData

    private val _resetPasswordErrorLiveData = MutableLiveData<String?>()
    val resetPasswordErrorLiveData: LiveData<String?>
        get() = _resetPasswordErrorLiveData

    fun removeResetPasswordErrorLivedata() {
        _resetPasswordErrorLiveData.value = null
    }

    fun resetPassword(resetPasswordRequest: JsonObject) {
        viewModelScope.launch(getCoroutineContext()) {
            baseEnergyRepository.resetPassword(object : ApiResultCallback<JsonObject?> {
                override fun onSuccess(response: JsonObject?) {
                    _resetPasswordLiveData.value = response
                }

                override fun onError(response: String) {
                    _resetPasswordErrorLiveData.value = response
                }
            }, resetPasswordRequest)
        }
    }

    private val _logoutLiveData = MutableLiveData<JsonObject?>()
    val logoutLiveData: LiveData<JsonObject?>
        get() = _logoutLiveData

    fun logout() {
        viewModelScope.launch(getCoroutineContext()) {
            baseEnergyRepository.logout(object : ApiResultCallback<JsonObject?> {
                override fun onSuccess(response: JsonObject?) {
                    _logoutLiveData.value = response
                }

                override fun onError(response: String) {
                    print(response)
                }

            })
        }
    }

    private val _checkEmailLiveData = MutableLiveData<String?>()
    val checkEmailLiveData: LiveData<String?>
        get() = _checkEmailLiveData

    fun removeCheckEmailLiveData(){
        _checkEmailLiveData.value = null
    }

    fun checkEmail(email: String) {
        viewModelScope.launch(getCoroutineContext()) {
            baseEnergyRepository.checkEmail(object : ApiResultCallback<String?> {
                override fun onSuccess(response: String?) {
                    _checkEmailLiveData.value = response
                }

                override fun onError(response: String) {
                    print(response)
                }

            }, email)
        }
    }

    private val _checkUsernameLiveData = MutableLiveData<String?>()
    val checkUsernameLiveData: LiveData<String?>
        get() = _checkUsernameLiveData

    fun removeCheckUsernameLiveData(){
        _checkUsernameLiveData.value = null
    }

    fun checkUsername(username: String) {
        viewModelScope.launch(getCoroutineContext()) {
            baseEnergyRepository.checkUsername(object : ApiResultCallback<String?> {
                override fun onSuccess(response: String?) {
                    _checkUsernameLiveData.value = response
                }

                override fun onError(response: String) {
                    print(response)
                }

            }, username)
        }
    }

    private val _checkPhoneLiveData = MutableLiveData<String?>()
    val checkPhoneLiveData: LiveData<String?>
        get() = _checkPhoneLiveData

    fun removeCheckPhoneData(){
        _checkPhoneLiveData.value = null
    }

    fun checkPhone(phone: String) {
        viewModelScope.launch(getCoroutineContext()) {
            baseEnergyRepository.checkPhoneNumber(object : ApiResultCallback<String?> {
                override fun onSuccess(response: String?) {
                    _checkPhoneLiveData.value = response
                }

                override fun onError(response: String) {
                    print(response)
                }

            }, phone)
        }
    }
}