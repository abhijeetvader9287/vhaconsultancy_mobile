package com.smile.vhaconsultancy.payment

import android.app.Activity
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
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
import com.smile.vhaconsultancy.databinding.ActivityAprilPaymentBinding
import com.smile.vhaconsultancy.utilities.SharedPref
import com.smile.vhaconsultancy.utilities.Utils
import java.math.BigDecimal
import java.math.RoundingMode
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.text.SimpleDateFormat
import java.util.*

////import com.payumoney.sdkui.ui.utils.PPConfig;
//class PaymentAprilActivity_ : BaseActivity(), View.OnClickListener {
//    private val isDisableExitConfirmation = false
//    var userUid: String? = ""
//    var userPhoneNumber: String? = ""
//    var plot_key: String? = ""
//    var txnId = ""
//    var database: FirebaseDatabase? = null
//    var aprilTransactionRefDatabaseRef: DatabaseReference? = null
//    var plot_keyRefDatabaseRef: DatabaseReference? = null
//    var aprilTransactionDateDatabaseRef: DatabaseReference? = null
//    var aprilPruningDateRefDatabaseRef: DatabaseReference? = null
//    private var mPaymentParams: PaymentParam? = null
//    var selectedDate = 0L
//    lateinit var pruning_date: Calendar
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_april_payment)
//        Utils.setLocal(this)
//
//        setSupportActionBar(custom_toolbar)
//        supportActionBar?.title = getString(R.string.payment)
//
//        userUid = SharedPref.Companion.getInstance(this@PaymentAprilActivity)?.getSharedPref(getString(R.string.userUid))
//        userPhoneNumber = SharedPref.Companion.getInstance(this@PaymentAprilActivity)?.getSharedPref(getString(R.string.userPhoneNumber))
//
//        database = FirebaseDatabase.getInstance()
//        plot_key = SharedPref.Companion.getInstance(this@PaymentAprilActivity)?.getSharedPref(getString(R.string.plot_key))
//        plot_keyRefDatabaseRef = database!!.getReference(getString(R.string.user_list)).child(userPhoneNumber!!).child(getString(R.string.plot_list)).child(SharedPref.Companion.getInstance(this@PaymentAprilActivity)?.getSharedPref(getString(R.string.plot_key)).toString()).child("plotKey")
//        plot_keyRefDatabaseRef!!.setValue(plot_key)
//        aprilTransactionRefDatabaseRef = database!!.getReference(getString(R.string.user_list)).child(userPhoneNumber!!).child(getString(R.string.plot_list)).child(SharedPref.Companion.getInstance(this@PaymentAprilActivity)?.getSharedPref(getString(R.string.plot_key)).toString()
//        ).child("aprilTransactionRef")
//
//        aprilTransactionDateDatabaseRef = database!!.getReference(getString(R.string.user_list)).child(userPhoneNumber!!).child(getString(R.string.plot_list)).child(SharedPref.Companion.getInstance(this@PaymentAprilActivity)?.getSharedPref(getString(R.string.plot_key)).toString()
//        ).child("aprilTransactionDate")
//        aprilPruningDateRefDatabaseRef = database!!.getReference(getString(R.string.user_list)).child(userPhoneNumber!!).child(getString(R.string.plot_list)).child(SharedPref.Companion.getInstance(this@PaymentAprilActivity)?.getSharedPref(getString(R.string.plot_key)).toString()
//        ).child("aprilPruningDate")
//
//        pay_now_button.setOnClickListener(this)
//        //Set Up SharedPref
//        setUpUserDetails()
//        (application as BaseApplication).appEnvironment = AppEnvironment.PRODUCTION
//    }
//
//    private fun setUpUserDetails() {
//        mobile_et!!.text = SharedPref.Companion.getInstance(this@PaymentAprilActivity)?.getSharedPref(getString(R.string.userPhoneNumber))
//        amount_et!!.text = SharedPref.Companion.getInstance(this@PaymentAprilActivity)?.getSharedPrefFloat(getString(R.string.rate)).toString()
//        name_et!!.text = SharedPref.Companion.getInstance(this@PaymentAprilActivity)?.getSharedPref(getString(R.string._name)).toString()
//        month_et!!.text = SharedPref.Companion.getInstance(this@PaymentAprilActivity)?.getSharedPref(getString(R.string.month)).toString()
//        area_et_et!!.text = SharedPref.Companion.getInstance(this@PaymentAprilActivity)?.getSharedPref(getString(R.string._area_in_acre)).toString()
//        var area = area_et_et!!.text.toString().toDouble()
//        if (area < 1.0) {
//            area = 0.0
//        }
//        val decimal = BigDecimal(amount_et!!.text.toString().toDouble() * (area)).setScale(2, RoundingMode.HALF_EVEN)
//        total_et!!.text = (decimal).toString()
//        pruining_date_et.setOnClickListener(View.OnClickListener {
//            pruning_date = Calendar.getInstance()
//            val mYear: Int = pruning_date.get(Calendar.YEAR)
//            val mMonth: Int = pruning_date.get(Calendar.MONTH)
//            val mDay: Int = pruning_date.get(Calendar.DAY_OF_MONTH)
//            val datePickerDialog = DatePickerDialog(this@PaymentAprilActivity,
//                    OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
//                        pruning_date.set(year, monthOfYear, dayOfMonth)
//                        val simpleDateFormat = SimpleDateFormat("dd-MMM-yyyy", Locale.US)
//                        val strDate = simpleDateFormat.format(Date(pruning_date.timeInMillis))
//                        selectedDate = pruning_date.timeInMillis
//                        pruining_date_et.text = strDate
//
//                    }, mYear, mMonth, mDay)
//            var strApril = getString(R.string.april)
//
//            datePickerDialog.show()
//           /* if (month_et.text.toString().equals(strApril)) {
//                var simpleDateFormat = SimpleDateFormat("dd-MMM-yyyy", Locale.US)
//
//                var dateEnd = Date()
//                var dateStart = Date()
//                var strYear = +dateEnd.year + 1900
//                dateEnd = simpleDateFormat.parse("31-Apr-" + strYear)
//                dateStart = simpleDateFormat.parse("1-Mar-" + strYear)
//                datePickerDialog.datePicker.maxDate = dateEnd.time
//                datePickerDialog.datePicker.minDate = dateStart.time
//            }*/
//        })
//    }
//
//    override fun onResume() {
//        super.onResume()
//        pay_now_button!!.isEnabled = true
//    }
//
//    override val layoutResource: Int
//        protected get() = R.layout.activity_april_payment
//
//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//        // Result Code is -1 send from Payumoney activity
//        Log.d("MainActivity", "request code $requestCode resultcode $resultCode")
//        if (requestCode == PayUmoneyFlowManager.REQUEST_CODE_PAYMENT && resultCode == Activity.RESULT_OK && data !=
//                null) {
//            val transactionResponse: TransactionResponse? = data.getParcelableExtra(PayUmoneyFlowManager.INTENT_EXTRA_TRANSACTION_RESPONSE)
//            //  val resultModel: ResultModel = data.getParcelableExtra(PayUmoneyFlowManager.ARG_RESULT)
//            // Check which object is non-null
//            if (transactionResponse != null && transactionResponse.getPayuResponse() != null) {
//                if (transactionResponse.transactionStatus == TransactionResponse.TransactionStatus.SUCCESSFUL) {
//                    //Success Transaction
//                    var payuResponse = transactionResponse.getPayuResponse()
//                    aprilTransactionRefDatabaseRef?.setValue(txnId)
//                    val pattern = "dd-MMM-yyyy HH:mm:ss.SSS"
//                    val simpleDateFormat = SimpleDateFormat(pattern, Locale.US)
//                    val transactionDateTime: String = simpleDateFormat.format(Date())
//                    aprilTransactionDateDatabaseRef?.setValue(transactionDateTime)
//                    aprilPruningDateRefDatabaseRef?.setValue(pruining_date_et.text.toString())
//                    AlertDialog.Builder(this)
//                            .setCancelable(false)
//                            .setTitle("Payment successfully recieved")
//                            .setMessage("Transaction id:" + txnId)
//                            .setPositiveButton(android.R.string.ok) { dialog, whichButton ->
//                                this@PaymentAprilActivity.finish()
//                                dialog.dismiss()
//                            }.show()
//
//                } else {
//                    //Failure Transaction
//                    AlertDialog.Builder(this)
//                            .setCancelable(false)
//                            .setTitle("Payment failed")
//
//                            .setPositiveButton(android.R.string.ok) { dialog, whichButton -> dialog.dismiss() }.show()
//                }
//                // Response from Payumoney
//
//
//            } else {
//                Log.d(TAG, "Both objects are null!")
//            }
//        }
//    }
//
//    override fun onClick(v: View) {
//        when (v.id) {
//            R.id.pay_now_button -> {
//                if (selectedDate != 0L) {
//                    pay_now_button!!.isEnabled = false
//                    launchPayUMoneyFlow()
//                } else {
//                    Toast.makeText(this@PaymentAprilActivity, getString(R.string.please_select_date_of_pruining), Toast.LENGTH_LONG).show()
//                }
//            }
//        }
//    }
//    /**
//     * This fucntion checks if email and mobile number are valid or not.
//     *
//     *
//     * @param mobile mobile number entered in edit text
//     * @return boolean value
//     */
//    /**
//     * This function prepares the data for payment and launches payumoney plug n play sdk
//     */
//    private fun launchPayUMoneyFlow() {
//        val payUmoneyConfig = PayUmoneyConfig.getInstance()
//        //Use this to set your custom text on result screen button
//        payUmoneyConfig.doneButtonText = "Done"
//        //Use this to set your custom title for the activity
//        payUmoneyConfig.payUmoneyActivityTitle = "Payment"
//        payUmoneyConfig.disableExitConfirmation(isDisableExitConfirmation)
//        val builder = PaymentParam.Builder()
//        var amount = 0.0
//        try {
//            amount = total_et!!.text.toString().toDouble()
//        } catch (e: Exception) {
//            e.printStackTrace()
//        }
//        txnId = "TXNID" + System.currentTimeMillis() + ""
//        //String txnId = "TXNID720431525261327973";
//        val phone = mobile_et!!.text.toString()
//        val productName = getString(R.string.app_name)
//        val firstName = name_et!!.text.toString()
//        val email = "amap.patil@gmail.com"
//        val udf1 = ""
//        val udf2 = ""
//        val udf3 = ""
//        val udf4 = ""
//        val udf5 = ""
//        val udf6 = ""
//        val udf7 = ""
//        val udf8 = ""
//        val udf9 = ""
//        val udf10 = ""
//        val appEnvironment = (application as BaseApplication).appEnvironment
//        builder.setAmount(amount.toString())
//                .setTxnId(txnId)
//                .setPhone(phone)
//                .setProductName(productName)
//                .setFirstName(firstName)
//                .setEmail(email)
//                .setsUrl(appEnvironment!!.surl())
//                .setfUrl(appEnvironment.furl())
//                .setUdf1(udf1)
//                .setUdf2(udf2)
//                .setUdf3(udf3)
//                .setUdf4(udf4)
//                .setUdf5(udf5)
//                .setUdf6(udf6)
//                .setUdf7(udf7)
//                .setUdf8(udf8)
//                .setUdf9(udf9)
//                .setUdf10(udf10)
//                .setIsDebug(appEnvironment.debug())
//                .setKey(appEnvironment.merchant_Key())
//                .setMerchantId(appEnvironment.merchant_ID())
//        try {
//            mPaymentParams = builder.build()
//
//            /*
//             * Hash should always be generated from your server side.
//             * */
//            //    generateHashFromServer(mPaymentParams);
//
//            /*            */
//            /**
//             * Do not use below code when going live
//             * Below code is provided to generate hash from sdk.
//             * It is recommended to generate hash from server side only.
//             */
//            mPaymentParams = calculateServerSideHashAndInitiatePayment1(mPaymentParams)
//            PayUmoneyFlowManager.startPayUMoneyFlow(mPaymentParams, this@PaymentAprilActivity, R.style.AppTheme_Base, true)
//        } catch (e: Exception) {
//            // some exception occurred
//            Toast.makeText(this, e.message, Toast.LENGTH_LONG).show()
//            pay_now_button!!.isEnabled = true
//        }
//    }
//
//    /**
//     * Thus function calculates the hash for transaction
//     *
//     * @param paymentParam payment params of transaction
//     * @return payment params along with calculated merchant hash
//     */
//    private fun calculateServerSideHashAndInitiatePayment1(paymentParam: PaymentParam?): PaymentParam? {
//        val stringBuilder = StringBuilder()
//        val params = paymentParam!!.params
//        stringBuilder.append(params[PayUmoneyConstants.KEY].toString() + "|")
//        stringBuilder.append(params[PayUmoneyConstants.TXNID].toString() + "|")
//        stringBuilder.append(params[PayUmoneyConstants.AMOUNT].toString() + "|")
//        stringBuilder.append(params[PayUmoneyConstants.PRODUCT_INFO].toString() + "|")
//        stringBuilder.append(params[PayUmoneyConstants.FIRSTNAME].toString() + "|")
//        stringBuilder.append(params[PayUmoneyConstants.EMAIL].toString() + "|")
//        stringBuilder.append(params[PayUmoneyConstants.UDF1].toString() + "|")
//        stringBuilder.append(params[PayUmoneyConstants.UDF2].toString() + "|")
//        stringBuilder.append(params[PayUmoneyConstants.UDF3].toString() + "|")
//        stringBuilder.append(params[PayUmoneyConstants.UDF4].toString() + "|")
//        stringBuilder.append(params[PayUmoneyConstants.UDF5].toString() + "||||||")
//        val appEnvironment = (application as BaseApplication).appEnvironment
//        stringBuilder.append(appEnvironment!!.salt())
//        val hash = hashCal(stringBuilder.toString())
//        paymentParam.setMerchantHash(hash)
//        return paymentParam
//    }
//
//
//    companion object {
//        const val TAG = "MainActivity : "
//        fun hashCal(str: String): String {
//            val hashseq = str.toByteArray()
//            val hexString = StringBuilder()
//            try {
//                val algorithm = MessageDigest.getInstance("SHA-512")
//                algorithm.reset()
//                algorithm.update(hashseq)
//                val messageDigest = algorithm.digest()
//                for (aMessageDigest in messageDigest) {
//                    val hex = Integer.toHexString(0xFF and aMessageDigest.toInt())
//                    if (hex.length == 1) {
//                        hexString.append("0")
//                    }
//                    hexString.append(hex)
//                }
//            } catch (ignored: NoSuchAlgorithmException) {
//            }
//            return hexString.toString()
//        }
//    }
//}


