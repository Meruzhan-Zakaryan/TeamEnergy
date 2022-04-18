package com.teamenergy.proxy.mapper

import com.teamenergy.proxy.base.Mapper
import com.teamenergy.proxy.domain.*
import com.teamenergy.proxy.network.masterData.ChargerDto
import com.teamenergy.proxy.network.masterData.MasterDto

object MasterMapper {
    val masterMapper: Mapper<MasterDto, Master> = { response ->
        Master(
            response.carList?.map {
                Car(
                    carVendorId = it.carVendorId,
                    it.models?.map { carModelData ->
                        CarModel(
                            carModelId = carModelData.carModelId,
                            name = carModelData.name

                        )
                    },
                    name = it.name
                )
            },
            response.connectorTypes?.map {
                ConnectorType(
                    key = it.key,
                    value = it.value,
                    isChecked = false
                )
            },
            response.statusList?.map {
                Status(
                    key = it.key,
                    value = it.value,
                    isChecked = false
                )
            }
        )
    }
}