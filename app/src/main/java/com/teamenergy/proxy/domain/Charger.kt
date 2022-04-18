package com.teamenergy.proxy.domain

import com.google.gson.annotations.SerializedName
import com.yandex.mapkit.geometry.Point
import java.io.Serializable

data class Charger(
    val data: List<ChargerItem>? = null,
    val errors: List<Any?>? = null,
    val messages: List<Any?>? = null,
    val succeeded: Boolean? = null
) : Serializable

data class ChargerItem(
    val address: String? = null,
    val chargePointId: String? = null,
    val connectors: List<Connector>? = null,
    val key: String? = null,
    val latitude: Double? = null,
    val longitude: Double? = null,
    val name: String? = null,
    val phone: String? = null,
    val status: String? = null,
    val point: Point? = null
) : Serializable

data class Connector(
    val connectorId: String? = null,
    val connectorType: String? = null,
    val connectorTypeId: Int? = null,
    val key: String? = null,
    val power: Int? = null,
    val price: Int? = null,
    val status: String? = null
) : Serializable