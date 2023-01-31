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
import com.smile.vhaconsultancy.databinding.ActivityAddPlotBinding
import com.smile.vhaconsultancy.models.Plot
import com.smile.vhaconsultancy.utilities.SharedPref
import com.smile.vhaconsultancy.utilities.Utils



class AddPlotActivity : AppCompatActivity() {
    var userUid: String? = ""
    var userPhoneNumber: String? = ""
    var database: FirebaseDatabase? = null
    var databaseReference: DatabaseReference? = null
    lateinit var dialog: ProgressDialog
    private lateinit var binding: ActivityAddPlotBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_add_plot)
        binding = ActivityAddPlotBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(true)
        Utils.setLocal(this)

        dialog = ProgressDialog(this);
        dialog.getWindow()?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT));
        dialog.setIndeterminate(true);
        dialog.setCancelable(false);



        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        userUid = SharedPref.Companion.getInstance(this@AddPlotActivity)?.getSharedPref(getString(R.string.userUid));
        userPhoneNumber = SharedPref.Companion.getInstance(this@AddPlotActivity)?.getSharedPref(getString(R.string.userPhoneNumber))

        database = FirebaseDatabase.getInstance()

        databaseReference = database!!.getReference(getString(R.string.user_list)).child(userPhoneNumber!!).child(getString(R.string.plot_list))





    binding.contentAddPlot .   btnSave.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            //set title for alert dialog
           // builder.setTitle(R.string.Warning)
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
            plot.area = binding.contentAddPlot . editEnterArea.text.toString().toDouble()
            plot.variety =binding.contentAddPlot .  editTextVariety.text.toString()
            plot.numberOfVine =binding.contentAddPlot .  editTextNumberOfVine.text.toString().toInt()
            plot.distance = binding.contentAddPlot . editTextDistance.text.toString().toDouble()
            databaseReference!!.push().setValue(plot).addOnCanceledListener {
                dialog.dismiss();

            }
                    .addOnCompleteListener {

                        dialog.dismiss();

                    }.addOnFailureListener {
                        dialog.dismiss();
                    binding.contentAddPlot .   editEnterArea.setText("")
                    binding.contentAddPlot .   editTextVariety.setText("")
                    binding.contentAddPlot .   editTextNumberOfVine.setText("")
                    binding.contentAddPlot .   editTextDistance.setText("")

                        Toast.makeText(this@AddPlotActivity, getString(R.string.Plot_save_failed) + it.localizedMessage, Toast.LENGTH_LONG).show()

                    }.addOnSuccessListener {
                        dialog.dismiss();
                    binding.contentAddPlot .     editEnterArea.setText("")
                    binding.contentAddPlot .     editTextVariety.setText("")
                    binding.contentAddPlot .     editTextNumberOfVine.setText("")
                    binding.contentAddPlot .     editTextDistance.setText("")
                        Toast.makeText(this@AddPlotActivity, getString(R.string.Plot_save_successfully), Toast.LENGTH_LONG).show()

                    }
        }
    }

    fun isValidFun(): Boolean {
        var isValid = true
        if (binding.contentAddPlot . editEnterArea.text.toString().isNullOrEmpty()) {
            binding.contentAddPlot . editEnterArea.setError(getString(R.string.Field_should_not_be_empty))
            isValid = false

        }
        if (binding.contentAddPlot . editTextVariety.text.toString().isNullOrEmpty()) {
            binding.contentAddPlot . editTextVariety.setError(getString(R.string.Field_should_not_be_empty))

            isValid = false

        }
        if (binding.contentAddPlot . editTextDistance.text.toString().isNullOrEmpty()) {
            binding.contentAddPlot . editTextDistance.setError(getString(R.string.Field_should_not_be_empty))

            isValid = false

        }
        if (binding.contentAddPlot . editTextNumberOfVine.text.toString().isNullOrEmpty()) {
            binding.contentAddPlot . editTextNumberOfVine.setError(getString(R.string.Field_should_not_be_empty))

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
