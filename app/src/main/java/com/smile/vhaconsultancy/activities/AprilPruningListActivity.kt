package com.smile.vhaconsultancy.activities

import android.app.ProgressDialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.*
import com.smile.vhaconsultancy.R
import com.smile.vhaconsultancy.adapters.PruningListAprilRecyclerViewAdapter
import com.smile.vhaconsultancy.models.AprilPruningModel
import com.smile.vhaconsultancy.utilities.SharedPref
import kotlinx.android.synthetic.main.activity_april_pruning_list.*
import kotlinx.android.synthetic.main.content_april_pruning_list.*
import java.text.SimpleDateFormat
import java.util.*


class AprilPruningListActivity : AppCompatActivity() {
    var userUid: String? = ""
    var plot_key: String? = ""
    var userPhoneNumber: String? = ""
    var database: FirebaseDatabase? = null
    var databasePlotListReference: DatabaseReference? = null
    lateinit var dialog: ProgressDialog
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_april_pruning_list)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(true)
        dialog = ProgressDialog(this)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.isIndeterminate = true
        dialog.setCancelable(false)
        dialog.show()
        dialog.setContentView(R.layout.progress_layout)


        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        userUid = SharedPref.Companion.getInstance(this@AprilPruningListActivity)?.getSharedPref(getString(R.string.userUid))
        userPhoneNumber = SharedPref.Companion.getInstance(this@AprilPruningListActivity)?.getSharedPref(getString(R.string.userPhoneNumber))

        database = FirebaseDatabase.getInstance()
        plot_key = SharedPref.Companion.getInstance(this@AprilPruningListActivity)?.getSharedPref(getString(R.string.plot_key))

        databasePlotListReference = database!!.getReference(getString(R.string.user_list)).child(userPhoneNumber!!).child(getString(R.string.plot_list)).child(plot_key.toString()).child("april_pruning_list")


        databasePlotListReference!!.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                //clearing the previous artist list
                val plots: ArrayList<AprilPruningModel> = ArrayList<AprilPruningModel>()
                //iterating through all the nodes
                for (postSnapshot in dataSnapshot.children) {
                    //getting artist
                    val plot: AprilPruningModel? = postSnapshot.getValue(AprilPruningModel::class.java)
                    //adding artist to the list
                    plot?.let {


                        try {
                            val formatter = SimpleDateFormat("dd-MMM-yyyy", Locale.US)

                            val datePruning = formatter.parse(it.strDate)
                            val dateFrom = addDays(-3)
                            val dateTo = addDays(6)
                            if (datePruning.after(dateFrom)) {
                                if (datePruning.before(dateTo)) {
                                    plots.add(it)
                                }
                            }
                        } catch (e: Exception) {
                        }


                    }
                }
                val pruningListRecyclerViewAdapter = PruningListAprilRecyclerViewAdapter(plots)
                recyclerViewPruningList.layoutManager = LinearLayoutManager(this@AprilPruningListActivity)
                recyclerViewPruningList.adapter = pruningListRecyclerViewAdapter
                dialog.dismiss()
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })


    }

    fun addDays(days: Int): Date {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DAY_OF_YEAR, days)

        return calendar.time
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

}
