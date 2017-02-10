package bell.beaconapp.model;

/**
 * Created by bell on 10.02.2017.
 */

public class BeaconResult extends BeaconSuper{
    public BeaconResult(byte[] frame, int rssi, String deviceName) {
        super(frame,rssi, deviceName);
        mDistance = calculateDistance(mTxPower, mRssi);
    }


}
