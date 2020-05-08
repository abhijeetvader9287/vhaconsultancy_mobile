package com.smile.vhaconsultancy.payment

/**
 * Created by Rahul Hooda on 14/7/17.
 */
enum class AppEnvironment {
    SANDBOX {
        override fun merchant_Key(): String {
            return "zk7Q3Fph"
        }

        override fun merchant_ID(): String {
            return "7041669"
        }

        override fun furl(): String {
            return "https://www.payumoney.com/mobileapp/payumoney/failure.php"
        }

        override fun surl(): String {
            return "https://www.payumoney.com/mobileapp/payumoney/success.php"
        }

        override fun salt(): String {
            return "rmSp2KSiZH"
        }

        override fun debug(): Boolean {
            return true
        }
    },
    PRODUCTION {
        override fun merchant_Key(): String {
            return "zk7Q3Fph"
        }

        override fun merchant_ID(): String {
            return "7041669"
        }

        override fun furl(): String {
            return "https://www.payumoney.com/mobileapp/payumoney/failure.php"
        }

        override fun surl(): String {
            return "https://www.payumoney.com/mobileapp/payumoney/success.php"
        }

        override fun salt(): String {
            return "rmSp2KSiZH"
        }

        override fun debug(): Boolean {
            return false
        }
    };

    abstract fun merchant_Key(): String
    abstract fun merchant_ID(): String
    abstract fun furl(): String
    abstract fun surl(): String
    abstract fun salt(): String
    abstract fun debug(): Boolean
}