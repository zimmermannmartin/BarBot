package at.barbot.barbot;

import at.barbot.barbot.database.Drink;
import at.barbot.barbot.database.Ingredient;

/**
 * Created by Martin on 27.12.2016.
 */

public class HandleOrder {
    public Drink drink;
    public Ingredient[] ingredients;

    public HandleOrder(Drink d, Ingredient[] i){
        drink = d;
        ingredients = i;
    }



}
