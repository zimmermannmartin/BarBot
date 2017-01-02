package at.barbot.barbot;

import java.util.HashMap;

import at.barbot.barbot.database.Drink;
import at.barbot.barbot.database.Ingredient;

/**
 * Created by Martin on 27.12.2016.
 */

public class Order {
    public Drink drink;
    public HashMap<Ingredient, Integer> ingredients;

    public Order(Drink d, HashMap<Ingredient, Integer> i){
        drink = d;
        ingredients = i;
    }

    public boolean submit(){

        return false;
    }
}