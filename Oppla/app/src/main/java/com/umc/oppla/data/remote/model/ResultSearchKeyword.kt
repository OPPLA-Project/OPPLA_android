package com.umc.oppla.data.remote.model

data class ResultSearchKeyword(
    var documents: List<Document?>? = null,
    var meta: Meta? = null
)

data class Meta(
    var is_end: Boolean? = null,
    var pageable_count: Int? = null,
    var same_name: SameName? = null,
    var total_count: Int? = null
)

data class Document(
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

data class SameName(
    var keyword: String? = null,
    var region: List<Any?>? = null,
    var selected_region: String? = null
)