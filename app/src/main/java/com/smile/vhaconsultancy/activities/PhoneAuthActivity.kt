package com.smile.vhaconsultancy.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.*
import com.google.firebase.auth.PhoneAuthProvider.ForceResendingToken
import com.google.firebase.auth.PhoneAuthProvider.OnVerificationStateChangedCallbacks
import com.smile.vhaconsultancy.R
import com.smile.vhaconsultancy.utilities.SharedPref
import kotlinx.android.synthetic.main.activity_phone_auth.*
import java.util.concurrent.TimeUnit

class PhoneAuthActivity constructor() : AppCompatActivity(), View.OnClickListener {
    // [START declare_auth]
    private var mAuth: FirebaseAuth? = null

    // [END declare_auth]
    private var mVerificationInProgress: Boolean = false
    private lateinit var mVerificationId: String
    private lateinit var mResendToken: ForceResendingToken
    private lateinit var mCallbacks: OnVerificationStateChangedCallbacks
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_phone_auth)
        // Restore instance state

        // Assign views
        // Assign click listeners
        buttonStartVerification.setOnClickListener(this)
        buttonVerifyPhone.setOnClickListener(this)
        buttonResend.setOnClickListener(this)
        fieldPhoneNumber.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (p0?.length == 10) {
                    hideSoftKeyBoard(this@PhoneAuthActivity, fieldPhoneNumber)
                }
            }
        })
        // [START initialize_auth]
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance()
        // [END initialize_auth]
        // Initialize phone auth callbacks
        // [START phone_auth_callbacks]
        mCallbacks = object : OnVerificationStateChangedCallbacks() {

            override fun onCodeSent(verificationId: String, resendToken: ForceResendingToken) {
                super.onCodeSent(verificationId, resendToken)
                mVerificationId=verificationId;
                mResendToken=resendToken;
                updateUI(STATE_CODE_SENT)
            }
            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                // This callback will be invoked in two situations:
                // 1 - Instant verification. In some cases the phone number can be instantly
                //     verified without needing to send or enter a verification code.
                // 2 - Auto-retrieval. On some devices Google Play services can automatically
                //     detect the incoming verification SMS and perform verification without
                //     user action.
                Log.d(TAG, "onVerificationCompleted:" + credential)
                // [START_EXCLUDE silent]
                mVerificationInProgress = false
                // [END_EXCLUDE]
                // [START_EXCLUDE silent]
                // Update the UI and attempt sign in with the phone credential
                updateUI(STATE_VERIFY_SUCCESS, credential)
                // [END_EXCLUDE]
                signInWithPhoneAuthCredential(credential)
            }

            override fun onVerificationFailed(e: FirebaseException) {
                // This callback is invoked in an invalid request for verification is made,
                // for instance if the the phone number format is not valid.
                Log.w(TAG, "onVerificationFailed", e)
                // [START_EXCLUDE silent]
                mVerificationInProgress = false
                // [END_EXCLUDE]
                if (e is FirebaseAuthInvalidCredentialsException) {
                    // Invalid request
                    // [START_EXCLUDE]
                    fieldPhoneNumber.setError("Invalid phone number.")
                    // [END_EXCLUDE]
                } else if (e is FirebaseTooManyRequestsException) {
                    // The SMS quota for the project has been exceeded
                    // [START_EXCLUDE]
                    Snackbar.make(findViewById(android.R.id.content), "Quota exceeded.",
                            Snackbar.LENGTH_SHORT).show()
                    // [END_EXCLUDE]
                }
                // Show a message and update the UI
                // [START_EXCLUDE]
                updateUI(STATE_VERIFY_FAILED)
                // [END_EXCLUDE]
            }

        }
