package at.barbot.barbot;

import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;

import at.barbot.barbot.Bluetooth.BarBotBluetoothService;
import at.barbot.barbot.database.BarBotDatabaseHelper;
import at.barbot.barbot.database.Drink;
import at.barbot.barbot.database.Ingredient;
import at.barbot.barbot.database.Slaveunit;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        MainFragment.OnListFragmentInteractionListener,
        CreateDrinkFragment.OnCreateDrinkFragmentInteractionListener,
        CreateIngredientFragment.OnCreateIngredientFragmentInteractionListener,
        ListIngredientFragment.OnIngredientListFragmentInteractionListener,
        DrinkDetailsFragment.OnDrinkDetailsFragmentInteractionListener,
        BarbotSettingFragment.OnSettingsFragmentInteractionListener,
        CreateSlaveunitFragment.OnCreateSlaveunitFragmentInteractionListener,
        ListBluetoothDevicesFragment.OnBluetoothFragmentInteractionListener,
        EditDrinkFragment.OnEditDrinkFragmentInteractionListener,
        BarBotBluetoothService.OnBluetoothInteractionListener {


    private static final String TAG = "Main Activity";
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

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
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
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
            selectItem(new CreateDrinkFragment());
        } else if (id == R.id.show_ingredients) {
            selectItem(new ListIngredientFragment());
        } else if (id == R.id.add_ingredient) {
            selectItem(new CreateIngredientFragment());
        } else if (id == R.id.nav_slideshow) {
            selectItem(new BarbotSettingFragment());
        } else if (id == R.id.nav_manage) {
            // TODO: dieses Fragment nach dem testen wieder löschen (Wird nachher von Bluetooth-Service Aufgerufen)
            selectItem(new CreateSlaveunitFragment());
        } else if (id == R.id.nav_bluetooth) {
            selectItem(new ListBluetoothDevicesFragment());
        } /*else if (id == R.id.nav_send) {

        }*/

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void selectItem(Fragment fragment) {
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
    public void onIngredientListFragmentInteraction(Ingredient ingredient) {

    }

    @Override
    public void onDrinkDetailsFragmentInteraction(Drink drink) {
        EditDrinkFragment editDrinkFragment = new EditDrinkFragment();
        editDrinkFragment.setDrink(drink);
        selectItem(editDrinkFragment);
    }

    @Override
    public void onSettingsFragmentInteraction() {

    }

    @Override
    public void onCreateSlaveunitFragmentInteraction() {

    }

    @Override
    public void onBluetoothFragmentInteraction() {
        selectItem(new MainFragment());
    }

    @Override
    public void onEditDrinkInteraction() {

    }

    @Override
    public void onBluetoothInteraction(String cmd, String data) {
        if (cmd.equals("NS")) {
            CreateSlaveunitFragment crsl = new CreateSlaveunitFragment();
            Bundle args = new Bundle();
            args.putInt("pk_slave", Integer.parseInt(data));
            crsl.setArguments(args);
            selectItem(crsl);
        } else if (cmd.equals("G")) {
            Log.d(TAG, "onBluetoothInteraction: Getränk: " + data);
        } else if (cmd.equals("S")) {
            Log.d(TAG, "onBluetoothInteraction: Anzahl der Slaves: " + data);
        } else if (cmd.equals("DS")) {
            BarBotDatabaseHelper databaseHelper = BarBotDatabaseHelper.getInstance(this.getApplicationContext());
            Log.d(TAG, "onBluetoothInteraction: delete Slaveunit: " + data);
            Slaveunit sl = databaseHelper.getSlaveunit(Integer.parseInt(data));
            databaseHelper.deleteSlaveunit(sl);
        } else {
            Log.e(TAG, "onBluetoothInteraction: Got not known command: " + data);
        }
    }

    @Override
    public void onBluetoothInteraction(String cmd, String[] data) {
        Log.d(TAG, "onBluetoothInteraction: " + cmd + "data: " + data[0] + "; " + data[1]);
    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("Main Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }
}
