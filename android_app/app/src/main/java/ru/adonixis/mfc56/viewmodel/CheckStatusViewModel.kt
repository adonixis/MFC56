package ru.adonixis.mfc56.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import retrofit2.HttpException
import ru.adonixis.mfc56.model.StatusResponse
import ru.adonixis.mfc56.network.ServiceFactory
import rx.Subscriber
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import java.io.IOException

class CheckStatusViewModel : ViewModel() {
    companion object {
        private const val TAG = "CheckStatusViewModel"
    }

    private var statusLiveData: MutableLiveData<StatusResponse>? = null
    var errorMessageLiveData: MutableLiveData<String>? = null

    fun getCheckStatusLiveData(): LiveData<StatusResponse> {
        statusLiveData = MutableLiveData()
        return statusLiveData as MutableLiveData<StatusResponse>
    }

    fun getErrorMessageLiveData(): LiveData<String> {
        errorMessageLiveData = MutableLiveData()
        return errorMessageLiveData as MutableLiveData<String>
    }

    fun checkStatus(dealNumber: Int, pinCode: Int) {
        val service = ServiceFactory.getMfcStatusService()
        service!!.checkStatus(dealNumber, pinCode)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Subscriber<StatusResponse?>() {
                override fun onCompleted() {}
                override fun onError(e: Throwable) {
                    if (e is HttpException) {
                        if (e.code() == 504) {
                            Log.e(TAG, "Check status failed: $e")
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
                            Log.e(TAG, "Check status failed: $message")
                            errorMessageLiveData!!.setValue(message)
                        } catch (ex: JSONException) {
                            Log.e(TAG, "Check status failed: ", ex)
                            errorMessageLiveData!!.setValue("Unknown error")
                        } catch (ex: IOException) {
                            Log.e(TAG, "Check status failed: ", ex)
                            errorMessageLiveData!!.setValue("Unknown error")
                        }
                    } else if (e is IOException) {
                        Log.e(TAG, "Check status failed: ", e)
                        errorMessageLiveData!!.setValue("Check your internet connection")
                    } else {
                        Log.e(TAG, "Check status failed: ", e)
                        errorMessageLiveData!!.setValue("Unknown error")
                    }
                }

                override fun onNext(t: StatusResponse?) {
                    statusLiveData!!.setValue(t)
                }
            })
    }

}