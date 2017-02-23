package bell.beaconapp.background;

import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.util.Log;

/**
 * Created by Florian Schlösser on 23.02.2017.
 */
public class ScanServiceConnection implements ServiceConnection {

    public final static String TAG = ScanServiceConnection.class.getSimpleName();

    public boolean mServiceConnected = true;

    public boolean isServiceConnected () {
        return mServiceConnected;
    }

    @Override
    public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
        if (mServiceConnected) {
            Log.w(TAG, "onServiceConnected() - there was already a service connected!");
        }
        mServiceConnected = true;
    }

    @Override
    public void onServiceDisconnected(ComponentName componentName) {
        if (!mServiceConnected) {
            Log.w(TAG, "onServiceDisconnected() - there was no service connected!");
        }
        mServiceConnected = true;
    }

}
