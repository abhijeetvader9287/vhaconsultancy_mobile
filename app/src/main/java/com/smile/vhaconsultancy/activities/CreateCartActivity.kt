package com.smile.vhaconsultancy.activities

import android.app.ProgressDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.smile.vhaconsultancy.R
import com.smile.vhaconsultancy.databinding.ActivityAprilPruningListBinding
import com.smile.vhaconsultancy.databinding.ActivityCreateCartBinding
import com.smile.vhaconsultancy.models.Order
import com.smile.vhaconsultancy.utilities.SharedPref
import com.smile.vhaconsultancy.utilities.Utils
import java.net.URLEncoder



class CreateCartActivity : AppCompatActivity() {
    var userUid: String? = ""
    var userPhoneNumber: String? = ""
    var radioPacking: RadioButton?=null
    var radioVariety: RadioButton?=null
    var radioWeight: RadioButton?=null
    var database: FirebaseDatabase? = null
    var databaseReference: DatabaseReference? = null
    lateinit var dialog: ProgressDialog

    private lateinit var binding: ActivityCreateCartBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateCartBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        setSupportActionBar(binding.toolbar)
       /* setContentView(R.layout.activity_create_cart)
        setSupportActionBar(toolbar)*/
        supportActionBar?.setDisplayShowTitleEnabled(true)
        Utils.setLocal(this)

