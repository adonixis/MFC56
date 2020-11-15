package ru.adonixis.mfc56.model

import com.google.gson.annotations.SerializedName

data class UnitsResponse(@SerializedName("_embedded") var embeddedUnits: EmbeddedUnits)

data class EmbeddedUnits(@SerializedName("units") var oktmoUnits: List<OktmoUnit>)

data class OktmoUnit(
    var id: Int? = null,
    var description: String? = null,
    var main: Boolean? = null,
    var terminalFooter: String? = null,
    var textScrolling: String? = null,
    var code: String? = null,
    var name: String? = null,
    var shortName: String? = null,
    var tabloHeader: String? = null,
    var legalAddress: String? = null,
    var legalAddressDescription: String? = null,
    var phone: String? = null,
    var email: String? = null,
    var workingHours: String? = null,
    var sperId: Int? = null,
    var mkguId: String? = null,
    var mkguOkato: String? = null
)