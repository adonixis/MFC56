package ru.adonixis.mfc56.model

import com.google.gson.annotations.SerializedName

data class BookingDatesResponse(
    var content: Content
)

data class Content(
    @SerializedName("2020-11-16T00:00:00.000+0500") var dates: List<BookingDate>? = null
)

data class BookingDate(
    var timeFrom: String? = null,
    var timeTo: String? = null,
    var capacity: Int? = null,
    var unitId: Int? = null
)