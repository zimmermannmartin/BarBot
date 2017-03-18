package at.barbot.barbot.Bluetooth;

import android.app.Activity;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.UUID;

import at.barbot.barbot.ListBluetoothDevicesFragment;
import at.barbot.barbot.MainActivity;
import at.barbot.barbot.R;

/**
 * Created by Martin on 27.12.2016.
 */

public class BarBotBluetoothService {
    private static final String TAG = "BarBotBluetoothService";
    OnBluetoothInteractionListener mListener;

    private static BarBotBluetoothService sInstance;

    BluetoothAdapter mAdapter;
    private int mState;
    private String mAddress;
    static final UUID mUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    BluetoothSocket mSocket;
    private InputStream mInputStream;
    private OutputStream mOutputStream;

    public Context mAppContext;

    // Constants that indicate the current connection state
    private static final int STATE_NONE = 0;       // we're doing nothing
    private static final int STATE_CONNECTING = 2; // now initiating an outgoing connection
    private static final int STATE_CONNECTED = 3;  // now connected to a remote device

    private boolean stopWorker = false;
    private int readBufferPosition = 0;

    /**
     * Constructor. Prepares a new BluetoothService.
     *
     * @param address The mac address of the bluetooth device to connect to
     * @param context The Context in which the Bluetooth-Service should connect (application context)
     * @param listener An {@link OnBluetoothInteractionListener} listener Interface
     */
    public BarBotBluetoothService(String address, Context context, OnBluetoothInteractionListener listener) {
        mAdapter = BluetoothAdapter.getDefaultAdapter();
        mState = STATE_NONE;
        mAddress = address;
        mAppContext = context;
        mListener = listener;

        new ConnectBT().execute(); //Call the class to connect

        sInstance = this;

        SharedPreferences prefs = context.getApplicationContext().getSharedPreferences("BTSession", Context.MODE_PRIVATE);
        if (!prefs.contains("address")){
            boolean success = prefs.edit().putString("address", mAddress).commit();
            if (!success){
                Log.e(TAG, "BarBotBluetoothService: failed to set preference for BT-address");
            }
        }else if (!prefs.getString("address", "").equalsIgnoreCase(mAddress)){
            boolean success = prefs.edit().putString("address", mAddress).commit();
            if (!success){
                Log.e(TAG, "BarBotBluetoothService: failed to update preference for BT-address");
            }
        }

        Log.d(TAG, "BarBotBluetoothService -> Adress: " + mAddress);
    }

    @Nullable
    public static synchronized BarBotBluetoothService getInstance() throws ClassNotFoundException {
        if (sInstance == null) {
            Log.d(TAG, "You haven't created a Bluetooth Connection yet, please connect to Bluetooth" +
                    "before getting a Instance");
            throw new ClassNotFoundException();
        } else {
            return sInstance;
        }
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

    void beginListenForData() {
        final Handler handler = new Handler();
        final byte delimiter = 10; //This is the ASCII code for a newline character

        final byte[] readBuffer = new byte[1024];
        Thread workerThread = new Thread(new Runnable() {
            public void run() {
                while (!Thread.currentThread().isInterrupted() && !stopWorker) {
                    try {
                        int bytesAvailable = mInputStream.available();
                        if (bytesAvailable > 0) {
                            byte[] packetBytes = new byte[bytesAvailable];
                            mInputStream.read(packetBytes);
                            for (int i = 0; i < bytesAvailable; i++) {
                                byte b = packetBytes[i];
                                if (b == delimiter) {
                                    byte[] encodedBytes = new byte[readBufferPosition];
                                    System.arraycopy(readBuffer, 0, encodedBytes, 0, encodedBytes.length);
                                    final String data = new String(encodedBytes, "US-ASCII");
                                    readBufferPosition = 0;

                                    handler.post(new Runnable() {
                                        public void run() {
                                            String[] cmd = data.split(";");
                                            if (cmd.length == 2){
                                                mListener.onBluetoothInteraction(cmd[0], cmd[1]);
                                            }else if (cmd.length > 2){
                                                String[] cmds = Arrays.copyOfRange(cmd, 1, cmd.length);
                                                mListener.onBluetoothInteraction(cmd[0], cmds);
                                            }else {
                                                Log.d(TAG, "run: Length of Array not allowed; LENGTH: " + cmd.length);
                                            }
                                            Log.d(TAG, "Data: " + data);
                                            //Toast.makeText(mAppContext, data, Toast.LENGTH_LONG).show();
                                        }
                                    });
                                } else {
                                    readBuffer[readBufferPosition++] = b;
                                }
                            }
                        }
                    } catch (IOException ex) {
                        stopWorker = true;
                    }
                }
            }
        });

        workerThread.start();
    }

    public void writeData(String data) {
        try {
            mOutputStream.write(data.getBytes());
        } catch (IOException e) {
            Log.d(TAG, "writeData: " + e);
        }

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
            Toast.makeText(mAppContext, "Connecting... Please wait!!!", Toast.LENGTH_LONG).show();
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

                    mInputStream = mSocket.getInputStream();
                    mOutputStream = mSocket.getOutputStream();
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
            } else {
                Log.d(TAG, "Connected.");
                Toast.makeText(mAppContext, "Connected.", Toast.LENGTH_SHORT).show();
                mState = STATE_CONNECTED;
                beginListenForData();
            }
        }
    }

    public interface OnBluetoothInteractionListener {
        void onBluetoothInteraction(String cmd, String[] data);
        void onBluetoothInteraction(String cmd, String data);
    }
}
