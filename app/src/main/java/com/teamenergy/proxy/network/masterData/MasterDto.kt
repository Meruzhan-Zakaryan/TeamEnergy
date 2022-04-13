package com.teamenergy.proxy.network.masterData

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class MasterDto(
    @SerializedName("CarList")
    val carList: List<CarDto>?,
    @SerializedName("ConnectorTypes")
    val connectorTypes: List<ConnectorTypeDto>?,
    @SerializedName("StatusList")
    val statusList: List<StatusDto>?
) : Serializable

data class CarDto(
    @SerializedName("carVendorId")
    val carVendorId: Int?,
    @SerializedName("models")
    val models: List<CarModelDto>?,
    @SerializedName("name")
    val name: String?
) : Serializable

data class CarModelDto(
    @SerializedName("carModelId")
    val carModelId: Int?,
    @SerializedName("name")
    val name: String?
) : Serializable

data class ConnectorTypeDto(
    @SerializedName("key")
    val key: Int?,
    @SerializedName("value")
    val value: String?
) : Serializable