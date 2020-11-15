package ru.adonixis.mfc56.model

data class RegisterTicketRequest(
    var unit: Id,
    var service: Id,
    var fio: String,
    var snils: String,
    var mobilePhone: String,
    var reserveTime: String,
    var countService: Int = 1,
    var ip: String = "192.168.0.1",
    var talonType: TalonType = TalonType("immediate"),
    var source: String = "androidApp"
)

data class TalonType(
    var name: String
)