//import com.payumoney.sdkui.ui.utils.PPConfig;
class PaymentAprilActivity : BaseActivity(), View.OnClickListener {
    private val isDisableExitConfirmation = false
    var userUid: String? = ""
    var userPhoneNumber: String? = ""
    var radio: RadioButton? = null
    var plot_key: String? = ""
    var txnId = ""
    var database: FirebaseDatabase? = null
    var aprilTransactionRefDatabaseRef: DatabaseReference? = null
    var plot_keyRefDatabaseRef: DatabaseReference? = null
    var aprilTransactionDateDatabaseRef: DatabaseReference? = null
    var aprilPruningDateRefDatabaseRef: DatabaseReference? = null
    private var mPaymentParams: PaymentParam? = null
    var selectedDate = 0L
    lateinit var pruning_date: Calendar
    private lateinit var binding: ActivityAprilPaymentBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAprilPaymentBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        //setContentView(R.layout.activity_april_payment)
        Utils.setLocal(this)

        setSupportActionBar(binding.customToolbar)
        supportActionBar?.title = getString(R.string.payment)

        userUid = SharedPref.Companion.getInstance(this@PaymentAprilActivity)?.getSharedPref(getString(R.string.userUid))
        userPhoneNumber = SharedPref.Companion.getInstance(this@PaymentAprilActivity)?.getSharedPref(getString(R.string.userPhoneNumber))

