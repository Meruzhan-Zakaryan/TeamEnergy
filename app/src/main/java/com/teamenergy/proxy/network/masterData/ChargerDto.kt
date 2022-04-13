package com.teamenergy.proxy.network.masterData


import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class ChargerDto(
    @SerializedName("data")
    val data: List<ChargerItemDto>?,
    @SerializedName("errors")
    val errors: List<Any?>?,
    @SerializedName("messages")
    val messages: List<Any?>?,
    @SerializedName("succeeded")
    val succeeded: Boolean?
):Serializable

data class ChargerItemDto(
    @SerializedName("address")
    val address: String?,
    @SerializedName("chargePointId")
    val chargePointId: String?,
    @SerializedName("connectors")
    val connectors: List<Connector>?,
    @SerializedName("key")
    val key: String?,
    @SerializedName("latitude")
    val latitude: Double?,
    @SerializedName("longitude")
    val longitude: Double?,
    @SerializedName("name")
    val name: String?,
    @SerializedName("phone")
    val phone: String?,
    @SerializedName("status")
    val status: String?
):Serializable

data class Connector(
    @SerializedName("connectorId")
    val connectorId: String?,
    @SerializedName("connectorType")
    val connectorType: String?,
    @SerializedName("connectorTypeId")
    val connectorTypeId: Int?,
    @SerializedName("key")
    val key: String?,
    @SerializedName("power")
    val power: Int?,
    @SerializedName("price")
    val price: Int?,
    @SerializedName("status")
    val status: String?
):Serializable

