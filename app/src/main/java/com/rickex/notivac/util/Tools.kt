package com.rickex.notivac.util

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.rickex.notivac.adapter.PreviewRvAdapter
import com.rickex.notivac.dataclass.MainResponseDataClas
import com.rickex.notivac.dataclass.Session
import com.tcp.rickexdriver.network.apiKotlin
import kotlinx.android.synthetic.main.activity_preview.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.Integer.parseInt
import java.text.SimpleDateFormat
import java.util.*

class Tools {

    fun postNotificationAdmin(jsonObject: JsonObject, context: Context){
        Log.d("jsonObjectNoti", Gson().toJson(jsonObject))
        val retrofit = Retrofit.Builder()
            .baseUrl(apiKotlin.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val api = retrofit.create(apiKotlin::class.java)

        val call2 : Call<MainResponseDataClas>

        call2 = api.getList(82,"16-05-2021")

        call2.enqueue(object : Callback<MainResponseDataClas> {
            override fun onResponse(
                call: Call<MainResponseDataClas>,
                response: Response<MainResponseDataClas>
            ) {

                val jsondata = response.body()
                Log.d("jsonData=", jsondata.toString())

            }

            override fun onFailure(call: Call<MainResponseDataClas>, t: Throwable) {
                //Toast.makeText(context, "Error posting comment, please try again.", Toast.LENGTH_SHORT).show()
                //customProgressBar.stopAnimation()
                //retryBtn.visibility = View.VISIBLE
                //call1.cancel()
                //postsByPageApiCall()

            }
        })
    }

    fun getFormattedDate(date1: Date) : String{
        val fmt23 = SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH)
        return fmt23.format(date1)
    }

    fun getFormattedTime(str: String) : String{
        val fmt1 = SimpleDateFormat("HH:mm:ss", Locale.ENGLISH)
        val temp1 = fmt1.parse(str)
        val fmt2 = SimpleDateFormat("hh:mm a", Locale.ENGLISH)
        return fmt2.format(temp1!!)
    }

    fun showToast(str : String, ctx : Context){
        Toast.makeText(ctx, str, Toast.LENGTH_LONG).show()
    }

    fun hasDigits(str : String) : Boolean{
        var numeric = true
        try {
            val num = parseInt(str)
        } catch (e: NumberFormatException) {
            numeric = false
        }
        return numeric

    }
}