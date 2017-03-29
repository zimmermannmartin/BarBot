package at.barbot.barbot;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import at.barbot.barbot.database.BarBotDatabaseHelper;
import at.barbot.barbot.database.Ingredient;
import at.barbot.barbot.database.StatisticDrink;
import at.barbot.barbot.database.StatisticIngredient;

import static at.barbot.barbot.R.id.statistic_listView;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link StatisticFragment.OnStatisticFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class StatisticFragment extends Fragment {
    private OnStatisticFragmentInteractionListener mListener;
    private int mColumnCount = 1;
    final BarBotDatabaseHelper databaseHelper = BarBotDatabaseHelper.getInstance(getActivity());
    private List<StatisticDrink> drinkStatistics;
    private List<StatisticIngredient> ingredientStatistics;
    private ListView drinkStatisticList;
    private ListView ingredientStatisticList;

    public StatisticFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_statistic, container, false);
        drinkStatisticList = (ListView) view.findViewById(R.id.statistic_listView);
        ingredientStatisticList = (ListView) view.findViewById(R.id.statistic_ingredients_listView);

        drinkStatisticList();
        ingredientStatisticList();
        return view;
        // Inflate the layout for this fragment
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnStatisticFragmentInteractionListener) {
            mListener = (OnStatisticFragmentInteractionListener) context;
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

    private void drinkStatisticList()
    {

        drinkStatistics = databaseHelper.getAllStatisticsDrinks();
        ArrayList list = new ArrayList();

        if (drinkStatistics.size()>0)
        {
            for( StatisticDrink dt : drinkStatistics)
            {
                list.add(dt.name + "\n" + dt.amount);
            }
        }


        final ArrayAdapter adapter = new ArrayAdapter(this.getContext(),android.R.layout.simple_list_item_1, list);
        drinkStatisticList.setAdapter(adapter);
        //drinkStatisticList.setOnItemClickListener(myListClickListener);

    }

    private void ingredientStatisticList()
    {

        ingredientStatistics = databaseHelper.getAllStatisticsIngredients();
        ArrayList list = new ArrayList();

        if (ingredientStatistics.size()>0)
        {
            for( StatisticIngredient si : ingredientStatistics)
            {
                list.add(si.name + "\n" + si.amount);
            }
        }


        final ArrayAdapter adapter = new ArrayAdapter(this.getContext(),android.R.layout.simple_list_item_1, list);
        ingredientStatisticList.setAdapter(adapter);
        //drinkStatisticList.setOnItemClickListener(myListClickListener);

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
    public interface OnStatisticFragmentInteractionListener {
        void onStatisticFragmentInteraction();
    }
}
