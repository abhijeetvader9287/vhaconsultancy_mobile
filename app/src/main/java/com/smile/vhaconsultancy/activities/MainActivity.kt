package com.smile.vhaconsultancy.activities

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.navigation.ui.AppBarConfiguration
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.smile.vhaconsultancy.BuildConfig
import com.smile.vhaconsultancy.R
import com.smile.vhaconsultancy.models.UserProfile
import com.smile.vhaconsultancy.utilities.SharedPref
import kotlinx.android.synthetic.main.content_main.*


class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private var mAuth: FirebaseAuth? = null
    var databaseProfileReference: DatabaseReference? = null
    var userUid: String? = ""
    var userPhoneNumber: String? = ""
    var database: FirebaseDatabase? = null
    var versionCodeReference: DatabaseReference? = null
    var rateReference: DatabaseReference? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(true)



        action_profile.setOnClickListener{
            val i = Intent(this, ProfileActivity::class.java)
            this.startActivity(i)
            true
        }
         action_add_plot .setOnClickListener {
            val i = Intent(this, AddPlotActivity::class.java)
            this.startActivity(i)
            true
        }
        action_list_plot .setOnClickListener {
            val i = Intent(this, PlotListActivity::class.java)
            this.startActivity(i)
            true
        }
        action_logout .setOnClickListener {
            mAuth?.signOut()
            finishAffinity();

            val i = Intent(this, PhoneAuthActivity::class.java)
            i.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            this.startActivity(i)
            true
        }



        mAuth = FirebaseAuth.getInstance()

        database = FirebaseDatabase.getInstance()
        versionCodeReference = database!!.getReference(getString(R.string.versionCode));
        rateReference = database!!.getReference(getString(R.string.rate));
        userUid = SharedPref.Companion.getInstance(this@MainActivity)?.getSharedPref(getString(R.string.userUid));
        userPhoneNumber = SharedPref.Companion.getInstance(this@MainActivity)?.getSharedPref(getString(R.string.userPhoneNumber))

        databaseProfileReference = database!!.getReference(getString(R.string.user_list)).child(userPhoneNumber!!).child(getString(R.string.user_profile))

        rateReference!!.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                TODO("Not yet implemented")
            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val rate: Double = dataSnapshot.value as Double;
                SharedPref.getInstance(this@MainActivity)?.putSharedPrefFloat(getString(R.string.rate), rate.toFloat());

            }

        })
        databaseProfileReference!!.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                //clearing the previous artist list
                //iterating through all the nodes
                //getting artist
                val userProfile: UserProfile? = dataSnapshot.getValue(UserProfile::class.java)
                //adding artist to the list
                 editTextMobileNumber.setText(userProfile?.mobileNumber)
              //  editTextDistrict.setText(userProfile?.district)
                editTextName.setText(userProfile?.name)
                SharedPref.getInstance(this@MainActivity)?.putSharedPrefString(getString(R.string._name), userProfile?.name);

                // editTextTaluka.setText(userProfile?.taluka)
               // editTextVillage.setText(userProfile?.village)

            }

            override fun onCancelled(databaseError: DatabaseError) {


            }
        })
        versionCodeReference!!.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                TODO("Not yet implemented")
            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {

                val versionCode: Long = BuildConfig.VERSION_CODE.toLong();
                val newVersionCode: Long = dataSnapshot.value as Long;
                if (versionCode != newVersionCode) {

                    val builder = AlertDialog.Builder(this@MainActivity)
                    //set title for alert dialog
                    builder.setTitle(R.string.Warning)
                    //set message for alert dialog
                    builder.setMessage(R.string.Please_download_new_app)
                    builder.setIcon(android.R.drawable.ic_dialog_alert)

                    //performing positive action
                    builder.setPositiveButton( getString(R.string.Ok)) { dialogInterface, which ->

                    }


                    // Create the AlertDialog
                    val alertDialog: AlertDialog = builder.create()
                    // Set other dialog properties
                    alertDialog.setCancelable(false)
                    alertDialog.show()
                   // Toast.makeText(this@MainActivity, getString(R.string.Please_download_new_app), Toast.LENGTH_LONG).show()

                }

            }

        });
    }




}
