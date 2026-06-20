package com.manta.whatsapp.services

import com.manta.whatsapp.models.*
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*
import java.util.concurrent.TimeUnit

interface ApiService {
    @POST("auth")
    suspend fun login(@Body request: LoginRequest): Response<LoginResponse>

    @GET("user/profile")
    suspend fun getProfile(@Header("Authorization") token: String): Response<ProfileResponse>

    @GET("whatsapp/accounts")
    suspend fun getAccounts(): Response<AccountsResponse>

    @POST("whatsapp/request-pairing")
    suspend fun requestPairing(): Response<PairingResponse>

    @GET("whatsapp/pairing-status/{code}")
    suspend fun checkPairingStatus(@Path("code") code: String): Response<PairingStatusResponse>

    @POST("whatsapp/pair-confirm")
    suspend fun confirmPairing(@Body request: Map<String, String>): Response<PairingStatusResponse>

    @DELETE("whatsapp/accounts/{accountId}")
    suspend fun deleteAccount(@Path("accountId") id: String): Response<Unit>
}

object ApiClient {
    private const val BASE_URL = "https://your-server.com/api/v1/"
    private val logging = HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY }
    private val client = OkHttpClient.Builder()
        .addInterceptor(logging)
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .build()

    val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(client)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val apiService: ApiService = retrofit.create(ApiService::class.java)
}