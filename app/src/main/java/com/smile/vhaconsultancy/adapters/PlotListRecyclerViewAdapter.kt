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
import com.smile.vhaconsultancy.models.Plot
import com.smile.vhaconsultancy.payment.PaymentAprilActivity
import com.smile.vhaconsultancy.payment.PaymentOctoberActivity
import com.smile.vhaconsultancy.utilities.SharedPref
import java.util.*

class PlotListRecyclerViewAdapter(private val plots: ArrayList<Plot>) : RecyclerView.Adapter<PlotListRecyclerViewAdapter.ViewHolder>() {
    var database: FirebaseDatabase? = null
    var databasePlotListReference: DatabaseReference? = null
    var userPhoneNumber: String? = ""

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        database = FirebaseDatabase.getInstance()
        userPhoneNumber = SharedPref.Companion.getInstance(parent.context)?.getSharedPref(parent.context.getString(R.string.userPhoneNumber))

        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.plotlist_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val plot = plots[position]
        databasePlotListReference = database!!.getReference(holder.txtArea.context.getString(R.string.user_list)).child(userPhoneNumber!!).child(holder.txtArea.context.getString(R.string.plot_list)).child(plot.plotKey.toString().toString())
        databasePlotListReference!!.child("plotKey").setValue(plot.plotKey.toString())
        holder.txtArea.text = plot.area.toString() + ""
        holder.txtVariety.text = plot.variety + ""
        holder.txtDistance.text = plot.distance.toString() + ""
        holder.txtNumerOfVine.text = plot.numberOfVine.toString() + ""
        val aprilTransactioRef = plot?.aprilTransactionRef
        val octoberTransactionRef = plot?.octoberTransactionRef
        if (aprilTransactioRef!!.isEmpty()) {
            if (octoberTransactionRef!!.isEmpty()) {
                holder.btn_deletePlot.visibility = View.VISIBLE
                holder.btn_deletePlot.setOnClickListener {
                    val builder = AlertDialog.Builder(holder.btn_deletePlot.context)
                    //set title for alert dialog
                    builder.setTitle(R.string.Warning)
                    //set message for alert dialog
                    builder.setMessage(R.string.you_want_to_delete)
                    builder.setIcon(android.R.drawable.ic_dialog_alert)

                    //performing positive action
                    builder.setPositiveButton(holder.btn_deletePlot.context.getString(R.string.Yes)) { dialogInterface, which ->
                        databasePlotListReference!!.removeValue()
                    }
                    //performing cancel action
                    builder.setNeutralButton(holder.btn_deletePlot.context.getString(R.string.Cancel)) { dialogInterface, which ->

                    }

                    // Create the AlertDialog
                    val alertDialog: AlertDialog = builder.create()
                    // Set other dialog properties
                    alertDialog.setCancelable(false)
                    alertDialog.show()

                }
            } else {
                holder.btn_deletePlot.visibility = View.GONE

            }
        } else {
            holder.btn_deletePlot.visibility = View.GONE

        }
        holder.mView.setOnClickListener { v: View? ->
          /*  val todaysDate = Date()
            val month = todaysDate.month + 1
            if (month >= 3 && month <= 8) {//3,8
                val aprilTransactioRef = plot?.aprilTransactionRef
                if (aprilTransactioRef!!.isEmpty()) {
                    val intent: Intent? = Intent(v?.context, PaymentAprilActivity::class.java)
                    SharedPref.Companion.getInstance(v?.context)?.putSharedPrefString(v?.context?.getString(R.string.month), v?.context?.getString(R.string.april))
                    SharedPref.Companion.getInstance(v?.context)?.putSharedPrefString(v?.context?.getString(R.string._area_in_acre), plot.area.toString());
                    SharedPref.Companion.getInstance(v?.context)?.putSharedPrefString(v?.context?.getString(R.string.plot_key), plot.plotKey.toString());
                    //SharedPref.Companion.getInstance(this@SplashscreenActivity)?.putSharedPrefString(getString(R.string.userPhoneNumber), currentUser.getPhoneNumber())
                    v?.context?.startActivity(intent)
                } else {
                    val intent: Intent? = Intent(v?.context, AprilPruningListActivity::class.java)
                    SharedPref.Companion.getInstance(v?.context)?.putSharedPrefString(v?.context?.getString(R.string.month), v?.context?.getString(R.string.april))
                    SharedPref.Companion.getInstance(v?.context)?.putSharedPrefString(v?.context?.getString(R.string._area_in_acre), plot.area.toString());
                    SharedPref.Companion.getInstance(v?.context)?.putSharedPrefString(v?.context?.getString(R.string.plot_key), plot.plotKey.toString());
                    //SharedPref.Companion.getInstance(this@SplashscreenActivity)?.putSharedPrefString(getString(R.string.userPhoneNumber), currentUser.getPhoneNumber())
                    v?.context?.startActivity(intent)
                }
            }
            else if (month >= 9 && month <= 12) {//9,12
                val octoberTransactionRef = plot?.octoberTransactionRef
                if (octoberTransactionRef!!.isEmpty()) {
                    val intent: Intent? = Intent(v?.context, PaymentOctoberActivity::class.java)
                    SharedPref.Companion.getInstance(v?.context)?.putSharedPrefString(v?.context?.getString(R.string.month), v?.context?.getString(R.string.october))
                    SharedPref.Companion.getInstance(v?.context)?.putSharedPrefString(v?.context?.getString(R.string._area_in_acre), plot.area.toString());
                    SharedPref.Companion.getInstance(v?.context)?.putSharedPrefString(v?.context?.getString(R.string.plot_key), plot.plotKey.toString());
                    //SharedPref.Companion.getInstance(this@SplashscreenActivity)?.putSharedPrefString(getString(R.string.userPhoneNumber), currentUser.getPhoneNumber())
                    v?.context?.startActivity(intent)
                } else {
                    val intent: Intent? = Intent(v?.context, OctoberPruningListActivity::class.java)
                    SharedPref.Companion.getInstance(v?.context)?.putSharedPrefString(v?.context?.getString(R.string.month), v?.context?.getString(R.string.october))
                    SharedPref.Companion.getInstance(v?.context)?.putSharedPrefString(v?.context?.getString(R.string._area_in_acre), plot.area.toString());
                    SharedPref.Companion.getInstance(v?.context)?.putSharedPrefString(v?.context?.getString(R.string.plot_key), plot.plotKey.toString());
                    //SharedPref.Companion.getInstance(this@SplashscreenActivity)?.putSharedPrefString(getString(R.string.userPhoneNumber), currentUser.getPhoneNumber())
                    v?.context?.startActivity(intent)
                }
            }
            else if (month >= 1 && month <= 2) {//1,2
                val octoberTransactionRef = plot?.octoberTransactionRef
                if (octoberTransactionRef!!.isEmpty()) {
                    val intent: Intent? = Intent(v?.context, PaymentOctoberActivity::class.java)
                    SharedPref.Companion.getInstance(v?.context)?.putSharedPrefString(v?.context?.getString(R.string.month), v?.context?.getString(R.string.october))
                    SharedPref.Companion.getInstance(v?.context)?.putSharedPrefString(v?.context?.getString(R.string._area_in_acre), plot.area.toString());
                    SharedPref.Companion.getInstance(v?.context)?.putSharedPrefString(v?.context?.getString(R.string.plot_key), plot.plotKey.toString());
                    //SharedPref.Companion.getInstance(this@SplashscreenActivity)?.putSharedPrefString(getString(R.string.userPhoneNumber), currentUser.getPhoneNumber())
                    v?.context?.startActivity(intent)
                } else {
                    val intent: Intent? = Intent(v?.context, OctoberPruningListActivity::class.java)
                    SharedPref.Companion.getInstance(v?.context)?.putSharedPrefString(v?.context?.getString(R.string.month), v?.context?.getString(R.string.october))
                    SharedPref.Companion.getInstance(v?.context)?.putSharedPrefString(v?.context?.getString(R.string._area_in_acre), plot.area.toString());
                    SharedPref.Companion.getInstance(v?.context)?.putSharedPrefString(v?.context?.getString(R.string.plot_key), plot.plotKey.toString());
                    //SharedPref.Companion.getInstance(this@SplashscreenActivity)?.putSharedPrefString(getString(R.string.userPhoneNumber), currentUser.getPhoneNumber())
                    v?.context?.startActivity(intent)
                }
            }
*/

            val builder = v?.context?.let { AlertDialog.Builder(it) }
            builder?.setTitle("कामाचे नियोजन निवडा")

//builder.setPositiveButton("OK", DialogInterface.OnClickListener(function = x))

            builder?.setNegativeButton("एप्रिल छाटणी") { dialog, which ->
                if (aprilTransactioRef!!.isEmpty()) {
                    val intent: Intent? = Intent(v?.context, PaymentAprilActivity::class.java)
                    SharedPref.Companion.getInstance(v?.context)?.putSharedPrefString(v?.context?.getString(R.string.month), v?.context?.getString(R.string.april))
                    SharedPref.Companion.getInstance(v?.context)?.putSharedPrefString(v?.context?.getString(R.string._area_in_acre), plot.area.toString());
                    SharedPref.Companion.getInstance(v?.context)?.putSharedPrefString(v?.context?.getString(R.string.plot_key), plot.plotKey.toString());
                    //SharedPref.Companion.getInstance(this@SplashscreenActivity)?.putSharedPrefString(getString(R.string.userPhoneNumber), currentUser.getPhoneNumber())
                    v?.context?.startActivity(intent)
                } else {
                    val intent: Intent? = Intent(v?.context, AprilPruningListActivity::class.java)
                    SharedPref.Companion.getInstance(v?.context)?.putSharedPrefString(v?.context?.getString(R.string.month), v?.context?.getString(R.string.april))
                    SharedPref.Companion.getInstance(v?.context)?.putSharedPrefString(v?.context?.getString(R.string._area_in_acre), plot.area.toString());
                    SharedPref.Companion.getInstance(v?.context)?.putSharedPrefString(v?.context?.getString(R.string.plot_key), plot.plotKey.toString());
                    //SharedPref.Companion.getInstance(this@SplashscreenActivity)?.putSharedPrefString(getString(R.string.userPhoneNumber), currentUser.getPhoneNumber())
                    v?.context?.startActivity(intent)
                }
            }

            builder?.setPositiveButton("ऑक्टोबर छाटणी") { dialog, which ->

                val octoberTransactionRef = plot?.octoberTransactionRef
                if (octoberTransactionRef!!.isEmpty()) {
                    val intent: Intent? = Intent(v?.context, PaymentOctoberActivity::class.java)
                    SharedPref.Companion.getInstance(v?.context)?.putSharedPrefString(v?.context?.getString(R.string.month), v?.context?.getString(R.string.october))
                    SharedPref.Companion.getInstance(v?.context)?.putSharedPrefString(v?.context?.getString(R.string._area_in_acre), plot.area.toString());
                    SharedPref.Companion.getInstance(v?.context)?.putSharedPrefString(v?.context?.getString(R.string.plot_key), plot.plotKey.toString());
                    //SharedPref.Companion.getInstance(this@SplashscreenActivity)?.putSharedPrefString(getString(R.string.userPhoneNumber), currentUser.getPhoneNumber())
                    v?.context?.startActivity(intent)
                } else {
                    val intent: Intent? = Intent(v?.context, OctoberPruningListActivity::class.java)
                    SharedPref.Companion.getInstance(v?.context)?.putSharedPrefString(v?.context?.getString(R.string.month), v?.context?.getString(R.string.october))
                    SharedPref.Companion.getInstance(v?.context)?.putSharedPrefString(v?.context?.getString(R.string._area_in_acre), plot.area.toString());
                    SharedPref.Companion.getInstance(v?.context)?.putSharedPrefString(v?.context?.getString(R.string.plot_key), plot.plotKey.toString());
                    //SharedPref.Companion.getInstance(this@SplashscreenActivity)?.putSharedPrefString(getString(R.string.userPhoneNumber), currentUser.getPhoneNumber())
                    v?.context?.startActivity(intent)
                }
            }
/*
            builder?.setNeutralButton("बेरी सेटिंग स्टेज") { dialog, which ->
                val octoberTransactionRef = plot?.octoberTransactionRef
                if (octoberTransactionRef!!.isEmpty()) {
                    val intent: Intent? = Intent(v?.context, PaymentOctoberActivity::class.java)
                    SharedPref.Companion.getInstance(v?.context)?.putSharedPrefString(v?.context?.getString(R.string.month), v?.context?.getString(R.string.october))
                    SharedPref.Companion.getInstance(v?.context)?.putSharedPrefString(v?.context?.getString(R.string._area_in_acre), plot.area.toString());
                    SharedPref.Companion.getInstance(v?.context)?.putSharedPrefString(v?.context?.getString(R.string.plot_key), plot.plotKey.toString());
                    //SharedPref.Companion.getInstance(this@SplashscreenActivity)?.putSharedPrefString(getString(R.string.userPhoneNumber), currentUser.getPhoneNumber())
                    v?.context?.startActivity(intent)
                } else {
                    val intent: Intent? = Intent(v?.context, OctoberPruningListActivity::class.java)
                    SharedPref.Companion.getInstance(v?.context)?.putSharedPrefString(v?.context?.getString(R.string.month), v?.context?.getString(R.string.october))
                    SharedPref.Companion.getInstance(v?.context)?.putSharedPrefString(v?.context?.getString(R.string._area_in_acre), plot.area.toString());
                    SharedPref.Companion.getInstance(v?.context)?.putSharedPrefString(v?.context?.getString(R.string.plot_key), plot.plotKey.toString());
                    //SharedPref.Companion.getInstance(this@SplashscreenActivity)?.putSharedPrefString(getString(R.string.userPhoneNumber), currentUser.getPhoneNumber())
                    v?.context?.startActivity(intent)
                }
            }*/
     var alertDialog=       builder?.show()

            alertDialog?.getButton(AlertDialog.BUTTON_POSITIVE)?.setTypeface(Typeface.DEFAULT, Typeface.BOLD)
            alertDialog?.getButton(AlertDialog.BUTTON_NEGATIVE)?.setTypeface(Typeface.DEFAULT, Typeface.BOLD)
            alertDialog?.getButton(AlertDialog.BUTTON_POSITIVE)?.textSize=20f
            alertDialog?.getButton(AlertDialog.BUTTON_NEGATIVE)?.textSize=20f

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