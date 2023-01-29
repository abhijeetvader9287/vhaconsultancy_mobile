package com.smile.vhaconsultancy.payment

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.payumoney.core.PayUmoneyConfig
import com.payumoney.core.PayUmoneyConstants
import com.payumoney.core.PayUmoneySdkInitializer.PaymentParam
import com.payumoney.core.entity.TransactionResponse
import com.payumoney.sdkui.ui.utils.PayUmoneyFlowManager
import com.smile.vhaconsultancy.R
import com.smile.vhaconsultancy.databinding.ActivityBerrysetPaymentBinding
import com.smile.vhaconsultancy.utilities.SharedPref
import com.smile.vhaconsultancy.utilities.Utils
import java.math.BigDecimal
import java.math.RoundingMode
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.text.SimpleDateFormat
import java.util.*

//import com.payumoney.sdkui.ui.utils.PPConfig;
class PaymentBerryStageActivity : BaseActivity(), View.OnClickListener {
    private val isDisableExitConfirmation = false
    var userUid: String? = ""
    var userPhoneNumber: String? = ""
    var plot_key: String? = ""
    var txnId = ""
    var database: FirebaseDatabase? = null
    var berryStageTransactionRefDatabaseRef: DatabaseReference? = null
    var plot_keyRefDatabaseRef: DatabaseReference? = null
    var berryStageTransactionDateDatabaseRef: DatabaseReference? = null
    private var mPaymentParams: PaymentParam? = null
    var radio: RadioButton? = null
    private lateinit var binding: ActivityBerrysetPaymentBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBerrysetPaymentBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        // setContentView(R.layout.activity_berryset_payment)
        Utils.setLocal(this)

        setSupportActionBar(binding.customToolbar)
        supportActionBar?.title = getString(R.string.payment)

        userUid = SharedPref.Companion.getInstance(this@PaymentBerryStageActivity)?.getSharedPref(getString(R.string.userUid))
        userPhoneNumber = SharedPref.Companion.getInstance(this@PaymentBerryStageActivity)?.getSharedPref(getString(R.string.userPhoneNumber))

        database = FirebaseDatabase.getInstance()
        plot_key = SharedPref.Companion.getInstance(this@PaymentBerryStageActivity)?.getSharedPref(getString(R.string.plot_key))
        plot_keyRefDatabaseRef = database!!.getReference(getString(R.string.user_list)).child(userPhoneNumber!!).child(getString(R.string.plot_list)).child(SharedPref.Companion.getInstance(this@PaymentBerryStageActivity)?.getSharedPref(getString(R.string.plot_key)).toString()).child("plotKey")
        plot_keyRefDatabaseRef!!.setValue(plot_key)
        berryStageTransactionRefDatabaseRef = database!!.getReference(getString(R.string.user_list)).child(userPhoneNumber!!).child(getString(R.string.plot_list)).child(
            SharedPref.Companion.getInstance(this@PaymentBerryStageActivity)?.getSharedPref(getString(R.string.plot_key)).toString()
        ).child("berryStageTransactionRef")

        berryStageTransactionDateDatabaseRef = database!!.getReference(getString(R.string.user_list)).child(userPhoneNumber!!).child(getString(R.string.plot_list)).child(
            SharedPref.Companion.getInstance(this@PaymentBerryStageActivity)?.getSharedPref(getString(R.string.plot_key)).toString()
        ).child("berryStageTransactionDate")


