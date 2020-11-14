package ru.adonixis.mfc56.network

import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import ru.adonixis.mfc56.model.CountVisitorsRequest
import ru.adonixis.mfc56.model.CountVisitorsResponse
import ru.adonixis.mfc56.model.OktmoObjectsResponse
import rx.Observable

interface MfcRecordService {

    @Headers("Content-Type: application/json")
    @GET("api/v1/oktmoObjects.json?size=1000")
    fun getOktmoObjects(): Observable<OktmoObjectsResponse>

    @Headers("Content-Type: application/json")
    @POST("api/v1/buildCountVisitorsInQueueReport")
    fun getCountVisitors(@Body countVisitorsRequest: CountVisitorsRequest): Observable<CountVisitorsResponse>

}