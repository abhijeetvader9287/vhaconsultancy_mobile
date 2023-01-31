package com.smile.vhaconsultancy.activities

import android.app.ProgressDialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.google.firebase.database.*
import com.smile.vhaconsultancy.R
import com.smile.vhaconsultancy.adapters.OrderListRecyclerViewAdapter
import com.smile.vhaconsultancy.adapters.PlotListRecyclerViewAdapter
import com.smile.vhaconsultancy.databinding.ActivityOrderListBinding
import com.smile.vhaconsultancy.databinding.ActivityPhoneAuthBinding
import com.smile.vhaconsultancy.databinding.ActivityPlotListBinding
import com.smile.vhaconsultancy.models.Order
import com.smile.vhaconsultancy.models.Plot
import com.smile.vhaconsultancy.utilities.SharedPref
import com.smile.vhaconsultancy.utilities.Utils
import java.util.*


class OrderListActivity : AppCompatActivity() {
    var userUid: String? = ""
    var userPhoneNumber: String? = ""
    var database: FirebaseDatabase? = null
    var databasePlotListReference: DatabaseReference? = null
    lateinit var dialog: ProgressDialog
    private lateinit var binding: ActivityOrderListBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOrderListBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
       // setContentView(R.layout.activity_plot_list)
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(true)
        Utils.setLocal(this)

        dialog = ProgressDialog(this);
        dialog.getWindow()?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT));
        dialog.setIndeterminate(true);
        dialog.setCancelable(false);
        dialog.show();
        dialog.setContentView(R.layout.progress_layout);


        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        userUid = SharedPref.Companion.getInstance(this@OrderListActivity)?.getSharedPref(getString(R.string.userUid));
        userPhoneNumber = SharedPref.Companion.getInstance(this@OrderListActivity)?.getSharedPref(getString(R.string.userPhoneNumber))

        database = FirebaseDatabase.getInstance()
        databasePlotListReference = database!!.getReference(getString(R.string.user_list)).child(userPhoneNumber!!).child(getString(R.string.order_list))


        databasePlotListReference!!.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                //clearing the previous artist list
                val plots: ArrayList<Order> = ArrayList<Order>()
                //iterating through all the nodes
                for (postSnapshot in dataSnapshot.children) {
                    //getting artist
                    val plot: Order? = postSnapshot.getValue(Order::class.java)
                    plot?.orderKey = postSnapshot.key;
                    //adding artist to the list
                    plot?.let { plots.add(it) }
                }
                val orderListRecyclerViewAdapter = OrderListRecyclerViewAdapter(plots)
                binding.contentOrderList.     recyclerViewOrderList.setLayoutManager(GridLayoutManager(this@OrderListActivity, 2))
                binding.contentOrderList.     recyclerViewOrderList.setAdapter(orderListRecyclerViewAdapter)
                dialog.dismiss()
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })


    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.getItemId()) {
            android.R.id.home -> {
                onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

}
