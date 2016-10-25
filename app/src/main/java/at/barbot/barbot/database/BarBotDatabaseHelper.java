package at.barbot.barbot.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Martin on 10.10.2016.
 */

public class BarBotDatabaseHelper extends SQLiteOpenHelper {
    //Database Instance
    private static BarBotDatabaseHelper sInstance;

    //Database Info
    private static final String DATABASE_NAME = "barBot";
    private static final int DATABASE_VERSION = 1;

    /**
     * Table Names
     */
    private static final String TABLE_BARBOT = "barbot";
    private static final String TABLE_SLAVEUNIT = "slaveunit";
    private static final String TABLE_INGREDIENT = "ingredient";
    private static final String TABLE_DRINK_HAS_INGREDIENT = "drink_has_ingredient";
    private static final String TABLE_DRINK = "drink";

    /**
     * Table Columns
     */

    //Table BarBot columns
    private static final String COLUMN_BARBOT_PK_ID_BARBOT = "pk_id_barbot";
    private static final String COLUMN_BARBOT_NAME = "name";

    //Table Slaveunit columns
    private static final String COLUMN_SLAVEUNIT_PK_ID_SLAVEUNIT = "pk_id_slaveunit";
    private static final String COLUMN_SLAVEUNIT_NAME = "name";
    private static final String COLUMN_SLAVEUNIT_FILLING_LEVEL_IN_ML = "filling_level_in_ml";
    private static final String COLUMN_SLAVEUNIT_FK_ID_BARBOT = "fk_id_barbot";
    private static final String COLUMN_SLAVEUNIT_FK_ID_INGREDIENT = "fk_id_ingredient";

    //Table Ingredient columns
    private static final String COLUMN_INGREDIENT_PK_ID_INGREDIENT = "pk_id_ingredient";
    private static final String COLUMN_INGREDIENT_NAME = "name";
    private static final String COLUMN_INGREDIENT_VOL_PERCENT = "vol_percent";

    //Table drink_has_ingredient columns
    private static final String COLUMN_DRINK_HAS_INGREDIENT_PK_FK_ID_DRINK = "pk_fk_id_drink";
    private static final String COLUMN_DRINK_HAS_INGREDIENT_PK_FK_ID_INGREDIENT = "pk_fk_id_ingredient";
    private static final String COLUMN_DRINK_HAS_INGREDIENT_INGREDIENT_AMOUNT_IN_ML = "ingredient_amount_in_ml";

    //Table drink columns
    private static final String COLUMN_DRINK_PK_ID_DRINK = "pk_id_drink";
    private static final String COLUMN_DRINK_NAME = "name";
    private static final String COLUMN_DRINK_PICTURE = "picture";
    private static final String COLUMN_DRINK_DESCRIPTION = "description";

    private static final String TAG = "BarBotDatabaseHelper";

