package at.barbot.barbot;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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
    private OnSettingsFragmentInteractionListener mListener;

    private List<Slaveunit> mItems = new ArrayList<>();
    private static final AtomicInteger sNextGeneratedId = new AtomicInteger(1);

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
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_barbot_setting, container, false);
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

    public void showSlaveunits (){
        BarBotDatabaseHelper databaseHelper = BarBotDatabaseHelper.getInstance(getActivity());
        mItems = databaseHelper.getAllSlaveunits();

        LinearLayout ll = new LinearLayout(getContext());
        ll.setOrientation(LinearLayout.HORIZONTAL);
        for (int i=0; i<mItems.size(); i++){
            Slaveunit sl = mItems.get(i);
            RelativeLayout rl = new RelativeLayout(getContext());
            TextView headtv = new TextView(getContext());
            ImageView iv = new ImageView(getContext());
            TextView ingrtv = new TextView(getContext());
            TextView leveltv = new TextView(getContext());
            ProgressBar pb = new ProgressBar(getContext());

            headtv.setId(generateViewId());
            iv.setId(generateViewId());
            ingrtv.setId(generateViewId());
            leveltv.setId(generateViewId());
            pb.setId(generateViewId());
        }
        FrameLayout fl = (FrameLayout) getActivity().findViewById(R.id.barBot_setting);
        fl.addView(ll);
    }

    /**
     * Generate a value suitable for use in {setId(int)}.
     * This value will not collide with ID values generated at build time by aapt for R.id.
     *
     * @return a generated ID value
     */
    public static int generateViewId() {
        for (;;) {
            final int result = sNextGeneratedId.get();
            // aapt-generated IDs have the high byte nonzero; clamp to the range under that.
            int newValue = result + 1;
            if (newValue > 0x00FFFFFF) newValue = 1; // Roll over to 1, not 0.
            if (sNextGeneratedId.compareAndSet(result, newValue)) {
                return result;
            }
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
