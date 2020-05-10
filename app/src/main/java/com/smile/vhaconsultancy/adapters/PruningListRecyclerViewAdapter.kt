package com.smile.vhaconsultancy.adapters

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.smile.vhaconsultancy.R
import com.smile.vhaconsultancy.models.AprilPruningModel
import com.smile.vhaconsultancy.models.Plot
import com.smile.vhaconsultancy.payment.PaymentMainActivity
import com.smile.vhaconsultancy.utilities.SharedPref
import java.util.*

class PruningListRecyclerViewAdapter(private val plots: ArrayList<AprilPruningModel>) : RecyclerView.Adapter<PruningListRecyclerViewAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.pruning_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val plot = plots[position]
        holder.txtArea.text = plot.srNo.toString() + ""
        holder.txtVariety.text = plot.strDate + ""
        holder.txtDistance.text = plot.work_spray.toString() + ""
        holder.txtNumerOfVine.text = plot.fertilizer.toString() + ""

    }

    override fun getItemCount(): Int {
        return plots.size
    }

    inner class ViewHolder(val mView: View) : RecyclerView.ViewHolder(mView) {
        val txtArea: TextView
        val txtVariety: TextView
        val txtDistance: TextView
        val txtNumerOfVine: TextView
        override fun toString(): String {
            return super.toString() + " '"
        }

        init {
            txtArea = mView.findViewById<View>(R.id.txtArea) as TextView
            txtVariety = mView.findViewById<View>(R.id.txtVariety) as TextView
            txtDistance = mView.findViewById<View>(R.id.txtDistance) as TextView
            txtNumerOfVine = mView.findViewById<View>(R.id.txtNumerOfVine) as TextView
        }
    }

}