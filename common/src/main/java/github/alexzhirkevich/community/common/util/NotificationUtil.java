//package github.alexzhirkevich.community.common.util;
//
//import android.app.NotificationChannel;
//import android.app.NotificationManager;
//import android.app.PendingIntent;
//import android.content.Context;
//import android.content.Intent;
//import android.graphics.Bitmap;
//import android.media.AudioAttributes;
//import android.media.RingtoneManager;
//import android.os.Build;
//
//import androidx.core.app.NotificationCompat;
//import androidx.core.app.NotificationManagerCompat;
//
//import github.alexzhirkevich.community.common.R;
//
//
//public class NotificationUtil {
//
//    private final Context context;
//    private final NotificationCompat.Builder builder;
//
//
//    private NotificationUtil(Context context){
//        this.context = context;
//        builder = new NotificationCompat.Builder(context,context.getString(R.string.app_name));
//        builder.setPriority(NotificationCompat.PRIORITY_DEFAULT);
//        builder.setSmallIcon(R.drawable.logo);
//        builder.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));
//    }
//
//    public static NotificationUtil with(Context context){
//        return new NotificationUtil(context);
//    }
//
//    public NotificationUtil setTitle(String title){
//        builder.setContentTitle(title);
//        return this;
//    }
//    public NotificationUtil setText(String text){
//        builder.setContentText(text);
//        return this;
//    }
//
//    public NotificationUtil setIcon(Bitmap icon){
//        builder.setLargeIcon(icon);
//        return this;
//    }
//
//    public NotificationUtil setIntent(Intent intent){
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//        builder.setContentIntent(PendingIntent.getActivity(context,0,intent, PendingIntent.FLAG_UPDATE_CURRENT));
//        return this;
//    }
//
//    public NotificationUtil setAutoCancel(boolean autoCancel){
//        builder.setAutoCancel(autoCancel);
//        return this;
//    }
//
//    public void execute(int id){
//        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            builder.setChannelId(context.getPackageName());
//        }
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            NotificationChannel channel = new NotificationChannel(
//                    context.getPackageName(),
//                    context.getString(R.string.app_name),
//                    NotificationManager.IMPORTANCE_DEFAULT
//            );
//            channel.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION),
//                    new AudioAttributes.Builder().build());
//            notificationManager.createNotificationChannel(channel);
//        }
//        notificationManager.notify(id, builder.build());
//    }
//}
