package com.smile.vhaconsultancy.activities

import android.app.ProgressDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.navigation.ui.AppBarConfiguration
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.smile.vhaconsultancy.BuildConfig
import com.smile.vhaconsultancy.R
import com.smile.vhaconsultancy.databinding.ActivityCreateCartBinding
import com.smile.vhaconsultancy.databinding.ActivityMainBinding
import com.smile.vhaconsultancy.models.UserProfile
import com.smile.vhaconsultancy.utilities.SharedPref
import com.smile.vhaconsultancy.utilities.Utils


class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private var mAuth: FirebaseAuth? = null
    var databaseProfileReference: DatabaseReference? = null
    var app_link_Reference: DatabaseReference? = null
    var userUid: String? = ""
    var userPhoneNumber: String? = ""
    var database: FirebaseDatabase? = null
    var versionCodeReference: DatabaseReference? = null
    var rateReference: DatabaseReference? = null
    var rate_nv_Reference: DatabaseReference? = null
    lateinit var dialog: ProgressDialog

    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
       // setContentView(R.layout.activity_main)
   //     val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(binding.appBarMain.toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(true)
        Utils.setLocal(this)

        if(!SharedPref.getInstance(this@MainActivity)?.getSharedPrefBool("terms")!!)
       {
           val i = Intent(this, TermsAndConditionsActivity::class.java)
           this.startActivity(i)
       }

        binding.appBarMain.contentMain.actionPlaceOrder.setOnClickListener {
    val i = Intent(this, CreateCartActivity::class.java)
    this.startActivity(i)
}



        dialog = ProgressDialog(this)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.isIndeterminate = true
        dialog.setCancelable(false)
        dialog.show()
        dialog.setContentView(R.layout.progress_layout)
        binding.appBarMain.contentMain.  changeLang.setOnClickListener {
            val builder =    AlertDialog.Builder(this@MainActivity)
            builder?.setTitle( getString(R.string.changeLang))
            builder?.setNegativeButton(getString(R.string.marathi)) { dialog, which ->
                SharedPref.getInstance(this@MainActivity)?.putSharedPrefString(getString(R.string.app_language),"mr")
                Utils.setLocal(this@MainActivity)
                finishAffinity()

                val i = Intent(this, SplashscreenActivity::class.java)
                i.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                this.startActivity(i)
            }

            builder?.setPositiveButton(getString(R.string.english)) { dialog, which ->

                SharedPref.getInstance(this@MainActivity)?.putSharedPrefString(getString(R.string.app_language),"en")
                Utils.setLocal(this@MainActivity)
                val i = Intent(this, SplashscreenActivity::class.java)
                i.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                this.startActivity(i)
            }
            var alertDialog=       builder?.show()

            alertDialog?.getButton(AlertDialog.BUTTON_POSITIVE)?.setTypeface(Typeface.DEFAULT, Typeface.BOLD)
            alertDialog?.getButton(AlertDialog.BUTTON_NEGATIVE)?.setTypeface(Typeface.DEFAULT, Typeface.BOLD)
            alertDialog?.getButton(AlertDialog.BUTTON_POSITIVE)?.textSize=20f
            alertDialog?.getButton(AlertDialog.BUTTON_NEGATIVE)?.textSize=20f
        }
        binding.appBarMain.contentMain.    txtTermsAndCond.setOnClickListener {
            val i = Intent(this, TermsAndConditionsActivity::class.java)
            this.startActivity(i)
        }
        binding.appBarMain.contentMain.     actionProfile.setOnClickListener {
            val i = Intent(this, ProfileActivity::class.java)
            this.startActivity(i)
            true
        }
        binding.appBarMain.contentMain.   actionAddPlot.setOnClickListener {
            val i = Intent(this, AddPlotActivity::class.java)
            this.startActivity(i)
            true
        }
        binding.appBarMain.contentMain.   actionListPlot.setOnClickListener {
            val i = Intent(this, PlotListActivity::class.java)
            this.startActivity(i)
            true
        }
        binding.appBarMain.contentMain.      actionLogout.setOnClickListener {
            mAuth?.signOut()
            finishAffinity()

            val i = Intent(this, PhoneAuthActivity::class.java)
            i.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            this.startActivity(i)
            true
        }



        mAuth = FirebaseAuth.getInstance()

        database = FirebaseDatabase.getInstance()
        versionCodeReference = database!!.getReference(getString(R.string.versionCode))
        rateReference = database!!.getReference(getString(R.string.rate))
        rate_nv_Reference = database!!.getReference(getString(R.string.rate_nv))
        app_link_Reference = database!!.getReference(getString(R.string.app_link))

        userUid = SharedPref.Companion.getInstance(this@MainActivity)?.getSharedPref(getString(R.string.userUid))
        userPhoneNumber = SharedPref.Companion.getInstance(this@MainActivity)?.getSharedPref(getString(R.string.userPhoneNumber))

        databaseProfileReference = database!!.getReference(getString(R.string.user_list)).child(userPhoneNumber!!).child(getString(R.string.user_profile))
      /*  databaseProfileReference = database!!.getReference(getString(R.string.user_list))
        databaseProfileReference!!.removeValue()*/
        rateReference!!.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                TODO("Not yet implemented")
            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val rate: Double = dataSnapshot.value as Double
                SharedPref.getInstance(this@MainActivity)?.putSharedPrefFloat(getString(R.string.rate), rate.toFloat())

            }

        })
        rate_nv_Reference!!.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                TODO("Not yet implemented")
            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val rate: Double = dataSnapshot.value as Double
                SharedPref.getInstance(this@MainActivity)?.putSharedPrefFloat(getString(R.string.rate_nv), rate.toFloat())

            }

        })
        databaseProfileReference!!.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                val userProfile: UserProfile? = dataSnapshot.getValue(UserProfile::class.java)
                binding.appBarMain.contentMain.  editTextMobileNumber.text = userProfile?.mobileNumber
                binding.appBarMain.contentMain.    editTextName.text = userProfile?.name
                SharedPref.getInstance(this@MainActivity)?.putSharedPrefString(getString(R.string._name), userProfile?.name)
                dialog.dismiss()


            }

            override fun onCancelled(databaseError: DatabaseError) {
                dialog.dismiss()


            }
        })
        binding.appBarMain.contentMain.  actionWhatsapp.setOnClickListener { openWhatsApp() }
        versionCodeReference!!.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                TODO("Not yet implemented")
            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {

                val versionCode: Long = BuildConfig.VERSION_CODE.toLong()
                val newVersionCode: Long = dataSnapshot.value as Long
                if (versionCode != newVersionCode) {

                    app_link_Reference!!.addValueEventListener(object : ValueEventListener {
                        override fun onCancelled(p0: DatabaseError) {
                            TODO("Not yet implemented")
                        }

                        override fun onDataChange(dataSnapshot: DataSnapshot) {
                            val appLink: String = dataSnapshot.value as String
                            val builder = AlertDialog.Builder(this@MainActivity)
                            //set title for alert dialog
                          //  builder.setTitle(R.string.Warning)
                            //set message for alert dialog
                            builder.setMessage(R.string.Please_download_new_app)
                            builder.setIcon(android.R.drawable.ic_dialog_alert)

                            //performing positive action
                            builder.setPositiveButton(getString(R.string.Ok)) { dialogInterface, which ->
                                val openURL = Intent(android.content.Intent.ACTION_VIEW)
                                openURL.data = Uri.parse(appLink)
                                startActivity(openURL)
                            }


                            // Create the AlertDialog
                            val alertDialog: AlertDialog = builder.create()
                            // Set other dialog properties
                            alertDialog.setCancelable(false)
                            alertDialog.show()
                        }

                    })

                    // Toast.makeText(this@MainActivity, getString(R.string.Please_download_new_app), Toast.LENGTH_LONG).show()

                }

            }

        })
    }

    fun openWhatsApp() {
        var pm: PackageManager = getPackageManager()
        try {


            var toNumber: String = getString(R.string.phoneNumber) // Replace with mobile phone number without +Sign or leading zeros, but with country code.
            //Suppose your country is India and your phone number is “xxxxxxxxxx”, then you need to send “91xxxxxxxxxx”.


            var sendIntent = Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:" + "" + toNumber))
            sendIntent.setPackage("com.whatsapp");
            startActivity(sendIntent);
        } catch (e: Exception) {
            e.printStackTrace();
            Toast.makeText(this@MainActivity, "it may be you dont have whats app", Toast.LENGTH_LONG).show();

        }
    }

}
