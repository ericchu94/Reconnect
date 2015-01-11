package net.ericchu.reconnect;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import java.io.IOException;
import java.net.Socket;

public class NetworkService extends Service implements Runnable {
    private static final String LOG_TAG = "NetworkService";
    private static final String SERVER_HOSTNAME = "home.ericchu.net";
    private static final int SERVER_PORT = 1864;
    private static final int SERVER_RETRY = 5;
    private static final int STATE_IDLE_ONLINE_CONNECTING = 0;
    private static final int STATE_IDLE_ONLINE_CONNECTED = 1;
    private static final int STATE_IDLE_ONLINE_FAILED = 2;

    private final IBinder mBinder = new NetworkBinder();
    private int mState = STATE_IDLE_ONLINE_CONNECTING;
    private Socket mClient;
    private Thread mThread = new Thread(this);

    @Override
    public void onCreate() {
        Log.v(LOG_TAG, "Service created");

        mThread.start();
    }

    @Override
    public void run() {
        while (true) {
            switch (mState) {
                case STATE_IDLE_ONLINE_CONNECTING:
                    connect();
                    break;
                case STATE_IDLE_ONLINE_FAILED:
                    retryDelay();
                    break;
                default:
                    return;
            }
        }
    }

    private void retryDelay() {
        try {
            Thread.sleep(SERVER_RETRY * 1000);
        } catch (InterruptedException e) {
            Log.w(LOG_TAG, "Thread.sleep() failed");
        }
        mState = STATE_IDLE_ONLINE_CONNECTING;
    }

    private void connect() {
        try {
            Log.v(LOG_TAG, "Connecting...");
            mClient = new Socket(SERVER_HOSTNAME, SERVER_PORT);
            mState = STATE_IDLE_ONLINE_CONNECTED;
            Log.v(LOG_TAG, "Connection connected");
        } catch (IOException e) {
            mState = STATE_IDLE_ONLINE_FAILED;
            Log.v(LOG_TAG, "Connection failed");
        }
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
