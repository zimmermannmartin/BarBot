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
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import at.barbot.barbot.database.BarBotDatabaseHelper;
import at.barbot.barbot.database.Drink;
import at.barbot.barbot.database.Ingredient;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link EditDrinkFragment.OnEditDrinkFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class EditDrinkFragment extends Fragment {



    static final String TAG = "CreateDrinkFragment";

    private OnEditDrinkFragmentInteractionListener mListener;
    private List<Ingredient> mItems = new ArrayList<>();
    private List<Ingredient> allIngredients = getAllIngredients();
    private HashMap<Ingredient, Integer> ingredient_amount;
    public Drink mDrink;

    public EditDrinkFragment() {
        // Required empty public constructor
    }




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    public void setDrink(Drink drink_obj) {
        mDrink = drink_obj;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_edit_drink, container, false);


        Button addIgredientButton = (Button) view.findViewById(R.id.addIngredientButton);
        addIgredientButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openIngredientDialog(v);
            }
        });

        Log.d(TAG, "onCreateView: name: " + mDrink.name);

        final TextView drinkName = (TextView) view.findViewById(R.id.editTextGetraenkeName);
        drinkName.setText(mDrink.name);

        final TextView drinkDesc = (TextView) view.findViewById(R.id.editTextBeschreibung);
        drinkDesc.setText(mDrink.description);

        getIngredientsWithAmounts(mDrink);
        //Log.d(TAG, "" + ingredient_amount);
        LinearLayout lv = (LinearLayout) view.findViewById(R.id.drinksIngredients);
        for (Map.Entry<Ingredient, Integer> entry : ingredient_amount.entrySet()){
            Ingredient in = entry.getKey();
            int val = entry.getValue();
            TextView tv = new TextView(getContext());
            tv.setText("" + in.name + "     " + val + "ml");
            lv.addView(tv);
        }

        Button updateDrinkButton = (Button) view.findViewById(R.id.button3);
        updateDrinkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mDrink.name = drinkName.getText().toString();
                mDrink.description = drinkDesc.getText().toString();

                BarBotDatabaseHelper databaseHelper = BarBotDatabaseHelper.getInstance(getActivity());
                databaseHelper.updateDrink(mDrink);
            }
        });






        return view;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public List<Ingredient> getAllIngredients(){
        BarBotDatabaseHelper databaseHelper = BarBotDatabaseHelper.getInstance(getActivity());
        List<Ingredient> ingrList = databaseHelper.getAllIngredients();

        return ingrList;
    }

    public void getIngredientsWithAmounts(Drink drink){
        BarBotDatabaseHelper databaseHelper = BarBotDatabaseHelper.getInstance(getActivity());
        ingredient_amount = databaseHelper.getIngredientswithAmounts(drink);
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
                    LinearLayout hl = new LinearLayout(getContext());
                    hl.setOrientation(LinearLayout.HORIZONTAL);
                    TextView ingr = new TextView(getContext());
                    EditText amount = new EditText(getContext());
                    ingr.setText(in.name);
                    amount.setHint(R.string.getraenke_erstellen_ml);
                    amount.setId(in.pk_id_ingredient);
                    /*LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    params.setMargins(0,10,5,0);*/
                    hl.addView(ingr/*, params*/);
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
    public interface OnEditDrinkFragmentInteractionListener {
        void onEditDrinkInteraction();
    }
}
