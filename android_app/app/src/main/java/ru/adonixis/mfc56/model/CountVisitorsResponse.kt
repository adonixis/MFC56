package ru.adonixis.mfc56.model

data class CountVisitorsResponse(
    val creationDate: String,
    val userName: String,
    val userId: Int,
    val guid: String,
    val beginDate: String,
    val endDate: String,
    val reportData: List<Object>,
    val plainReportData: List<Object>,
    val reportCountVisitorsData: List<Object>
)