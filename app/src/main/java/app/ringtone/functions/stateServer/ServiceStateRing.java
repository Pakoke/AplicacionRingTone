package app.ringtone.functions.stateServer;


import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.provider.SyncStateContract;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.RemoteViews;

import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import app.pacoke.aplicacionringtone.R;
import app.ringtone.FunctionsSystem;
import cz.msebera.android.httpclient.Header;
import dtos.RingToneRestClient;
import factories.IntentFactory;


public class ServiceStateRing extends Service {
    private static final String TAG = ServiceStateRing.class.getSimpleName();
    TimerTask timerTask;
    public JSONObject responsejson = null;
    public ServiceStateRing(){}

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        Log.d(TAG, "Servicio creado...");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "Servicio iniciado...");


        final ActivityManager.MemoryInfo memoryInfo = new ActivityManager.MemoryInfo();
        final ActivityManager activityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);


        Timer timer = new Timer();

        timerTask = new TimerTask() {
            @Override
            public void run() {

                String direccion="cameramethods/stateRing/";
                Boolean result=false;
                try{
                    //RingToneRestClient.post();
                    RingToneRestClient.get(direccion, null, new JsonHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                            // If the response is JSONObject instead of expected JSONArray
                            responsejson = response;
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                            Log.i(ServiceStateRing.class.getName(),"Ha fallado la llamada de la notificacion al servicio");
                        }

                        @Override
                        public boolean getUseSynchronousMode() {
                            return false;
                        }

                    });
                }catch (Exception e){
                    Log.d("Excepcion", e.toString());
                }
                //Log.d("http",responsejson.toString());

                if(responsejson==null){
                    result=false;
                }else{
                    try {
                        Boolean temp=(Boolean) responsejson.get("statering");
                        result = temp;
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                String name = "Javier";
                if(result)
                {
                    startNotification(name);
                    responsejson = null;
                }
            }
        };

        timer.scheduleAtFixedRate(timerTask, 0, 5000);

        return START_NOT_STICKY;
    }

    private void startNotification(String name) {
        // BEGIN_INCLUDE(notificationCompat)
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        // END_INCLUDE(notificationCompat)

        // BEGIN_INCLUDE(intent)
        //Create Intent to launch this Activity again if the notification is clicked.
        Intent i = IntentFactory.createIntent(this,FunctionsSystem.class);// new Intent(this, FunctionsSystem.class);
        /*try{
           i = IntentFactory.getFactory(FunctionsSystem.class.getName());//
        }catch (Exception e){
            Log.e(e.getMessage(),"El Intent para la clase "+FunctionsSystem.class.getName()+" no existe");
        }*/

        i.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent intent = PendingIntent.getActivity(this, 0, i,PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(intent);
        // END_INCLUDE(intent)

        // BEGIN_INCLUDE(ticker)
        // Sets the ticker text
        builder.setTicker(getResources().getString(R.string.custom_notification));

        // Sets the small icon for the ticker
        builder.setSmallIcon(R.mipmap.ic_launcher);
        // END_INCLUDE(ticker)

        // BEGIN_INCLUDE(buildNotification)
        // Cancel the notification when clicked
        builder.setAutoCancel(true);

        // Build the notification
        Notification notification = builder.build();
        // END_INCLUDE(buildNotification)

        // BEGIN_INCLUDE(customLayout)
        // Inflate the notification layout as RemoteViews
        RemoteViews contentView = new RemoteViews(getPackageName(), R.layout.notification);

        // Set text on a TextView in the RemoteViews programmatically.
        final String time = DateFormat.getTimeInstance().format(new Date()).toString();
        final String text = getResources().getString(R.string.collapsed, time);
        contentView.setTextViewText(R.id.textView, text);

        /* Workaround: Need to set the content view here directly on the notification.
         * NotificationCompatBuilder contains a bug that prevents this from working on platform
         * versions HoneyComb.
         * See https://code.google.com/p/android/issues/detail?id=30495
         */
        notification.contentView = contentView;

        // Add a big content view to the notification if supported.
        // Support for expanded notifications was added in API level 16.
        // (The normal contentView is shown when the notification is collapsed, when expanded the
        // big content view set here is displayed.)
        if (Build.VERSION.SDK_INT >= 16) {
            // Inflate and set the layout for the expanded notification view
            RemoteViews expandedView =
                    new RemoteViews(getPackageName(), R.layout.notification_expanded);
            notification.bigContentView = expandedView;
        }
        // END_INCLUDE(customLayout)

        // START_INCLUDE(notify)
        // Use the NotificationManager to show the notification
        NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        nm.notify(0, notification);
        // END_INCLUDE(notify)
    }

    @Override
    public void onDestroy() {
        timerTask.cancel();
        //Intent localIntent = new Intent(SyncStateContract.Constants.ACTION_MEMORY_EXIT);
        //IntentFactory.getFactory()
        // Emitir el intent a la actividad
        //LocalBroadcastManager.getInstance(ServiceStateRing.this).sendBroadcast(localIntent);
        Log.d(TAG, "Servicio destruido...");
    }

}
