package com.rickex.notivac

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.GravityCompat
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.navigation.NavigationView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.rickex.notivac.adapter.DistrictRVAdapter
import com.rickex.notivac.adapter.StateRVAdapter
import com.rickex.notivac.dataclass.*
import com.rickex.notivac.network.ApiClient2
import com.rickex.notivac.util.Tools
import com.tcp.rickexdriver.network.apiKotlin
import com.rickex.notivac.preferences.UserPreferenceManager
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_preview.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class MainActivity : AppCompatActivity(), DatePickerDialog.OnDateSetListener, NavigationView.OnNavigationItemSelectedListener {

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

    var fDatabase = FirebaseDatabase.getInstance().reference.child(Tools().decodePassword(com.rickex.notivac.BuildConfig.APP_KEY_1)).child("adminValues")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if(intent.getBooleanExtra("fromNoti",false)){
            //intent.putExtra("notiType",notiType)
            //intent.putExtra("notiUrl",notiUrl)
            if(intent.getStringExtra("notiType") == "2"){
                val uri = Uri.parse("${intent.getStringExtra("notiUrl")}")
                val intent = Intent(Intent.ACTION_VIEW, uri)
                startActivity(intent)
                finish()
            }
            else {
                initialMethods()
            }
        }
        else {
            initialMethods()
        }

    }

    fun initialMethods(){
        fetchFirebaseValue()
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

        nav_view.setNavigationItemSelectedListener(this)

        val mDrawerToggle: ActionBarDrawerToggle = object : ActionBarDrawerToggle(
            this@MainActivity, drawer_layout, null, R.string.app_name, R.string.app_name
        ) {
            override fun onDrawerClosed(drawerView: View) {
                super.onDrawerClosed(drawerView)

            }

            override fun onDrawerOpened(drawerView: View) {
                super.onDrawerOpened(drawerView)


            }
        }
        drawer_layout.addDrawerListener(mDrawerToggle)
        topToolbar_actmain.setNavigationOnClickListener{
            drawer_layout.openDrawer(GravityCompat.START)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.nav_share -> {
                val shareBody = ("Check realtime vaccine slot availability, download Vaxinate app now through this link https://rebrand.ly/vaxinate .")
                val shareIntent = Intent(Intent.ACTION_SEND)
                shareIntent.type = "text/plain"
                shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Vaxinate")
                shareIntent.putExtra(Intent.EXTRA_TEXT, shareBody)
                startActivity(shareIntent)
            }

            R.id.nav_privacy -> {
                val uri = Uri.parse("https://rebrand.ly/vaxinateprivacypolicy")
                val intent = Intent(Intent.ACTION_VIEW, uri)
                startActivity(intent)
            }

            R.id.nav_notifier -> {
                val uri = Uri.parse("https://rebrand.ly/vaxinatenotifierlink")
                val intent = Intent(Intent.ACTION_VIEW, uri)
                startActivity(intent)
            }
        }
        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }
    fun initial(){
        setupAgeBtn()
        setupVaccineBtn()
        setupFilterDistrictOrPincodeBtn()
        setupDoseBtn()
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

    fun setupDoseBtn(){
        tv1_cl7_actmain.setOnClickListener {
            tv1_cl7_actmain.setTextColor(Color.WHITE)
            tv1_cl7_actmain.background = ResourcesCompat.getDrawable(resources, R.drawable.radio_high_solid,  theme)

            tv2_cl7_actmain.setTextColor(ResourcesCompat.getColor(resources, R.color.workinprogress, theme))
            tv2_cl7_actmain.background = ResourcesCompat.getDrawable(resources, R.drawable.radio_black_prior,  theme)

            tv4_cl7_actmain.setTextColor(ResourcesCompat.getColor(resources, R.color.workinprogress, theme))
            tv4_cl7_actmain.background = ResourcesCompat.getDrawable(resources, R.drawable.radio_black_prior,  theme)
            UserPreferenceManager.setSelectedDose(this,"DOSE1 AND DOSE2")
        }

        tv2_cl7_actmain.setOnClickListener {
            tv1_cl7_actmain.setTextColor(ResourcesCompat.getColor(resources, R.color.workinprogress, theme))
            tv1_cl7_actmain.background = ResourcesCompat.getDrawable(resources, R.drawable.radio_black_prior,  theme)

            tv2_cl7_actmain.setTextColor(Color.WHITE)
            tv2_cl7_actmain.background = ResourcesCompat.getDrawable(resources, R.drawable.radio_high_solid,  theme)

            tv4_cl7_actmain.setTextColor(ResourcesCompat.getColor(resources, R.color.workinprogress, theme))
            tv4_cl7_actmain.background = ResourcesCompat.getDrawable(resources, R.drawable.radio_black_prior,  theme)
            UserPreferenceManager.setSelectedDose(this,"DOSE1")
        }

        tv4_cl7_actmain.setOnClickListener {
            tv1_cl7_actmain.setTextColor(ResourcesCompat.getColor(resources, R.color.workinprogress, theme))
            tv1_cl7_actmain.background = ResourcesCompat.getDrawable(resources, R.drawable.radio_black_prior,  theme)

            tv2_cl7_actmain.setTextColor(ResourcesCompat.getColor(resources, R.color.workinprogress, theme))
            tv2_cl7_actmain.background = ResourcesCompat.getDrawable(resources, R.drawable.radio_black_prior,  theme)

            tv4_cl7_actmain.setTextColor(Color.WHITE)
            tv4_cl7_actmain.background = ResourcesCompat.getDrawable(resources, R.drawable.radio_high_solid,  theme)
            UserPreferenceManager.setSelectedDose(this,"DOSE2")
        }

        if(UserPreferenceManager.getSelectedDose(this).toString() == "DOSE1 AND DOSE2"){
            tv1_cl7_actmain.performClick()
        }
        else if(UserPreferenceManager.getSelectedDose(this).toString() == "DOSE1"){
            tv2_cl7_actmain.performClick()
        }
        else if(UserPreferenceManager.getSelectedDose(this).toString() == "DOSE2"){
            tv4_cl7_actmain.performClick()
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

    fun fetchFirebaseValue(){
        fDatabase.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()){
                    if(snapshot.child("app_online").value.toString().toBoolean()){

                        nav_view.menu.findItem(R.id.nav_notifier).isVisible =
                            snapshot.child("show_notifier_feature").value.toString().toBoolean()

                        checkForUpdate(snapshot.child("usable_version_flexible_user").value.toString().toLong(),
                            snapshot.child("usable_version_immediate_user").value.toString().toLong(),
                            snapshot.child("flexible_update_message").value.toString(),
                            snapshot.child("immediate_update_message").value.toString())
                    }
                    else {
                        Tools().showAlertDialogWithExitButton(snapshot.child("app_offline_message").value.toString(),this@MainActivity)
                    }
                }

            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
        Tools().subscribeToUserGeneralTopic()
    }

    fun checkForUpdate(usableVersionFlexible : Long, usableVersionImmediate: Long, flexibleUpdateMessage : String, immediateUpdateMessage : String){
        if (!(usableVersionImmediate <= com.rickex.notivac.BuildConfig.VERSION_CODE || usableVersionImmediate == 0L)
        ) {
            showImmediateUpdateDialog(immediateUpdateMessage)
        } else if (!(usableVersionFlexible <= com.rickex.notivac.BuildConfig.VERSION_CODE || usableVersionFlexible == 0L)
        ) {
            showFlexibleUpdateDialog(flexibleUpdateMessage)
        }
    }

    fun showFlexibleUpdateDialog(desc : String){
        val builder = AlertDialog.Builder(this)
        //set title for alert dialog
        builder.setTitle("Alert")
        //set message for alert dialog
        builder.setMessage(desc)
        builder.setIcon(android.R.drawable.ic_dialog_alert)

        //performing positive action
        builder.setPositiveButton("Update"){dialogInterface, which ->
            val uri = Uri.parse("https://rebrand.ly/vaxinate")
            val intent = Intent(Intent.ACTION_VIEW, uri)
            startActivity(intent)
        }
        //performing cancel action
        /*builder.setNeutralButton("Cancel"){dialogInterface , which ->
            Toast.makeText(this,"clicked cancel\n operation cancel",Toast.LENGTH_LONG).show()
        }*/
        //performing negative action
        builder.setNegativeButton("Not now"){dialogInterface, which ->

        }
        // Create the AlertDialog
        val alertDialog: AlertDialog = builder.create()
        // Set other dialog properties
        alertDialog.setCancelable(false)
        alertDialog.show()
    }
    fun showImmediateUpdateDialog(desc : String){
        val builder = AlertDialog.Builder(this)
        //set title for alert dialog
        builder.setTitle("Alert")
        //set message for alert dialog
        builder.setMessage(desc)
        builder.setIcon(android.R.drawable.ic_dialog_alert)

        //performing positive action
        builder.setPositiveButton("Update"){dialogInterface, which ->
            val uri = Uri.parse("https://rebrand.ly/vaxinate")
            val intent = Intent(Intent.ACTION_VIEW, uri)
            startActivity(intent)
            finish()
        }
        //performing cancel action
        /*builder.setNeutralButton("Cancel"){dialogInterface , which ->
            Toast.makeText(this,"clicked cancel\n operation cancel",Toast.LENGTH_LONG).show()
        }*/
        //performing negative action
      /*  builder.setNegativeButton("Not now"){dialogInterface, which ->

        }*/
        // Create the AlertDialog
        val alertDialog: AlertDialog = builder.create()
        // Set other dialog properties
        alertDialog.setCancelable(false)
        alertDialog.show()
    }

}