package com.example.assignment.data

import com.example.assignment.model.User
import com.google.gson.GsonBuilder
import kotlinx.coroutines.Deferred
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query


//​
// page={page}
// ​, ​
// per_page={per_page}
//https://reqres.in/api/users?page=2&per_page=5


private const val BASE_URL = "https://reqres.in/api/"

val interceptor: HttpLoggingInterceptor =
    HttpLoggingInterceptor()
        .setLevel(HttpLoggingInterceptor.Level.BODY)

val client: OkHttpClient = OkHttpClient.Builder()
    .addInterceptor(interceptor)
    .build()

private val retrofit = Retrofit.Builder()
    .baseUrl(BASE_URL)
    .addConverterFactory(GsonConverterFactory.create())
    .client(client)
    .build()


interface UserService {
    @GET("users")
    suspend fun getUser(
        @Query(value = "page") page: Int,
        @Query(value = "per_page") per_page: Int
    ): User?
}

object UserApi {
    val retrofitService: UserService by lazy {
        retrofit.create(UserService::class.java)
    }
}
