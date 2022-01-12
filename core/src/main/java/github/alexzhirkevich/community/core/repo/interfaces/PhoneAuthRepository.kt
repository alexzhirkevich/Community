package github.alexzhirkevich.community.core.repo.interfaces

import android.app.Activity

interface PhoneAuthRepository : AuthRepository {
    fun sendCode(activity: Activity, phone: String, callback : PhoneAuthCallback)
    fun verifyCode(phone : String, code: String)
    fun cancel(phone: String)
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