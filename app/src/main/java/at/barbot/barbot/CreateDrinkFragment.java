package at.barbot.barbot;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import android.provider.MediaStore;
import android.support.v4.app.Fragment;

import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.content.Context;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import at.barbot.barbot.database.BarBotDatabaseHelper;
import at.barbot.barbot.database.Drink;
import at.barbot.barbot.database.Drink_has_ingredient;
import at.barbot.barbot.database.Ingredient;
import at.barbot.barbot.database.StatisticDrink;

import static android.app.Activity.RESULT_OK;


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


    private int PICK_IMAGE_REQUEST = 1;



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

        Button imageButton = (Button) view.findViewById(R.id.button5);
        imageButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                /*Intent intent = new Intent();
                // Show only images, no videos or anything else
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                // Always show the chooser (if there are multiple options available)
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);*/

                Intent i = new Intent(
                        Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                startActivityForResult(i, PICK_IMAGE_REQUEST);

            }
        });

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {

                //TODO: Image verarbeiten!!

        }

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = { MediaStore.Images.Media.DATA };

            Cursor cursor = getActivity().getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            try {
                cursor.moveToFirst();
                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                String picturePath = cursor.getString(columnIndex);
                cursor.close();

                ImageView imageView = (ImageView) getActivity().findViewById(R.id.drinkImage);
                imageView.setImageBitmap(BitmapFactory.decodeFile(picturePath));
            }catch (Exception e){
                Log.e(TAG, "onActivityResult: " + e);
            }
        }
    }


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
        //final EditText picture_field = (EditText) view.getRootView().findViewById(R.id.editTextPicture);
        String name = name_field.getText().toString();
        String desc = desc_field.getText().toString();
        //String picture = picture_field.getText().toString();

        Drink drink = new Drink();
        drink.name = name;
        drink.description = desc;
        //drink.picture = picture;
        StatisticDrink stDrink = new StatisticDrink();
        stDrink.name = name;
        stDrink.amount =0;

        BarBotDatabaseHelper databaseHelper = BarBotDatabaseHelper.getInstance(getActivity());
        databaseHelper.addDrink(drink);
        databaseHelper.addStatisticDrink(stDrink);
        Log.d("Statistic Added",stDrink.name);

        drink.pk_id_drink = databaseHelper.getDrinkByName(name).pk_id_drink;

        for (Ingredient i : mItems){
            EditText ingrAmount = (EditText) view.getRootView().findViewById(i.pk_id_ingredient);

            Drink_has_ingredient drink_has_ingredient = new Drink_has_ingredient();
            drink_has_ingredient.pk_fk_id_drink = drink.pk_id_drink;
            drink_has_ingredient.pk_fk_id_ingredient = i.pk_id_ingredient;
            drink_has_ingredient.ingredient_amount_in_ml = Integer.parseInt(ingrAmount.getText().toString());

            databaseHelper.addDrinkHasIngredient(drink_has_ingredient);
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(R.string.getraenk_erstellen_success)
                .setTitle(R.string.getraenke_erstellen_title)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        name_field.setText("");
                        desc_field.setText("");
                        //picture_field.setText("");
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
    public interface OnCreateDrinkFragmentInteractionListener {
        void onCreateFragmentInteraction();
    }
}
