package bell.beaconapp.interfaces;

import java.util.ArrayList;

/**
 * Created by admincl on 14.02.2017.
 */

public class RangeNotifier {
    ArrayList<Region> mRegions;

    public RangeNotifier() {
        mRegions = new ArrayList<>();
    }

    public void addRegion(Region region) {
        mRegions.add(region);
    }


    public void onRegionEnter(Region region) {

    }

}
