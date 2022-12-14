//package com.d201.eyeson.util
//
//import android.content.Context
//import android.media.session.MediaSession
//import com.google.android.gms.common.api.Api
//import com.google.gson.GsonBuilder
//import kotlinx.coroutines.Dispatchers
//import kotlinx.coroutines.GlobalScope
//import kotlinx.coroutines.async
//import kotlinx.coroutines.runBlocking
//import okhttp3.*
//import okhttp3.logging.HttpLoggingInterceptor
//import retrofit2.Retrofit
//import retrofit2.converter.gson.GsonConverterFactory
//
//class TokenAuthenticator constructor(
//    val context: Context,
//    private val refreshToken: String,
//) : Authenticator {
//
//    companion object {
//        private val TAG = TokenAuthenticator::class.java.simpleName
//    }
//
//    override fun authenticate(route: Route?, response: Response): Request? {
//
//        if (response.code == 401) {
//
//            val refreshToken = CommonHelper.getRefreshToken(sharedPref)
//            val getNewDeviceToken = GlobalScope.async(Dispatchers.Default) {
//                getNewDeviceToken(refreshToken)
//            }
//
//            val token = runBlocking {
//                getNewDeviceToken.await()
//            }
//            if(token != null) {
//                return getRequest(response, token)
//            }
//        }
//        return null
//    }
//
//    private suspend inline fun getNewDeviceToken(token: String): String? {
//        return GlobalScope.async(Dispatchers.Default) {
//            callApiNewDeviceToken(token)
//        }.await()
//    }
//
//
//    private suspend inline fun callApiNewDeviceToken(token: String) : String? = suspendCoroutine { continuation ->
//        createWebService<Api>()
//            .refreshToken(RefreshToken(token))
//            .with(rx)
//            .response(object : ApiCallback<MediaSession.Token>{
//                override fun success(data: MediaSession.Token?) {
//                    if(data != null) {
//                        CommonHelper.saveTokenInfo(sharedPref, data)
//                        continuation.resume(data.accessToken)
//                    } else {
//                        continuation.resume(null)
//                    }
//                }
//
//                override fun error(statusCode: Int, message: String?) {
//                    continuation.resume(null)
//                }
//            })
//
//        return@suspendCoroutine
//    }
//
//    private val okHttp =  OkHttpClient.Builder()
//        .connectTimeout(TIMEOUT_LIMIT, TimeUnit.SECONDS)
//        .readTimeout(TIMEOUT_LIMIT, TimeUnit.SECONDS)
//        .writeTimeout(TIMEOUT_LIMIT, TimeUnit.SECONDS)
//        .addInterceptor(HttpLoggingInterceptor().apply {
//            level = if (BuildConfig.DEBUG) {
//                HttpLoggingInterceptor.Level.BODY
//            } else {
//                HttpLoggingInterceptor.Level.NONE
//            }
//        })
//        .build()
//
//    private inline fun <reified T> createWebService(): T {
//        val retrofit = Retrofit.Builder()
//            .baseUrl(BuildConfig.SERVER_URL)
//            .client(okHttp)
//            .addConverterFactory(
//                GsonConverterFactory.create(
//                GsonBuilder().serializeNulls().create()
//            ))
//            .addCallAdapterFactory(RxJava2CallAdapterFactory.create()).build()
//        return retrofit.create(T::class.java)
//    }
//
//    private fun getRequest(response: Response, token: String): Request {
//        return response.request
//            .newBuilder()
//            .removeHeader("Authorization")
//            .addHeader("Authorization", "Bearer $token")
//            .build()
//    }
//}