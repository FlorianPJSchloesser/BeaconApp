package bell.beaconapp.ui.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import bell.beaconapp.R;

/**
 * Created by Florian Schlösser on 08.02.2017.
 */
public class EmulateFragment extends Fragment {

    public final static String TAG = EmulateFragment.class.getSimpleName();

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_emulate, container, false);

        return root;
    }

}
