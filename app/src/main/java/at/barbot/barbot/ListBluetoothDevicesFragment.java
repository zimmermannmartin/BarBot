package at.barbot.barbot;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Set;

import at.barbot.barbot.Bluetooth.BarBotBluetoothService;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ListBluetoothDevicesFragment.OnBluetoothFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class ListBluetoothDevicesFragment extends Fragment {

    private OnBluetoothFragmentInteractionListener mListener;
    Button btnPaired;
    ListView devicelist;
    //Bluetooth
    private BluetoothAdapter mAdapter = null;
    private Set<BluetoothDevice> pairedDevices;

    public ListBluetoothDevicesFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list_bluetooth_devices, container, false);

        //Calling widgets
        btnPaired = (Button) view.findViewById(R.id.listPairedDevices);
        devicelist = (ListView) view.findViewById(R.id.pairedDevicesList);

        //if the device has bluetooth
        mAdapter = BluetoothAdapter.getDefaultAdapter();

        if(mAdapter == null)
        {
            //Show a mensag. that the device has no bluetooth adapter
            Toast.makeText(view.getContext(), "Bluetooth Device Not Available", Toast.LENGTH_LONG).show();

            //finish apk
            //TODO: Wenn die App in einer virtuellen Umgebung getestet wird muss finish() auskommentiert werden
            getActivity().finish();
        }
        else if(!mAdapter.isEnabled())
        {
            //Ask to the user turn the bluetooth on
            Intent turnBTon = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(turnBTon,1);
        }

        btnPaired.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                pairedDevicesList();
            }
        });


        // Inflate the layout for this fragment
        return view;
    }

    private void pairedDevicesList()
    {
        pairedDevices = mAdapter.getBondedDevices();
        ArrayList list = new ArrayList();

        if (pairedDevices.size()>0)
        {
            for(BluetoothDevice bt : pairedDevices)
            {
                list.add(bt.getName() + "\n" + bt.getAddress()); //Get the device's name and the address
            }
        }
        else
        {
            Toast.makeText(this.getContext() , "No Paired Bluetooth Devices Found.", Toast.LENGTH_LONG).show();
        }

        final ArrayAdapter adapter = new ArrayAdapter(this.getContext(),android.R.layout.simple_list_item_1, list);
        devicelist.setAdapter(adapter);
        devicelist.setOnItemClickListener(myListClickListener);

    }

    private AdapterView.OnItemClickListener myListClickListener = new AdapterView.OnItemClickListener()
    {
        public void onItemClick (AdapterView<?> av, View v, int arg2, long arg3)
        {
            // Get the device MAC address, the last 17 chars in the View
            String info = ((TextView) v).getText().toString();
            String address = info.substring(info.length() - 17);

            //TODO: überlegen ob eine newInstance Methode für Bluetooth gut wäre
            BarBotBluetoothService bluetoothService = new BarBotBluetoothService(address);

            mListener.onBluetoothFragmentInteraction();
        }
    };

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnBluetoothFragmentInteractionListener) {
            mListener = (OnBluetoothFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnBluetoothFragmentInteractionListener {
        void onBluetoothFragmentInteraction();
    }
}
