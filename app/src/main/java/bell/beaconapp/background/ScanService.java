package bell.beaconapp.background;

import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanFilter;
import android.bluetooth.le.ScanRecord;
import android.bluetooth.le.ScanResult;
import android.bluetooth.le.ScanSettings;
import android.content.ComponentName;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import java.nio.ByteBuffer;
import java.util.UUID;

import bell.beaconapp.model.Beacon;
import bell.beaconapp.model.BeaconResult;

/**
 * Created by Florian Schlösser on 22.02.2017.
 */
public class ScanService extends Service {

    public final static String TAG = ScanService.class.getSimpleName();

    public final static String ACTION_SCAN = ScanService.class.getName() + ".ACTION_SCAN";

    /* BLUETOOTH */
    private ScanFilter mScanFilter;
    private BluetoothAdapter mBluetoothAdapter;
    private BluetoothLeScanner mBluetoothScanner;
    private ScanSettings mScanSettings;
    private ScanServiceCallback mScanServiceCallback;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate() - created service.");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand() - starting command with action: " + intent.getAction());
        if (intent.getAction().equals(ACTION_SCAN)) {
            mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
            mBluetoothScanner = mBluetoothAdapter.getBluetoothLeScanner();

            setScanSettings();
            setScanFilter();
            mBluetoothScanner.startScan(mScanCallback);
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy() - destroying service.");
    }

    public void setScanServiceCallback (ScanServiceCallback scanServiceCallback) {
        mScanServiceCallback = scanServiceCallback;
    }

    public ScanCallback mScanCallback = new ScanCallback() {
        @Override
        public void onScanResult(int callbackType, ScanResult result) {
            ScanRecord mScanRecord = result.getScanRecord();
            if (mScanRecord.getDeviceName() != null && mScanRecord.getManufacturerSpecificData(76) != null) {
                byte[] mBytes = mScanRecord.getBytes();
                int mRssi = result.getRssi();
                byte[] manData = mScanRecord.getManufacturerSpecificData(76);
                BeaconResult res = new BeaconResult(manData, mRssi, mScanRecord.getDeviceName());

                Log.d(TAG, "Scan Beacon! " + mScanRecord.getDeviceName() + ":");
                Log.d(TAG, res.getManId() + ":" + res.getUuid());
                Log.d(TAG, res.getMajor() + ":" + res.getMinor());
                Log.d(TAG, res.getRssi() + "/" + res.getTxPower());
                Log.d(TAG, res.getDistance() + "m");
                Log.d(TAG, toHexString(mScanRecord.getManufacturerSpecificData(76)));

                mScanServiceCallback.onBeaconFound(res);
            }
        }
    };

    private void setScanFilter() {
        ScanFilter.Builder mBuilder = new ScanFilter.Builder();
        ByteBuffer mManufacturerData = ByteBuffer.allocate(23);
        ByteBuffer mManufacturerDataMask = ByteBuffer.allocate(24);
        byte[] uuid = getIdAsByte(UUID.fromString("E2C56DB5-DFFB-48D2-B060-D0F5A71096E2"));
        mManufacturerData.put(0, (byte) 0x02);
        mManufacturerData.put(1, (byte) 0x15);
        for (int i = 2; i <= 17; i++) {
            mManufacturerData.put(i, uuid[i - 2]);
        }
        for (int i = 0; i <= 17; i++) {
            mManufacturerDataMask.put((byte) 0x01);
        }
        mBuilder.setManufacturerData(76, mManufacturerData.array(), mManufacturerDataMask.array());
        mScanFilter = mBuilder.build();
    }

    private void setScanSettings() {
        ScanSettings.Builder mBuilder = new ScanSettings.Builder();
        mBuilder.setReportDelay(0);
        mBuilder.setScanMode(ScanSettings.SCAN_MODE_LOW_POWER);
        mScanSettings = mBuilder.build();
    }

    private byte[] getIdAsByte(UUID uuid) {
        ByteBuffer bb = ByteBuffer.wrap(new byte[16]);
        bb.putLong(uuid.getMostSignificantBits());
        bb.putLong(uuid.getLeastSignificantBits());
        return bb.array();

    }

    private String toHexString(byte[] bytes) {
        StringBuilder hexString = new StringBuilder();

        for (int i = 0; i < bytes.length; i++) {
            String hex = Integer.toHexString(0xFF & bytes[i]);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }

        return hexString.toString();
    }

    public interface ScanServiceCallback {
        void onBeaconFound(BeaconResult beacon);
    }
}
