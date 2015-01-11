package net.ericchu.reconnect;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class NetworkService extends Service {
    private static String LOG_TAG = "NetworkService";

    @Override
    public void onCreate() {
        Log.v(LOG_TAG, "Service created");
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.v(LOG_TAG, "Service bound");
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.v(LOG_TAG, "Service started");

        // TODO verify return values?
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        Log.v(LOG_TAG, "Service destroyed");
    }
}
