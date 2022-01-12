//package github.alexzhirkevich.community.common.util
//
//import android.Manifest
//import android.content.Context
//import android.media.AudioAttributes
//import android.os.*
//import androidx.annotation.RequiresApi
//import androidx.annotation.RequiresPermission
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.remember
//import androidx.compose.ui.platform.LocalContext
//import androidx.core.content.ContextCompat
//
//@RequiresApi(Build.VERSION_CODES.Q)
//enum class VibratorType(type : Int){
//    Click(VibrationEffect.EFFECT_CLICK),
//    DoubleClick(VibrationEffect.EFFECT_DOUBLE_CLICK),
//    HeavyClick(VibrationEffect.EFFECT_HEAVY_CLICK),
//    Tick(VibrationEffect.EFFECT_TICK),
//}
//
//@Composable
//fun rememberVibrator(type: VibratorType?=null) : Vibrator = remember {
//
//}
//
//
//interface Vibrator {
//    fun vibrate(): Boolean
//
//    companion object {
//
//        @Composable
//        fun click(): Vibrator =
//            VibratorImpl(
//                LocalContext.current,
//                100,
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q)
//                    VibrationEffect.createPredefined(
//                        VibrationEffect.EFFECT_CLICK
//                    ) else null,
//            )
//
//        @Composable
//        fun doubleClick() : Vibrator =
//            VibratorImpl(
//                LocalContext.current,
//                100,
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q)
//                    VibrationEffect.createPredefined(
//                        VibrationEffect.EFFECT_DOUBLE_CLICK
//                    ) else null,
//            )
//    }
//}
//
//class VibratorImpl(
//    context: Context,
//    ms : Long,
//    var effect: VibrationEffect? = null,
//) : Vibrator{
//
//    protected val service = (ContextCompat.getSystemService(
//        context,android.os.Vibrator::class.java
//    ))
//
//    override fun vibrate() : Boolean{
//        if (service == null || !service.hasVibrator())
//            return false
//        when {
//            effect != null && Build.VERSION.SDK_INT >= Build.VERSION_CODES.O ->
//                service.vibrate(effect)
//            else -> service.vibra
//        }
//        return true
//    }
//
//}
//
//class VibrateUtil private constructor(
//    private val context: Context,
//    var effect: VibrationEffect? = null,
//    var attrs: AudioAttributes? = null
//) : Vibrator {
//
//    @Suppress("deprecation")
//    @RequiresPermission(value = Manifest.permission.VIBRATE)
//    fun vibrate(
//        ms : Long,
//        effect: VibrationEffect?=null,
//        audioAttributes: AudioAttributes?=null) {
//        when {
//            effect != null && audioAttributes != null &&
//                    Build.VERSION.SDK_INT >= Build.VERSION_CODES.O ->
//                vibrator.vibrate(effect, audioAttributes)
//            effect != null &&
//                    Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q ->
//                vibrator.vibrate(effect)
//            Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q ->
//                vibrator.vibrate(
//                    VibrationEffect.createOneShot(
//                        ms,
//                        VibrationEffect.DEFAULT_AMPLITUDE
//                    )
//                )
//            else -> vibrator.vibrate(ms)
//        }
//    }
//
//    @RequiresPermission(value = Manifest.permission.VIBRATE)
//    @RequiresApi(api = Build.VERSION_CODES.O)
//    fun vibrate(): VibrateUtil {
//        if (effect != null) {
//            vibrator.vibrate()
//            if (attrs != null) vibrator.vibrate(effect, attrs) else vibrator.vibrate(effect)
//        }
//        return this
//    }
//
//    @RequiresPermission(value = Manifest.permission.VIBRATE)
//    fun vibrate(ms: Int): VibrateUtil {
//        if (vibrator.hasVibrator()) {
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
//                if (attrs != null) vibrator.vibrate(
//                    VibrationEffect.createOneShot(
//                        ms.toLong(),
//                        VibrationEffect.DEFAULT_AMPLITUDE
//                    ), attrs
//                ) else vibrator.vibrate(
//                    VibrationEffect.createOneShot(
//                        ms.toLong(),
//                        VibrationEffect.DEFAULT_AMPLITUDE
//                    )
//                )
//            } else vibrator.vibrate(ms.toLong())
//        }
//        return this
//    }
//
//    @RequiresPermission(value = Manifest.permission.VIBRATE)
//    fun vibrate(ms: Int, power: Float): VibrateUtil {
//        if (vibrator.hasVibrator()) {
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
//                vibrator.vibrate(
//                    VibrationEffect.createWaveform(
//                        longArrayOf(ms.toLong()), intArrayOf(
//                            (power * 10).toInt()
//                        ), -1
//                    )
//                )
//            } else vibrator.vibrate(ms.toLong())
//        }
//        return this
//    }
//
//    companion object {
//        const val POWER_LOW = 0.1f
//        const val POWER_MEDUIM = 1f
//        const val POWER_HIGH = 3f
//        fun with(context: Context): VibrateUtil {
//            return VibrateUtil(context.getSystemService(Context.VIBRATOR_SERVICE,Vibrator::class.java) as Vibrator)
//            return VibrateUtil(
//                ContextCompat.getSystemService(context,VibratorManager::class.java)
//            )
//        }
//    }
//}