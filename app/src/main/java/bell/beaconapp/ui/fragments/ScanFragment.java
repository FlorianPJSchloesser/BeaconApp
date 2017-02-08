package bell.beaconapp.ui.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import bell.beaconapp.R;
import bell.beaconapp.model.Beacon;
import bell.beaconapp.ui.adapter.BeaconListAdapter;

/**
 * Created by Florian Schlösser on 08.02.2017.
 */
public class ScanFragment extends Fragment {

    public final static String TAG = ScanFragment.class.getSimpleName();

    /* UI */
    private RecyclerView mBeaconsList;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_scan, container, false);
        mBeaconsList = (RecyclerView) root.findViewById(R.id.scan_recycler);

        BeaconListAdapter beaconListAdapter = new BeaconListAdapter(getContext(), mBeaconsList);
        beaconListAdapter.add(new Beacon());
        beaconListAdapter.add(new Beacon());
        beaconListAdapter.add(new Beacon());
        beaconListAdapter.add(new Beacon());

        mBeaconsList.setLayoutManager(new LinearLayoutManager(getContext()));
        mBeaconsList.setAdapter(beaconListAdapter);

        return root;
    }
}
