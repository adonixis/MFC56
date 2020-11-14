package ru.adonixis.mfc56.viewmodel;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.HttpException;
import ru.adonixis.mfc56.model.StatusResponse;
import ru.adonixis.mfc56.network.MfcStatusService;
import ru.adonixis.mfc56.network.ServiceFactory;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class CheckStatusViewModel extends ViewModel {

    private static final String TAG = "CheckStatusViewModel";
    private MutableLiveData<StatusResponse> statusLiveData;
    private MutableLiveData<String> errorMessageLiveData;

    public LiveData<StatusResponse> getCheckStatusLiveData() {
        statusLiveData = new MutableLiveData<>();
        return statusLiveData;
    }

    public LiveData<String> getErrorMessageLiveData() {
        errorMessageLiveData = new MutableLiveData<>();
        return errorMessageLiveData;
    }

    public void checkStatus(int dealNumber, int pinCode) {
        MfcStatusService service = ServiceFactory.getMfcStatusService();
        service.checkStatus(dealNumber, pinCode)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<StatusResponse>() {
                    @Override
                    public final void onCompleted() {
                    }

                    @Override
                    public final void onError(Throwable e) {
                        if (e instanceof HttpException) {
                            if ((((HttpException) e).code() == 504)) {
                                Log.e(TAG, "Check status failed: " + e);
                                errorMessageLiveData.setValue("Check your internet connection");
                                return;
                            }
                            ResponseBody body = ((HttpException) e).response().errorBody();
                            try {
                                JSONObject jObjError = new JSONObject (body.string());
                                String message = "";
                                if (jObjError.has("message")) {
                                    message = (String) jObjError.get("message");
                                } else if (jObjError.has("errors")) {
                                    JSONArray errors = (JSONArray) jObjError.get("errors");
                                    JSONObject error = (JSONObject) errors.get(0);
                                    JSONArray messages = (JSONArray) error.get("messages");
                                    message = (String) messages.get(0);
                                }
                                Log.e(TAG, "Check status failed: " + message);
                                errorMessageLiveData.setValue(message);
                            } catch (JSONException | IOException ex) {
                                Log.e(TAG, "Check status failed: ", ex);
                                errorMessageLiveData.setValue("Unknown error");
                            }
                        } else if (e instanceof IOException) {
                            Log.e(TAG, "Check status failed: ", e);
                            errorMessageLiveData.setValue("Check your internet connection");
                        } else {
                            Log.e(TAG, "Check status failed: ", e);
                            errorMessageLiveData.setValue("Unknown error");
                        }
                    }

                    @Override
                    public void onNext(StatusResponse statusResponse) {
                        statusLiveData.setValue(statusResponse);
                    }
                });
    }
}