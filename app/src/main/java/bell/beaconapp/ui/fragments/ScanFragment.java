package bell.beaconapp.ui.fragments;

import android.Manifest;
import android.app.NotificationManager;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanFilter;
import android.bluetooth.le.ScanRecord;
import android.bluetooth.le.ScanResult;
import android.bluetooth.le.ScanSettings;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.nio.ByteBuffer;
import java.util.UUID;

import bell.beaconapp.R;
import bell.beaconapp.model.Beacon;
import bell.beaconapp.ui.adapter.BeaconListAdapter;

/**
 * Created by Florian Schlösser on 08.02.2017.
 */
public class ScanFragment extends Fragment implements BeaconListAdapter.OnBeaconAddedListener {

    public final static String TAG = ScanFragment.class.getSimpleName();

    /* UI */
    private RecyclerView mBeaconsList;

    /* ADAPTER */
    private BeaconListAdapter mBeaconListAdapter;

    /* BLUETOOTH */
    private ScanFilter mScanFilter;
    private BluetoothAdapter mBluetoothAdapter;
    private BluetoothLeScanner mBluetoothScanner;
    private ScanSettings mScanSettings;

    public ScanCallback mScanCallback = new ScanCallback() {
        @Override
        public void onScanResult(int callbackType, ScanResult result) {
            ScanRecord mScanRecord = result.getScanRecord();
            if (mScanRecord.getDeviceName() != null && mScanRecord.getManufacturerSpecificData(76) != null) {
                byte[] mBytes = mScanRecord.getBytes();
                int mRssi = result.getRssi();
                byte[] manData = mScanRecord.getManufacturerSpecificData(76);
                Beacon res = new Beacon(manData, mRssi, mScanRecord.getDeviceName());
                Log.d(TAG, "Scan Beacon! " + mScanRecord.getDeviceName() + ":");
                Log.d(TAG, res.getManId() + ":" + res.getUuid());
                Log.d(TAG, res.getMajor() + ":" + res.getMinor());
                Log.d(TAG, res.getRssi() + "/" + res.getTxPower());
                Log.d(TAG, res.getDistance() + "m");
                Log.d(TAG, toHexString(mScanRecord.getManufacturerSpecificData(76)));
                mBeaconListAdapter.add(res);
            }
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_scan, container, false);
        mBeaconsList = (RecyclerView) root.findViewById(R.id.scan_recycler);

        mBeaconListAdapter = new BeaconListAdapter(getContext());
        mBeaconListAdapter.setOnBeaconAddedListener(this);

        mBeaconsList.setLayoutManager(new LinearLayoutManager(getContext()));
        mBeaconsList.setAdapter(mBeaconListAdapter);

        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        mBluetoothScanner = mBluetoothAdapter.getBluetoothLeScanner();

        setScanSettings();
        setScanFilter();

        mBluetoothScanner.startScan(mScanCallback);

        return root;
    }

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

    @Override
    public void onBeaconAdded(final Beacon beacon) {
        Snackbar.make(getView(), R.string.snackbar_beacon_added, Snackbar.LENGTH_LONG).setAction(R.string.action_expand, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mBeaconListAdapter.setExpanded(beacon);
            }
        }).show();
    }
}
