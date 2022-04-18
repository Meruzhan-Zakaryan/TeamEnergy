package com.teamenergy.proxy.domain

import com.google.gson.annotations.SerializedName
import com.teamenergy.proxy.network.masterData.StatusDto
import java.io.Serializable

data class Master(
    val carList: List<Car>? = null,
    val connectorTypes: List<ConnectorType>? = null,
    val statusList: List<Status>? = null
) : Serializable

data class Car(
    val carVendorId: Int? = null,
    val models: List<CarModel>? = null,
    val name: String? = null
) : Serializable

data class CarModel(
    val carModelId: Int? = null,
    val name: String? = null
) : Serializable

data class ConnectorType(
    val key: Int? = null,
    val value: String? = null,
    var isChecked: Boolean? = null
) : Serializable

data class Status(
    val key: Int? = null,
    val value: String? = null,
    var isChecked: Boolean? = null
) : Serializable