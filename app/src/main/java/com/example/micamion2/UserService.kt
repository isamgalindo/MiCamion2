package com.example.micamion2
import com.google.gson.JsonObject
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

    @GET("/users/email/{email}")
    fun getUserIdByEmail(@Path("email") email: String): Call<JsonObject>

    @GET("/trips")
    fun getAllTrips(): Call<List<Trip>>

    @GET("loads/{id}")
    fun getLoadById(@Path("id") id: Int): Call<Load>


    @GET("accesspoints/{id}")
    fun getAccessPointById(@Path("id") id: Int): Call<AccessPoint>

    @GET("accesspoints")
    fun getAllAccessPoints(): Call<List<AccessPoint>>

    // POST a Trip
    @POST("/trips")
    fun createTrip(@Body trip: Trip): Call<Trip>

    @POST("/trailers")
    fun createTrailer(@Body trailer: Trailer): Call<Trailer>

    // POST a Load
    @POST("/loads")
    fun createLoad(@Body load: Load): Call<Load>

    // POST an AccessPoint
    @POST("/accesspoints")
    fun createAccessPoint(@Body accessPoint: AccessPoint): Call<AccessPoint>

    @GET("trailers")
    fun getAllTrailers(): Call<List<Trailer>>
    @GET("/trips/loadOwner/{loadOwnerId}")
    fun getTripsByLoadOwner(@Path("loadOwnerId") loadOwnerId: String): Call<List<Trip>>

    @GET("/trailers/driver/{driver}")
    fun getTrailerByDriver(@Path("driver") driverId: String): Call<List<Trailer>>

    @PUT("/assignTrailer")
    fun assignTrailer(@Body request: AssignDriverRequest): Call<Void> // Replace Void with your response type

    @PUT("trips/updateStatus/{trip_id}")
    fun updateTripStatusToDE(@Path("trip_id") tripId: Int): Call<Void>
}
data class AssignDriverRequest(val user_id: String)
