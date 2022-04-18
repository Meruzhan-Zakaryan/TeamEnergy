package com.teamenergy.proxy.mapper

import com.teamenergy.proxy.base.Mapper
import com.teamenergy.proxy.domain.Charger
import com.teamenergy.proxy.domain.ChargerItem
import com.teamenergy.proxy.domain.Connector
import com.teamenergy.proxy.network.masterData.ChargerDto
import com.yandex.mapkit.geometry.Point

object ChargerMapper {
    val chargerMapper:Mapper<ChargerDto,Charger> = {response->
       Charger(
           response.data?.map {
               ChargerItem(
                   address = it.address,
                   chargePointId = it.chargePointId,
                   it.connectors?.map {connectorResponse->
                       Connector(
                           connectorId = connectorResponse.connectorId,
                           connectorType = connectorResponse.connectorType,
                           connectorTypeId = connectorResponse.connectorTypeId,
                           key = connectorResponse.key,
                           power = connectorResponse.power,
                           price = connectorResponse.price,
                           status = connectorResponse.status
                       )
                   },
                   key = it.key,
                   latitude = it.latitude,
                   longitude = it.longitude,
                   name = it.name,
                   phone = it.phone,
                   status = it.status,
                   it.latitude?.let { it1 -> it.longitude?.let { it2 -> Point(it1, it2) } }
               )
           },
           response.errors?.map {
               Any()
           },
           response.messages?.map {
               Any()
           },
           succeeded = response.succeeded
       )
    }
}