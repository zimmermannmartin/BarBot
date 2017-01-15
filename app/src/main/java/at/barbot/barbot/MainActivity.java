package at.barbot.barbot;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import at.barbot.barbot.database.Drink;
import at.barbot.barbot.database.Ingredient;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, MainFragment.OnListFragmentInteractionListener, CreateDrinkFragment.OnCreateDrinkFragmentInteractionListener,
        CreateIngredientFragment.OnCreateIngredientFragmentInteractionListener, ListIngredientFragment.OnIngredientListFragmentInteractionListener,
        DrinkDetailsFragment.OnDrinkDetailsFragmentInteractionListener, BarbotSettingFragment.OnSettingsFragmentInteractionListener, CreateSlaveunitFragment.OnCreateSlaveunitFragmentInteractionListener{

    private static final String TAG = "Main Activity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        if (savedInstanceState == null) {
            Fragment mainFragment = new MainFragment();
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.content_frame, mainFragment).commit();
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.bar_list) {
            Fragment mainFragment = new MainFragment();
            selectItem(mainFragment);
        } else if (id == R.id.add_drink) {
            Fragment createDrinkFragment = new CreateDrinkFragment();
            selectItem(createDrinkFragment);
        } else if (id == R.id.show_ingredients){
            Fragment showIngredientsFragment = new ListIngredientFragment();
            selectItem(showIngredientsFragment);
        } else if (id == R.id.add_ingredient){
            Fragment createIngredientFragment = new CreateIngredientFragment();
            selectItem(createIngredientFragment);
        } else if (id == R.id.nav_slideshow) {
            Fragment barBotSettingFragment = new BarbotSettingFragment();
            selectItem(barBotSettingFragment);
        } else if (id == R.id.nav_manage) {
            // TODO: dieses Fragment nach dem testen wieder löschen (Wird nachher von Bluetooth-Service Aufgerufen)
            Fragment createSlaveunitFragment = new CreateSlaveunitFragment();
            selectItem(createSlaveunitFragment);
        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void selectItem(Fragment fragment){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.content_frame, fragment);
        transaction.addToBackStack(null);

        transaction.commit();
    }

    @Override
    public void onListFragmentInteraction(Drink drink) {
        DrinkDetailsFragment drinkDetailsFragment = new DrinkDetailsFragment();
        drinkDetailsFragment.setDrink(drink);
        selectItem(drinkDetailsFragment);
    }

    @Override
    public void onCreateFragmentInteraction() {

    }

    @Override
    public void onCreateIngredientFragmentInteraction() {

    }

    @Override
    public void onIngredientListFragmentInteraction(Ingredient ingredient){

    }

    @Override
    public void onDrinkDetailsFragmentInteraction(Drink drink){

    }

    @Override
    public void onSettingsFragmentInteraction() {

    }

    @Override
    public void onCreateSlaveunitFragmentInteraction() {

    }
}
