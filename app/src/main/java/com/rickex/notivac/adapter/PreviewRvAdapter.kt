package com.rickex.notivac.adapter

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.rickex.notivac.R
import com.rickex.notivac.dataclass.MainResponseDataClas
import com.rickex.notivac.dataclass.Session
import com.rickex.notivac.util.Tools
import java.util.ArrayList
import androidx.constraintlayout.widget.ConstraintLayout


class PreviewRvAdapter(val dataa: ArrayList<Session>, val context: Context?) : androidx.recyclerview.widget.RecyclerView.Adapter<PreviewRvAdapter.ViewHolder>() {

    override fun onCreateViewHolder(p0: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.preview_recycler_layout, p0, false))
    }

    override fun getItemCount() =
        dataa.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        //holder.textView.text = ("Total Fare : ${dataa[position].totalFare} \nTotal Earning : ${dataa[position].totalEarning} \nTotal Commission : ${dataa[position].totalCommision}")
        holder.textView.text = dataa[position].vaccine
        holder.textView1.text = dataa[position].available_capacity_dose1.toString()
        holder.textView6.text = dataa[position].available_capacity_dose2.toString()
        if(position%2==0) {
            holder.constraintL1.setBackgroundColor(Color.parseColor("#f6f6f6"))
        }
        else {
            holder.constraintL1.setBackgroundColor(Color.parseColor("#FFFFFF"))
        }
        if(dataa[position].min_age_limit == 18){
            holder.textView2.text = "Age: 18-44"
        }
        else if(dataa[position].min_age_limit == 45){
            holder.textView2.text = "Age: 45+"
        }
        holder.textView3.text = dataa[position].name
        holder.textView4.text = dataa[position].address + ", ${dataa[position].state_name}, ${dataa[position].district_name}, ${dataa[position].pincode}"
        //holder.textView5.text = "${Tools().getFormattedTime(dataa[position].from)}-${Tools().getFormattedTime(dataa[position].to)}"
        holder.itemView.setOnClickListener {
            val uri = Uri.parse("https://selfregistration.cowin.gov.in/")
            val intent = Intent(Intent.ACTION_VIEW, uri)
            context?.startActivity(intent)
        }
    }

    class ViewHolder(view: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(view) {
        //val imageView = view.findViewById<ImageView>(R.id.imageview1)
        val textView = view.findViewById<TextView>(R.id.tv1_tlrl)
        val textView1 = view.findViewById<TextView>(R.id.tv3_tlrl)
        val textView2 = view.findViewById<TextView>(R.id.tv6_tlrl)
        val textView3 = view.findViewById<TextView>(R.id.tv4_tlrl)
        val textView4 = view.findViewById<TextView>(R.id.tv8_tlrl)
        val textView5 = view.findViewById<TextView>(R.id.tv9_tlrl)
        val textView6 = view.findViewById<TextView>(R.id.tv11_tlrl)
        val constraintL1 = view.findViewById<ConstraintLayout>(R.id.cl1_tlrl)
        //val textView2 = view.findViewById<TextView>(R.id.textView2)
    }


}