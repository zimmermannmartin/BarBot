package at.barbot.barbot.database;

/**
 * Created by Martin on 11.10.2016.
 */

public class Slaveunit {
    public int pk_id_slaveunit;
    public String name;
    public String filling_level_in_ml;
    public int fk_id_barbot;
    public int fk_id_ingredient;
}
