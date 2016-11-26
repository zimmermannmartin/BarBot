package at.barbot.barbot;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import at.barbot.barbot.ListIngredientFragment.OnIngredientListFragmentInteractionListener;
import at.barbot.barbot.database.Ingredient;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link Ingredient} and makes a call to the
 * specified {@link OnIngredientListFragmentInteractionListener}.
 */
public class IngredientItemRecyclerViewAdapter extends RecyclerView.Adapter<IngredientItemRecyclerViewAdapter.ViewHolder> {

    private final List<Ingredient> mValues;
    private final OnIngredientListFragmentInteractionListener mListener;

    private static final String TAG = "IngredientAdapter";

    public IngredientItemRecyclerViewAdapter(List<Ingredient> items, OnIngredientListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_list_ingredient, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mNameView.setText(mValues.get(position).name);
        holder.mPercentView.setText(String.format("%s %%", mValues.get(position).vol_percent));

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onIngredientListFragmentInteraction(holder.mItem);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mNameView;
        public final TextView mPercentView;
        public Ingredient mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mNameView = (TextView) view.findViewById(R.id.ingredient_name);
            mPercentView = (TextView) view.findViewById(R.id.ingredient_content);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mNameView.getText() + "'";
        }
    }
}
