package github.alexzhirkevich.community.core.providers.imp

import android.app.Activity
import github.alexzhirkevich.community.core.providers.interfaces.*
import com.google.firebase.FirebaseException
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton
import com.google.firebase.auth.PhoneAuthProvider as FirebasePhoneAuthProvider

@Singleton
class PhoneAuthProviderImp @Inject constructor(
    private val usersProvider: UsersProvider,
    private val callback : PhoneAuthCallback?
    ) : AuthProviderImp(usersProvider), PhoneAuthProvider {


    override fun sendCode(activity: Activity, phone: String) {
        val options = PhoneAuthOptions.newBuilder(FirebaseAuth.getInstance())
                .setPhoneNumber(phone)
                .setTimeout(60L, TimeUnit.SECONDS)
                .setActivity(activity)
                .setCallbacks(mCallback)
                .apply {
                    forceResendingToken?.let { setForceResendingToken(it) }
                }
                .build()
        FirebasePhoneAuthProvider.verifyPhoneNumber(options)
    }

    override fun verifyCode(code: String) {
        if (verificationId == null){
            callback?.onError(Exception("Could not find verificationId"))
        }
        val creds =  FirebasePhoneAuthProvider.getCredential(verificationId!!, code)
        signIn(creds)
    }



    private var forceResendingToken: FirebasePhoneAuthProvider.ForceResendingToken? = null
    private var verificationId: String? = null
    private var mCallback = object : FirebasePhoneAuthProvider.OnVerificationStateChangedCallbacks()  {

        override fun onVerificationCompleted(pac: PhoneAuthCredential) {
            signIn(pac)
        }

        override fun onVerificationFailed(ex: FirebaseException) {
            this@PhoneAuthProviderImp.callback?.onError(ex)
        }

        override fun onCodeSent(
            verifId: String,
            frt: FirebasePhoneAuthProvider.ForceResendingToken
        ) {
            super.onCodeSent(verifId, frt)
            verificationId = verifId
            forceResendingToken = frt
            callback?.onCodeSend()
        }
    }

    private fun signIn(creds : AuthCredential) {
        FirebaseAuth.getInstance().signInWithCredential(creds)
                .addOnSuccessListener {
                    callback?.onSuccess(SignedInUserImp(it.user!!.uid,
                        it.additionalUserInfo?.isNewUser ?: false))
                }
                .addOnFailureListener { err -> callback?.onError(err) }
    }
}

