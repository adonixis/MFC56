package ru.adonixis.mfc56.network;


import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Query;
import ru.adonixis.mfc56.model.StatusResponse;
import rx.Observable;

public interface MfcStatusService {

    @Headers("Content-Type: application/json")
    @GET("api/v1/smev-client/getAppealState")
    Observable<StatusResponse> checkStatus(@Query("dealNumber") int dealNumber, @Query("pinCode") int pinCode);

}