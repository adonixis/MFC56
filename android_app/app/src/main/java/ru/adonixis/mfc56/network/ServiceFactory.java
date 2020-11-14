package ru.adonixis.mfc56.network;

import java.util.concurrent.TimeUnit;

import okhttp3.Authenticator;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class ServiceFactory {

    private static final String MFC_RECORD_BASE_URL = "http://193.169.35.211";
    private static final String MFC_STATUS_BASE_URL = "http://193.169.35.211:8080";

    private static MfcRecordService mfcRecordService;
    private static MfcStatusService mfcStatusService;

    public static MfcRecordService getMfcRecordService() {
        if (mfcRecordService == null) {
            OkHttpClient client = new OkHttpClient.Builder()
                    .addInterceptor(new BasicAuthInterceptor("eq", "eq"))
                    .connectTimeout(20, TimeUnit.SECONDS)
                    .writeTimeout(20, TimeUnit.SECONDS)
                    .readTimeout(20, TimeUnit.SECONDS)
                    .build();

            mfcRecordService = createRetrofitService(MfcRecordService.class, MFC_RECORD_BASE_URL, client);
        }
        return mfcRecordService;
    }

    public static MfcStatusService getMfcStatusService() {
        if (mfcStatusService == null) {
            OkHttpClient client = new OkHttpClient.Builder()
                    .addInterceptor(new BasicAuthInterceptor("mnhtn", ""))
                    .connectTimeout(20, TimeUnit.SECONDS)
                    .writeTimeout(20, TimeUnit.SECONDS)
                    .readTimeout(20, TimeUnit.SECONDS)
                    .build();

            mfcStatusService = createRetrofitService(MfcStatusService.class, MFC_STATUS_BASE_URL, client);
        }
        return mfcStatusService;
    }

    private static <T> T createRetrofitService(final Class<T> clazz, final String baseUrl, final OkHttpClient client) {
        final Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(client)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        T service = retrofit.create(clazz);

        return service;
    }
}