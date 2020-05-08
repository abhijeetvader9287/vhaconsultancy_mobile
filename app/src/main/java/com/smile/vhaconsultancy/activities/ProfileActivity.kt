package com.smile.vhaconsultancy.activities

import android.app.ProgressDialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.*
import com.smile.vhaconsultancy.R
import com.smile.vhaconsultancy.models.UserProfile
import com.smile.vhaconsultancy.utilities.SharedPref
import com.smile.vhaconsultancy.utilities.Utils
import kotlinx.android.synthetic.main.activity_profile.*
import kotlinx.android.synthetic.main.content_profile.*


class ProfileActivity : AppCompatActivity() {
    var userUid: String? = ""
    var userPhoneNumber: String? = ""
    var database: FirebaseDatabase? = null
    var databaseProfileReference: DatabaseReference? = null
    lateinit var dialog: ProgressDialog
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Utils.setLocal(this)

        setContentView(R.layout.activity_profile)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(true)
        dialog = ProgressDialog(this);
        dialog.getWindow().setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT));
        dialog.setIndeterminate(true);
        dialog.setCancelable(false);
        dialog.show();
        dialog.setContentView(R.layout.progress_layout);
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        userUid = SharedPref.Companion.getInstance(this@ProfileActivity)?.getSharedPref(getString(R.string.userUid));
        userPhoneNumber = SharedPref.Companion.getInstance(this@ProfileActivity)?.getSharedPref(getString(R.string.userPhoneNumber))

        database = FirebaseDatabase.getInstance()
        databaseProfileReference = database!!.getReference(getString(R.string.user_list)).child(userPhoneNumber!!).child(getString(R.string.user_profile))

        if (!userPhoneNumber.isNullOrEmpty()) {
            editTextMobileNumber.setText(userPhoneNumber)

        }

        databaseProfileReference!!.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                //clearing the previous artist list
                //iterating through all the nodes
                //getting artist
                val userProfile: UserProfile? = dataSnapshot.getValue(UserProfile::class.java)
                //adding artist to the list
                //   editTextMobileNumber.setText(userProfile?.mobileNumber)
                editTextDistrict.setText(userProfile?.district)
                editTextName.setText(userProfile?.name)
                editTextTaluka.setText(userProfile?.taluka)
                editTextVillage.setText(userProfile?.village)
                dialog.dismiss()
            }

            override fun onCancelled(databaseError: DatabaseError) {
                dialog.dismiss();

            }
        })


        btnSave.setOnClickListener {
            if (isValidFun()) {
                dialog.show();

                val userProfile: UserProfile = UserProfile()
                userProfile.name = editTextName.text.toString()
                userProfile.mobileNumber = editTextMobileNumber.text.toString()
                userProfile.village = editTextVillage.text.toString()
                userProfile.taluka = editTextTaluka.text.toString()
                userProfile.district = editTextDistrict.text.toString()
                databaseProfileReference!!.setValue(userProfile).addOnCanceledListener {
                    dialog.dismiss();

                }
                        .addOnCompleteListener {

                            dialog.dismiss();

                        }.addOnFailureListener {
                            dialog.dismiss();

                            Toast.makeText(this@ProfileActivity, getString(R.string.User_profile_save_failed) + it.localizedMessage, Toast.LENGTH_LONG).show()

                        }.addOnSuccessListener {
                            dialog.dismiss();

                            Toast.makeText(this@ProfileActivity, getString(R.string.User_profile_save_successfully), Toast.LENGTH_LONG).show()

                        }
            }
        }

    }

    fun isValidFun(): Boolean {
        var isValid = true
        if (editTextDistrict.text.toString().isNullOrEmpty()) {
            editTextDistrict.setError(getString(R.string.Field_should_not_be_empty))
            isValid = false

        }
        if (editTextMobileNumber.text.toString().isNullOrEmpty()) {
            editTextMobileNumber.setError(getString(R.string.Field_should_not_be_empty))

            isValid = false

        }
        if (editTextName.text.toString().isNullOrEmpty()) {
            editTextName.setError(getString(R.string.Field_should_not_be_empty))

            isValid = false

        }
        if (editTextTaluka.text.toString().isNullOrEmpty()) {
            editTextTaluka.setError(getString(R.string.Field_should_not_be_empty))

            isValid = false

        }
        if (editTextVillage.text.toString().isNullOrEmpty()) {
            editTextVillage.setError(getString(R.string.Field_should_not_be_empty))

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
