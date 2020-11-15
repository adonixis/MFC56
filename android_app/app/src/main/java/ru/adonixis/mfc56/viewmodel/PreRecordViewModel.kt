package ru.adonixis.mfc56.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.LiveData
import okhttp3.ResponseBody
import ru.adonixis.mfc56.network.ServiceFactory
import rx.schedulers.Schedulers
import rx.android.schedulers.AndroidSchedulers
import org.json.JSONObject
import org.json.JSONArray
import org.json.JSONException
import retrofit2.HttpException
import retrofit2.Response
import ru.adonixis.mfc56.model.*
import rx.Subscriber
import java.io.IOException

class PreRecordViewModel : ViewModel() {
    companion object {
        private const val TAG = "PreRecordViewModel"
    }
    var oktmoObjectsLiveData: MutableLiveData<OktmoObjectsResponse>? = null
    var unitsLiveData: MutableLiveData<UnitsResponse>? = null
    var serviceLiveData: MutableLiveData<List<ServiceResponse>>? = null
    var bookingDatesLiveData: MutableLiveData<BookingDatesResponse>? = null
    var ticketLiveData: MutableLiveData<TicketResponse>? = null
    var errorMessageLiveData: MutableLiveData<String>? = null

    fun getOktmoObjectsLiveData(): LiveData<OktmoObjectsResponse> {
        oktmoObjectsLiveData = MutableLiveData()
        return oktmoObjectsLiveData as MutableLiveData<OktmoObjectsResponse>
    }

    fun getUnitsLiveData(): LiveData<UnitsResponse> {
        unitsLiveData = MutableLiveData()
        return unitsLiveData as MutableLiveData<UnitsResponse>
    }

    fun getServicesLiveData(): LiveData<List<ServiceResponse>> {
        serviceLiveData = MutableLiveData()
        return serviceLiveData as MutableLiveData<List<ServiceResponse>>
    }

    fun getBookingDatesLiveData(): LiveData<BookingDatesResponse> {
        bookingDatesLiveData = MutableLiveData()
        return bookingDatesLiveData as MutableLiveData<BookingDatesResponse>
    }

    fun getTicketLiveData(): LiveData<TicketResponse> {
        ticketLiveData = MutableLiveData()
        return ticketLiveData as MutableLiveData<TicketResponse>
    }

    fun getErrorMessageLiveData(): LiveData<String> {
        errorMessageLiveData = MutableLiveData()
        return errorMessageLiveData as MutableLiveData<String>
    }

