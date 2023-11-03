package com.example.micamion2

import com.google.gson.GsonBuilder
import com.google.gson.JsonDeserializer
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

object RetrofitInstance {
    private const val BASE_URL = "http://10.0.2.2:8000/"

    val apiUsuario: UserService by lazy {
        val gson = GsonBuilder().registerTypeAdapter(LocalDateTime::class.java, JsonDeserializer<LocalDateTime> { json, _, _ ->
            LocalDateTime.parse(json.asJsonPrimitive.asString, DateTimeFormatter.ISO_OFFSET_DATE_TIME)
        }).create()
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        retrofit.create(UserService::class.java)
    }
}