    private BarBotDatabaseHelper (Context context){
        super(context,DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static synchronized BarBotDatabaseHelper getInstance (Context context){
        if (sInstance == null){
            sInstance = new BarBotDatabaseHelper(context.getApplicationContext());
        }
        return sInstance;
    }

    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
        db.setForeignKeyConstraintsEnabled(true);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String create_table_barbot = "CREATE TABLE IF NOT EXISTS" + TABLE_BARBOT +
                "(" +   COLUMN_BARBOT_PK_ID_BARBOT + " INTEGER PRIMARY KEY," +
                        COLUMN_BARBOT_NAME + " VARCHAR(45)" +
                ")";
        String create_table_slaveunit = "CREATE TABLE IF NOT EXISTS" + TABLE_SLAVEUNIT +
                "(" +   COLUMN_SLAVEUNIT_PK_ID_SLAVEUNIT + " INTEGER PRIMARY KEY," +
                        COLUMN_SLAVEUNIT_NAME + " VARCHAR(45)," +
                        COLUMN_SLAVEUNIT_FILLING_LEVEL_IN_ML + " INTEGER," +
                        COLUMN_SLAVEUNIT_FK_ID_BARBOT + " INTEGER," +
                        COLUMN_SLAVEUNIT_FK_ID_INGREDIENT + " INTEGER," +
                        "FOREIGN KEY(" + COLUMN_SLAVEUNIT_FK_ID_BARBOT + ") REFERENCES " + TABLE_BARBOT + "(" + COLUMN_BARBOT_PK_ID_BARBOT + ")" +
                ")";
        String create_table_ingredient = "CREATE TABLE" + TABLE_INGREDIENT +
                "(" +   COLUMN_INGREDIENT_PK_ID_INGREDIENT + " INTEGER PRIMARY KEY," +
                        COLUMN_INGREDIENT_NAME + " VARCHAR(100)," +
                        COLUMN_INGREDIENT_VOL_PERCENT + " INTEGER" +
                ")";
        String create_table_drink_has_ingredient = "CREATE TABLE" + TABLE_DRINK_HAS_INGREDIENT +
                "(" +   COLUMN_DRINK_HAS_INGREDIENT_PK_FK_ID_INGREDIENT + " INTEGER," +
                        COLUMN_DRINK_HAS_INGREDIENT_PK_FK_ID_DRINK + " INTEGER," +
                        COLUMN_DRINK_HAS_INGREDIENT_INGREDIENT_AMOUNT_IN_ML + " INTEGER" +
                        "PRIMARY KEY (" + COLUMN_DRINK_HAS_INGREDIENT_PK_FK_ID_INGREDIENT + "," + COLUMN_DRINK_HAS_INGREDIENT_PK_FK_ID_DRINK + ")" +
                        "FOREIGN KEY(" + COLUMN_DRINK_HAS_INGREDIENT_PK_FK_ID_INGREDIENT + ") REFERENCES " + TABLE_INGREDIENT + "(" + COLUMN_INGREDIENT_PK_ID_INGREDIENT + ")" +
                        "FOREIGN KEY(" + COLUMN_DRINK_HAS_INGREDIENT_PK_FK_ID_DRINK + ") REFERENCES " + TABLE_DRINK + "(" + COLUMN_DRINK_PK_ID_DRINK + ")" +
                ")";
        String create_table_drink = "CREATE TABLE" + TABLE_DRINK +
                "(" +   COLUMN_DRINK_PK_ID_DRINK + " INTEGER," +
                        COLUMN_DRINK_NAME + " VARCHAR(100)," +
                        COLUMN_DRINK_DESCRIPTION + " MEDIUMTEXT," +
                        COLUMN_DRINK_PICTURE + "VARCHAR(200)" +
                ")";

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldV, int newV) {
        if (oldV != newV) {
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_BARBOT);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_SLAVEUNIT);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_INGREDIENT);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_DRINK_HAS_INGREDIENT);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_DRINK);
            onCreate(db);
        }
    }

    /**
     * Einen neuen BarBot hinzufuegen
     * @param barBot: Ein Model, welches den BarBot repraesentiert
     */
    public void addBarBot (BarBot barBot){
        // create and/or open database
        SQLiteDatabase db = getWritableDatabase();

        db.beginTransaction();
        try {
            ContentValues values = new ContentValues();
            values.put(COLUMN_BARBOT_PK_ID_BARBOT, barBot.pk_id_barbot);
            values.put(COLUMN_BARBOT_NAME, barBot.name);

            db.insertOrThrow(TABLE_BARBOT, null, values);
            db.setTransactionSuccessful();
        }catch (Exception e){
            Log.d(TAG, "Error adding a new BarBot: " + e);
        }finally {
            db.endTransaction();
        }
    }

    public void addSlaveunit (Slaveunit slave){
        SQLiteDatabase db = getWritableDatabase();

        db.beginTransaction();
        try {
            ContentValues values = new ContentValues();
            values.put(COLUMN_SLAVEUNIT_PK_ID_SLAVEUNIT, slave.pk_id_slaveunit);
            values.put(COLUMN_SLAVEUNIT_NAME, slave.name);
            values.put(COLUMN_SLAVEUNIT_FILLING_LEVEL_IN_ML, slave.filling_level_in_ml);
            values.put(COLUMN_SLAVEUNIT_FK_ID_BARBOT, slave.fk_id_barbot);
            values.put(COLUMN_SLAVEUNIT_FK_ID_INGREDIENT, slave.fk_id_ingredient);

            db.insertOrThrow(TABLE_SLAVEUNIT, null, values);
            db.setTransactionSuccessful();
        }catch (Exception e){
            Log.d(TAG, "Error adding a new Slaveunit: " + e);
        }finally {
            db.endTransaction();
        }
    }

    public void addIngredient (Ingredient ingredient){
        SQLiteDatabase db = getWritableDatabase();

        db.beginTransaction();
        try {
            ContentValues values = new ContentValues();
            values.put(COLUMN_INGREDIENT_PK_ID_INGREDIENT, ingredient.pk_id_ingredient);
            values.put(COLUMN_INGREDIENT_NAME, ingredient.name);
            values.put(COLUMN_INGREDIENT_VOL_PERCENT, ingredient.vol_percent);

            db.insertOrThrow(TABLE_INGREDIENT, null, values);
            db.setTransactionSuccessful();
        }catch (Exception e){
            Log.d(TAG, "Error adding a new Ingredient: " + e);
        }finally {
            db.endTransaction();
        }
    }

    public void addDrinkHasIngredient (Drink_has_ingredient drinkIngredient){
        SQLiteDatabase db = getWritableDatabase();

        db.beginTransaction();
        try {
            ContentValues values = new ContentValues();
            values.put(COLUMN_DRINK_HAS_INGREDIENT_PK_FK_ID_INGREDIENT, drinkIngredient.pk_fk_id_ingredient);
            values.put(COLUMN_DRINK_HAS_INGREDIENT_PK_FK_ID_DRINK, drinkIngredient.pk_fk_id_drink);
            values.put(COLUMN_DRINK_HAS_INGREDIENT_INGREDIENT_AMOUNT_IN_ML, drinkIngredient.ingredient_amount_in_ml);

            db.insertOrThrow(TABLE_DRINK_HAS_INGREDIENT, null, values);
            db.setTransactionSuccessful();
        }catch (Exception e){
            Log.d(TAG, "Error adding a new drink_has_ingredient: " + e);
        }finally {
            db.endTransaction();
        }
    }

    public void addDrink (Drink drink){
        SQLiteDatabase db = getWritableDatabase();

        db.beginTransaction();
        try {
            ContentValues values = new ContentValues();
            values.put(COLUMN_DRINK_PK_ID_DRINK, drink.pk_id_drink);
            values.put(COLUMN_DRINK_NAME, drink.name);
            values.put(COLUMN_DRINK_DESCRIPTION, drink.description);
            values.put(COLUMN_DRINK_PICTURE, drink.picture);

            db.insertOrThrow(TABLE_DRINK, null, values);
            db.setTransactionSuccessful();
        }catch (Exception e){
            Log.d(TAG, "Error adding a new Drink: " + e);
        }finally {
            db.endTransaction();
        }
    }

    public void updateBarBot (BarBot barBot){
        SQLiteDatabase db = getWritableDatabase();

        db.beginTransaction();
        try {
            ContentValues values = new ContentValues();
            values.put(COLUMN_BARBOT_PK_ID_BARBOT, barBot.pk_id_barbot);
            values.put(COLUMN_BARBOT_NAME, barBot.name);

            int rows = db.update(TABLE_BARBOT, values, COLUMN_BARBOT_PK_ID_BARBOT + "= ?", new String[]{""+barBot.pk_id_barbot});
            db.setTransactionSuccessful();
        }catch (Exception e){
            Log.d(TAG, "Error updating the BarBot" + e);
        }finally {
            db.endTransaction();
        }
    }

    public void updateSlaveUnit (Slaveunit slave){
        SQLiteDatabase db = getWritableDatabase();

        db.beginTransaction();
        try {
            ContentValues values = new ContentValues();
            values.put(COLUMN_SLAVEUNIT_PK_ID_SLAVEUNIT, slave.pk_id_slaveunit);
            values.put(COLUMN_SLAVEUNIT_NAME, slave.name);
            values.put(COLUMN_SLAVEUNIT_FILLING_LEVEL_IN_ML, slave.filling_level_in_ml);
            values.put(COLUMN_SLAVEUNIT_FK_ID_BARBOT, slave.fk_id_barbot);
            values.put(COLUMN_SLAVEUNIT_FK_ID_INGREDIENT, slave.fk_id_ingredient);

            int rows = db.update(TABLE_SLAVEUNIT, values, COLUMN_SLAVEUNIT_PK_ID_SLAVEUNIT + "= ?", new String[]{""+slave.pk_id_slaveunit});
            db.setTransactionSuccessful();
        }catch (Exception e){
            Log.d(TAG, "Error updating Slaveunit" + e);
        }finally {
            db.endTransaction();
        }
    }

    public void updateIngredient (Ingredient ingredient){
        SQLiteDatabase db = getWritableDatabase();

        db.beginTransaction();
        try {
            ContentValues values = new ContentValues();
            values.put(COLUMN_INGREDIENT_PK_ID_INGREDIENT, ingredient.pk_id_ingredient);
            values.put(COLUMN_INGREDIENT_NAME, ingredient.name);
            values.put(COLUMN_INGREDIENT_VOL_PERCENT, ingredient.vol_percent);

            int rows = db.update(TABLE_INGREDIENT, values, COLUMN_INGREDIENT_PK_ID_INGREDIENT + "= ?", new String[]{""+ingredient.pk_id_ingredient});
            db.setTransactionSuccessful();
        }catch (Exception e){
            Log.d(TAG, "Error updating Ingredient" + e);
        }finally {
            db.endTransaction();
        }
    }

    public void updateDrinkHasIngredient (Drink_has_ingredient drinkHasIngredient){
        SQLiteDatabase db = getWritableDatabase();

        db.beginTransaction();
        try {
            ContentValues values = new ContentValues();
            values.put(COLUMN_DRINK_HAS_INGREDIENT_PK_FK_ID_DRINK, drinkHasIngredient.pk_fk_id_drink);
            values.put(COLUMN_DRINK_HAS_INGREDIENT_PK_FK_ID_INGREDIENT, drinkHasIngredient.pk_fk_id_ingredient);
            values.put(COLUMN_DRINK_HAS_INGREDIENT_INGREDIENT_AMOUNT_IN_ML, drinkHasIngredient.ingredient_amount_in_ml);

            int rows = db.update(TABLE_DRINK_HAS_INGREDIENT, values, COLUMN_DRINK_HAS_INGREDIENT_PK_FK_ID_DRINK + "= ? AND " + COLUMN_DRINK_HAS_INGREDIENT_PK_FK_ID_DRINK + "= ?", new String[]{""+drinkHasIngredient.pk_fk_id_drink, ""+drinkHasIngredient.pk_fk_id_ingredient});
            db.setTransactionSuccessful();
        }catch (Exception e){
            Log.d(TAG, "Error updating Drink_has_Ingredient" + e);
        }finally {
            db.endTransaction();
        }
    }

    public void updateDrink (Drink drink){
        SQLiteDatabase db = getWritableDatabase();

        db.beginTransaction();
        try {
            ContentValues values = new ContentValues();
            values.put(COLUMN_DRINK_PK_ID_DRINK, drink.pk_id_drink);
            values.put(COLUMN_DRINK_NAME, drink.name);
            values.put(COLUMN_DRINK_DESCRIPTION, drink.description);
            values.put(COLUMN_DRINK_PICTURE, drink.picture);
            int rows = db.update(TABLE_DRINK, values, COLUMN_DRINK_PK_ID_DRINK + "= ?", new String[]{""+drink.pk_id_drink});
            db.setTransactionSuccessful();
        }catch (Exception e){
            Log.d(TAG, "Error updating Drink" + e);
        }finally {
            db.endTransaction();
        }
    }
}
