package com.smile.vhaconsultancy.adapters

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.smile.vhaconsultancy.R
import com.smile.vhaconsultancy.models.AprilPruningModel
import com.smile.vhaconsultancy.utilities.SharedPref
import java.util.*

class PruningListRecyclerViewAdapter(private val plots: ArrayList<AprilPruningModel>) : RecyclerView.Adapter<PruningListRecyclerViewAdapter.ViewHolder>() {
    var userPhoneNumber: String? = ""
    var database: FirebaseDatabase? = null
    var plot_key: String? = ""
    var databasePlotListReference: DatabaseReference? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.pruning_item, parent, false)
        database = FirebaseDatabase.getInstance()

        userPhoneNumber = SharedPref.Companion.getInstance(parent.context)?.getSharedPref(parent.context.getString(R.string.userPhoneNumber))
        plot_key=SharedPref.Companion.getInstance(parent.context)?.getSharedPref(parent.context.getString(R.string.plot_key))

        databasePlotListReference = database!!.getReference(parent.context.getString(R.string.user_list)).child(userPhoneNumber!!).child(parent.context.getString(R.string.plot_list)).child(plot_key.toString()).child("april_pruning_list")

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
         plots[position]
        holder.txtArea.text = plots[position].srNo.toString() + ""
        holder.txtVariety.text = plots[position].strDate + ""
        holder.txtDistance.text = plots[position].work_spray.toString() + ""
        holder.txtNumerOfVine.text = plots[position].fertilizer.toString() + ""
     //   holder.checkboxFertilizer.isEnabled=!plots[position].fertilizer_completed
       // holder.checkboxWork.isEnabled=!plots[position].work_spray_completed
        if(plots[position].fertilizer_completed)
        {
            holder.checkboxFertilizer.setPaintFlags(holder.checkboxFertilizer.getPaintFlags() or Paint.STRIKE_THRU_TEXT_FLAG)

        //    holder.checkboxFertilizer.setBackgroundColor(holder.checkboxFertilizer.context.resources.getColor(R.color.add_amount_grey))
        }
        else
        {
            holder.checkboxFertilizer.setPaintFlags(holder.checkboxFertilizer.getPaintFlags() and Paint.STRIKE_THRU_TEXT_FLAG.inv())

        }

        if(plots[position].work_spray_completed)
        {
            holder.checkboxWork.setPaintFlags(holder.checkboxWork.getPaintFlags() or Paint.STRIKE_THRU_TEXT_FLAG)

            //    holder.checkboxFertilizer.setBackgroundColor(holder.checkboxFertilizer.context.resources.getColor(R.color.add_amount_grey))
        }
        else
        {
            holder.checkboxWork.setPaintFlags(holder.checkboxWork.getPaintFlags() and Paint.STRIKE_THRU_TEXT_FLAG.inv())

        }
        holder.checkboxWork.setOnClickListener { buttonView  ->
            if( !plots[position].work_spray_completed) {
                databasePlotListReference?.child(plots[position].srNo.toString())?.child("work_spray_completed")?.setValue(true)
                plots[position].work_spray_completed=true
            }else{
                databasePlotListReference?.child(plots[position].srNo.toString())?.child("work_spray_completed")?.setValue(false)
                plots[position].work_spray_completed=false
            }


         //   notifyItemChanged(position)
                //Do Whatever you want in isChecked

        }
        holder.checkboxFertilizer.setOnClickListener { buttonView  ->
if(! plots[position].fertilizer_completed) {
    databasePlotListReference?.child(plots[position].srNo.toString())?.child("fertilizer_completed")?.setValue(true)
    //Do Whatever you want in isChecked
    plots[position].fertilizer_completed = true
}else{
    databasePlotListReference?.child(plots[position].srNo.toString())?.child("fertilizer_completed")?.setValue(false)
    //Do Whatever you want in isChecked
    plots[position].fertilizer_completed = false
}
//            notifyItemChanged(position)
        }
    }

    override fun getItemCount(): Int {
        return plots.size
    }

    inner class ViewHolder(val mView: View) : RecyclerView.ViewHolder(mView) {
        val txtArea: TextView
        val txtVariety: TextView
        val txtDistance: TextView
        val txtNumerOfVine: TextView
        val checkboxWork: Button
        val checkboxFertilizer: Button

        override fun toString(): String {
            return super.toString() + " '"
        }

        init {
            txtArea = mView.findViewById<View>(R.id.txtArea) as TextView
            txtVariety = mView.findViewById<View>(R.id.txtVariety) as TextView
            txtDistance = mView.findViewById<View>(R.id.txtDistance) as TextView
            txtNumerOfVine = mView.findViewById<View>(R.id.txtNumerOfVine) as TextView
            checkboxFertilizer = mView.findViewById<View>(R.id.checkboxFertilizer) as Button
            checkboxWork = mView.findViewById<View>(R.id.checkboxWork) as Button

        }
    }

}