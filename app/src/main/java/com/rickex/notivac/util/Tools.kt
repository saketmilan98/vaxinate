package com.rickex.notivac.util

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.util.Base64
import android.util.Log
import android.widget.Toast
import com.google.firebase.messaging.FirebaseMessaging
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.rickex.notivac.dataclass.MainResponseDataClas
import com.tcp.rickexdriver.network.apiKotlin
import com.rickex.notivac.preferences.UserPreferenceManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.Integer.parseInt
import java.text.SimpleDateFormat
import java.util.*

class Tools {

    fun decodePassword(pass : String) : String {
        val decodedPass1 = Base64.decode(pass, Base64.DEFAULT).toString(charset("UTF-8"))
        var decodedPass2 = ""
        for (i in decodedPass1.indices){
            decodedPass2 = if(i%3==0){
                (decodedPass2 + (decodedPass1[i]-1))
            }
            else if (i%3==1) {
                (decodedPass2 + (decodedPass1[i]-2))
            }
            else{
                (decodedPass2 + (decodedPass1[i]-3))
            }
        }
        return decodedPass2
    }

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
    fun returnDose(dose1 : Int, dose2 : Int, context : Context) : Boolean{
        var result = false
        if(UserPreferenceManager.getSelectedDose(context).toString() == "DOSE1 AND DOSE2"){
            result = true
        }
        else if(UserPreferenceManager.getSelectedDose(context).toString() == "DOSE1"){
            result = dose1 != 0
        }
        else if(UserPreferenceManager.getSelectedDose(context).toString() == "DOSE2"){
            result = dose2 != 0
        }
        return result
    }

    fun showAlertDialogWithExitButton(desc : String, context: Context){

        val builder = AlertDialog.Builder(context)
        //set title for alert dialog
        builder.setTitle("Alert")
        //set message for alert dialog
        builder.setMessage(desc)
        builder.setIcon(android.R.drawable.ic_dialog_alert)

        //performing positive action
        builder.setPositiveButton("Exit"){dialogInterface, which ->
            (context as Activity).finish()
            //Toast.makeText(context,"clicked ok",Toast.LENGTH_LONG).show()
        }
        //performing cancel action
        /*builder.setNeutralButton("Cancel"){dialogInterface , which ->
            Toast.makeText(this,"clicked cancel\n operation cancel",Toast.LENGTH_LONG).show()
        }
        //performing negative action
        builder.setNegativeButton("No"){dialogInterface, which ->
            Toast.makeText(this,"clicked No",Toast.LENGTH_LONG).show()
        }*/
        // Create the AlertDialog
        val alertDialog: AlertDialog = builder.create()
        // Set other dialog properties
        alertDialog.setCancelable(false)
        alertDialog.show()
    }
    fun subscribeToUserGeneralTopic(){
        FirebaseMessaging.getInstance().subscribeToTopic("general_user")
            .addOnCompleteListener { task ->
                //var msg = getString(R.string.msg_subscribed)
                if (!task.isSuccessful) {
                    //msg = getString(R.string.msg_subscribe_failed)
                }
                //Toast.makeText(this@Main2Activity, "msg", Toast.LENGTH_SHORT).show()
            }

    }
}