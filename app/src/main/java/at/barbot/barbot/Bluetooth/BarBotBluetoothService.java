package at.barbot.barbot.Bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;

import static android.support.v4.app.ActivityCompat.startActivityForResult;

/**
 * Created by Martin on 27.12.2016.
 */

public class BarBotBluetoothService {
    private static final String TAG = "BarBotBluetoothService";

    private final BluetoothAdapter mAdapter;
    //private final Handler mHandler;
    private int mState;

    // Constants that indicate the current connection state
    public static final int STATE_NONE = 0;       // we're doing nothing
    public static final int STATE_LISTEN = 1;     // now listening for incoming connections
    public static final int STATE_CONNECTING = 2; // now initiating an outgoing connection
    public static final int STATE_CONNECTED = 3;  // now connected to a remote device

    /**
     * Constructor. Prepares a new BluetoothChat session.
     *
     * @param context The UI Activity Context
     */
    public BarBotBluetoothService(Context context) {
        mAdapter = BluetoothAdapter.getDefaultAdapter();
        mState = STATE_NONE;
    }

    /**
     * Set the current state of the chat connection
     *
     * @param state An integer defining the current connection state
     */
    private synchronized void setState(int state) {
        Log.d(TAG, "setState() " + mState + " -> " + state);
        mState = state;
    }

    /**
     * Return the current connection state.
     */
    public synchronized int getState() {
        return mState;
    }
}
