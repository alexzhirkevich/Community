package github.alexzhirkevich.community.core.repo.imp

import android.app.Activity
import github.alexzhirkevich.community.core.repo.interfaces.*
import com.google.firebase.FirebaseException
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton
import com.google.firebase.auth.PhoneAuthProvider as FirebasePhoneAuthProvider

class PhoneAuthRepositoryImp @Inject constructor(
    private val usersRepository: UsersRepository,
    ) : AuthRepositoryImp(usersRepository), PhoneAuthRepository {

    private val callbacks: MutableMap<String, FirebasePhoneAuthCallback> =
        ConcurrentHashMap()

    override fun sendCode(activity: Activity, phone: String, callback: PhoneAuthCallback) {

        val fpCallback = callbacks[phone] ?: FirebasePhoneAuthCallback(phone,callback).also {
            callbacks[phone] = it
        }

        val options = PhoneAuthOptions.newBuilder(FirebaseAuth.getInstance())
            .setPhoneNumber(phone)
            .setTimeout(60L, TimeUnit.SECONDS)
            .setActivity(activity)
            .setCallbacks(fpCallback)
            .apply {
                fpCallback.forceResendingToken?.let {
                    setForceResendingToken(it)
                }
            }
            .build()
        FirebasePhoneAuthProvider.verifyPhoneNumber(options)
    }

    override fun cancel(phone : String) {
        callbacks.remove(phone)
    }

    override fun verifyCode(phone: String, code: String) {

        val callback = callbacks[phone] ?: return

        if (callback.verificationId == null) {
            callback.onError(Exception("Could not find verificationId"))
            return
        }

        val creds = FirebasePhoneAuthProvider
            .getCredential(callback.verificationId!!, code)
        signIn(creds, callback)
    }

    private fun signIn(creds: AuthCredential, callback: FirebasePhoneAuthCallback) {

        FirebaseAuth.getInstance().signInWithCredential(creds)
            .addOnSuccessListener {

                callbacks.remove(callback.phone)

                callback.onSuccess(
                    SignedInUserImp(
                        it.user!!.uid,
                        it.additionalUserInfo?.isNewUser ?: false
                    )
                )
            }
            .addOnFailureListener { err ->
                callback.onError(err)
            }
    }

    inner class FirebasePhoneAuthCallback(
        val phone: String,
        uiCallback: PhoneAuthCallback,
    ) : PhoneAuthCallback by uiCallback,
        FirebasePhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        var verificationId: String? = null
        var forceResendingToken: FirebasePhoneAuthProvider.ForceResendingToken? = null

        override fun onVerificationCompleted(pac: PhoneAuthCredential) {
            signIn(pac, this)
            callbacks.remove(phone)
        }

        override fun onVerificationFailed(err: FirebaseException) {
            onError(err)
        }

        override fun onCodeSent(
            vId: String,
            token: FirebasePhoneAuthProvider.ForceResendingToken
        ) {
            super.onCodeSent(vId, token)
            verificationId = vId
            forceResendingToken = token
            onCodeSend()
        }
    }
}
