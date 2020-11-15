package ru.adonixis.mfc56.model

data class BookingDatesRequest(
    var unit: Id,
    var id: Int,
    var maxCountService: Int = 1
)