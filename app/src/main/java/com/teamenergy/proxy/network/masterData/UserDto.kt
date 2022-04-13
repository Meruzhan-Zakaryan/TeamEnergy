package com.teamenergy.proxy.network.masterData


import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class UserDto(
    @SerializedName("data")
    val data: UserDataDto?,
    @SerializedName("errors")
    val errors: List<Any?>?,
    @SerializedName("messages")
    val messages: List<Any?>?,
    @SerializedName("succeeded")
    val succeeded: Boolean?
) : Serializable

data class UserDataDto(
    @SerializedName("idToken")
    val idToken: String?,
    @SerializedName("mobile")
    val mobile: String?,
    @SerializedName("userId")
    val userId: String?,
    @SerializedName("userName")
    val userName: String?
) : Serializable