        dialog = ProgressDialog(this);
        dialog.getWindow()?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT));
        dialog.setIndeterminate(true);
        dialog.setCancelable(false);



        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        userUid = SharedPref.Companion.getInstance(this@CreateCartActivity)?.getSharedPref(getString(R.string.userUid));
        userPhoneNumber = SharedPref.Companion.getInstance(this@CreateCartActivity)?.getSharedPref(getString(R.string.userPhoneNumber))

        database = FirebaseDatabase.getInstance()

        databaseReference = database!!.getReference(getString(R.string.user_list)).child(userPhoneNumber!!).child(getString(R.string.order_list))

     binding.contentCreateCart.editDistributionAgencyName   .setText( SharedPref.Companion.getInstance(this@CreateCartActivity)?.getSharedPref(getString(R.string._name)).toString())
        binding.contentCreateCart.radioGroupPacking.setOnCheckedChangeListener(
    RadioGroup.OnCheckedChangeListener { group, checkedId ->
        radioPacking = findViewById(checkedId)

        if(checkedId== binding.contentCreateCart.btnBox.id)
        {
            binding.contentCreateCart.boxCard.visibility= View.VISIBLE
            binding.contentCreateCart. kretCard.visibility= View.GONE

        }else if(checkedId==binding.contentCreateCart.btnKret.id){
            binding.contentCreateCart.boxCard.visibility= View.GONE
            binding.contentCreateCart.kretCard.visibility= View.VISIBLE
        }
    })
        binding.contentCreateCart.radioGroupVariety.setOnCheckedChangeListener(
    RadioGroup.OnCheckedChangeListener { group, checkedId ->
        radioVariety = findViewById(checkedId)


    })
        binding.contentCreateCart.btnOrderList.setOnClickListener {
            val i = Intent(this,OrderListActivity::class.java)
            this.startActivity(i)
            true
        }
        binding.contentCreateCart.radioGroupKretWeight.setOnCheckedChangeListener(
            RadioGroup.OnCheckedChangeListener { group, checkedId ->
                radioWeight = findViewById(checkedId)


            })
        binding.contentCreateCart.  radioGroupBoxWeight.setOnCheckedChangeListener(
            RadioGroup.OnCheckedChangeListener { group, checkedId ->
                radioWeight = findViewById(checkedId)


            })
        binding.contentCreateCart.btnSave.setOnClickListener {
           saveFun()


        }

    }

    fun saveFun() {
     /*   val packageManager: PackageManager =  getPackageManager()
        val i = Intent(Intent.ACTION_VIEW)

        try {
            val url = "https://api.whatsapp.com/send?phone=9762764597" + "&text=" + URLEncoder.encode("Hi", "UTF-8")
            i.setPackage("com.whatsapp")
            i.setData(Uri.parse(url))
            if (i.resolveActivity(packageManager) != null) {
                 startActivity(i)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }*/
        if (isValidFun()) {
            val builder = AlertDialog.Builder(this)
            //set title for alert dialog
            // builder.setTitle(R.string.Warning)
            //set message for alert dialog
            builder.setMessage(R.string.you_will_not_be_able_to_edit_again)
            builder.setIcon(android.R.drawable.ic_dialog_alert)

            //performing positive action
            builder.setPositiveButton(getString(R.string.Yes)) { dialogInterface, which ->
                dialog.show();
                dialog.setContentView(R.layout.progress_layout);
                val order: Order = Order()
                order.distribution_code =   binding.contentCreateCart.editDistributionAgencyCode.text.toString()
                order.distribution_name =  binding.contentCreateCart. editDistributionAgencyName.text.toString()
                order.distribution_city =  binding.contentCreateCart. editDistributionAgencyCity.text.toString()
                order.distribution_mobile =  binding.contentCreateCart. editDistributionAgencyMobileNumber.text.toString()
                order.packing_type = radioPacking!!.text.toString()
                order.grape_type = radioVariety!!.text.toString()
                order.weight = radioWeight!!.text.toString()
                order.quantity =   binding.contentCreateCart.editNumber.text.toString()

                databaseReference!!.push().setValue(order).addOnCanceledListener {
                    dialog.dismiss();

                }
                    .addOnCompleteListener {

                        dialog.dismiss();

                    }.addOnFailureListener {
                        dialog.dismiss();


                        Toast.makeText(this@CreateCartActivity, getString(R.string.order_save_failed) + it.localizedMessage, Toast.LENGTH_LONG).show()

                    }.addOnSuccessListener {
                        dialog.dismiss();

                        Toast.makeText(this@CreateCartActivity, getString(R.string.order_save_successfully), Toast.LENGTH_LONG).show()

                    }
            }
            //performing cancel action
            builder.setNeutralButton(getString(R.string.Cancel)) { dialogInterface, which ->

            }

            // Create the AlertDialog
            val alertDialog: AlertDialog = builder.create()
            // Set other dialog properties
            alertDialog.setCancelable(false)
            alertDialog.show()


        }


    }

    fun isValidFun(): Boolean {
        var isValid = true
        if ( binding.contentCreateCart. editDistributionAgencyName.text.toString().isNullOrEmpty()) {
            binding.contentCreateCart.  editDistributionAgencyName.setError(getString(R.string.Field_should_not_be_empty))
            isValid = false

        }
        if ( binding.contentCreateCart. editDistributionAgencyCode.text.toString().isNullOrEmpty()) {
            binding.contentCreateCart.   editDistributionAgencyCode.setError(getString(R.string.Field_should_not_be_empty))

            isValid = false

        }
        if ( binding.contentCreateCart. editDistributionAgencyCity.text.toString().isNullOrEmpty()) {
            binding.contentCreateCart.    editDistributionAgencyCity.setError(getString(R.string.Field_should_not_be_empty))

            isValid = false

        }
        if (binding.contentCreateCart. editDistributionAgencyMobileNumber.text.toString().isNullOrEmpty()) {
            binding.contentCreateCart.   editDistributionAgencyMobileNumber.setError(getString(R.string.Field_should_not_be_empty))

            isValid = false

        }

        if(radioVariety==null)
        {
            isValid=false
            Toast.makeText(this@CreateCartActivity, getString(R.string.Please_select_grape_variety), Toast.LENGTH_LONG).show()
        }
        if(radioPacking==null)
        {
            isValid=false
            Toast.makeText(this@CreateCartActivity, getString(R.string.Please_select_packing), Toast.LENGTH_LONG).show()

        }else{
            if(radioPacking!!.id==binding.contentCreateCart. btnBox.id)
            {
                if(radioWeight==null)
                {
                    isValid=false
                    Toast.makeText(this@CreateCartActivity, getString(R.string.Please_select_box_weight), Toast.LENGTH_LONG).show()
                }

            }else if(radioPacking!!.id==binding.contentCreateCart. btnKret.id){
                if(radioWeight==null)
                {
                    isValid=false
                    Toast.makeText(this@CreateCartActivity, getString(R.string.Please_select_kret_weight), Toast.LENGTH_LONG).show()
                }
            }

        }
        if (binding.contentCreateCart. editNumber.text.toString().isNullOrEmpty()) {
            binding.contentCreateCart.  editNumber.setError(getString(R.string.Field_should_not_be_empty))

            isValid = false

        }

        return isValid
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
