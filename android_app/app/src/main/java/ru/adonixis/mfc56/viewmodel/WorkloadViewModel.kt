package ru.adonixis.mfc56.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.MutableLiveData
import ru.adonixis.mfc56.model.CountVisitorsResponse
import ru.adonixis.mfc56.model.OktmoObjectsResponse
import androidx.lifecycle.LiveData
import ru.adonixis.mfc56.network.MfcRecordService
import ru.adonixis.mfc56.network.ServiceFactory
import rx.schedulers.Schedulers
import rx.android.schedulers.AndroidSchedulers
import ru.adonixis.mfc56.viewmodel.WorkloadViewModel
import okhttp3.ResponseBody
import org.json.JSONObject
import org.json.JSONArray
import org.json.JSONException
import retrofit2.HttpException
import ru.adonixis.mfc56.model.CountVisitorsRequest
import rx.Subscriber
import java.io.IOException

class WorkloadViewModel : ViewModel() {
    companion object {
        private const val TAG = "WorkloadViewModel"
    }
    private var countVisitorsLiveData: MutableLiveData<CountVisitorsResponse>? = null
    var oktmoObjectsLiveData: MutableLiveData<OktmoObjectsResponse>? = null
    var errorMessageLiveData: MutableLiveData<String>? = null

    fun getCountVisitorsLiveData(): LiveData<CountVisitorsResponse> {
        countVisitorsLiveData = MutableLiveData()
        return countVisitorsLiveData as MutableLiveData<CountVisitorsResponse>
    }

    fun getOktmoObjectsLiveData(): LiveData<OktmoObjectsResponse> {
        oktmoObjectsLiveData = MutableLiveData()
        return oktmoObjectsLiveData as MutableLiveData<OktmoObjectsResponse>
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

    fun getCountVisitors(unitId: Int) {
        val service = ServiceFactory.getMfcRecordService()
        service!!.getCountVisitors(CountVisitorsRequest(unitId))
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Subscriber<CountVisitorsResponse?>() {
                override fun onCompleted() {}
                override fun onError(e: Throwable) {
                    if (e is HttpException) {
                        if (e.code() == 504) {
                            Log.e(TAG, "Get count visitors failed: $e")
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
                            Log.e(TAG, "Get count visitors failed: $message")
                            errorMessageLiveData!!.setValue(message)
                        } catch (ex: JSONException) {
                            Log.e(TAG, "Get count visitors failed: ", ex)
                            errorMessageLiveData!!.setValue("Unknown error")
                        } catch (ex: IOException) {
                            Log.e(TAG, "Get count visitors failed: ", ex)
                            errorMessageLiveData!!.setValue("Unknown error")
                        }
                    } else if (e is IOException) {
                        Log.e(TAG, "Get count visitors failed: ", e)
                        errorMessageLiveData!!.setValue("Check your internet connection")
                    } else {
                        Log.e(TAG, "Get count visitors failed: ", e)
                        errorMessageLiveData!!.setValue("Unknown error")
                    }
                }

                override fun onNext(t: CountVisitorsResponse?) {
                    countVisitorsLiveData!!.setValue(t)
                }
            })
    }

}