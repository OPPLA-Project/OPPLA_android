package com.umc.oppla.data.model

data class Document(
    //
    val address: Address? = null,
    val road_address: RoadAddress? = null,
    //
    var address_name: String? = null,
    var category_group_code: String? = null,
    var category_group_name: String? = null,
    var category_name: String? = null,
    var distance: String? = null,
    var id: String? = null,
    var phone: String? = null,
    var place_name: String? = null,
    var place_url: String? = null,
    var road_address_name: String? = null,
    var x: String? = null,
    var y: String? = null
)

data class Meta(
    //
    val total_count: Int? = null,
    //
    var is_end: Boolean? = null,
    var pageable_count: Int? = null,
    var same_name: SameName? = null,
)

//data class Meta(
//    var is_end: Boolean? = null,
//    var pageable_count: Int? = null,
//    var same_name: SameName? = null,
//    var total_count: Int? = null
//)

//data class Document(
//    var address_name: String? = null,
//    var category_group_code: String? = null,
//    var category_group_name: String? = null,
//    var category_name: String? = null,
//    var distance: String? = null,
//    var id: String? = null,
//    var phone: String? = null,
//    var place_name: String? = null,
//    var place_url: String? = null,
//    var road_address_name: String? = null,
//    var x: String? = null,
//    var y: String? = null
//)
