package at.barbot.barbot.database;

/**
 * Created by Martin on 11.10.2016.
 */

public class Ingredient {
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Ingredient that = (Ingredient) o;

        return name != null ? name.equals(that.name) : that.name == null;

    }

    @Override
    public int hashCode() {
        return name != null ? name.hashCode() : 0;
    }

    public int pk_id_ingredient;
    public String name;
    public int vol_percent;
}
