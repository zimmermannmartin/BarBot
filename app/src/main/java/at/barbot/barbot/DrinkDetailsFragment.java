package at.barbot.barbot;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import at.barbot.barbot.database.BarBotDatabaseHelper;
import at.barbot.barbot.database.Drink;
import at.barbot.barbot.database.Ingredient;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link DrinkDetailsFragment.OnDrinkDetailsFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class DrinkDetailsFragment extends Fragment {
    private Drink drink;
    private HashMap<Ingredient, Integer> ingredient_amount;
    private OnDrinkDetailsFragmentInteractionListener mListener;

    private static final String TAG = "DrinkDetailsFragment";

    public DrinkDetailsFragment() {
        // Required empty public constructor
    }

    public Drink getDrink() {
        return drink;
    }

    public void setDrink(Drink drink_obj) {
        this.drink = drink_obj;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_drink_details, container, false);

        TextView drinkName = (TextView) view.findViewById(R.id.detailDrinkName);
        drinkName.setText(drink.name);

        TextView desc = (TextView) view.findViewById(R.id.drinkDescription);
        desc.setText(drink.description);

        getIngredientsWithAmounts(drink);
        LinearLayout lv = (LinearLayout) view.findViewById(R.id.drinkDetail_IngredientsList);
        for (Map.Entry<Ingredient, Integer> entry : ingredient_amount.entrySet()){
            Ingredient in = entry.getKey();
            int val = entry.getValue();
            TextView tv = new TextView(getContext());
            tv.setText("" + in.name + "     " + val + "ml");
            lv.addView(tv);
        }
        // Inflate the layout for this fragment
        return view;
    }

    public void onButtonPressed(Drink drink) {
        if (mListener != null) {
            mListener.onDrinkDetailsFragmentInteraction(drink);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnDrinkDetailsFragmentInteractionListener) {
            mListener = (OnDrinkDetailsFragmentInteractionListener) context;
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

    public void getIngredientsWithAmounts(Drink drink){
        BarBotDatabaseHelper databaseHelper = BarBotDatabaseHelper.getInstance(getActivity());
        ingredient_amount = databaseHelper.getIngredientswithAmounts(drink);
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
    public interface OnDrinkDetailsFragmentInteractionListener {
        void onDrinkDetailsFragmentInteraction(Drink drink);
    }
}
