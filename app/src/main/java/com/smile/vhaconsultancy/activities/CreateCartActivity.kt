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
import com.smile.vhaconsultancy.models.Order
import com.smile.vhaconsultancy.utilities.SharedPref
import com.smile.vhaconsultancy.utilities.Utils
import kotlinx.android.synthetic.main.activity_add_plot.*
import kotlinx.android.synthetic.main.activity_berryset_payment.*
import kotlinx.android.synthetic.main.content_add_plot.*
import kotlinx.android.synthetic.main.content_add_plot.btnSave
import kotlinx.android.synthetic.main.content_create_cart.*
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
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_cart)
        setSupportActionBar(toolbar)
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

        edit_distribution_agency_name.setText( SharedPref.Companion.getInstance(this@CreateCartActivity)?.getSharedPref(getString(R.string._name)).toString())
radio_group_packing.setOnCheckedChangeListener(
    RadioGroup.OnCheckedChangeListener { group, checkedId ->
        radioPacking = findViewById(checkedId)

        if(checkedId==btnBox.id)
        {
            boxCard.visibility= View.VISIBLE
            kretCard.visibility= View.GONE

        }else if(checkedId==btnKret.id){
            boxCard.visibility= View.GONE
            kretCard.visibility= View.VISIBLE
        }
    })
        radio_group_variety.setOnCheckedChangeListener(
    RadioGroup.OnCheckedChangeListener { group, checkedId ->
        radioVariety = findViewById(checkedId)


    })
        radio_group_kret_weight.setOnCheckedChangeListener(
            RadioGroup.OnCheckedChangeListener { group, checkedId ->
                radioWeight = findViewById(checkedId)


            })
        radio_group_box_weight.setOnCheckedChangeListener(
            RadioGroup.OnCheckedChangeListener { group, checkedId ->
                radioWeight = findViewById(checkedId)


            })
        btnSave.setOnClickListener {
           saveFun()


        }

    }

    fun saveFun() {
        val packageManager: PackageManager =  getPackageManager()
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
        }
        if (isValidFun()) {
            val builder = AlertDialog.Builder(this)
            //set title for alert dialog
            // builder.setTitle(R.string.Warning)
            //set message for alert dialog
            builder.setMessage(R.string.you_will_not_be_able_to_edit_again)
            builder.setIcon(android.R.drawable.ic_dialog_alert)

            //performing positive action
            builder.setPositiveButton(getString(R.string.Yes)) { dialogInterface, which ->
                val order: Order = Order()
                order.distribution_code = edit_distribution_agency_code.text.toString()
                order.distribution_name = edit_distribution_agency_name.text.toString()
                order.distribution_city = edit_distribution_agency_city.text.toString()
                order.distribution_mobile = edit_distribution_agency_mobile_number.text.toString()
                order.packing_type = radioPacking!!.text.toString()
                order.grape_type = radioVariety!!.text.toString()
                order.weight = radioWeight!!.text.toString()
                order.quantity = edit_number.text.toString()

                databaseReference!!.push().setValue(order).addOnCanceledListener {
                    dialog.dismiss();

                }
                    .addOnCompleteListener {

                        dialog.dismiss();

                    }.addOnFailureListener {
                        dialog.dismiss();


                        Toast.makeText(this@CreateCartActivity, getString(R.string.Plot_save_failed) + it.localizedMessage, Toast.LENGTH_LONG).show()

                    }.addOnSuccessListener {
                        dialog.dismiss();

                        Toast.makeText(this@CreateCartActivity, getString(R.string.Plot_save_successfully), Toast.LENGTH_LONG).show()

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
            dialog.show();
            dialog.setContentView(R.layout.progress_layout);

        }


    }

    fun isValidFun(): Boolean {
        var isValid = true
        if (edit_distribution_agency_name.text.toString().isNullOrEmpty()) {
            edit_distribution_agency_name.setError(getString(R.string.Field_should_not_be_empty))
            isValid = false

        }
        if (edit_distribution_agency_code.text.toString().isNullOrEmpty()) {
            edit_distribution_agency_code.setError(getString(R.string.Field_should_not_be_empty))

            isValid = false

        }
        if (edit_distribution_agency_city.text.toString().isNullOrEmpty()) {
            edit_distribution_agency_city.setError(getString(R.string.Field_should_not_be_empty))

            isValid = false

        }
        if (edit_distribution_agency_mobile_number.text.toString().isNullOrEmpty()) {
            edit_distribution_agency_mobile_number.setError(getString(R.string.Field_should_not_be_empty))

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
            if(radioPacking!!.id==btnBox.id)
            {
                if(radioWeight==null)
                {
                    isValid=false
                    Toast.makeText(this@CreateCartActivity, getString(R.string.Please_select_box_weight), Toast.LENGTH_LONG).show()
                }

            }else if(radioPacking!!.id==btnKret.id){
                if(radioWeight==null)
                {
                    isValid=false
                    Toast.makeText(this@CreateCartActivity, getString(R.string.Please_select_kret_weight), Toast.LENGTH_LONG).show()
                }
            }

        }
        if (edit_number.text.toString().isNullOrEmpty()) {
            edit_number.setError(getString(R.string.Field_should_not_be_empty))

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
