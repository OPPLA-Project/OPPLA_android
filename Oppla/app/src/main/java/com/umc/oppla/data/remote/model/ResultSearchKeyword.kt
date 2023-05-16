package com.umc.oppla.data.remote.model

data class ResultSearchKeyword(
    var documents: List<Document?>? = null,
    var meta: Meta? = null
)

data class SameName(
    var keyword: String? = null,
    var region: List<Any?>? = null,
    var selected_region: String? = null
)