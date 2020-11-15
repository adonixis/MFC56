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
    val reportCountVisitorsData: List<ReportCountVisitorsData>
)

data class ReportCountVisitorsData(
    val serviceName: String,
    val serviceId: Int,
    val applicantsCount: Int,
    val applicantsInTimeCount: String? = null,
    val applicantsOutOfTimeCount: String? = null,
    val averageTimeInQueue: String? = null
)