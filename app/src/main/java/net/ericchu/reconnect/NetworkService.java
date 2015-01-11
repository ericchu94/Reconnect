package net.ericchu.reconnect;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

public class NetworkService extends Service {
    private static String LOG_TAG = "NetworkService";

    private final IBinder mBinder = new NetworkBinder();

    @Override
    public void onCreate() {
        Log.v(LOG_TAG, "Service created");
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.v(LOG_TAG, "Service bound");
        return mBinder;
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

    public class NetworkBinder extends Binder {
        NetworkService getService() {
            return NetworkService.this;
        }
    }
}
