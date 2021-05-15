package com.tcp.rickexdriver.network

import com.rickex.notivac.dataclass.DistrictDataClass
import com.rickex.notivac.dataclass.MainResponseDataClas
import com.rickex.notivac.dataclass.StatesDataClass
import retrofit2.Call
import retrofit2.http.*

interface apiKotlin {


    @GET("appointment/sessions/public/findByDistrict/")
    fun getList(
        @Query("district_id") districtId: Int,
        @Query("date") date: String
    ): Call<MainResponseDataClas>

    @GET("admin/location/states")
    fun getStateList(): Call<StatesDataClass>

    @GET("admin/location/districts/{stateId}")
    fun getDistrictList(
        @Path("stateId") stateId : Int
    ): Call<DistrictDataClass>

    @GET("appointment/sessions/public/findByPin/")
    fun getListByPincode(
        @Query("pincode") pincode: Int,
        @Query("date") date: String
    ): Call<MainResponseDataClas>

    //https://cdn-api.co-vin.in/api/v2/appointment/sessions/public/findByPin?pincode=853204&date=16-05-2021

    //http://worldtimeapi.org/api/timezone/Asia/Kolkata
    //@GET("timeapi.php")
    //fun getCurrentTime(): Call<TimeApiDataClass>
//https://cdn-api.co-vin.in/api/v2/appointment/sessions/public/findByDistrict?district_id=83&date=15-05-2021 //slots
    //https://cdn-api.co-vin.in/api/v2/admin/location/states states
    //https://cdn-api.co-vin.in/api/v2/admin/location/districts/5 district

    companion object {
        val BASE_URL = "https://cdn-api.co-vin.in/api/v2/"
    }
}

