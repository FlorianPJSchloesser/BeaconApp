package bell.beaconapp.model;

import java.util.Random;

/**
 * Created by Florian Schlösser on 08.02.2017.
 */
public class Beacon {

    public final static String TAG = Beacon.class.getSimpleName();

    /**
     * Returns beacons title.
     * @return Title of the beacon
     */
    public String getTitle () {
        return "Beacon";
    }

    /**
     * Returns distance to beacon in meters.
     * @return Distance between device and beacon.
     */
    public int getDistance () {
        return new Random().nextInt(100);
    }

}
