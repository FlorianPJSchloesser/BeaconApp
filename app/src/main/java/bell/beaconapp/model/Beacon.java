package bell.beaconapp.model;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Random;

/**
 * Created by Florian Schlösser on 08.02.2017.
 */
public class Beacon extends BeaconSuper{
    private int distCount;

    public Beacon(byte[] frame, int rssi, String deviceName) {
        super(frame,rssi, deviceName);
    }

    public Beacon(BeaconResult result) {
        super(result);
    }

    @Override
    public void setDistance(double distance) {
        mDistance = (4*mDistance+distance)/5;
    }

    public void update(BeaconResult beacon) {
        mRssi = beacon.getRssi();
        mTxPower = beacon.getTxPower();
        setDistance(beacon.getDistance());
    }

}
