package ru.adonixis.mfc56.network

import retrofit2.http.*
import ru.adonixis.mfc56.model.*
import rx.Observable

interface MfcRecordService {

    @Headers("Content-Type: application/json")
    @GET("api/v1/oktmoObjects.json?size=1000")
    fun getOktmoObjects(): Observable<OktmoObjectsResponse>

    @Headers("Content-Type: application/json")
    @GET("api/v1/oktmoObjects.json/{oktmoID}/units/?size=1000")
    fun getUnits(@Path("oktmoID") oktmoID: Int): Observable<UnitsResponse>

    @Headers("Content-Type: application/json")
    @GET("api/v1/dto/services/getServices?preRecord=true")
    fun getServices(@Query("unitId") unitId: Int): Observable<List<ServiceResponse>>

    @Headers("Content-Type: application/json")
    @POST("api/v1/getBookingDatesWithSlots.json")
    fun getBookingDates(@Body bookingDatesRequest: BookingDatesRequest): Observable<BookingDatesResponse>

    @Headers("Content-Type: application/json")
    @POST("api/v1/book")
    fun registerTicket(@Body registerTicketRequest: RegisterTicketRequest): Observable<TicketResponse>

    @Headers("Content-Type: application/json")
    @POST("api/v1/buildCountVisitorsInQueueReport")
    fun getCountVisitors(@Body countVisitorsRequest: CountVisitorsRequest): Observable<CountVisitorsResponse>

}