package at.barbot.barbot;

import android.util.Log;

import java.util.HashMap;
import java.util.Map;

import at.barbot.barbot.Bluetooth.BarBotBluetoothService;
import at.barbot.barbot.database.BarBotDatabaseHelper;
import at.barbot.barbot.database.Drink;
import at.barbot.barbot.database.Ingredient;
import at.barbot.barbot.database.Slaveunit;

/**
 * Created by Martin on 27.12.2016.
 */

public class Order {
    private static final String TAG = "Order";

    public Drink drink;
    public HashMap<Ingredient, Integer> ingredients;

    public Order(Drink d, HashMap<Ingredient, Integer> i){
        drink = d;
        ingredients = i;
    }

    public boolean submit(){
        try {
            BarBotBluetoothService bluetoothService = BarBotBluetoothService.getInstance();
            BarBotDatabaseHelper databaseHelper = BarBotDatabaseHelper.getInstance(bluetoothService.mAppContext);
            String orderString = "G";
            for (HashMap.Entry<Ingredient, Integer> entry: ingredients.entrySet()){
                Slaveunit sl = databaseHelper.getSlaveunitByIngredient(entry.getKey());

                if (sl != null){
                    sl.filling_level_in_ml = sl.filling_level_in_ml - entry.getValue();
                    orderString += ";" + sl.pk_id_slaveunit + ":" + entry.getValue();
                }else {
                    Log.d(TAG, "submit: slaveunit doesn't exist");
                    return false;
                }
            }
            orderString += "\n";
            bluetoothService.writeData(orderString);

            return true;
        }catch (Exception e){
            Log.d(TAG, "submit: " + e);
            return false;
        }
    }
}