        binding.payNowButton.setOnClickListener(this)
        //Set Up SharedPref
        //setUpUserDetails()
        binding.btnVisiting.isChecked = true
        radio = findViewById(R.id.btnVisiting)
        binding.amountEt!!.text = SharedPref.Companion.getInstance(this@PaymentBerryStageActivity)?.getSharedPrefFloat(getString(R.string.rate)).toString()
        setUpUserDetails()
        (application as BaseApplication).appEnvironment = AppEnvironment.PRODUCTION
        binding.radioGroup.setOnCheckedChangeListener(
            RadioGroup.OnCheckedChangeListener { group, checkedId ->
                radio = findViewById(checkedId)

                if (checkedId == binding.btnVisiting.id) {
                    binding.amountEt!!.text = SharedPref.Companion.getInstance(this@PaymentBerryStageActivity)?.getSharedPrefFloat(getString(R.string.rate)).toString()
                    setUpUserDetails()
                } else if (checkedId == binding.btnNonVisiting.id) {
                    binding.amountEt!!.text = SharedPref.Companion.getInstance(this@PaymentBerryStageActivity)?.getSharedPrefFloat(getString(R.string.rate_nv)).toString()
                    setUpUserDetails()
                }
            })
        binding.payCashButton.setOnClickListener {

            AlertDialog.Builder(this)
                .setCancelable(false)
                .setTitle("Payment in cash")
                .setMessage("Do you want to pay in cash?")
                .setPositiveButton(android.R.string.ok) { dialog, whichButton ->
                    txnId = "TXNID" + System.currentTimeMillis() + ""
                    berryStageTransactionRefDatabaseRef?.setValue(txnId + "_cash_" + (radio?.text) + "_" + binding.amountEt!!.text)
                    val pattern = "dd-MMM-yyyy HH:mm:ss.SSS"
                    val simpleDateFormat = SimpleDateFormat(pattern, Locale.US)
                    val transactionDateTime: String = simpleDateFormat.format(Date())
                    berryStageTransactionDateDatabaseRef?.setValue(transactionDateTime)
                    // octoberPruiningDateRefDatabaseRef?.setValue(pruining_date_et.text.toString())


                    AlertDialog.Builder(this)
                        .setCancelable(false)
                        .setTitle("Payment in cash")
                        .setMessage("Please pay " + binding.amountEt!!.text + " to our executive")
                        .setPositiveButton(android.R.string.ok) { dialog, whichButton ->
                            this@PaymentBerryStageActivity.finish()
                            dialog.dismiss()
                        }.show()
                }.show()


        }
        (application as BaseApplication).appEnvironment = AppEnvironment.PRODUCTION
    }

    private fun setUpUserDetails() {
        binding.mobileEt!!.text = SharedPref.Companion.getInstance(this@PaymentBerryStageActivity)?.getSharedPref(getString(R.string.userPhoneNumber))
        binding.amountEt!!.text = SharedPref.Companion.getInstance(this@PaymentBerryStageActivity)?.getSharedPrefFloat(getString(R.string.rate)).toString()
        binding.nameEt!!.text = SharedPref.Companion.getInstance(this@PaymentBerryStageActivity)?.getSharedPref(getString(R.string._name)).toString()
        binding.areaEtEt!!.text = SharedPref.Companion.getInstance(this@PaymentBerryStageActivity)?.getSharedPref(getString(R.string._area_in_acre)).toString()
        var area = binding.areaEtEt!!.text.toString().toDouble()
        if (area < 1.0) {
            area = 0.0
        }
        val decimal = BigDecimal(binding.amountEt!!.text.toString().toDouble() * (area)).setScale(2, RoundingMode.HALF_EVEN)
        binding.totalEt!!.text = (decimal).toString()

    }

    override fun onResume() {
        super.onResume()
        binding.payNowButton!!.isEnabled = true
    }

    override val layoutResource: Int
        protected get() = R.layout.activity_berryset_payment

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        // Result Code is -1 send from Payumoney activity
        Log.d("MainActivity", "request code $requestCode resultcode $resultCode")
        if (requestCode == PayUmoneyFlowManager.REQUEST_CODE_PAYMENT && resultCode == Activity.RESULT_OK && data !=
            null
        ) {
            val transactionResponse: TransactionResponse? = data.getParcelableExtra(PayUmoneyFlowManager.INTENT_EXTRA_TRANSACTION_RESPONSE)
            //  val resultModel: ResultModel = data.getParcelableExtra(PayUmoneyFlowManager.ARG_RESULT)
            // Check which object is non-null
            if (transactionResponse != null && transactionResponse.getPayuResponse() != null) {
                if (transactionResponse.transactionStatus == TransactionResponse.TransactionStatus.SUCCESSFUL) {
                    //Success Transaction
                    var payuResponse = transactionResponse.getPayuResponse()
                    berryStageTransactionRefDatabaseRef?.setValue(txnId + "_online_" + (radio?.text) + "_" + binding.amountEt!!.text)
                    val pattern = "dd-MMM-yyyy HH:mm:ss.SSS"
                    val simpleDateFormat = SimpleDateFormat(pattern, Locale.US)
                    val transactionDateTime: String = simpleDateFormat.format(Date())
                    berryStageTransactionDateDatabaseRef?.setValue(transactionDateTime)
                    AlertDialog.Builder(this)
                        .setCancelable(false)
                        .setTitle("Payment successfully recieved")
                        .setMessage("Transaction id:" + txnId)
                        .setPositiveButton(android.R.string.ok) { dialog, whichButton ->
                            this@PaymentBerryStageActivity.finish()
                            dialog.dismiss()
                        }.show()

                } else {
                    //Failure Transaction
                    AlertDialog.Builder(this)
                        .setCancelable(false)
                        .setTitle("Payment failed")

                        .setPositiveButton(android.R.string.ok) { dialog, whichButton -> dialog.dismiss() }.show()
                }
                // Response from Payumoney


            } else {
                Log.d(TAG, "Both objects are null!")
            }
        }
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.pay_now_button -> {

                binding.payNowButton!!.isEnabled = false
                launchPayUMoneyFlow()

            }
        }
    }
    /**
     * This fucntion checks if email and mobile number are valid or not.
     *
     *
     * @param mobile mobile number entered in edit text
     * @return boolean value
     */
    /**
     * This function prepares the data for payment and launches payumoney plug n play sdk
     */
    private fun launchPayUMoneyFlow() {
        val payUmoneyConfig = PayUmoneyConfig.getInstance()
        //Use this to set your custom text on result screen button
        payUmoneyConfig.doneButtonText = "Done"
        //Use this to set your custom title for the activity
        payUmoneyConfig.payUmoneyActivityTitle = "Payment"
        payUmoneyConfig.disableExitConfirmation(isDisableExitConfirmation)
        val builder = PaymentParam.Builder()
        var amount = 0.0
        try {
            amount = binding.totalEt!!.text.toString().toDouble()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        txnId = "TXNID" + System.currentTimeMillis() + ""
        //String txnId = "TXNID720431525261327973";
        val phone = binding.mobileEt!!.text.toString()
        val productName = getString(R.string.app_name)
        val firstName = binding.nameEt!!.text.toString()
        val email = "amap.patil@gmail.com"
        val udf1 = ""
        val udf2 = ""
        val udf3 = ""
        val udf4 = ""
        val udf5 = ""
        val udf6 = ""
        val udf7 = ""
        val udf8 = ""
        val udf9 = ""
        val udf10 = ""
        val appEnvironment = (application as BaseApplication).appEnvironment
        builder.setAmount(amount.toString())
            .setTxnId(txnId)
            .setPhone(phone)
            .setProductName(productName)
            .setFirstName(firstName)
            .setEmail(email)
            .setsUrl(appEnvironment!!.surl())
            .setfUrl(appEnvironment.furl())
            .setUdf1(udf1)
            .setUdf2(udf2)
            .setUdf3(udf3)
            .setUdf4(udf4)
            .setUdf5(udf5)
            .setUdf6(udf6)
            .setUdf7(udf7)
            .setUdf8(udf8)
            .setUdf9(udf9)
            .setUdf10(udf10)
            .setIsDebug(appEnvironment.debug())
            .setKey(appEnvironment.merchant_Key())
            .setMerchantId(appEnvironment.merchant_ID())
        try {
            mPaymentParams = builder.build()

            /*
             * Hash should always be generated from your server side.
             * */
            //    generateHashFromServer(mPaymentParams);

            /*            */
            /**
             * Do not use below code when going live
             * Below code is provided to generate hash from sdk.
             * It is recommended to generate hash from server side only.
             */
            mPaymentParams = calculateServerSideHashAndInitiatePayment1(mPaymentParams)
            PayUmoneyFlowManager.startPayUMoneyFlow(mPaymentParams, this@PaymentBerryStageActivity, R.style.AppTheme_purple, true)
        } catch (e: Exception) {
            // some exception occurred
            Toast.makeText(this, e.message, Toast.LENGTH_LONG).show()
            binding.payNowButton!!.isEnabled = true
        }
    }

    /**
     * Thus function calculates the hash for transaction
     *
     * @param paymentParam payment params of transaction
     * @return payment params along with calculated merchant hash
     */
    private fun calculateServerSideHashAndInitiatePayment1(paymentParam: PaymentParam?): PaymentParam? {
        val stringBuilder = StringBuilder()
        val params = paymentParam!!.params
        stringBuilder.append(params[PayUmoneyConstants.KEY].toString() + "|")
        stringBuilder.append(params[PayUmoneyConstants.TXNID].toString() + "|")
        stringBuilder.append(params[PayUmoneyConstants.AMOUNT].toString() + "|")
        stringBuilder.append(params[PayUmoneyConstants.PRODUCT_INFO].toString() + "|")
        stringBuilder.append(params[PayUmoneyConstants.FIRSTNAME].toString() + "|")
        stringBuilder.append(params[PayUmoneyConstants.EMAIL].toString() + "|")
        stringBuilder.append(params[PayUmoneyConstants.UDF1].toString() + "|")
        stringBuilder.append(params[PayUmoneyConstants.UDF2].toString() + "|")
        stringBuilder.append(params[PayUmoneyConstants.UDF3].toString() + "|")
        stringBuilder.append(params[PayUmoneyConstants.UDF4].toString() + "|")
        stringBuilder.append(params[PayUmoneyConstants.UDF5].toString() + "||||||")
        val appEnvironment = (application as BaseApplication).appEnvironment
        stringBuilder.append(appEnvironment!!.salt())
        val hash = hashCal(stringBuilder.toString())
        paymentParam.setMerchantHash(hash)
        return paymentParam
    }


    companion object {
        const val TAG = "MainActivity : "
        fun hashCal(str: String): String {
            val hashseq = str.toByteArray()
            val hexString = StringBuilder()
            try {
                val algorithm = MessageDigest.getInstance("SHA-512")
                algorithm.reset()
                algorithm.update(hashseq)
                val messageDigest = algorithm.digest()
                for (aMessageDigest in messageDigest) {
                    val hex = Integer.toHexString(0xFF and aMessageDigest.toInt())
                    if (hex.length == 1) {
                        hexString.append("0")
                    }
                    hexString.append(hex)
                }
            } catch (ignored: NoSuchAlgorithmException) {
            }
            return hexString.toString()
        }
    }
}