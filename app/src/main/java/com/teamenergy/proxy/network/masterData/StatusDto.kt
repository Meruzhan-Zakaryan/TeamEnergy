package com.teamenergy.proxy.network.masterData


import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class StatusDto(
    @SerializedName("key")
    val key: Int?,
    @SerializedName("value")
    val value: String?
):Serializable