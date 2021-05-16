package com.rickex.notivac.adapter

import android.app.Dialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.rickex.notivac.MainActivity
import com.rickex.notivac.R
import com.rickex.notivac.dataclass.District
import com.rickex.notivac.dataclass.State
import com.rickex.notivac.preferences.UserPreferenceManager

class StateRVAdapter(val dataa: ArrayList<State>, val dialog: Dialog, val context: Context?) : androidx.recyclerview.widget.RecyclerView.Adapter<StateRVAdapter.ViewHolder>() {

    override fun onCreateViewHolder(p0: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.state_district_item_recycler_layout, p0, false))
    }

    override fun getItemCount() =
        dataa.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.textView2.text = dataa[position].state_name

        holder.itemView.setOnClickListener {
            UserPreferenceManager.setSelectedState(context!!,dataa[position].state_name)
            UserPreferenceManager.setSelectedStateID(context,dataa[position].state_id.toString())
            UserPreferenceManager.setSelectedDistrict(context,"DISTRICT")
            UserPreferenceManager.setSelectedDistrictID(context,"DISTRICTID")
            (context as MainActivity).closeStateDialogAndShowDistrictDialog(dialog)
            //val dial = "tel:${dataa[position].phone}"
            //context.startActivity(Intent(Intent.ACTION_CALL, Uri.parse(dial)))
        }
    }

    class ViewHolder(view: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(view) {
        val textView2 = view.findViewById<TextView>(R.id.tv3_nirl)
        val view1 = view.findViewById<View>(R.id.view1_nirl)
    }


}

class DistrictRVAdapter(val dataa: ArrayList<District>, val dialog: Dialog, val context: Context?) : androidx.recyclerview.widget.RecyclerView.Adapter<DistrictRVAdapter.ViewHolder>() {

    override fun onCreateViewHolder(p0: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.state_district_item_recycler_layout, p0, false))
    }

    override fun getItemCount() =
        dataa.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.textView2.text = dataa[position].district_name

        holder.itemView.setOnClickListener {
            UserPreferenceManager.setSelectedDistrict(context!!,dataa[position].district_name)
            UserPreferenceManager.setSelectedDistrictID(context,dataa[position].district_id.toString())
            (context as MainActivity).closeDistrictDialog(dialog)
            //val dial = "tel:${dataa[position].phone}"
            //context.startActivity(Intent(Intent.ACTION_CALL, Uri.parse(dial)))
        }
    }

    class ViewHolder(view: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(view) {
        val textView2 = view.findViewById<TextView>(R.id.tv3_nirl)
        val view1 = view.findViewById<View>(R.id.view1_nirl)
    }


}