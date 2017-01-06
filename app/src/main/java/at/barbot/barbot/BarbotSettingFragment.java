package at.barbot.barbot;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import at.barbot.barbot.database.BarBotDatabaseHelper;
import at.barbot.barbot.database.Slaveunit;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link BarbotSettingFragment.OnSettingsFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link BarbotSettingFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BarbotSettingFragment extends Fragment {
    private static final String TAG = "BarBotSettingFragment";
    private OnSettingsFragmentInteractionListener mListener;

    private List<Slaveunit> mItems = new ArrayList<>();

    public BarbotSettingFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment BarbotSettingFragment.
     */
    public static BarbotSettingFragment newInstance() {
        BarbotSettingFragment fragment = new BarbotSettingFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_barbot_setting, container, false);
        showSlaveunits(view);

        // Inflate the layout for this fragment
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnSettingsFragmentInteractionListener) {
            mListener = (OnSettingsFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnSettingsFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public void showSlaveunits (View view){
        LayoutInflater inflater = (LayoutInflater.from(view.getContext()));
        LinearLayout ll = (LinearLayout) view.getRootView().findViewById(R.id.barBot_setting);
        BarBotDatabaseHelper databaseHelper = BarBotDatabaseHelper.getInstance(getActivity());
        mItems = databaseHelper.getAllSlaveunits();

        for (int i=0; i<mItems.size(); i++){
            Slaveunit sl = mItems.get(i);

            View v = inflater.inflate(R.layout.slaveunit, null);

            TextView headtv = (TextView) v.findViewById(R.id.textView3);
            ImageView iv = (ImageView) v.findViewById(R.id.imageView3);
            TextView ingrtv = (TextView) v.findViewById(R.id.textView10);
            TextView leveltv = (TextView) v.findViewById(R.id.textView6);
            ProgressBar pb = (ProgressBar) v.findViewById(R.id.progressBar);

            headtv.setText(sl.name);
            iv.setImageResource(R.drawable.ic_menu_camera);
            ingrtv.setText(String.format("%s %s", "Zutat:", databaseHelper.getIngredientNameFromSlaveunit(sl)));
            leveltv.setText(R.string.Fuellstand);
            pb.setMax(1500); // 1500ml = 1,5L
            if(Build.VERSION.SDK_INT < 24) {
                pb.setProgress(sl.filling_level_in_ml);
            }else {
                pb.setProgress(sl.filling_level_in_ml, true);
            }

            ll.addView(v);
        }
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
    public interface OnSettingsFragmentInteractionListener {
        void onSettingsFragmentInteraction();
    }
}