//signOut()
        // [END phone_auth_callbacks]
    }

    fun hideSoftKeyBoard(context: Context, view: View) {
        try {
            val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm?.hideSoftInputFromWindow(view.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
        } catch (e: Exception) {
            // TODO: handle exception
            e.printStackTrace()
        }

    }

    // [START on_start_check_user]
    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser: FirebaseUser? = mAuth?.getCurrentUser()
        updateUI(currentUser)
        // [START_EXCLUDE]
        if (mVerificationInProgress && validatePhoneNumber()) {
            startPhoneNumberVerification(getString(R.string.country_code) + fieldPhoneNumber.getText().toString())
        }
        // [END_EXCLUDE]
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean(KEY_VERIFY_IN_PROGRESS, mVerificationInProgress)
    }

    // [END on_start_check_user]
    override fun onRestoreInstanceState(savedInstanceState: Bundle?) {
        super.onRestoreInstanceState(savedInstanceState)
        if (savedInstanceState != null) {
            mVerificationInProgress = savedInstanceState.getBoolean(KEY_VERIFY_IN_PROGRESS)
        }
    }

    private fun startPhoneNumberVerification(phoneNumber: String) {
        // [START start_phone_auth]
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,  // Phone number to verify
                60,  // Timeout duration
                TimeUnit.SECONDS,  // Unit of timeout
                this,  // Activity (for callback binding)
                mCallbacks) // OnVerificationStateChangedCallbacks
        // [END start_phone_auth]
        mVerificationInProgress = true
    }

    private fun verifyPhoneNumberWithCode(verificationId: String, code: String) {
        // [START verify_with_code]
        val credential: PhoneAuthCredential = PhoneAuthProvider.getCredential(verificationId, code)
        // [END verify_with_code]
        signInWithPhoneAuthCredential(credential)
    }

    // [START resend_verification]
    private fun resendVerificationCode(phoneNumber: String,
                                       token: ForceResendingToken?) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,  // Phone number to verify
                60,  // Timeout duration
                TimeUnit.SECONDS,  // Unit of timeout
                this,  // Activity (for callback binding)
                mCallbacks,  // OnVerificationStateChangedCallbacks
                token) // ForceResendingToken from callbacks
    }

    // [END resend_verification]
    // [START sign_in_with_phone]
    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        mAuth?.signInWithCredential(credential)
                ?.addOnCompleteListener(this, object : OnCompleteListener<AuthResult?> {
                    public override fun onComplete(task: Task<AuthResult?>) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success")
                            val user: FirebaseUser? = task.getResult()?.getUser()
                            // [START_EXCLUDE]
                            updateUI(STATE_SIGNIN_SUCCESS, user)
                            // [END_EXCLUDE]
                        } else {
                            // Sign in failed, display a message and update the UI
                            Log.w(TAG, "signInWithCredential:failure", task.getException())
                            if (task.getException() is FirebaseAuthInvalidCredentialsException) {
                                // The verification code entered was invalid
                                // [START_EXCLUDE silent]
                                fieldVerificationCode?.setError("Invalid code.")
                                // [END_EXCLUDE]
                            }
                            // [START_EXCLUDE silent]
                            // Update UI
                            updateUI(STATE_SIGNIN_FAILED)
                            // [END_EXCLUDE]
                        }
                    }
                })
    }

    // [END sign_in_with_phone]
    private fun signOut() {
        mAuth?.signOut()
        updateUI(STATE_INITIALIZED)
    }

    private fun updateUI(user: FirebaseUser?) {
        if (user != null) {
            updateUI(STATE_SIGNIN_SUCCESS, user)
        } else {
            updateUI(STATE_INITIALIZED)
        }
    }

    private fun updateUI(uiState: Int, cred: PhoneAuthCredential?) {
        updateUI(uiState, null, cred)
    }

    private fun updateUI(uiState: Int, user: FirebaseUser? = mAuth?.getCurrentUser(), cred: PhoneAuthCredential? = null) {
        when (uiState) {
            STATE_INITIALIZED -> {
                // Initialized state, show only the phone number field and start button
                enableViews(buttonStartVerification, fieldPhoneNumber)
                disableViews(buttonVerifyPhone, buttonResend, fieldVerificationCode)
                detail?.setText(null)
            }
            STATE_CODE_SENT -> {
                // Code sent state, show the verification field, the
                enableViews(buttonVerifyPhone, buttonResend, fieldPhoneNumber, fieldVerificationCode)
                disableViews(buttonStartVerification)
                detail?.setText(R.string.status_code_sent)
            }
            STATE_VERIFY_FAILED -> {
                // Verification has failed, show all options
                enableViews(buttonStartVerification, buttonVerifyPhone, buttonResend, fieldPhoneNumber,
                        fieldVerificationCode)
                detail?.setText(R.string.status_verification_failed)
            }
            STATE_VERIFY_SUCCESS -> {
                // Verification has succeeded, proceed to firebase sign in
                disableViews(buttonStartVerification, buttonVerifyPhone, buttonResend, fieldPhoneNumber, fieldVerificationCode)
                detail?.setText(R.string.status_verification_succeeded)
                // Set the verification text based on the credential
                if (cred != null) {
                    if (cred.getSmsCode() != null) {
                        fieldVerificationCode?.setText(cred.getSmsCode())
                    } else {
                        fieldVerificationCode?.setText(R.string.instant_validation)
                    }
                }
            }
            STATE_SIGNIN_FAILED ->                 // No-op, handled by sign-in check
                detail?.setText(R.string.status_sign_in_failed)
            STATE_SIGNIN_SUCCESS -> {
            }
        }
        if (user == null) {
            // Signed out
            phoneAuthFields?.setVisibility(View.VISIBLE)
            status?.setText(R.string.signed_out)
        } else {
            // Signed in
            finish()

            phoneAuthFields?.setVisibility(View.GONE)
            enableViews(fieldPhoneNumber, fieldVerificationCode)
            fieldPhoneNumber.setText(null)
            fieldVerificationCode?.setText(null)
            status?.setText(R.string.signed_in)
            detail?.setText(getString(R.string.firebase_status_fmt, user.getUid()))
            val intent: Intent? = Intent(this@PhoneAuthActivity, MainActivity::class.java)
            SharedPref.Companion.getInstance(this@PhoneAuthActivity)?.putSharedPrefString(getString(R.string.userUid), user.getUid())
            SharedPref.Companion.getInstance(this@PhoneAuthActivity)?.putSharedPrefString(getString(R.string.userPhoneNumber), user.getPhoneNumber())
            startActivity(intent)
        }
    }

    private fun validatePhoneNumber(): Boolean {
        val phoneNumber: String? = fieldPhoneNumber.getText().toString()
        if (TextUtils.isEmpty(phoneNumber)) {
            fieldPhoneNumber.setError("Invalid phone number.")
            return false
        }
        return true
    }

    private fun enableViews(vararg views: View?) {
        for (v: View? in views) {
            v?.setEnabled(true)
        }
    }

    private fun disableViews(vararg views: View?) {
        for (v: View? in views) {
            v?.setEnabled(false)
        }
    }

    public override fun onClick(view: View?) {
        when (view?.getId()) {
            R.id.buttonStartVerification -> {
                if (!validatePhoneNumber()) {
                    return
                }
                startPhoneNumberVerification(getString(R.string.country_code) + fieldPhoneNumber.getText().toString())
            }
            R.id.buttonVerifyPhone -> {
                val code: String = fieldVerificationCode?.getText().toString()
                if (TextUtils.isEmpty(code)) {
                    fieldVerificationCode?.setError("Cannot be empty.")
                    return
                }
                verifyPhoneNumberWithCode(mVerificationId, code)
            }
            R.id.buttonResend -> resendVerificationCode(getString(R.string.country_code) + fieldPhoneNumber.getText().toString(), mResendToken)
        }
    }

    companion object {
        private val TAG: String? = "PhoneAuthActivity"
        private val KEY_VERIFY_IN_PROGRESS: String? = "key_verify_in_progress"
        private val STATE_INITIALIZED: Int = 1
        private val STATE_CODE_SENT: Int = 2
        private val STATE_VERIFY_FAILED: Int = 3
        private val STATE_VERIFY_SUCCESS: Int = 4
        private val STATE_SIGNIN_FAILED: Int = 5
        private val STATE_SIGNIN_SUCCESS: Int = 6
    }
}