package github.alexzhirkevich.community.common.util;

import android.Manifest;
import android.content.Context;
import android.media.AudioAttributes;
import android.os.Build;
import android.os.VibrationEffect;
import android.os.Vibrator;

import androidx.annotation.RequiresApi;
import androidx.annotation.RequiresPermission;

public class VibrateUtil {

    private final Vibrator vibrator;

    public static final float POWER_LOW = 0.1f;
    public static final float POWER_MEDUIM = 1f;
    public static final float POWER_HIGH = 3f;

    private VibrationEffect effect;
    private AudioAttributes attrs;

    private VibrateUtil(Vibrator v){
        this.vibrator = v;
        effect = null;
        attrs = null;
    }

    public static VibrateUtil with(Context context){
        return new VibrateUtil ((Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE));
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @RequiresPermission(value = Manifest.permission.VIBRATE)
    public VibrateUtil setEffect(VibrationEffect effect){
        this.effect =effect;
        return this;
    }

    public VibrateUtil setAudioAttributes(AudioAttributes audioAttributes){
        this.attrs = audioAttributes;
        return this;
    }

    @RequiresPermission(value = Manifest.permission.VIBRATE)
    @RequiresApi(api = Build.VERSION_CODES.O)
    public VibrateUtil vibrate(){
        if (effect!=null && vibrator != null && vibrator.hasVibrator()){
            if (attrs != null)
                vibrator.vibrate(effect,attrs);
            else
                vibrator.vibrate(effect);
        }
        return this;
    }

    @RequiresPermission(value = Manifest.permission.VIBRATE)
    public VibrateUtil vibrate(int ms){
        if (vibrator != null && vibrator.hasVibrator()){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                if (attrs != null)
                    vibrator.vibrate(VibrationEffect.createOneShot(ms,VibrationEffect.DEFAULT_AMPLITUDE),attrs);
                else
                    vibrator.vibrate(VibrationEffect.createOneShot(ms,VibrationEffect.DEFAULT_AMPLITUDE));
            }else
                vibrator.vibrate(ms);
        }
        return this;
    }

    @RequiresPermission(value = Manifest.permission.VIBRATE)
    public VibrateUtil vibrate(int ms, float power){
        if (vibrator != null && vibrator.hasVibrator()){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                vibrator.vibrate(VibrationEffect.createWaveform(new long[]{ms},new int[]{(int)(power*10)},-1));
            }else
                vibrator.vibrate(ms);
        }
        return this;
    }
}
