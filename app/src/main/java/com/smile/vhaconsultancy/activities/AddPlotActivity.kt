package com.smile.vhaconsultancy.activities

import android.app.ProgressDialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.smile.vhaconsultancy.R
import com.smile.vhaconsultancy.models.Plot
import com.smile.vhaconsultancy.utilities.SharedPref
import kotlinx.android.synthetic.main.activity_add_plot.*
import kotlinx.android.synthetic.main.content_add_plot.*


class AddPlotActivity : AppCompatActivity() {
    var userUid: String? = ""
    var userPhoneNumber: String? = ""
    var database: FirebaseDatabase? = null
    var databaseReference: DatabaseReference? = null
    lateinit var dialog: ProgressDialog
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_plot)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(true)
        dialog = ProgressDialog(this);
        dialog.getWindow()?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT));
        dialog.setIndeterminate(true);
        dialog.setCancelable(false);



        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        userUid = SharedPref.Companion.getInstance(this@AddPlotActivity)?.getSharedPref(getString(R.string.userUid));
        userPhoneNumber = SharedPref.Companion.getInstance(this@AddPlotActivity)?.getSharedPref(getString(R.string.userPhoneNumber))

        database = FirebaseDatabase.getInstance()

        databaseReference = database!!.getReference(getString(R.string.user_list)).child(userPhoneNumber!!).child(getString(R.string.plot_list))





        btnSave.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            //set title for alert dialog
            builder.setTitle(R.string.Warning)
            //set message for alert dialog
            builder.setMessage(R.string.you_will_not_be_able_to_edit_again)
            builder.setIcon(android.R.drawable.ic_dialog_alert)

            //performing positive action
            builder.setPositiveButton(getString(R.string.Yes)) { dialogInterface, which ->
                saveFun();
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

    fun saveFun() {
        if (isValidFun()) {
            dialog.show();
            dialog.setContentView(R.layout.progress_layout);
            val plot: Plot = Plot()
            plot.area = editEnterArea.text.toString().toDouble()
            plot.variety = editTextVariety.text.toString()
            plot.numberOfVine = editTextNumberOfVine.text.toString().toInt()
            plot.distance = editTextDistance.text.toString().toDouble()
            databaseReference!!.push().setValue(plot).addOnCanceledListener {
                dialog.dismiss();

            }
                    .addOnCompleteListener {

                        dialog.dismiss();

                    }.addOnFailureListener {
                        dialog.dismiss();
                        editEnterArea.setText("")
                        editTextVariety.setText("")
                        editTextNumberOfVine.setText("")
                        editTextDistance.setText("")

                        Toast.makeText(this@AddPlotActivity, getString(R.string.Plot_save_failed) + it.localizedMessage, Toast.LENGTH_LONG).show()

                    }.addOnSuccessListener {
                        dialog.dismiss();
                        editEnterArea.setText("")
                        editTextVariety.setText("")
                        editTextNumberOfVine.setText("")
                        editTextDistance.setText("")
                        Toast.makeText(this@AddPlotActivity, getString(R.string.Plot_save_successfully), Toast.LENGTH_LONG).show()

                    }
        }
    }

    fun isValidFun(): Boolean {
        var isValid = true
        if (editEnterArea.text.toString().isNullOrEmpty()) {
            editEnterArea.setError(getString(R.string.Field_should_not_be_empty))
            isValid = false

        }
        if (editTextVariety.text.toString().isNullOrEmpty()) {
            editTextVariety.setError(getString(R.string.Field_should_not_be_empty))

            isValid = false

        }
        if (editTextDistance.text.toString().isNullOrEmpty()) {
            editTextDistance.setError(getString(R.string.Field_should_not_be_empty))

            isValid = false

        }
        if (editTextNumberOfVine.text.toString().isNullOrEmpty()) {
            editTextNumberOfVine.setError(getString(R.string.Field_should_not_be_empty))

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
