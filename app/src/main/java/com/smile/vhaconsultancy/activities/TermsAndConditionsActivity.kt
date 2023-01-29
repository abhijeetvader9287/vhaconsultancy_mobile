package com.smile.vhaconsultancy.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.smile.vhaconsultancy.databinding.ActivityTermsAndConditionsBinding
import com.smile.vhaconsultancy.utilities.SharedPref
import com.smile.vhaconsultancy.utilities.Utils


class TermsAndConditionsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTermsAndConditionsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTermsAndConditionsBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        //  setContentView(R.layout.activity_terms_and_conditions)
        setSupportActionBar(binding.toolbar)

        Utils.setLocal(this)

        binding.contentTermsAndConditions.btnIAgree.setOnClickListener {
            SharedPref.getInstance(this@TermsAndConditionsActivity)?.putSharedPrefBool("terms", true);
            finish()
        }

    }

}
