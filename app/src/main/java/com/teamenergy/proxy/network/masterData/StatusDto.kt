package com.teamenergy.proxy.network.masterData


import com.google.gson.annotations.SerializedName

data class StatusDto(
    @SerializedName("key")
    val key: Int?,
    @SerializedName("value")
    val value: String?
)