package github.alexzhirkevich.community.core.providers.interfaces

import android.app.Activity

interface PhoneAuthProvider : AuthProvider {
    fun sendCode(activity: Activity, phone: String)
    fun verifyCode(code: String)
}

interface SignedInUser {
    val uid : String
    val isNew : Boolean
}

data class SignedInUserImp(override val uid: String, override val isNew: Boolean) : SignedInUser

interface PhoneAuthCallback{
    fun onCodeSend() {}
    fun onSuccess(signedInUser: SignedInUser) {}
    fun onError(t : Throwable) {}
}