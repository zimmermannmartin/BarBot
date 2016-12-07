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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import at.barbot.barbot.database.BarBotDatabaseHelper;
import at.barbot.barbot.database.Drink;
import at.barbot.barbot.database.Ingredient;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CreateDrinkFragment.OnCreateDrinkFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class CreateDrinkFragment extends Fragment {
    private OnCreateDrinkFragmentInteractionListener mListener;
    private List<Ingredient> mItems = new ArrayList<>();
    private List<Ingredient> allIngredients = getAllIngredients();

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

        Button addIgredientButton = (Button) view.findViewById(R.id.addIngredientButton);
        addIgredientButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openIngredientDialog(v);
            }
        });

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
        if (context instanceof OnCreateDrinkFragmentInteractionListener) {
            mListener = (OnCreateDrinkFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnCreateDrinkFragmentInteractionListener");
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

    public List<Ingredient> getAllIngredients(){
        BarBotDatabaseHelper databaseHelper = BarBotDatabaseHelper.getInstance(getActivity());
        List<Ingredient> ingrList = databaseHelper.getAllIngredients();

        return ingrList;
    }

    public void openIngredientDialog(View v){
        mItems = new ArrayList<>();
        CharSequence ingrNameList[] = new CharSequence[allIngredients.size()];
        int counter = 0;
        for (Ingredient item : allIngredients){
            ingrNameList[counter] = item.name;
            counter++;
        }
        final ArrayList mSelectedItems = new ArrayList();
        AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
        builder.setTitle(R.string.drinkDetailIngr);
        builder.setMultiChoiceItems(ingrNameList, null, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                if (isChecked) {
                    mSelectedItems.add(which);
                } else if (mSelectedItems.contains(which)) {
                    mSelectedItems.remove(Integer.valueOf(which));
                }
            }
        });
        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                LinearLayout ll = (LinearLayout) getActivity().findViewById(R.id.drinksIngredients);
                for (int i=0; i<mSelectedItems.size(); i++){
                    Ingredient in = allIngredients.get((int) mSelectedItems.get(i));
                    mItems.add(in);
                    LinearLayout hl = new LinearLayout(getActivity());
                    hl.setOrientation(LinearLayout.HORIZONTAL);
                    TextView ingr = new TextView(getActivity());
                    EditText amount = new EditText(getActivity());
                    ingr.setText(in.name);
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    params.setMargins(0,10,5,0);
                    amount.setHint(R.string.getraenke_erstellen_ml);
                    hl.addView(ingr, params);
                    hl.addView(amount);
                    ll.addView(hl);
                }
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Log.d(TAG, "onClick: cancel");
            }
        });
        builder.show();
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
    public interface OnCreateDrinkFragmentInteractionListener {
        void onCreateFragmentInteraction();
    }
}
