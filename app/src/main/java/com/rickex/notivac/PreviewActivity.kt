package com.rickex.notivac

import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.LinearLayout
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import com.rickex.notivac.adapter.PreviewRvAdapter
import com.rickex.notivac.dataclass.MainResponseDataClas
import com.rickex.notivac.dataclass.Session
import com.rickex.notivac.network.ApiClient2
import com.rickex.notivac.util.Tools
import com.tcp.rickexdriver.network.apiKotlin
import com.rickex.notivac.preferences.UserPreferenceManager
import kotlinx.android.synthetic.main.activity_preview.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*


class PreviewActivity : AppCompatActivity() {

    lateinit var originalResponse : ArrayList<Session>
    lateinit var filteredResponse : ArrayList<Session>
    lateinit var call2 : Call<MainResponseDataClas>
    var builder: androidx.appcompat.app.AlertDialog.Builder? = null
    var progressDialog: androidx.appcompat.app.AlertDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_preview)
        progressDialog = getDialogProgressBar()?.create()
        progressDialog?.setCancelable(false)
        topToolbar_actpreview.setNavigationOnClickListener{
            finish()
        }
        fetchData()
        iv1_prevact.setOnClickListener {
            progressDialog?.show()
            fetchData()
        }
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        onBackPressed()
        return true
    }
    fun fetchData(){

        originalResponse = ArrayList()
        filteredResponse = ArrayList()
        val api = ApiClient2.client.create(apiKotlin::class.java)



        if(UserPreferenceManager.getSelectedDistrictOrPincode(this).toString() == "DISTRICT"){
            call2 = api.getList(
                UserPreferenceManager.getSelectedDistrictID(this).toString().toInt(),
                UserPreferenceManager.getSelectedDate(
                    this
                ).toString()
            )
        }
        else if(UserPreferenceManager.getSelectedDistrictOrPincode(this).toString() == "PINCODE"){
            call2 = api.getListByPincode(
                UserPreferenceManager.getSelectedPincode(this).toString().toInt(),
                UserPreferenceManager.getSelectedDate(
                    this
                ).toString()
            )
        }
        else {
            call2 = api.getList(
                UserPreferenceManager.getSelectedDistrictID(this).toString().toInt(),
                UserPreferenceManager.getSelectedDate(
                    this
                ).toString()
            )
        }



        call2.enqueue(object : Callback<MainResponseDataClas> {
            override fun onResponse(
                call: Call<MainResponseDataClas>,
                response: Response<MainResponseDataClas>
            ) {
                pb1_prevact.visibility = View.GONE
                progressDialog?.hide()
                val jsondata = response.body()
                Log.d("jsonData=", jsondata.toString())
                if (jsondata != null) {
                    iv1_prevact.visibility = View.VISIBLE
                    originalResponse = jsondata.sessions as ArrayList<Session>
                    filterResponse()
                    if (filteredResponse.isEmpty()) {
                        tv1_prevact.visibility = View.VISIBLE
                        rv1_prevact.visibility = View.GONE
                    } else {
                        tv1_prevact.visibility = View.GONE
                        rv1_prevact.visibility = View.VISIBLE
                        val adapter = PreviewRvAdapter(filteredResponse, this@PreviewActivity)
                        rv1_prevact.layoutManager = androidx.recyclerview.widget.GridLayoutManager(
                            this@PreviewActivity,
                            1
                        )
                        rv1_prevact.isNestedScrollingEnabled = true
                        rv1_prevact.adapter = adapter
                    }
                }
                else {
                    Tools().showToast("Error loading data. Please try again.", this@PreviewActivity)
                }
            }

            override fun onFailure(call: Call<MainResponseDataClas>, t: Throwable) {
                Log.e("failedCall", t.message.toString())
                pb1_prevact.visibility = View.GONE
                progressDialog?.hide()
                Tools().showToast("Error loading data. Please try again.", this@PreviewActivity)
                //Toast.makeText(context, "Error posting comment, please try again.", Toast.LENGTH_SHORT).show()
                //customProgressBar.stopAnimation()
                //retryBtn.visibility = View.VISIBLE
                //call1.cancel()
                //postsByPageApiCall()

            }
        })
    }

    fun filterResponse(){
        filteredResponse = originalResponse.filter { s -> UserPreferenceManager.getSelectedAge(this).toString().contains(
            s.min_age_limit.toString()
        ) && UserPreferenceManager.getSelectedVaccine(this).toString().contains(s.vaccine)
        && Tools().returnDose(s.available_capacity_dose1, s.available_capacity_dose2, this)} as ArrayList<Session>
    }

    override fun onDestroy() {
        super.onDestroy()
        try{ call2.cancel() }
        catch(e : Exception){}
    }

    fun getDialogProgressBar(): androidx.appcompat.app.AlertDialog.Builder? {
        if (builder == null) {
            builder = androidx.appcompat.app.AlertDialog.Builder(this)
            val progressBar = ProgressBar(this)
            val lp = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            progressBar.layoutParams = lp
            builder!!.setView(progressBar)
        }
        return builder
    }

}