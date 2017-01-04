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
        BarBotDatabaseHelper databaseHelper = BarBotDatabaseHelper.getInstance(getActivity());
        mItems = databaseHelper.getAllSlaveunits();

        LinearLayout ll = (LinearLayout) view.getRootView().findViewById(R.id.barBot_setting);
        for (int i=0; i<mItems.size(); i++){
            Slaveunit sl = mItems.get(i);

            RelativeLayout rl = new RelativeLayout(getContext());
            TextView headtv = new TextView(getContext());
            ImageView iv = new ImageView(getContext());
            TextView ingrtv = new TextView(getContext());
            TextView leveltv = new TextView(getContext());
            ProgressBar pb = new ProgressBar(getContext(), null, android.R.attr.progressBarStyleHorizontal);

            headtv.setId(View.generateViewId());
            iv.setId(View.generateViewId());
            ingrtv.setId(View.generateViewId());
            leveltv.setId(View.generateViewId());
            pb.setId(View.generateViewId());

            RelativeLayout.LayoutParams rlParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
            rl.setLayoutParams(rlParams);

            RelativeLayout.LayoutParams headtvParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
            headtvParams.setMarginStart(191);
            headtvParams.setMargins(191,243,0,0);
            headtvParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
            headtvParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
            headtvParams.addRule(RelativeLayout.ALIGN_PARENT_START);
            headtv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 24);
            headtv.setTypeface(headtv.getTypeface(), Typeface.BOLD);
            headtv.setLayoutParams(headtvParams);

            RelativeLayout.LayoutParams ivParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
            ivParams.setMarginStart(167);
            ivParams.leftMargin = 167;
            ivParams.bottomMargin = 58;
            ivParams.height = 150;
            ivParams.width = 200;
            ivParams.addRule(RelativeLayout.ALIGN_BOTTOM, headtv.getId());
            ivParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
            ivParams.addRule(RelativeLayout.ALIGN_PARENT_START);
            iv.setLayoutParams(ivParams);

            RelativeLayout.LayoutParams ingrtvParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,50);
            ingrtvParams.topMargin = 30;
            ingrtvParams.leftMargin = 15;
            ingrtvParams.setMarginStart(15);
            ingrtvParams.addRule(RelativeLayout.ALIGN_LEFT, leveltv.getId());
            ingrtvParams.addRule(RelativeLayout.ALIGN_START, leveltv.getId());
            ingrtvParams.addRule(RelativeLayout.BELOW, headtv.getId());
            ingrtv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
            ingrtv.setTypeface(headtv.getTypeface(), Typeface.BOLD);
            ingrtv.setLayoutParams(ingrtvParams);

            RelativeLayout.LayoutParams leveltvParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,50);
            leveltvParams.rightMargin = 35;
            leveltvParams.bottomMargin = 34;
            leveltvParams.setMarginEnd(35);
            leveltvParams.addRule(RelativeLayout.ALIGN_RIGHT, pb.getId());
            leveltvParams.addRule(RelativeLayout.ALIGN_BOTTOM, pb.getId());
            leveltvParams.addRule(RelativeLayout.ALIGN_END, pb.getId());
            leveltv.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
            leveltv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
            leveltv.setLayoutParams(leveltvParams);

            RelativeLayout.LayoutParams pbParams = new RelativeLayout.LayoutParams(150,20);
            pbParams.topMargin = 107;
            pbParams.addRule(RelativeLayout.BELOW, ingrtv.getId());
            pbParams.addRule(RelativeLayout.ALIGN_LEFT, headtv.getId());
            pbParams.addRule(RelativeLayout.ALIGN_START, headtv.getId());
            pb.setLayoutParams(pbParams);

            headtv.setText(sl.name);
            iv.setImageResource(R.drawable.ic_menu_camera);
            ingrtv.setText(databaseHelper.getIngredientNameFromSlaveunit(sl));
            leveltv.setText(R.string.Fuellstand);
            pb.setMax(1500); // 1500ml = 1,5L
            if(Build.VERSION.SDK_INT < 24) {
                pb.setProgress(sl.filling_level_in_ml);
            }else {
                pb.setProgress(sl.filling_level_in_ml, true);
            }

            // Add the View Elements of one Slaveunit to the surrounding relative Layout
            rl.addView(headtv);
            rl.addView(iv);
            rl.addView(ingrtv);
            rl.addView(leveltv);
            rl.addView(pb);

            // Add the relative Layout to the linear Layout
            ll.addView(rl);
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
