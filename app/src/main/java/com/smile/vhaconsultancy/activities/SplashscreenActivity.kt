package com.smile.vhaconsultancy.activities

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.multidex.BuildConfig
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.smile.vhaconsultancy.R
import com.smile.vhaconsultancy.utilities.SharedPref
import com.smile.vhaconsultancy.utilities.Utils
import kotlinx.android.synthetic.main.activity_splashscreen.*


/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
class SplashscreenActivity : AppCompatActivity() {
    private var mAuth: FirebaseAuth? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_splashscreen)
        mAuth = FirebaseAuth.getInstance()

        Utils.setLocal(this)
        try {
            // val pInfo: PackageInfo = getPackageManager().getPackageInfo(packageName, 0)
            // val version = pInfo.versionName
            val versno = BuildConfig.VERSION_CODE
            txt_version.text = "Version: " + versno.toString()
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
        val background: Thread = object : Thread() {
            override fun run() {
                try {
                    sleep(1500)
                    finish()
                    val currentUser: FirebaseUser? = mAuth?.getCurrentUser()
                    if (currentUser != null) {
                        val intent: Intent? = Intent(this@SplashscreenActivity, MainActivity::class.java)
                        SharedPref.Companion.getInstance(this@SplashscreenActivity)?.putSharedPrefString(getString(R.string.userUid), currentUser.getUid())
                        SharedPref.Companion.getInstance(this@SplashscreenActivity)?.putSharedPrefString(getString(R.string.userPhoneNumber), currentUser.getPhoneNumber())
                        startActivity(intent)
                    } else {
                        val intent = Intent(this@SplashscreenActivity, PhoneAuthActivity::class.java)
                        startActivity(intent)
                    }


                } catch (e: InterruptedException) {
                    currentThread().interrupt()
                }
            }
        }
        background.start()
    }
}
