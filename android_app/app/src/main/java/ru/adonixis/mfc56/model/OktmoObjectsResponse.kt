package ru.adonixis.mfc56.model

import com.google.gson.annotations.SerializedName

data class OktmoObjectsResponse(@SerializedName("_embedded") var embedded: Embedded)

data class Embedded(var oktmoObjects: List<OktmoObject>)

data class OktmoObject(
    var id: Int? = null,
    var name: String? = null,
    var kladrCode: Int? = null,
    var description: String? = null,
    var oktmoCode: String? = null
)