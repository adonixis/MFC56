package ru.adonixis.mfc56.model

data class TicketResponse(
    var id: Int,
    var servDay: String,
    var prefix: String,
    var number: Int,
    var fullNumber: String,
    var pin: String
)