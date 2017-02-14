package bell.beaconapp.ui.fragments;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanFilter;
import android.bluetooth.le.ScanRecord;
import android.bluetooth.le.ScanResult;
import android.bluetooth.le.ScanSettings;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/*import com.lemmingapex.trilateration.NonLinearLeastSquaresSolver;
import com.lemmingapex.trilateration.TrilaterationFunction;

import org.apache.commons.math3.fitting.leastsquares.LeastSquaresOptimizer;
import org.apache.commons.math3.fitting.leastsquares.LevenbergMarquardtOptimizer;*/

import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.UUID;

import bell.beaconapp.R;
import bell.beaconapp.model.Beacon;
import bell.beaconapp.model.BeaconResult;

import bell.beaconapp.ui.adapter.BeaconListAdapter;



/**
 * Created by Florian Schlösser on 08.02.2017.
 */
public class ScanFragment extends Fragment implements BeaconListAdapter.OnBeaconListChangedListener {

    public final static String TAG = ScanFragment.class.getSimpleName();

    /* UI */
    private RecyclerView mBeaconsList;
    private View mNoBeaconsLayout;

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
                BeaconResult res = new BeaconResult(manData, mRssi, mScanRecord.getDeviceName());

                Log.d(TAG, "Scan Beacon! " + mScanRecord.getDeviceName() + ":");
                Log.d(TAG, res.getManId() + ":" + res.getUuid());
                Log.d(TAG, res.getMajor() + ":" + res.getMinor());
                Log.d(TAG, res.getRssi() + "/" + res.getTxPower());
                Log.d(TAG, res.getDistance() + "m");
                Log.d(TAG, toHexString(mScanRecord.getManufacturerSpecificData(76)));

                mBeaconListAdapter.add(res);
                test();
            }
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_scan, container, false);
        mBeaconsList = (RecyclerView) root.findViewById(R.id.scan_recycler);
        mNoBeaconsLayout = (View) root.findViewById(R.id.scan_nobeacons);

        mBeaconListAdapter = new BeaconListAdapter(getContext());
        mBeaconListAdapter.setOnBeaconListChangedListener(this);

        mBeaconsList.setLayoutManager(new LinearLayoutManager(getContext()));
        mBeaconsList.setAdapter(mBeaconListAdapter);

        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        mBluetoothScanner = mBluetoothAdapter.getBluetoothLeScanner();

        setScanSettings();
        setScanFilter();

        return root;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (mBluetoothScanner != null) {
            mBluetoothScanner.startScan(mScanCallback);
        } else {
            Log.w(TAG, "onStart() - mBluetoothScanner is a null object!");
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        mBluetoothScanner.stopScan(mScanCallback);
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

    private void test() {
        /*if(mBeaconListAdapter.getItemCount()==3) {
            double[] distances = new double[3];
            double[][] locations = new double[3][3];
            for(int i=0;i<3;i++) {
                distances[i] = mBeaconListAdapter.getBeaconOnIndex(i).getDistance();
                locations[i] = mBeaconListAdapter.getBeaconOnIndex(i).getLocation();
                Log.d(TAG,"Distanz[" + i + "]:" + distances[i]);
                Log.d(TAG,"Location" + i + "]:" + locations[i][0] + ":" + locations[i][1]);
            }
            NonLinearLeastSquaresSolver solver = new NonLinearLeastSquaresSolver(new TrilaterationFunction(locations, distances), new LevenbergMarquardtOptimizer());
            LeastSquaresOptimizer.Optimum optimum = solver.solve();

            double[] location = optimum.getPoint().toArray();
            if(location!=null) {
                Log.d(TAG, "Location:"+ Arrays.toString(location));
            }else{
                Log.d(TAG, "Oops, something wrent wrong!");
            }
        }else{
            Log.d(TAG,"Not enough beacons found for localization!");
        }
        */
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

    @Override
    public void onBeaconRemoved() {
    }

    private void toggleNoBeaconsHintLayout () {
        int visibility = mBeaconListAdapter.getItemCount() == 0 ? View.VISIBLE : View.GONE;
        mNoBeaconsLayout.setVisibility(visibility);
    }
}
