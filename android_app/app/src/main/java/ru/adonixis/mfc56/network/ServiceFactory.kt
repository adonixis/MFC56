package ru.adonixis.mfc56.network

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object ServiceFactory {

    private const val MFC_RECORD_BASE_URL = "http://193.169.35.211"
    private const val MFC_STATUS_BASE_URL = "http://193.169.35.211:8080"

    private var mfcRecordService: MfcRecordService? = null
    private var mfcStatusService: MfcStatusService? = null

    fun getMfcRecordService(): MfcRecordService? {
        if (mfcRecordService == null) {
            val client = OkHttpClient.Builder()
                .addInterceptor(BasicAuthInterceptor("eq", "eq"))
                .connectTimeout(20, TimeUnit.SECONDS)
                .writeTimeout(20, TimeUnit.SECONDS)
                .readTimeout(20, TimeUnit.SECONDS)
                .build()
            mfcRecordService = createRetrofitService(MfcRecordService::class.java, MFC_RECORD_BASE_URL, client
            )
        }
        return mfcRecordService
    }

    fun getMfcStatusService(): MfcStatusService? {
        if (mfcStatusService == null) {
            val client = OkHttpClient.Builder()
                .addInterceptor(BasicAuthInterceptor("mnhtn", ""))
                .connectTimeout(20, TimeUnit.SECONDS)
                .writeTimeout(20, TimeUnit.SECONDS)
                .readTimeout(20, TimeUnit.SECONDS)
                .build()
            mfcStatusService = createRetrofitService(MfcStatusService::class.java, MFC_STATUS_BASE_URL, client
            )
        }
        return mfcStatusService
    }

    private fun <T> createRetrofitService(clazz: Class<T>, baseUrl: String, client: OkHttpClient): T {
        val retrofit = Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(client)
            .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        return retrofit.create(clazz)
    }
}