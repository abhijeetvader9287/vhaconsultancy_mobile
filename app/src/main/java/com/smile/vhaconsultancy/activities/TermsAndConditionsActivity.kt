package com.smile.vhaconsultancy.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.smile.vhaconsultancy.R

import kotlinx.android.synthetic.main.activity_terms_and_conditions.*
import kotlinx.android.synthetic.main.content_terms_and_conditions.*

class TermsAndConditionsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_terms_and_conditions)
        setSupportActionBar(toolbar)
btn_i_agree.setOnClickListener {
    finish()
}

    }

}