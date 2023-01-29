package com.smile.vhaconsultancy.activities

import android.app.ProgressDialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.*
import com.smile.vhaconsultancy.databinding.ActivityProfileBinding
import com.smile.vhaconsultancy.models.UserProfile
import com.smile.vhaconsultancy.utilities.SharedPref
import com.smile.vhaconsultancy.utilities.Utils


class ProfileActivity : AppCompatActivity() {
    var userUid: String? = ""
    var userPhoneNumber: String? = ""
    var database: FirebaseDatabase? = null
    var databaseProfileReference: DatabaseReference? = null
    lateinit var dialog: ProgressDialog
    private lateinit var binding: ActivityProfileBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        Utils.setLocal(this)

        //  setContentView(R.layout.activity_profile)
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(true)
        dialog = ProgressDialog(this);
        dialog.getWindow()?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT));
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
            binding.contentProfile.editTextMobileNumber.setText(userPhoneNumber)

        }

        databaseProfileReference!!.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                //clearing the previous artist list
                //iterating through all the nodes
                //getting artist
                val userProfile: UserProfile? = dataSnapshot.getValue(UserProfile::class.java)
                //adding artist to the list
                //   editTextMobileNumber.setText(userProfile?.mobileNumber)
                binding.contentProfile.editTextDistrict.setText(userProfile?.district)
                binding.contentProfile.editTextName.setText(userProfile?.name)
                binding.contentProfile.editTextTaluka.setText(userProfile?.taluka)
                binding.contentProfile.editTextVillage.setText(userProfile?.village)
                binding.contentProfile.editTextAadhar.setText(userProfile?.adharnumber)
                dialog.dismiss()
            }

            override fun onCancelled(databaseError: DatabaseError) {
                dialog.dismiss();

            }
        })


        binding.contentProfile.btnSave.setOnClickListener {
            if (isValidFun()) {
                dialog.show();

                val userProfile: UserProfile = UserProfile()
                userProfile.name = binding.contentProfile.editTextName.text.toString()
                userProfile.mobileNumber = binding.contentProfile.editTextMobileNumber.text.toString()
                userProfile.village = binding.contentProfile.editTextVillage.text.toString()
                userProfile.taluka = binding.contentProfile.editTextTaluka.text.toString()
                userProfile.district = binding.contentProfile.editTextDistrict.text.toString()
                userProfile.adharnumber = binding.contentProfile.editTextAadhar.text.toString()
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
        if (binding.contentProfile.editTextDistrict.text.toString().isNullOrEmpty()) {
            binding.contentProfile.editTextDistrict.setError(getString(R.string.Field_should_not_be_empty))
            isValid = false

        }
        if (binding.contentProfile.editTextMobileNumber.text.toString().isNullOrEmpty()) {
            binding.contentProfile.editTextMobileNumber.setError(getString(R.string.Field_should_not_be_empty))

            isValid = false

        }
        if (binding.contentProfile.editTextAadhar.text.toString().isNullOrEmpty()) {
            binding.contentProfile.editTextAadhar.setError(getString(R.string.Field_should_not_be_empty))

            isValid = false

        }
        if (binding.contentProfile.editTextName.text.toString().isNullOrEmpty()) {
            binding.contentProfile.editTextName.setError(getString(R.string.Field_should_not_be_empty))

            isValid = false

        }
        if (binding.contentProfile.editTextTaluka.text.toString().isNullOrEmpty()) {
            binding.contentProfile.editTextTaluka.setError(getString(R.string.Field_should_not_be_empty))

            isValid = false

        }
        if (binding.contentProfile.editTextVillage.text.toString().isNullOrEmpty()) {
            binding.contentProfile.editTextVillage.setError(getString(R.string.Field_should_not_be_empty))

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
