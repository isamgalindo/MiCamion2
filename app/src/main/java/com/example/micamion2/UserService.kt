package com.example.micamion2
import retrofit2.Call
import retrofit2.http.*

interface UserService {

    @GET("auth/{id}/")
    fun getUser(@Path("id") id: Int): Call<User>

    @POST("auth/registration/")
    fun createUser(@Body user: CreateUserRequest): Call<User>

    @POST("auth/login/") // Replace with actual authentication endpoint
    @FormUrlEncoded
    fun authenticate(
        @Field("email") email: String,
        @Field("password") password: String
    ): Call<AuthenticationResponse>

    @PUT("auth/{id}/")
    fun updateUser(@Path("id") id: Int, @Body user: User): Call<User>

    @DELETE("auth/{id}/")
    fun deleteUser(@Path("id") id: Int): Call<Void>

    @GET("/users/{id}/")
    fun getUserType(@Query("userType") userType: String): Call<User>

    @GET("/users/email/{email}")
    fun getUserByEmail(@Path("email") email: String): Call<User>

    @GET("/trips")
    fun getAllTrips(): Call<List<Trip>>

    @GET("loads/{id}")
    fun getLoadById(@Path("id") id: Int): Call<Load>
    @GET("accesspoints/{id}")
    fun getAccessPointById(@Path("id") id: Int): Call<AccessPoint>

    @GET("accesspoints")
    fun getAllAccessPoints(): Call<List<AccessPoint>>

    @GET("trailers")
    fun getAllTrailers(): Call<List<Trailer>>
}