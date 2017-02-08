package bell.beaconapp.ui.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import bell.beaconapp.ui.fragments.EmulateFragment;
import bell.beaconapp.ui.fragments.ScanFragment;

/**
 * Created by Florian Schlösser on 08.02.2017.
 */
public class ContentPagerAdapter extends FragmentStatePagerAdapter{

    public final static String TAG = ContentPagerAdapter.class.getSimpleName();

    public ContentPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public Fragment getItem(int position) {
        switch(position) {
            case 0:
                return new ScanFragment();
            case 1:
                return new EmulateFragment();
        }
        return null;
    }
}
