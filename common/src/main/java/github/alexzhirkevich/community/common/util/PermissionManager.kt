package github.alexzhirkevich.community.common.util

import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment

interface PermissionManager {
    fun has(permission: String) : Boolean
    fun shouldShowEducationalDialog(activity: Activity, permission: String) : Boolean
    fun request(activity: Activity, requestCode: Int,vararg permissions: String)
    fun request(fragment : Fragment, requestCode: Int,vararg permissions: String)
}

private class PermissionManagerImpl(private val context: Context) : PermissionManager {

    override fun shouldShowEducationalDialog(activity: Activity, permission: String): Boolean =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            activity.shouldShowRequestPermissionRationale(permission)
        } else {
            true
        }

    override fun has(permission: String): Boolean =
            ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED

    override fun request(fragment: Fragment, requestCode: Int,vararg permissions: String) {
        fragment.requestPermissions(permissions, requestCode)
    }

    override fun request(activity: Activity, requestCode: Int,vararg permissions: String){
        ActivityCompat.requestPermissions(activity, permissions, requestCode)
    }
}

val Fragment.permissions : PermissionManager
    get() = PermissionManagerImpl(requireContext())

val Context.permissions : PermissionManager
    get() = PermissionManagerImpl(this)