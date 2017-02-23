package bell.beaconapp.ui.fragments;

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
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
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
import bell.beaconapp.background.ScanService;
import bell.beaconapp.background.ScanServiceConnection;
import bell.beaconapp.model.Beacon;
import bell.beaconapp.model.BeaconResult;

import bell.beaconapp.ui.adapter.BeaconListAdapter;



/**
 * Created by Florian Schlösser on 08.02.2017.
 */
public class ScanFragment extends Fragment implements BeaconListAdapter.OnBeaconListChangedListener, View.OnClickListener, ScanService.ScanServiceCallback {

    public final static String TAG = ScanFragment.class.getSimpleName();

    /* UI */
    private RecyclerView mBeaconsList;
    private View mNoBeaconsLayout;
    private FloatingActionButton mToggleScanFab;

    /* ADAPTER */
    private BeaconListAdapter mBeaconListAdapter;

    private static boolean SCANNING = false;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_scan, container, false);
        mBeaconsList = (RecyclerView) root.findViewById(R.id.scan_recycler);
        mNoBeaconsLayout = root.findViewById(R.id.scan_nobeacons);
        mToggleScanFab = (FloatingActionButton) root.findViewById(R.id.scan_toggle_fab);

        mBeaconListAdapter = new BeaconListAdapter(getContext());
        mBeaconListAdapter.setOnBeaconListChangedListener(this);

        updateFabIcon();
        mToggleScanFab.setOnClickListener(this);

        mBeaconsList.setLayoutManager(new LinearLayoutManager(getContext()));
        mBeaconsList.setAdapter(mBeaconListAdapter);

        return root;

    }

    private void updateFabIcon() {
        mToggleScanFab.setImageResource(scanIsRunning() ? R.drawable.ic_action_stop : R.drawable.ic_action_start);
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

    @Override
    public void onClick(View view) {
        if (view.equals(mToggleScanFab)) {
            if (scanIsRunning()) {
                stopScan();
            } else {
                startScan();
            }
        }
    }

    private void startScan() {
        Log.d(TAG, "startScan() - starting to scan.");
        Intent scanServiceIntent = new Intent (getContext(), ScanService.class);
        if (!getContext().bindService(scanServiceIntent, mServiceConnection, Service.BIND_AUTO_CREATE)) {
            Log.w(TAG, "startScan() - failed to bind service.");
        }
    }

    private void stopScan() {
        Log.d(TAG, "stopScan() - stopped scanning.");
    }

    private ScanServiceConnection mServiceConnection = new ScanServiceConnection();

    private boolean scanIsRunning() {
        return mServiceConnection.isServiceConnected();
    }

    @Override
    public void onBeaconFound(BeaconResult beacon) {
        mBeaconListAdapter.add(beacon);
    }
}
