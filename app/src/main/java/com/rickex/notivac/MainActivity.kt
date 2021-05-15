package com.rickex.notivac

import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.DatePicker
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import com.rickex.notivac.adapter.DistrictRVAdapter
import com.rickex.notivac.adapter.PreviewRvAdapter
import com.rickex.notivac.adapter.StateRVAdapter
import com.rickex.notivac.dataclass.*
import com.rickex.notivac.network.ApiClient2
import com.rickex.notivac.util.Tools
import com.tcp.rickexdriver.network.apiKotlin
import com.tcp.rickexuser.preferences.UserPreferenceManager
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_preview.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity(), DatePickerDialog.OnDateSetListener {

    var day = 0
    var month:Int = 0
    var year:Int = 0
    var hour:Int = 0
    var minute:Int = 0
    var myday = 0
    var myMonth:Int = 0
    var myYear:Int = 0
    var myHour:Int = 0
    var myMinute:Int = 0
    var choosenDate = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initial()
        bt1_actmain.setOnClickListener{

            if(UserPreferenceManager.getSelectedDistrictOrPincode(this).toString() == "DISTRICT"){
                if(UserPreferenceManager.getSelectedState(this).toString() == "STATE"){
                    if(UserPreferenceManager.getSelectedDistrict(this).toString() == "DISTRICT"){
                        Tools().showToast("Please select a valid State and District", this)
                    }
                    else {
                        Tools().showToast("Please select a valid State", this)
                    }
                }
                else if(UserPreferenceManager.getSelectedDistrict(this).toString() == "DISTRICT"){
                    Tools().showToast("Please select a valid District", this)
                }
                else {
                    val intent = Intent(this, PreviewActivity::class.java)
                    startActivity(intent)
                }
            }
            else if(UserPreferenceManager.getSelectedDistrictOrPincode(this).toString() == "PINCODE"){
                if(et1_cl6_actmain.text.toString().length == 6){
                    if(Tools().hasDigits(et1_cl6_actmain.text.toString())){
                        UserPreferenceManager.setSelectedPincode(this, et1_cl6_actmain.text.toString())
                        val intent = Intent(this, PreviewActivity::class.java)
                        startActivity(intent)
                    }
                    else {
                        Tools().showToast("Please enter a valid pincode", this)
                    }

                }

                else {
                    Tools().showToast("Please enter a valid pincode", this)
                    //val intent = Intent(this, PreviewActivity::class.java)
                    //startActivity(intent)
                }
            }


        }
        tv1_actmain.setOnClickListener {
            showStatePage()
        }
        tv2_actmain.setOnClickListener {
            if(UserPreferenceManager.getSelectedState(this).toString() != "STATE"){
                showDistrictPage()
            }
            else {
                showStatePage()
            }

        }
        tv3_actmain.setOnClickListener {
            openDateChooser()
        }

    }
    fun initial(){
        setupAgeBtn()
        setupVaccineBtn()
        setupFilterDistrictOrPincodeBtn()
        tv1_actmain.text = UserPreferenceManager.getSelectedState(this)
        tv2_actmain.text = UserPreferenceManager.getSelectedDistrict(this)
        et1_cl6_actmain.setText(UserPreferenceManager.getSelectedPincode(this))
        if(UserPreferenceManager.getSelectedDate(this).toString().isEmpty()){
            UserPreferenceManager.setSelectedDate(this,Tools().getFormattedDate(Date())).toString()
            tv3_actmain.text = UserPreferenceManager.getSelectedDate(this).toString()
        }
        else {
            tv3_actmain.text = UserPreferenceManager.getSelectedDate(this).toString()
        }

    }
    fun openDateChooser(){
        val calendar = Calendar.getInstance()
        year = calendar[Calendar.YEAR]
        month = calendar[Calendar.MONTH]
        day = calendar[Calendar.DAY_OF_MONTH]
        val datePickerDialog =
            DatePickerDialog(this@MainActivity, this@MainActivity, year, month, day)
        datePickerDialog.show()
    }
    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        myYear = year
        myday = dayOfMonth
        myMonth = month

        val calendar = Calendar.getInstance()
        calendar.set(Calendar.YEAR, myYear)
        calendar.set(Calendar.MONTH, myMonth)
        calendar.set(Calendar.DAY_OF_MONTH, myday)
        calendar.set(Calendar.HOUR_OF_DAY, myHour)
        calendar.set(Calendar.MINUTE, myMinute)
        calendar.set(Calendar.SECOND, 0)

        val date1 = Date()
        date1.time = calendar.timeInMillis

        val date2 = Date()

        choosenDate = Tools().getFormattedDate(date1)
        Log.e("datediff","${date1.time - date2.time}")
        if(date1.time - date2.time >= -86399000){
            UserPreferenceManager.setSelectedDate(this, choosenDate)
            tv3_actmain.text = UserPreferenceManager.getSelectedDate(this).toString()
            //properTime = true
            //tv2InfoSRA.setTextColor(resources.getColor(R.color.green, theme))
            //tv2InfoSRA.text = ("$timeStr\nClick on Proceed")
        }
        else {
            Tools().showToast("Please select a valid date.", this)
            openDateChooser()
            //properTime = false
            //tv2InfoSRA.setTextColor(resources.getColor(R.color.red, theme))
            //tv2InfoSRA.text = ("$timeStr\nPlease select a time in future.")
        }
    }

    fun setupAgeBtn(){
        tv1_cl2_actmain.setOnClickListener {
            tv1_cl2_actmain.setTextColor(Color.WHITE)
            tv1_cl2_actmain.background = ResourcesCompat.getDrawable(resources, R.drawable.radio_high_solid,  theme)

            tv2_cl2_actmain.setTextColor(ResourcesCompat.getColor(resources, R.color.workinprogress, theme))
            tv2_cl2_actmain.background = ResourcesCompat.getDrawable(resources, R.drawable.radio_black_prior,  theme)

            tv4_cl2_actmain.setTextColor(ResourcesCompat.getColor(resources, R.color.workinprogress, theme))
            tv4_cl2_actmain.background = ResourcesCompat.getDrawable(resources, R.drawable.radio_black_prior,  theme)
            UserPreferenceManager.setSelectedAge(this,"18 AND 45")
        }

        tv2_cl2_actmain.setOnClickListener {
            tv1_cl2_actmain.setTextColor(ResourcesCompat.getColor(resources, R.color.workinprogress, theme))
            tv1_cl2_actmain.background = ResourcesCompat.getDrawable(resources, R.drawable.radio_black_prior,  theme)

            tv2_cl2_actmain.setTextColor(Color.WHITE)
            tv2_cl2_actmain.background = ResourcesCompat.getDrawable(resources, R.drawable.radio_high_solid,  theme)

            tv4_cl2_actmain.setTextColor(ResourcesCompat.getColor(resources, R.color.workinprogress, theme))
            tv4_cl2_actmain.background = ResourcesCompat.getDrawable(resources, R.drawable.radio_black_prior,  theme)
            UserPreferenceManager.setSelectedAge(this,"18")
        }

        tv4_cl2_actmain.setOnClickListener {
            tv1_cl2_actmain.setTextColor(ResourcesCompat.getColor(resources, R.color.workinprogress, theme))
            tv1_cl2_actmain.background = ResourcesCompat.getDrawable(resources, R.drawable.radio_black_prior,  theme)

            tv2_cl2_actmain.setTextColor(ResourcesCompat.getColor(resources, R.color.workinprogress, theme))
            tv2_cl2_actmain.background = ResourcesCompat.getDrawable(resources, R.drawable.radio_black_prior,  theme)

            tv4_cl2_actmain.setTextColor(Color.WHITE)
            tv4_cl2_actmain.background = ResourcesCompat.getDrawable(resources, R.drawable.radio_high_solid,  theme)
            UserPreferenceManager.setSelectedAge(this,"45")
        }

        if(UserPreferenceManager.getSelectedAge(this).toString() == "18 AND 45"){
            tv1_cl2_actmain.performClick()
        }
        else if(UserPreferenceManager.getSelectedAge(this).toString() == "18"){
            tv2_cl2_actmain.performClick()
        }
        else if(UserPreferenceManager.getSelectedAge(this).toString() == "45"){
            tv4_cl2_actmain.performClick()
        }
    }

    fun setupVaccineBtn(){
        tv1_cl1_actmain.setOnClickListener {
            tv1_cl1_actmain.setTextColor(Color.WHITE)
            tv1_cl1_actmain.background = ResourcesCompat.getDrawable(resources, R.drawable.radio_high_solid,  theme)

            tv2_cl1_actmain.setTextColor(ResourcesCompat.getColor(resources, R.color.workinprogress, theme))
            tv2_cl1_actmain.background = ResourcesCompat.getDrawable(resources, R.drawable.radio_black_prior,  theme)

            tv4_cl1_actmain.setTextColor(ResourcesCompat.getColor(resources, R.color.workinprogress, theme))
            tv4_cl1_actmain.background = ResourcesCompat.getDrawable(resources, R.drawable.radio_black_prior,  theme)
            UserPreferenceManager.setSelectedVaccine(this,"COVAXIN AND COVISHIELD")
        }

        tv2_cl1_actmain.setOnClickListener {
            tv1_cl1_actmain.setTextColor(ResourcesCompat.getColor(resources, R.color.workinprogress, theme))
            tv1_cl1_actmain.background = ResourcesCompat.getDrawable(resources, R.drawable.radio_black_prior,  theme)

            tv2_cl1_actmain.setTextColor(Color.WHITE)
            tv2_cl1_actmain.background = ResourcesCompat.getDrawable(resources, R.drawable.radio_high_solid,  theme)

            tv4_cl1_actmain.setTextColor(ResourcesCompat.getColor(resources, R.color.workinprogress, theme))
            tv4_cl1_actmain.background = ResourcesCompat.getDrawable(resources, R.drawable.radio_black_prior,  theme)
            UserPreferenceManager.setSelectedVaccine(this,"COVAXIN")
        }

        tv4_cl1_actmain.setOnClickListener {
            tv1_cl1_actmain.setTextColor(ResourcesCompat.getColor(resources, R.color.workinprogress, theme))
            tv1_cl1_actmain.background = ResourcesCompat.getDrawable(resources, R.drawable.radio_black_prior,  theme)

            tv2_cl1_actmain.setTextColor(ResourcesCompat.getColor(resources, R.color.workinprogress, theme))
            tv2_cl1_actmain.background = ResourcesCompat.getDrawable(resources, R.drawable.radio_black_prior,  theme)

            tv4_cl1_actmain.setTextColor(Color.WHITE)
            tv4_cl1_actmain.background = ResourcesCompat.getDrawable(resources, R.drawable.radio_high_solid,  theme)
            UserPreferenceManager.setSelectedVaccine(this,"COVISHIELD")
        }

        if(UserPreferenceManager.getSelectedVaccine(this).toString() == "COVAXIN AND COVISHIELD"){
            tv1_cl1_actmain.performClick()
        }
        else if(UserPreferenceManager.getSelectedVaccine(this).toString() == "COVAXIN"){
            tv2_cl1_actmain.performClick()
        }
        else if(UserPreferenceManager.getSelectedVaccine(this).toString() == "COVISHIELD"){
            tv4_cl1_actmain.performClick()
        }
    }

    fun setupFilterDistrictOrPincodeBtn(){
        tv1_cl3_actmain.setOnClickListener {
            tv1_cl3_actmain.setTextColor(Color.WHITE)
            tv1_cl3_actmain.background = ResourcesCompat.getDrawable(resources, R.drawable.radio_high_solid,  theme)

            tv2_cl3_actmain.setTextColor(ResourcesCompat.getColor(resources, R.color.workinprogress, theme))
            tv2_cl3_actmain.background = ResourcesCompat.getDrawable(resources, R.drawable.radio_black_prior,  theme)
            UserPreferenceManager.setSelectedDistrictOrPincode(this,"DISTRICT")
            cl5_actmain.visibility = View.VISIBLE
            cl6_actmain.visibility = View.GONE

        }

        tv2_cl3_actmain.setOnClickListener {
            tv1_cl3_actmain.setTextColor(ResourcesCompat.getColor(resources, R.color.workinprogress, theme))
            tv1_cl3_actmain.background = ResourcesCompat.getDrawable(resources, R.drawable.radio_black_prior,  theme)

            tv2_cl3_actmain.setTextColor(Color.WHITE)
            tv2_cl3_actmain.background = ResourcesCompat.getDrawable(resources, R.drawable.radio_high_solid,  theme)
            UserPreferenceManager.setSelectedDistrictOrPincode(this,"PINCODE")
            cl5_actmain.visibility = View.GONE
            cl6_actmain.visibility = View.VISIBLE
        }

        if(UserPreferenceManager.getSelectedDistrictOrPincode(this).toString() == "DISTRICT"){
            tv1_cl3_actmain.performClick()
        }
        else if(UserPreferenceManager.getSelectedDistrictOrPincode(this).toString() == "PINCODE"){
            tv2_cl3_actmain.performClick()
        }
    }

    fun showStatePage(){
        val dialog = Dialog(this, android.R.style.Theme_Black_NoTitleBar_Fullscreen)
        dialog.setContentView(R.layout.state_district_page_dialog_layout)
        dialog.show()
        val toolbar1 = dialog.findViewById<Toolbar>(R.id.topToolbar_notidialog)
        val rv1_notipage = dialog.findViewById<RecyclerView>(R.id.rv1_notidialog)
        val pb1_notipage = dialog.findViewById<ProgressBar>(R.id.pb1_notidialog)
        val toolbarTitle = dialog.findViewById<TextView>(R.id.tv1_toolbar_notidialog)
        toolbarTitle.text = "Select State"
        toolbar1.setNavigationOnClickListener{
            dialog.cancel()
        }
        loadAndShowState(dialog, rv1_notipage, pb1_notipage)
        //closeStateDialogAndShowDistrictDialog(dialog)

    }
    fun closeStateDialogAndShowDistrictDialog(dialog : Dialog){
        dialog.cancel()
        tv1_actmain.text = UserPreferenceManager.getSelectedState(this)
        tv2_actmain.text = UserPreferenceManager.getSelectedDistrict(this)
        showDistrictPage()
    }

    fun loadAndShowState(dialog : Dialog, rv1NotiPage : RecyclerView, pb1NotiPage : ProgressBar){
        val api = ApiClient2.client.create(apiKotlin::class.java)
        val call2 : Call<StatesDataClass>
        call2 = api.getStateList()
        call2.enqueue(object : Callback<StatesDataClass> {
            override fun onResponse(
                call: Call<StatesDataClass>,
                response: Response<StatesDataClass>
            ) {

                val jsondata = response.body()
                Log.d("jsonData=", jsondata.toString())
                if(jsondata != null){
                    val adapter = StateRVAdapter(jsondata.states as ArrayList<State>, dialog, this@MainActivity)
                    rv1NotiPage.layoutManager = androidx.recyclerview.widget.GridLayoutManager(this@MainActivity, 1)
                    rv1NotiPage.isNestedScrollingEnabled = true
                    rv1NotiPage.adapter = adapter

                }
            }

            override fun onFailure(call: Call<StatesDataClass>, t: Throwable) {
                Log.e("failedCall", t.message.toString())
                Tools().showToast("Error loading data. Please try again.", this@MainActivity)
                //Toast.makeText(context, "Error posting comment, please try again.", Toast.LENGTH_SHORT).show()
                //customProgressBar.stopAnimation()
                //retryBtn.visibility = View.VISIBLE
                //call1.cancel()
                //postsByPageApiCall()

            }
        })
    }

    fun showDistrictPage(){
        val dialog = Dialog(this, android.R.style.Theme_Black_NoTitleBar_Fullscreen)
        dialog.setContentView(R.layout.state_district_page_dialog_layout)
        dialog.show()
        val toolbar1 = dialog.findViewById<Toolbar>(R.id.topToolbar_notidialog)
        val rv1_notipage = dialog.findViewById<RecyclerView>(R.id.rv1_notidialog)
        val pb1_notipage = dialog.findViewById<ProgressBar>(R.id.pb1_notidialog)
        val toolbarTitle = dialog.findViewById<TextView>(R.id.tv1_toolbar_notidialog)
        toolbarTitle.text = "Select District"
        //tv1_toolbar_notidialog
        toolbar1.setNavigationOnClickListener{
            dialog.cancel()
        }
        loadAndShowDistrict(dialog, rv1_notipage)
        //closeDistrictDialog(dialog)

    }
    fun closeDistrictDialog(dialog : Dialog){
        dialog.cancel()
        tv2_actmain.text = UserPreferenceManager.getSelectedDistrict(this)
    }

    fun loadAndShowDistrict(dialog : Dialog, rv1NotiPage : RecyclerView){
        val api = ApiClient2.client.create(apiKotlin::class.java)
        val call2 : Call<DistrictDataClass>
        call2 = api.getDistrictList(UserPreferenceManager.getSelectedStateID(this).toString().toInt())
        call2.enqueue(object : Callback<DistrictDataClass> {
            override fun onResponse(
                call: Call<DistrictDataClass>,
                response: Response<DistrictDataClass>
            ) {

                val jsondata = response.body()
                Log.d("jsonData=", jsondata.toString())
                if(jsondata != null){
                    val adapter = DistrictRVAdapter(jsondata.districts as ArrayList<District>, dialog, this@MainActivity)
                    rv1NotiPage.layoutManager = androidx.recyclerview.widget.GridLayoutManager(this@MainActivity, 1)
                    rv1NotiPage.isNestedScrollingEnabled = true
                    rv1NotiPage.adapter = adapter
                }
            }

            override fun onFailure(call: Call<DistrictDataClass>, t: Throwable) {
                Log.e("failedCall", t.message.toString())
                Tools().showToast("Error loading data. Please try again.", this@MainActivity)
                //Toast.makeText(context, "Error posting comment, please try again.", Toast.LENGTH_SHORT).show()
                //customProgressBar.stopAnimation()
                //retryBtn.visibility = View.VISIBLE
                //call1.cancel()
                //postsByPageApiCall()

            }
        })
    }

}