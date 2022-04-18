package com.teamenergy.teamenergy.repository

import com.google.gson.JsonObject
import com.teamenergy.proxy.network.masterData.ChargerDto
import com.teamenergy.proxy.network.masterData.LoginDto
import com.teamenergy.proxy.network.masterData.MasterDto
import com.teamenergy.proxy.network.masterData.UserDto
import com.teamenergy.teamenergy.net.ApiResultCallback
import com.teamenergy.teamenergy.net.EnergyDataSource
import com.teamenergy.teamenergy.net.coroutineResponseWithContext
import org.json.JSONObject

interface BaseEnergyRepository {
    suspend fun getMasterData(resultCallback: ApiResultCallback<MasterDto?>)

    suspend fun login(resultCallback: ApiResultCallback<LoginDto?>, loginData: JsonObject)

    suspend fun logout(resultCallback: ApiResultCallback<JsonObject?>)

    suspend fun register(resultCallback: ApiResultCallback<UserDto?>, registerData: JsonObject)

    suspend fun verifyPhoneNumber(resultCallback: ApiResultCallback<JsonObject?>, verifyCode: JsonObject)

    suspend fun resetVerifyCode(resultCallback: ApiResultCallback<JsonObject?>, resetVerifyCode: JsonObject)

    suspend fun getAllChargers(resultCallback: ApiResultCallback<ChargerDto?>, getAllChargersRequest: JsonObject)

    suspend fun startTransaction(resultCallback: ApiResultCallback<JsonObject?>, startTansactionRequest: JsonObject)

    suspend fun resetPassword(resultCallback: ApiResultCallback<JsonObject?>, resetPasswordRequest: JsonObject)

    suspend fun sendPasswordResetCode(resultCallback: ApiResultCallback<JsonObject?>, senPasswordResetCodeRequest: JsonObject)

    suspend fun checkEmail(resultCallback: ApiResultCallback<String?>, email: String)

    suspend fun checkPhoneNumber(resultCallback: ApiResultCallback<String?>, phoneNumber: String)

    suspend fun checkUsername(resultCallback: ApiResultCallback<String?>, username: String)
}

class BaseEnergyRepositoryImpl(private val dataSource: EnergyDataSource) : BaseEnergyRepository {

    override suspend fun getMasterData(resultCallback: ApiResultCallback<MasterDto?>) {
        coroutineResponseWithContext(resultCallBack = resultCallback) {
            dataSource.getMasterData()
        }
    }

    override suspend fun login(resultCallback: ApiResultCallback<LoginDto?>, loginData: JsonObject) {
        coroutineResponseWithContext(resultCallBack = resultCallback) {
            dataSource.login(loginRequest = loginData)
        }
    }

    override suspend fun logout(resultCallback: ApiResultCallback<JsonObject?>) {
        coroutineResponseWithContext(resultCallBack = resultCallback) {
            dataSource.logout()
        }
    }

    override suspend fun register(resultCallback: ApiResultCallback<UserDto?>, registerData: JsonObject) {
        coroutineResponseWithContext(resultCallBack = resultCallback) {
            dataSource.register(registerData)
        }
    }

    override suspend fun verifyPhoneNumber(resultCallback: ApiResultCallback<JsonObject?>, verifyCode: JsonObject) {
        coroutineResponseWithContext(resultCallBack = resultCallback) {
            dataSource.verifyPhoneNumber(verifyCode)
        }
    }

    override suspend fun resetVerifyCode(resultCallback: ApiResultCallback<JsonObject?>, resetVerifyCode: JsonObject) {
        coroutineResponseWithContext(resultCallBack = resultCallback) {
            dataSource.resetVerifyCode(resetCodeRequest = resetVerifyCode)
        }
    }

    override suspend fun getAllChargers(resultCallback: ApiResultCallback<ChargerDto?>, getAllChargersRequest: JsonObject) {
        coroutineResponseWithContext(resultCallBack = resultCallback) {
            dataSource.getAllChargers(getAllChargersRequest)
        }
    }

    override suspend fun startTransaction(resultCallback: ApiResultCallback<JsonObject?>, startTansactionRequest: JsonObject) {
        coroutineResponseWithContext(resultCallBack = resultCallback) {
            dataSource.startTransaction(startTansactionRequest)
        }
    }

    override suspend fun resetPassword(resultCallback: ApiResultCallback<JsonObject?>, resetPasswordRequest: JsonObject) {
        coroutineResponseWithContext(resultCallBack = resultCallback) {
            dataSource.resetPassword(resetPasswordRequest)
        }
    }

    override suspend fun sendPasswordResetCode(resultCallback: ApiResultCallback<JsonObject?>, senPasswordResetCodeRequest: JsonObject) {
        coroutineResponseWithContext(resultCallBack = resultCallback) {
            dataSource.sendPasswordResetCode(senPasswordResetCodeRequest)
        }
    }

    override suspend fun checkEmail(resultCallback: ApiResultCallback<String?>, email: String) {
        coroutineResponseWithContext(resultCallBack = resultCallback) {
            dataSource.checkEmail(email = email)
        }
    }

    override suspend fun checkPhoneNumber(resultCallback: ApiResultCallback<String?>, phoneNumber: String) {
        coroutineResponseWithContext(resultCallBack = resultCallback) {
            dataSource.checkPhone(phone = phoneNumber)
        }
    }

    override suspend fun checkUsername(resultCallback: ApiResultCallback<String?>, username: String) {
        coroutineResponseWithContext(resultCallBack = resultCallback) {
            dataSource.checkUsername(username = username)
        }
    }

}