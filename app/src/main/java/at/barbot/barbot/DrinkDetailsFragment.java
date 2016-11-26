package at.barbot.barbot;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import at.barbot.barbot.database.BarBotDatabaseHelper;
import at.barbot.barbot.database.Drink;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link DrinkDetailsFragment.OnDrinkDetailsFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class DrinkDetailsFragment extends Fragment {
    private static final String ARG_PKDRINK = "pk_drink";

    private int drinkIdParam;

    private OnDrinkDetailsFragmentInteractionListener mListener;

    private Drink drink;

    public DrinkDetailsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            drinkIdParam = getArguments().getInt(ARG_PKDRINK);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_drink_details, container, false);
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

    public void getInformation(int pk_Drink){
        BarBotDatabaseHelper databaseHelper = BarBotDatabaseHelper.getInstance(getActivity());
        drink = databaseHelper.getDrink(pk_Drink);

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
