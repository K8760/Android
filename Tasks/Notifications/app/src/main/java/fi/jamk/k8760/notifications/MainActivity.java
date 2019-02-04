package fi.jamk.k8760.notifications;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.os.Build;
import android.os.Bundle;
import android.app.Activity;
import android.app.DialogFragment;
import android.support.v4.app.NotificationCompat;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends Activity implements CustomDialog.CustomListener {

    private int notification_id = 1;
    private final String NOTIFICATION_CHANNEL_ID = "boo_chanel";
    private final String NOTIFICATION_CHANNEL_NAME = "boo notifications";
    private final String NOTIFICATION_CHANNEL_DESCRIPTION = "Boo description";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void customDialog(View view) {
        CustomDialog eDialog = new CustomDialog();
        eDialog.show(getFragmentManager(), "What is your name?");
    }

    @Override
    public void onDialogPositiveClick(DialogFragment dialog, String name) {
        Toast.makeText(getApplicationContext(), "Hello " + name, Toast.LENGTH_SHORT).show();
        createNotif(name);
    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {
        Toast.makeText(getApplicationContext(), "Cancel", Toast.LENGTH_SHORT).show();
    }

    public void createNotif(String name){
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        //version checking
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, NOTIFICATION_CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT);
            notificationChannel.setDescription(NOTIFICATION_CHANNEL_DESCRIPTION);
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.YELLOW);
            notificationChannel.setVibrationPattern(new long[]{0,1000,500,1000});
            notificationChannel.enableVibration(true);

            notificationManager.createNotificationChannel((notificationChannel));
        }

        //build notification
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
                .setContentTitle("Greetings")
                .setContentText(getString(R.string.notif_text) + " " + name)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setAutoCancel(true)
                .setSmallIcon(R.drawable.ic_sentiment_satisfied_24dp);
        notificationManager.notify(notification_id, builder.build());
    }
}
