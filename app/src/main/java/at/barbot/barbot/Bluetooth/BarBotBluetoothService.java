package at.barbot.barbot.Bluetooth;

import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;
import java.util.UUID;

/**
 * Created by Martin on 27.12.2016.
 */

public class BarBotBluetoothService {
    private static final String TAG = "BarBotBluetoothService";

    BluetoothAdapter mAdapter;
    private int mState;
    private String mAddress;
    static final UUID mUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    BluetoothSocket mSocket;

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

    private class ConnectBT extends AsyncTask<Void, Void, Void>  // UI thread
    {
        private boolean ConnectSuccess = true; //if it's here, it's almost connected

        @Override
        protected void onPreExecute() {
            //progress = ProgressDialog.show(ledControl.this, "Connecting...", "Please wait!!!");  //show a progress dialog
        }

        @Override
        protected Void doInBackground(Void... devices) //while the progress dialog is shown, the connection is done in background
        {
            try {
                if (mSocket == null || mState == STATE_NONE) {
                    mState = STATE_CONNECTING;
                    mAdapter = BluetoothAdapter.getDefaultAdapter();//get the mobile bluetooth device
                    BluetoothDevice dispositivo = mAdapter.getRemoteDevice(mAddress);//connects to the device's address and checks if it's available
                    mSocket = dispositivo.createInsecureRfcommSocketToServiceRecord(mUUID);//create a RFCOMM (SPP) connection
                    BluetoothAdapter.getDefaultAdapter().cancelDiscovery();
                    mSocket.connect();//start connection
                }
            } catch (IOException e) {
                ConnectSuccess = false;//if the try failed, you can check the exception here
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) //after the doInBackground, it checks if everything went fine
        {
            super.onPostExecute(result);

            if (!ConnectSuccess) {
                Log.d(TAG, "Connection Failed. Is it a SPP Bluetooth? Try again.");
                //finish();
            } else {
                Log.d(TAG, "Connected.");
                mState = STATE_CONNECTED;
            }
            //progress.dismiss();
        }
    }
}
