package com.umc.oppla.data.remote.model

data class ResultSearchLatLng(
    val documents: List<Document?>? = null,
    val meta: Meta? = null
)

data class RoadAddress(
    val address_name: String? = null,
    val building_name: String? = null,
    val main_building_no: String? = null,
    val region_1depth_name: String? = null,
    val region_2depth_name: String? = null,
    val region_3depth_name: String? = null,
    val road_name: String? = null,
    val sub_building_no: String? = null,
    val underground_yn: String? = null,
    val zone_no: String? = null
)

data class Address(
    val address_name: String? = null,
    val main_address_no: String? = null,
    val mountain_yn: String? = null,
    val region_1depth_name: String? = null,
    val region_2depth_name: String? = null,
    val region_3depth_name: String? = null,
    val sub_address_no: String? = null
)