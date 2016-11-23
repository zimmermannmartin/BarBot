package at.barbot.barbot;

import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import at.barbot.barbot.database.BarBotDatabaseHelper;
import at.barbot.barbot.database.Drink;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CreateDrinkFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class CreateDrinkFragment extends Fragment {
    private OnFragmentInteractionListener mListener;

    private static final String TAG = "CreateDrinkFragment";

    public CreateDrinkFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_create_drink, container, false);

        Button btn = (Button) view.findViewById(R.id.button3);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addNewDrink(view);
            }
        });
        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onCreateFragmentInteraction();
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
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

    public void addNewDrink (View view){
        final EditText name_field = (EditText) view.getRootView().findViewById(R.id.editTextGetraenkeName);
        final EditText desc_field = (EditText) view.getRootView().findViewById(R.id.editTextBeschreibung);
        final EditText picture_field = (EditText) view.getRootView().findViewById(R.id.editTextPicture);
        String name = name_field.getText().toString();
        String desc = desc_field.getText().toString();
        String picture = picture_field.getText().toString();

        Drink drink = new Drink();
        drink.name = name;
        drink.description = desc;
        drink.picture = picture;

        BarBotDatabaseHelper databaseHelper = BarBotDatabaseHelper.getInstance(getActivity());
        databaseHelper.addDrink(drink);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(R.string.getraenk_erstellen_success)
                .setTitle(R.string.getraenke_erstellen_title)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        name_field.setText("");
                        desc_field.setText("");
                        picture_field.setText("");
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
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
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onCreateFragmentInteraction();
    }
}
