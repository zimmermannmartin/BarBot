package at.barbot.barbot.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Martin on 10.10.2016.
 */

public class BarBotDatabaseHelper extends SQLiteOpenHelper {
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
    private static final String COLUMN_DRINK_HAS_INGREDIENT_PK_FK_ID_INGREDIENT = "pk_fk_id_drink";
    private static final String COLUMN_DRINK_HAS_INGREDIENT_INGREDIENT_AMOUNT_IN_ML = "pk_fk_id_drink";

    //Table drink columns
    private static final String COLUMN_DRINK_PK_ID_DRINK = "pk_id_drink";
    private static final String COLUMN_DRINK_NAME = "name";
    private static final String COLUMN_DRINK_PICTURE = "picture";
    private static final String COLUMN_DRINK_DESCRIPTION = "pk_id_drink";

    public BarBotDatabaseHelper (Context context){
        super(context,DATABASE_NAME, null, DATABASE_VERSION);
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
                        COLUMN_SLAVEUNIT_FK_ID_INGREDIENT + " INTEGER" +
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
                ")";
        String create_table_drink = "CREATE TABLE" + TABLE_DRINK +
                "(" +   COLUMN_DRINK_PK_ID_DRINK + " INTEGER," +
                        COLUMN_DRINK_NAME + " VARCHAR(100)," +
                        COLUMN_DRINK_DESCRIPTION + " MEDIUMTEXT," +
                        COLUMN_DRINK_PICTURE + "VARCHAR(200)" +
                ")";

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
