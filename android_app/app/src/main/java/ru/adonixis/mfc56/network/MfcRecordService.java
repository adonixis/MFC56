package ru.adonixis.mfc56.network;


import java.util.List;

import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

public interface MfcRecordService {

/*    @Headers("apiKey: " + BuildConfig.API_KEY)
    @POST("users/login")
    Observable<UserResponse> login(@Body LoginRequest loginRequest);

    @Headers("apiKey: " + BuildConfig.API_KEY)
    @POST("users/loginFacebook")
    Observable<UserResponse> loginFacebook(@Body FacebookLoginRequest facebookLoginRequest);

    @Headers("apiKey: " + BuildConfig.API_KEY)
    @POST("users/signup")
    Observable<UserResponse> signUp(@Body LoginRequest loginRequest);

    @Headers("apiKey: " + BuildConfig.API_KEY)
    @POST("users/requestResetPassword")
    Observable<OkResponse> requestResetPassword(@Body RequestResetPasswordRequest requestResetPasswordRequest);

    @Headers("apiKey: " + BuildConfig.API_KEY)
    @POST("users/resetPassword")
    Observable<OkResponse> resetPassword(@Body ResetPasswordRequest resetPasswordRequest);

    @Headers("apiKey: " + BuildConfig.API_KEY)
    @POST("users/togglePushNotifications")
    Observable<OkResponse> togglePushNotifications(@Header("token") String token, @Body TogglePushRequest togglePushRequest);

    @Headers("apiKey: " + BuildConfig.API_KEY)
    @GET("users/dashboardv2")
    Observable<DashboardResponse> getDashboard(@Header("token") String token);

    @Headers("apiKey: " + BuildConfig.API_KEY)
    @GET("users/profile")
    Observable<ProfileResponse> getProfile(@Header("token") String token);

    @Headers("apiKey: " + BuildConfig.API_KEY)
    @POST("users/update")
    Observable<OkResponse> updateProfile(@Header("token") String token, @Body UpdateProfileRequest updateProfileRequest);

    @Headers("apiKey: " + BuildConfig.API_KEY)
    @POST("fitbit")
    Observable<OkResponse> toggleFitbit(@Header("token") String token, @Body ToggleFitbitRequest toggleFitbitRequest);

    @Headers("apiKey: " + BuildConfig.API_KEY)
    @POST("googlefit")
    Observable<OkResponse> toggleGoogleFit(@Header("token") String token, @Body ToggleGoogleFitRequest toggleGoogleFitRequest);

    @Headers("apiKey: " + BuildConfig.API_KEY)
    @POST("users/logout")
    Observable<OkResponse> logout(@Header("token") String token);

    @Headers("apiKey: " + BuildConfig.API_KEY)
    @POST("firebase")
    Observable<OkResponse> changeFCMToken(@Header("token") String token, @Body SendFCMTokenRequest sendFCMTokenRequest);

    @Headers("apiKey: " + BuildConfig.API_KEY)
    @POST("dialogs/{dialogId}/messages/{messageOrd}")
    Observable<OkResponse> sendMessage(@Header("token") String token, @Path("dialogId") int dialogId, @Path("messageOrd") int messageOrd, @Body MessageControl messageControl);

    @Headers("apiKey: " + BuildConfig.API_KEY)
    @GET("food/db")
    Observable<FoodResponse> searchFood(@Header("token") String token, @Query("name") String foodName, @Query("page") int page);

    @Headers("apiKey: " + BuildConfig.API_KEY)
    @GET("food/log")
    Observable<List<FoodHistoryResponse>> getRecentFood(@Header("token") String token, @Query("dateStart") String dateStart, @Query("dateEnd") String dateEnd);

    @Headers("apiKey: " + BuildConfig.API_KEY)
    @POST("food")
    Observable<List<FoodServingResponse>> addFood(@Header("token") String token, @Body AddFoodRequest addFoodRequest);

    @Headers("apiKey: " + BuildConfig.API_KEY)
    @GET("mood")
    Observable<List<MoodResponse>> getMood(@Header("token") String token, @Query("start_time") long startTime);

    @Headers("apiKey: " + BuildConfig.API_KEY)
    @POST("mood")
    Observable<OkResponse> addMood(@Header("token") String token, @Body AddMoodRequest addMoodRequest);

    @Headers("apiKey: " + BuildConfig.API_KEY)
    @GET("sleep")
    Observable<List<SleepResponse>> getSleep(@Header("token") String token, @Query("dateStart") String dateStart, @Query("dateEnd") String dateEnd);

    @Headers("apiKey: " + BuildConfig.API_KEY)
    @POST("sleep")
    Observable<OkResponse> addSleep(@Header("token") String token, @Body AddSleepRequest addSleepRequest);

    @Headers("apiKey: " + BuildConfig.API_KEY)
    @GET("activity")
    Observable<List<StepsResponse>> getSteps(@Header("token") String token, @Query("dateStart") String dateStart, @Query("dateEnd") String dateEnd);

    @Headers("apiKey: " + BuildConfig.API_KEY)
    @POST("activity")
    Observable<OkResponse> addSteps(@Header("token") String token, @Body AddStepsRequest addStepsRequest);

    @Headers("apiKey: " + BuildConfig.API_KEY)
    @POST("comm/chat_history")
    Observable<ChatMessagesResponse> getChatHistory(@Header("token") String token, @Body GetChatHistoryRequest getChatHistoryRequest);

    @Headers("apiKey: " + BuildConfig.API_KEY)
    @POST("comm/chat")
    Observable<ChatMessageResponse> sendChatMessage(@Header("token") String token, @Body SendChatMessageRequest sendChatMessageRequest);*/

}