package com.smile.vhaconsultancy.adapters

import android.content.Intent
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.smile.vhaconsultancy.R
import com.smile.vhaconsultancy.activities.AprilPruningListActivity
import com.smile.vhaconsultancy.activities.OctoberPruningListActivity
import com.smile.vhaconsultancy.models.Order
import com.smile.vhaconsultancy.models.Plot
import com.smile.vhaconsultancy.payment.PaymentAprilActivity
import com.smile.vhaconsultancy.payment.PaymentOctoberActivity
import com.smile.vhaconsultancy.utilities.SharedPref
import java.util.*

class OrderListRecyclerViewAdapter(private val orders: ArrayList<Order>) : RecyclerView.Adapter<OrderListRecyclerViewAdapter.ViewHolder>() {
    var database: FirebaseDatabase? = null
    var databasePlotListReference: DatabaseReference? = null
    var userPhoneNumber: String? = ""

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        database = FirebaseDatabase.getInstance()
        userPhoneNumber = SharedPref.Companion.getInstance(parent.context)?.getSharedPref(parent.context.getString(R.string.userPhoneNumber))

        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.orderlist_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val plot = orders[position]
        databasePlotListReference = database!!.getReference(holder.txtArea.context.getString(R.string.user_list)).child(userPhoneNumber!!).child(holder.txtArea.context.getString(R.string.plot_list)).child(plot.orderKey.toString().toString())
        databasePlotListReference!!.child("orderKey").setValue(plot.orderKey.toString())
        holder.txtArea.text = plot.grape_type.toString() + ""
        holder.txtVariety.text = plot.grape_type + ""
        holder.txtDistance.text = plot.grape_type.toString() + ""
        holder.txtNumerOfVine.text = plot.grape_type.toString() + ""


    }

    override fun getItemCount(): Int {
        return orders.size
    }

    inner class ViewHolder(val mView: View) : RecyclerView.ViewHolder(mView) {
        val txtArea: TextView
        val txtVariety: TextView
        val txtDistance: TextView
        val txtNumerOfVine: TextView
        val btn_deletePlot: Button
        override fun toString(): String {
            return super.toString() + " '"
        }

        init {
            txtArea = mView.findViewById<View>(R.id.txtArea) as TextView
            txtVariety = mView.findViewById<View>(R.id.txtVariety) as TextView
            txtDistance = mView.findViewById<View>(R.id.txtDistance) as TextView
            txtNumerOfVine = mView.findViewById<View>(R.id.txtNumerOfVine) as TextView
            btn_deletePlot = mView.findViewById<View>(R.id.btn_deletePlot) as Button
        }
    }

}