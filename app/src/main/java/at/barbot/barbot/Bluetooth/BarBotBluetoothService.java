package at.barbot.barbot.Bluetooth;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;

import at.barbot.barbot.MainActivity;

import static android.support.v4.app.ActivityCompat.startActivityForResult;

/**
 * Created by Martin on 27.12.2016.
 */

public class BarBotBluetoothService {
    private static final String TAG = "BarBotBluetoothService";

    private final BluetoothAdapter mAdapter;
    private int mState;

    // Constants that indicate the current connection state
    public static final int STATE_NONE = 0;       // we're doing nothing
    public static final int STATE_CONNECTING = 2; // now initiating an outgoing connection
    public static final int STATE_CONNECTED = 3;  // now connected to a remote device

    /**
     * Constructor. Prepares a new BluetoothChat session.
     *
     * @param address The mac adress of the bluetooth device to connect to
     */
    public BarBotBluetoothService(String address) {
        mAdapter = BluetoothAdapter.getDefaultAdapter();
        mState = STATE_NONE;
    }

    /**
     * Set the current state of the chat connection
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
