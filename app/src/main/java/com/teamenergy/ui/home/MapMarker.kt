package com.teamenergy.ui.home

data class MapMarker (
    val markerId: Long,
    val latitude: Double,
    val longitude: Double,
    val title: String?,
    var isClicked: Boolean,
    var isChecked: Boolean,
)