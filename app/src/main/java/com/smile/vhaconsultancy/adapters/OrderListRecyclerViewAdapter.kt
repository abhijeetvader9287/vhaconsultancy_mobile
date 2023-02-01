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
    var databaseOrderListReference: DatabaseReference? = null
    var userPhoneNumber: String? = ""

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        database = FirebaseDatabase.getInstance()
        userPhoneNumber = SharedPref.Companion.getInstance(parent.context)?.getSharedPref(parent.context.getString(R.string.userPhoneNumber))

        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.orderlist_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val order = orders[position]
        databaseOrderListReference = database!!.getReference(holder.txtGrapeType.context.getString(R.string.user_list)).child(userPhoneNumber!!).child(holder.txtGrapeType.context.getString(R.string.order_list)).child(order.orderKey.toString().toString())
        databaseOrderListReference!!.child("orderKey").setValue(order.orderKey.toString())
        holder.txtGrapeType.text = order.grape_type.toString() + ""
        holder.txtPackingType.text = order.packing_type + ""
        holder.txtWeight.text = order.weight.toString() + ""
        holder.txtQuantity.text = order.quantity.toString() + ""


    }

    override fun getItemCount(): Int {
        return orders.size
    }

    inner class ViewHolder(val mView: View) : RecyclerView.ViewHolder(mView) {
       /* var grape_type: String = ""
        var packing_type: String = ""
        var weight: String = ""
        var quantity: String = ""*/

        val txtGrapeType: TextView
        val txtPackingType: TextView
        val txtWeight: TextView
        val txtQuantity: TextView
        val btn_OrderStatus: Button
        override fun toString(): String {
            return super.toString() + " '"
        }

        init {
            txtGrapeType = mView.findViewById<View>(R.id.txtGrapeType) as TextView
            txtPackingType = mView.findViewById<View>(R.id.txtPackingType) as TextView
            txtWeight = mView.findViewById<View>(R.id.txtWeight) as TextView
            txtQuantity = mView.findViewById<View>(R.id.txtQuantity) as TextView
            btn_OrderStatus = mView.findViewById<View>(R.id.btn_OrderStatus) as Button
        }
    }

}