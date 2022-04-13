package com.teamenergy.teamenergy.net

import com.google.gson.JsonObject
import com.teamenergy.proxy.network.masterData.*
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface EnergyDataSource {

    @GET("masterData/getMasterData")
    suspend fun getMasterData(): Response<MasterDto?>

    @GET("masterData/getMasterData")
    suspend fun getCarList(@Query("CarList") car:String = "CarList"):Response<CarDto?>

    @POST("usermanagement/login")
    suspend fun login(@Body loginRequest:JsonObject): Response<LoginDto?>

    @POST("usermanagement/register")
    suspend fun register(@Body registerRequest:JsonObject): Response<UserDto?>

    @POST("usermanagement/VerifyPhoneNumber")
    suspend fun verifyPhoneNumber(@Body verifyPhoneNumber:JsonObject): Response<JsonObject?>

    @POST("usermanagement/SendPasswordResetCode")
    suspend fun resetVerifyCode(@Body resetCodeRequest:JsonObject): Response<JsonObject?>

    @POST("ChargePoint/search")
    suspend fun getAllChargers(@Body getAllChargersRequest:JsonObject):Response<ChargerDto?>

    @POST("ChargePoint/StartTransaction")
    suspend fun startTransaction(@Body startTransactionRequest:JsonObject):Response<JsonObject>

}