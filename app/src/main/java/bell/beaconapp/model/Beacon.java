package bell.beaconapp.model;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Random;

/**
 * Created by Florian Schlösser on 08.02.2017.
 */
public class Beacon {
    private String mManId;
    private String mUuid;
    private int mMajor;
    private int mMinor;
    private int mTxPower;
    private int mRssi;
    private double mDistance;
    private String mDeviceName;

    public Beacon(byte[] frame, int rssi, String deviceName) {
        mDeviceName = deviceName;
        mManId = toHexString(Arrays.copyOfRange(frame,0,2));
        mUuid = toHexString(Arrays.copyOfRange(frame,2,18));
        mMajor = byteArrayToInt(Arrays.copyOfRange(frame,18,20));
        mMinor = byteArrayToInt(Arrays.copyOfRange(frame,20,22));
        mTxPower = (int)frame[22];
        mRssi = rssi;
        mDistance = calculateDistance(mTxPower,mRssi);
    }

    private double calculateDistance(int txPower, double rssi) {
        if (rssi == 0) {
            return -1.0; // if we cannot determine accuracy, return -1.
        }
        double ratio = rssi*1.0/txPower;
        if (ratio < 1.0) {
            return Math.pow(ratio,10);
        }
        else {
            double accuracy =  (0.89976)*Math.pow(ratio,7.7095) + 0.111;
            return accuracy;
        }
    }

    private int byteArrayToInt(byte[] b)
    {
        return new BigInteger(b).intValue();
    }

    private String toHexString(byte[] bytes) {
        StringBuilder hexString = new StringBuilder();

        for (int i = 0; i < bytes.length; i++) {
            String hex = Integer.toHexString(0xFF & bytes[i]);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }

        return hexString.toString();
    }

    public String getManId() {
        return mManId;
    }

    public void setManId(String manId) {
        mManId = manId;
    }

    public String getUuid() {
        return mUuid;
    }

    public void setUuid(String uuid) {
        mUuid = uuid;
    }

    public int getMajor() {
        return mMajor;
    }

    public void setMajor(int major) {
        mMajor = major;
    }

    public int getMinor() {
        return mMinor;
    }

    public void setMinor(int minor) {
        mMinor = minor;
    }

    public String getDeviceName() {
        return mDeviceName;
    }

    public void setDeviceName(String deviceName) {
        mDeviceName = deviceName;
    }

    public double getDistance() {
        return mDistance;
    }

    public void setDistance(double distance) {
        mDistance = distance;
    }

    public int getTxPower() {
        return mTxPower;
    }

    public void setTxPower(int txPower) {
        mTxPower = txPower;
    }

    public int getRssi() {
        return mRssi;
    }

    public void setRssi(int rssi) {
        mRssi = rssi;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Beacon)) return false;
        if (!((Beacon) obj).getUuid().equals(mUuid)) return false;
        if (((Beacon) obj).getMinor() != mMinor) return false;
        if (((Beacon) obj).getMajor() != mMajor) return false;
        return true;
    }
}
