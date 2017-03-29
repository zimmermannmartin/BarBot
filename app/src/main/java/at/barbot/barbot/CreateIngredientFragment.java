package at.barbot.barbot;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import at.barbot.barbot.database.BarBotDatabaseHelper;
import at.barbot.barbot.database.Ingredient;
import at.barbot.barbot.database.StatisticIngredient;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CreateIngredientFragment.OnCreateIngredientFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CreateIngredientFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CreateIngredientFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnCreateIngredientFragmentInteractionListener mListener;

    public CreateIngredientFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CreateIngredientFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CreateIngredientFragment newInstance(String param1, String param2) {
        CreateIngredientFragment fragment = new CreateIngredientFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_create_ingredient, container, false);

        Button btn = (Button) view.findViewById(R.id.zutatHinzufügen);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addNewIngredient(view);
            }
        });

        return view;
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onCreateIngredientFragmentInteraction();
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnCreateIngredientFragmentInteractionListener) {
            mListener = (OnCreateIngredientFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnCreateIngredientFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public void addNewIngredient (View view){
        final EditText name_field = (EditText) view.getRootView().findViewById(R.id.editTextZutatName);
        String name = name_field.getText().toString();

        Ingredient ingredient = new Ingredient();
        ingredient.name = name;
        ingredient.vol_percent = 40;

        StatisticIngredient statisticIngredient = new StatisticIngredient();
        statisticIngredient.name = name;
        statisticIngredient.amount = 0;

        BarBotDatabaseHelper databaseHelper = BarBotDatabaseHelper.getInstance(getActivity());
        databaseHelper.addIngredient(ingredient);
        databaseHelper.addStatisticIngredient(statisticIngredient);

        Snackbar showSuccessDialog = Snackbar.make(getActivity().findViewById(R.id.drawer_layout), R.string.ingredient_erstellen_sucess, Snackbar.LENGTH_SHORT);
        showSuccessDialog.show();

        name_field.setText("");
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
    public interface OnCreateIngredientFragmentInteractionListener {
        void onCreateIngredientFragmentInteraction();
    }
}
