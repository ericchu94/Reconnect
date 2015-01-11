package net.ericchu.reconnect;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;


public class MainActivity extends ActionBarActivity implements NameDialogFragment.NameDialogListener {
    private static final String LOG_TAG = "MainActivity";
    private static final String PREF_NAME = "name";
    private static final String FRAG_NAME_DIALOG = "NameDialog";

    private TextView mName;
    private SharedPreferences mSharedPreferences;

    private ServiceConnection mConn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.v(LOG_TAG, "Service connected");
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.v(LOG_TAG, "Service disconnected");
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mName = (TextView) findViewById(R.id.name);
        mSharedPreferences = getPreferences(MODE_PRIVATE);

        restorePreferences();
    }

    private void restorePreferences() {
        mName.setText(mSharedPreferences.getString(PREF_NAME, ""));
    }

    private void savePreferences() {
        SharedPreferences.Editor editor = mSharedPreferences.edit();

        editor.putString(PREF_NAME, mName.getText().toString());

        editor.commit();
    }

    @Override
    protected void onResume() {
        super.onResume();

        Intent intent = new Intent(this, NetworkService.class);
        startService(intent);
        // TODO research flags
        bindService(intent, mConn, BIND_AUTO_CREATE);

        if (mName.getText().toString().equals("") && !isShowingNameDialog()) {
            showNameDialog();
        }
    }

    private boolean isShowingNameDialog() {
        return getFragmentManager().findFragmentByTag(FRAG_NAME_DIALOG) != null;
    }

    private void showNameDialog() {
        Bundle args = new Bundle();
        args.putString(NameDialogFragment.ARGS_NAME, mName.getText().toString());

        NameDialogFragment nameDialogFragment = new NameDialogFragment();
        nameDialogFragment.setArguments(args);
        nameDialogFragment.show(getFragmentManager(), FRAG_NAME_DIALOG);
    }

    @Override
    public void onNameChange(String name) {
        mName.setText(name);
    }

    @Override
    protected void onPause() {
        super.onPause();

        Intent intent = new Intent(this, NetworkService.class);
        unbindService(mConn);
        stopService(intent);

        savePreferences();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch (id) {
            case R.id.action_settings:
                return true;
            case R.id.action_name:
                showNameDialog();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
