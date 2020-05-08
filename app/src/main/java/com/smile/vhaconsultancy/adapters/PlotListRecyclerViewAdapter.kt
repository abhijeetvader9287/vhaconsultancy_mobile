package com.smile.vhaconsultancy.adapters

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.smile.vhaconsultancy.R
import com.smile.vhaconsultancy.models.Plot
import com.smile.vhaconsultancy.payment.PaymentMainActivity
import com.smile.vhaconsultancy.utilities.SharedPref
import java.util.*

class PlotListRecyclerViewAdapter(private val plots: ArrayList<Plot>) : RecyclerView.Adapter<PlotListRecyclerViewAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.plotlist_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val plot = plots[position]
        holder.txtArea.text = plot.area.toString() + ""
        holder.txtVariety.text = plot.variety + ""
        holder.txtDistance.text = plot.distance.toString() + ""
        holder.txtNumerOfVine.text = plot.numberOfVine.toString() + ""
        holder.mView.setOnClickListener { v: View? ->
            val todaysDate = Date()
            val month = todaysDate.month + 1
            if (month >= 3 && month <= 5) {
                val aprilTransactioRef = plot?.aprilTransactionRef
                if (aprilTransactioRef!!.isEmpty()) {
                    val intent: Intent? = Intent(v?.context, PaymentMainActivity::class.java)
                    SharedPref.Companion.getInstance(v?.context)?.putSharedPrefString(v?.context?.getString(R.string.month), v?.context?.getString(R.string.april))
                    SharedPref.Companion.getInstance(v?.context)?.putSharedPrefString(v?.context?.getString(R.string._area_in_acre), plot.area.toString());
                    SharedPref.Companion.getInstance(v?.context)?.putSharedPrefString(v?.context?.getString(R.string.plot_key), plot.plotKey.toString());
                    //SharedPref.Companion.getInstance(this@SplashscreenActivity)?.putSharedPrefString(getString(R.string.userPhoneNumber), currentUser.getPhoneNumber())
                    v?.context?.startActivity(intent)
                } else {
                }
            } else if (month >= 9 && month <= 10) {
            }
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