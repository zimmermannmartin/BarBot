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
    public Map<Ingredient, Integer> ingredients;

    public Order(Drink d, Map<Ingredient, Integer> i){
        drink = d;
        ingredients = i;
    }

    public boolean submit(){
        try {
            BarBotBluetoothService bluetoothService = BarBotBluetoothService.getInstance();
            BarBotDatabaseHelper databaseHelper = BarBotDatabaseHelper.getInstance(bluetoothService.mAppContext);
            String orderString = "G";
            for (Map.Entry<Ingredient, Integer> entry: ingredients.entrySet()){
                Slaveunit sl = databaseHelper.getSlaveunitByIngredient(entry.getKey());
                if ((sl.filling_level_in_ml-entry.getValue()) >= 0){
                    Slaveunit slaveunit = new Slaveunit();
                    slaveunit = sl;
                    slaveunit.filling_level_in_ml = sl.filling_level_in_ml-entry.getValue();

                    databaseHelper.updateSlaveunit(slaveunit);
                }else {
                    Log.d(TAG, "submit: filling level of Slaveunit is too low! Fillinglevel: " + sl.filling_level_in_ml + ", needed level: " + entry.getValue());
                    return false;
                }
                if (sl.name != null){
                    orderString += ";" + sl.pk_id_slaveunit + ":" + entry.getValue();
                }else {
                    Log.d(TAG, "submit: slaveunit doesn't exist or duplicate Slaveunit exists.");
                    return false;
                }
            }
            orderString += "\n";
            bluetoothService.writeData(orderString);

            return true;
        }catch (Exception e){
            Log.e(TAG, "submit: " + e, e);
            return false;
        }
    }
}