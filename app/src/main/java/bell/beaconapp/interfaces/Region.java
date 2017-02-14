package bell.beaconapp.interfaces;

import bell.beaconapp.model.Beacon;

/**
 * Created by admincl on 14.02.2017.
 */

public class Region {
    private Beacon mBeacon;
    private double mRange;
    private String mOnEnterDisplayText;
    private String mOnExitDisplayText;

    public Region(Beacon beacon, double range, String onEnterDisplayText) {
        mBeacon = beacon;
        mRange = range;
        mOnEnterDisplayText = onEnterDisplayText;
    }

    public Region(Beacon beacon, double range, String onEnterDisplayText, String onExitDisplayText) {
        mBeacon = beacon;
        mRange = range;
        mOnEnterDisplayText = onEnterDisplayText;
        mOnExitDisplayText = onExitDisplayText;
    }

    public void setOnEnterDisplayText(String onEnterDisplayText) { mOnEnterDisplayText = onEnterDisplayText; }

    public String getOnEnterDisplayText() { return mOnEnterDisplayText; }

    public void setOnExitDisplayText(String onExitDisplayText) { mOnExitDisplayText = onExitDisplayText; }

    public String getOnExitDisplayText() { return mOnExitDisplayText; }



}