        database = FirebaseDatabase.getInstance()
        plot_key = SharedPref.Companion.getInstance(this@PaymentAprilActivity)?.getSharedPref(getString(R.string.plot_key))
        plot_keyRefDatabaseRef = database!!.getReference(getString(R.string.user_list)).child(userPhoneNumber!!).child(getString(R.string.plot_list)).child(SharedPref.Companion.getInstance(this@PaymentAprilActivity)?.getSharedPref(getString(R.string.plot_key)).toString()).child("plotKey")
        plot_keyRefDatabaseRef!!.setValue(plot_key)
        aprilTransactionRefDatabaseRef = database!!.getReference(getString(R.string.user_list)).child(userPhoneNumber!!).child(getString(R.string.plot_list)).child(
            SharedPref.Companion.getInstance(this@PaymentAprilActivity)?.getSharedPref(getString(R.string.plot_key)).toString()
        ).child("aprilTransactionRef")

        aprilTransactionDateDatabaseRef = database!!.getReference(getString(R.string.user_list)).child(userPhoneNumber!!).child(getString(R.string.plot_list)).child(
            SharedPref.Companion.getInstance(this@PaymentAprilActivity)?.getSharedPref(getString(R.string.plot_key)).toString()
        ).child("aprilTransactionDate")
        aprilPruningDateRefDatabaseRef = database!!.getReference(getString(R.string.user_list)).child(userPhoneNumber!!).child(getString(R.string.plot_list)).child(
            SharedPref.Companion.getInstance(this@PaymentAprilActivity)?.getSharedPref(getString(R.string.plot_key)).toString()
        ).child("aprilPruningDate")

