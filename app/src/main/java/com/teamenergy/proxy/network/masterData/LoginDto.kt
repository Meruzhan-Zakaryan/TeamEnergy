package com.teamenergy.proxy.network.masterData


import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class LoginDto(
    @SerializedName("data")
    val data: Data?,
    @SerializedName("errors")
    val errors: List<Error?>?,
    @SerializedName("messages")
    val messages: List<String?>?,
    @SerializedName("succeeded")
    val succeeded: Boolean?
) : Serializable

data class Data(
    @SerializedName("idToken")
    val idToken: String?,
    @SerializedName("mobile")
    val mobile: String?,
    @SerializedName("userId")
    val userId: String?,
    @SerializedName("userName")
    val userName: String?
) : Serializable

data class Error(
    @SerializedName("code")
    val code: String?,
    @SerializedName("description")
    val description: String?
) : Serializable