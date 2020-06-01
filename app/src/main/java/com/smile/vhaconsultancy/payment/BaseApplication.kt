package com.smile.vhaconsultancy.payment

import android.app.Application

/**
 * Created by Rahul Hooda on 14/7/17.
 */
class BaseApplication : Application() {
    var appEnvironment: AppEnvironment? = null
    override fun onCreate() {
        super.onCreate()
        appEnvironment = AppEnvironment.SANDBOX
    }

}