        binding.payNowButton.setOnClickListener(this)
        //Set Up SharedPref
        //setUpUserDetails()
        binding.btnVisiting.isChecked = true
        radio = findViewById(R.id.btnVisiting)
        binding.amountEt!!.text = SharedPref.Companion.getInstance(this@PaymentAprilActivity)?.getSharedPrefFloat(getString(R.string.rate)).toString()
        setUpUserDetails()
        (application as BaseApplication).appEnvironment = AppEnvironment.PRODUCTION
        binding.radioGroup.setOnCheckedChangeListener(
            RadioGroup.OnCheckedChangeListener { group, checkedId ->
                radio = findViewById(checkedId)

                if (checkedId == binding.btnVisiting.id) {
                    binding.amountEt!!.text = SharedPref.Companion.getInstance(this@PaymentAprilActivity)?.getSharedPrefFloat(getString(R.string.rate)).toString()
                    setUpUserDetails()
                } else if (checkedId == binding.btnNonVisiting.id) {
                    binding.amountEt!!.text = SharedPref.Companion.getInstance(this@PaymentAprilActivity)?.getSharedPrefFloat(getString(R.string.rate_nv)).toString()
                    setUpUserDetails()
                }
            })
        binding.payCashButton.setOnClickListener {

            if (selectedDate != 0L) {
                AlertDialog.Builder(this)
                    .setCancelable(false)
                    .setTitle("Payment in cash")
                    .setMessage("Do you want to pay in cash?")
                    .setPositiveButton(android.R.string.ok) { dialog, whichButton ->
                        txnId = "TXNID" + System.currentTimeMillis() + ""
                        aprilTransactionRefDatabaseRef?.setValue(txnId + "_cash_" + (radio?.text) + "_" + binding.amountEt!!.text)
                        val pattern = "dd-MMM-yyyy HH:mm:ss.SSS"
                        val simpleDateFormat = SimpleDateFormat(pattern, Locale.US)
                        val transactionDateTime: String = simpleDateFormat.format(Date())
                        aprilTransactionDateDatabaseRef?.setValue(transactionDateTime)
                        aprilPruningDateRefDatabaseRef?.setValue(binding.pruiningDateEt.text.toString())


                        AlertDialog.Builder(this)
                            .setCancelable(false)
                            .setTitle("Payment in cash")
                            .setMessage("Please pay " + binding.amountEt!!.text + " to our executive")
                            .setPositiveButton(android.R.string.ok) { dialog, whichButton ->
                                this@PaymentAprilActivity.finish()
                                dialog.dismiss()
                            }.show()
                    }.show()


            } else {
                Toast.makeText(this@PaymentAprilActivity, getString(R.string.please_select_date_of_pruining), Toast.LENGTH_LONG).show()
            }


        }
    }

    private fun setUpUserDetails() {

        binding.mobileEt!!.text = SharedPref.Companion.getInstance(this@PaymentAprilActivity)?.getSharedPref(getString(R.string.userPhoneNumber))
        binding.nameEt!!.text = SharedPref.Companion.getInstance(this@PaymentAprilActivity)?.getSharedPref(getString(R.string._name)).toString()
        binding.monthEt!!.text = SharedPref.Companion.getInstance(this@PaymentAprilActivity)?.getSharedPref(getString(R.string.month)).toString()
        binding.areaEtEt!!.text = SharedPref.Companion.getInstance(this@PaymentAprilActivity)?.getSharedPref(getString(R.string._area_in_acre)).toString()
        var area = binding.areaEtEt!!.text.toString().toDouble()
        if (area < 1.0) {
            area = 0.0
        }
        val decimal = BigDecimal(binding.amountEt!!.text.toString().toDouble() * (area)).setScale(2, RoundingMode.HALF_EVEN)
        binding.totalEt!!.text = (decimal).toString()
        binding.pruiningDateEt.setOnClickListener(View.OnClickListener {
            pruning_date = Calendar.getInstance()
            val mYear: Int = pruning_date.get(Calendar.YEAR)
            val mMonth: Int = pruning_date.get(Calendar.MONTH)
            val mDay: Int = pruning_date.get(Calendar.DAY_OF_MONTH)
            val datePickerDialog = DatePickerDialog(this@PaymentAprilActivity,
                OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                    pruning_date.set(year, monthOfYear, dayOfMonth)
                    val simpleDateFormat = SimpleDateFormat("dd-MMM-yyyy", Locale.US)
                    val strDate = simpleDateFormat.format(Date(pruning_date.timeInMillis))
                    selectedDate = pruning_date.timeInMillis
                    binding.pruiningDateEt.text = strDate

                }, mYear, mMonth, mDay
            )
            var strapril = getString(R.string.april)

            datePickerDialog.show()
            /* if (month_et.text.toString().equals(stroctober)) {
                 var simpleDateFormat = SimpleDateFormat("dd-MMM-yyyy", Locale.US)

                 var dateEnd = Date()
                 var dateStart = Date()
                 var strYear = +dateEnd.year + 1900
                 dateEnd = simpleDateFormat.parse("31-Oct-" + strYear)
                 dateStart = simpleDateFormat.parse("1-Sep-" + strYear)
                 datePickerDialog.datePicker.maxDate = dateEnd.time
                 datePickerDialog.datePicker.minDate = dateStart.time
             }*/
        })
    }

    override fun onResume() {
        super.onResume()
        // pay_now_button!!.isEnabled = true
    }

    override val layoutResource: Int
        protected get() = R.layout.activity_october_payment

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
                    aprilTransactionRefDatabaseRef?.setValue(txnId + "_online_" + (radio?.text) + "_" + binding.amountEt!!.text)
                    val pattern = "dd-MMM-yyyy HH:mm:ss.SSS"
                    val simpleDateFormat = SimpleDateFormat(pattern, Locale.US)
                    val transactionDateTime: String = simpleDateFormat.format(Date())
                    aprilTransactionDateDatabaseRef?.setValue(transactionDateTime)
                    aprilPruningDateRefDatabaseRef?.setValue(binding.pruiningDateEt.text.toString())
                    AlertDialog.Builder(this)
                        .setCancelable(false)
                        .setTitle("Payment successfully recieved")
                        .setMessage("Transaction id:" + txnId)
                        .setPositiveButton(android.R.string.ok) { dialog, whichButton ->
                            this@PaymentAprilActivity.finish()
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
                if (selectedDate != 0L) {
                    binding.payNowButton!!.isEnabled = false
                    launchPayUMoneyFlow()
                } else {
                    Toast.makeText(this@PaymentAprilActivity, getString(R.string.please_select_date_of_pruining), Toast.LENGTH_LONG).show()
                }
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
            PayUmoneyFlowManager.startPayUMoneyFlow(mPaymentParams, this@PaymentAprilActivity, R.style.AppTheme_purple, true)
        } catch (e: Exception) {
            // some exception occurred
            Toast.makeText(this, e.message, Toast.LENGTH_LONG).show()
            // pay_now_button!!.isEnabled = true
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