package at.barbot.barbot.Bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.util.Log;

import java.util.UUID;

/**
 * Created by Martin on 27.12.2016.
 */

public class BarBotBluetoothService {
    private static final String TAG = "BarBotBluetoothService";

    private final BluetoothAdapter mAdapter;
    private int mState;
    private String mAddress;
    static final UUID mUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    // Constants that indicate the current connection state
    public static final int STATE_NONE = 0;       // we're doing nothing
    public static final int STATE_CONNECTING = 2; // now initiating an outgoing connection
    public static final int STATE_CONNECTED = 3;  // now connected to a remote device

    /**
     * Constructor. Prepares a new BluetoothService.
     *
     * @param address The mac adress of the bluetooth device to connect to
     */
    public BarBotBluetoothService(String address) {
        mAdapter = BluetoothAdapter.getDefaultAdapter();
        mState = STATE_NONE;
        mAddress = address;

        Log.d(TAG, "BarBotBluetoothService -> Adress: " + mAddress);
    }

    /**
     * Set the current state of the connection
     *
     * @param state An integer defining the current connection state
     */
    private synchronized void setBtState(int state) {
        Log.d(TAG, "setState() " + mState + " -> " + state);
        mState = state;
    }

    /**
     * Return the current connection state.
     */
    public synchronized int getBtState() {
        return mState;
    }
}