    fun getOktmoObjects() {
        val service = ServiceFactory.getMfcRecordService()
        service!!.getOktmoObjects()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Subscriber<OktmoObjectsResponse?>() {
                override fun onCompleted() {}
                override fun onError(e: Throwable) {
                    if (e is HttpException) {
                        if (e.code() == 504) {
                            Log.e(TAG, "Get OKTMO objects failed: $e")
                            errorMessageLiveData!!.setValue("Check your internet connection")
                            return
                        }
                        val body = e.response()!!.errorBody()
                        try {
                            val jObjError = JSONObject(body!!.string())
                            var message = ""
                            if (jObjError.has("message")) {
                                message = jObjError["message"] as String
                            } else if (jObjError.has("errors")) {
                                val errors = jObjError["errors"] as JSONArray
                                val error = errors[0] as JSONObject
                                val messages = error["messages"] as JSONArray
                                message = messages[0] as String
                            }
                            Log.e(TAG, "Get OKTMO objects failed: $message")
                            errorMessageLiveData!!.setValue(message)
                        } catch (ex: JSONException) {
                            Log.e(TAG, "Get OKTMO objects failed: ", ex)
                            errorMessageLiveData!!.setValue("Unknown error")
                        } catch (ex: IOException) {
                            Log.e(TAG, "Get OKTMO objects failed: ", ex)
                            errorMessageLiveData!!.setValue("Unknown error")
                        }
                    } else if (e is IOException) {
                        Log.e(TAG, "Get OKTMO objects failed: ", e)
                        errorMessageLiveData!!.setValue("Check your internet connection")
                    } else {
                        Log.e(TAG, "Get OKTMO objects failed: ", e)
                        errorMessageLiveData!!.setValue("Unknown error")
                    }
                }

                override fun onNext(t: OktmoObjectsResponse?) {
                    oktmoObjectsLiveData!!.setValue(t)
                }
            })
    }

    fun getUnits(oktmoID: Int) {
        val service = ServiceFactory.getMfcRecordService()
        service!!.getUnits(oktmoID)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Subscriber<UnitsResponse?>() {
                override fun onCompleted() {}
                override fun onError(e: Throwable) {
                    if (e is HttpException) {
                        if (e.code() == 504) {
                            Log.e(TAG, "Get units failed: $e")
                            errorMessageLiveData!!.setValue("Check your internet connection")
                            return
                        }
                        val body = e.response()!!.errorBody()
                        try {
                            val jObjError = JSONObject(body!!.string())
                            var message = ""
                            if (jObjError.has("message")) {
                                message = jObjError["message"] as String
                            } else if (jObjError.has("errors")) {
                                val errors = jObjError["errors"] as JSONArray
                                val error = errors[0] as JSONObject
                                val messages = error["messages"] as JSONArray
                                message = messages[0] as String
                            }
                            Log.e(TAG, "Get units failed: $message")
                            errorMessageLiveData!!.setValue(message)
                        } catch (ex: JSONException) {
                            Log.e(TAG, "Get units failed: ", ex)
                            errorMessageLiveData!!.setValue("Unknown error")
                        } catch (ex: IOException) {
                            Log.e(TAG, "Get units failed: ", ex)
                            errorMessageLiveData!!.setValue("Unknown error")
                        }
                    } else if (e is IOException) {
                        Log.e(TAG, "Get units failed: ", e)
                        errorMessageLiveData!!.setValue("Check your internet connection")
                    } else {
                        Log.e(TAG, "Get units failed: ", e)
                        errorMessageLiveData!!.setValue("Unknown error")
                    }
                }

                override fun onNext(t: UnitsResponse?) {
                    unitsLiveData!!.setValue(t)
                }
            })
    }

    fun getServices(unitID: Int) {
        val service = ServiceFactory.getMfcRecordService()
        service!!.getServices(unitID)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Subscriber<List<ServiceResponse>>() {
                override fun onCompleted() {}
                override fun onError(e: Throwable) {
                    if (e is HttpException) {
                        if (e.code() == 504) {
                            Log.e(TAG, "Get services failed: $e")
                            errorMessageLiveData!!.setValue("Check your internet connection")
                            return
                        }
                        val body = e.response()!!.errorBody()
                        try {
                            val jObjError = JSONObject(body!!.string())
                            var message = ""
                            if (jObjError.has("message")) {
                                message = jObjError["message"] as String
                            } else if (jObjError.has("errors")) {
                                val errors = jObjError["errors"] as JSONArray
                                val error = errors[0] as JSONObject
                                val messages = error["messages"] as JSONArray
                                message = messages[0] as String
                            }
                            Log.e(TAG, "Get services failed: $message")
                            errorMessageLiveData!!.setValue(message)
                        } catch (ex: JSONException) {
                            Log.e(TAG, "Get services failed: ", ex)
                            errorMessageLiveData!!.setValue("Unknown error")
                        } catch (ex: IOException) {
                            Log.e(TAG, "Get services failed: ", ex)
                            errorMessageLiveData!!.setValue("Unknown error")
                        }
                    } else if (e is IOException) {
                        Log.e(TAG, "Get services failed: ", e)
                        errorMessageLiveData!!.setValue("Check your internet connection")
                    } else {
                        Log.e(TAG, "Get services failed: ", e)
                        errorMessageLiveData!!.setValue("Unknown error")
                    }
                }

                override fun onNext(t: List<ServiceResponse>?) {
                    serviceLiveData!!.setValue(t)
                }
            })
    }

    fun getBookingDates(unitId: Int, serviceId: Int) {
        val service = ServiceFactory.getMfcRecordService()
        service!!.getBookingDates(BookingDatesRequest(Id(unitId), serviceId))
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Subscriber<BookingDatesResponse?>() {
                override fun onCompleted() {}
                override fun onError(e: Throwable) {
                    if (e is HttpException) {
                        if (e.code() == 504) {
                            Log.e(TAG, "Get booking dates failed: $e")
                            errorMessageLiveData!!.setValue("Check your internet connection")
                            return
                        }
                        val body = e.response()!!.errorBody()
                        try {
                            val jObjError = JSONObject(body!!.string())
                            var message = ""
                            if (jObjError.has("message")) {
                                message = jObjError["message"] as String
                            } else if (jObjError.has("errors")) {
                                val errors = jObjError["errors"] as JSONArray
                                val error = errors[0] as JSONObject
                                val messages = error["messages"] as JSONArray
                                message = messages[0] as String
                            }
                            Log.e(TAG, "Get booking dates failed: $message")
                            errorMessageLiveData!!.setValue(message)
                        } catch (ex: JSONException) {
                            Log.e(TAG, "Get booking dates failed: ", ex)
                            errorMessageLiveData!!.setValue("Unknown error")
                        } catch (ex: IOException) {
                            Log.e(TAG, "Get booking dates failed: ", ex)
                            errorMessageLiveData!!.setValue("Unknown error")
                        }
                    } else if (e is IOException) {
                        Log.e(TAG, "Get booking dates failed: ", e)
                        errorMessageLiveData!!.setValue("Check your internet connection")
                    } else {
                        Log.e(TAG, "Get booking dates failed: ", e)
                        errorMessageLiveData!!.setValue("Unknown error")
                    }
                }

                override fun onNext(t: BookingDatesResponse?) {
                    bookingDatesLiveData!!.setValue(t)
                }
            })
    }

    fun registerTicket(unitId: Int, serviceId: Int, fio: String, snils: String, phone: String, reserveTime: String) {
        val service = ServiceFactory.getMfcRecordService()
        service!!.registerTicket(RegisterTicketRequest(Id(unitId), Id(serviceId), fio, snils, phone, reserveTime))
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Subscriber<TicketResponse?>() {
                override fun onCompleted() {}
                override fun onError(e: Throwable) {
                    if (e is HttpException) {
                        if (e.code() == 504) {
                            Log.e(TAG, "Register ticket failed: $e")
                            errorMessageLiveData!!.setValue("Check your internet connection")
                            return
                        }
                        val body = e.response()!!.errorBody()
                        try {
                            val jObjError = JSONObject(body!!.string())
                            var message = ""
                            if (jObjError.has("message")) {
                                message = jObjError["message"] as String
                            } else if (jObjError.has("errors")) {
                                val errors = jObjError["errors"] as JSONArray
                                val error = errors[0] as JSONObject
                                val messages = error["messages"] as JSONArray
                                message = messages[0] as String
                            }
                            Log.e(TAG, "Register ticket failed: $message")
                            errorMessageLiveData!!.setValue(message)
                        } catch (ex: JSONException) {
                            Log.e(TAG, "Register ticket failed: ", ex)
                            errorMessageLiveData!!.setValue("Unknown error")
                        } catch (ex: IOException) {
                            Log.e(TAG, "Register ticket failed: ", ex)
                            errorMessageLiveData!!.setValue("Unknown error")
                        }
                    } else if (e is IOException) {
                        Log.e(TAG, "Register ticket failed: ", e)
                        errorMessageLiveData!!.setValue("Check your internet connection")
                    } else {
                        Log.e(TAG, "Register ticket failed: ", e)
                        errorMessageLiveData!!.setValue("Unknown error")
                    }
                }

                override fun onNext(t: TicketResponse?) {
                    ticketLiveData!!.setValue(t)
                }
            })
    }
}