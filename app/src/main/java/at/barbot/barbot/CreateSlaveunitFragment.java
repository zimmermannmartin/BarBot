package at.barbot.barbot;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.List;

import at.barbot.barbot.database.BarBotDatabaseHelper;
import at.barbot.barbot.database.Ingredient;
import at.barbot.barbot.database.Slaveunit;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CreateSlaveunitFragment.OnCreateSlaveunitFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CreateSlaveunitFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CreateSlaveunitFragment extends Fragment {
    private static final String TAG = "CreateSlaveunitFragment";

    private OnCreateSlaveunitFragmentInteractionListener mListener;

    public Ingredient mItem;

    public int mSlaveID;

    public CreateSlaveunitFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * @return A new instance of fragment CreateSlaveunitFragment.
     */
    public static CreateSlaveunitFragment newInstance() {
        return new CreateSlaveunitFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Bundle bundle = getArguments();
        final int pk = bundle.getInt("pk_slave");
        View view = inflater.inflate(R.layout.fragment_create_slaveunit, container, false);

        Button addIngredient = (Button) view.findViewById(R.id.button4);
        addIngredient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openIngredientDialog(v);
            }
        });

        Button saveSlaveunit = (Button) view.findViewById(R.id.button2);
        saveSlaveunit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveSlaveunit(v, pk);
            }
        });
        // Inflate the layout for this fragment
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnCreateSlaveunitFragmentInteractionListener) {
            mListener = (OnCreateSlaveunitFragmentInteractionListener) context;
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

    public void openIngredientDialog (View v){
        BarBotDatabaseHelper databaseHelper = BarBotDatabaseHelper.getInstance(getActivity());
        final List<Ingredient> allIngredients = databaseHelper.getAllIngredients();
        CharSequence ingrNameList[] = new CharSequence[allIngredients.size()];
        int counter = 0;
        for (Ingredient item : allIngredients){
            ingrNameList[counter++] = item.name;
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
        builder.setTitle(R.string.drinkDetailIngr);
        builder.setSingleChoiceItems(ingrNameList, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Log.d(TAG, "onClick: " + allIngredients.get(which).name);
                mItem = allIngredients.get(which);
            }
        });
        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                TextView tv = (TextView) getActivity().findViewById(R.id.textView10);
                tv.setText(R.string.slaveunitIngredient + mItem.name);
                tv.setTextColor(Color.BLACK);
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mItem = null;
                Log.d(TAG, "onClick: cancel");
            }
        });
        builder.show();
    }

    public void saveSlaveunit (View v, int pk_slave){
        BarBotDatabaseHelper databaseHelper = BarBotDatabaseHelper.getInstance(getActivity());
        String name = ((EditText) v.getRootView().findViewById(R.id.editText2)).getText().toString();
        int filling_level_in_ml = Integer.parseInt(((EditText) v.getRootView().findViewById(R.id.editText3)).getText().toString());
        Slaveunit slaveunit = new Slaveunit();
        slaveunit.pk_id_slaveunit = pk_slave;
        slaveunit.filling_level_in_ml = filling_level_in_ml;
        slaveunit.name = name;
        slaveunit.fk_id_ingredient = mItem.pk_id_ingredient;

        databaseHelper.addSlaveunit(slaveunit);

        mListener.onCreateSlaveunitFragmentInteraction();

        Snackbar showSuccessDialog = Snackbar.make(getActivity().findViewById(R.id.drawer_layout),
                R.string.slaveunit_erstellen_sucess, Snackbar.LENGTH_SHORT);
        showSuccessDialog.show();
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
    public interface OnCreateSlaveunitFragmentInteractionListener {
        void onCreateSlaveunitFragmentInteraction();
    }
}
