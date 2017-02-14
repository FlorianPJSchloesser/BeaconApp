package bell.beaconapp.model;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Random;

/**
 * Created by Florian Schlösser on 08.02.2017.
 */
public class Beacon extends BeaconSuper{
    private int distCount;
    private double[] location;

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

    public double[] getLocation() {
        double[] rLocation = new double[3];
        switch(getDeviceName()) {
            case "CCBPbeacon00001":
                rLocation[0] = 0;
                rLocation[1] = 0;
                rLocation[2] = 0;
                break;
            case "CCBPbeacon00002":
                rLocation[0] = 0;
                rLocation[1] = 9.7;
                rLocation[2] = 0;
                break;
            case "CCBPbeacon00003":
                rLocation[0] = 4.2;
                rLocation[1] = 0;
                rLocation[2] = 0;
                break;

        }
        return rLocation;
    }

}
