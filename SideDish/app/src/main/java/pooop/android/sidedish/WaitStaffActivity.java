package pooop.android.sidedish;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class WaitStaffActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public static Intent newIntent(Context packageContext){
        return new Intent(packageContext, WaitStaffActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wait_staff);


        // Set default fragment to TableTableFragment
        TableTableFragment tableTableFragment = new TableTableFragment();
        FragmentManager fm = getSupportFragmentManager();
        fm.beginTransaction()
                .add(R.id.fragment_container, tableTableFragment)
                .commit();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        // receive bundle with user's type, hide buttons based on that
        Bundle b = new Bundle();
        b = getIntent().getExtras();
        int userType = b.getInt("type");

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        if(userType == 1){
            navigationView.getMenu().clear();
            navigationView.inflateMenu(R.menu.manager_nav);
        }
        if(userType == 2){
            navigationView.getMenu().clear();
            navigationView.inflateMenu(R.menu.wait_staff_nav);
        }
        if(userType == 3){
            navigationView.getMenu().clear();
            navigationView.inflateMenu(R.menu.kitchen_staff_nav);
            KitchenFragment kitchenFragment = new KitchenFragment();
            fm.beginTransaction()
                    .replace(R.id.fragment_container, kitchenFragment)
                    .commit();
        }
        navigationView.setNavigationItemSelectedListener(this);
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
        getMenuInflater().inflate(R.menu.wait_staff, menu);


        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.logout_options_item) {
            startActivity(LoginActivity.newIntent(this));
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        FragmentManager fm = getSupportFragmentManager();

        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_view_tables) {
            // Change to TableTableFragment
            TableTableFragment tableTableFragment = new TableTableFragment();
            fm.beginTransaction()
                    .replace(R.id.fragment_container, tableTableFragment)
                    .commit();
        }else if (id == R.id.nav_manage_users) {
            // Change to EditUsersScreen
            EditUsersFragment editUserFragment = new EditUsersFragment();
            fm.beginTransaction()
                    .replace(R.id.fragment_container, editUserFragment)
                    .commit();
        }else if(id == R.id.nav_view_kitchen){
            // Change to KitchenFragment
            KitchenFragment kitchenFragment = new KitchenFragment();
            fm.beginTransaction()
                    .replace(R.id.fragment_container, kitchenFragment)
                    .commit();
        } else if (id == R.id.nav_manage_menu) {
            // Change to EditMenuFragment
            EditMenuFragment editMenuFragment = new EditMenuFragment();
            fm.beginTransaction()
                    .replace(R.id.fragment_container, editMenuFragment)
                    .commit();
        }
         else if (id == R.id.nav_statistics){
            EditMenuFragment statisticFragment = new EditMenuFragment();
            fm.beginTransaction()
                    .replace(R.id.fragment_container, statisticFragment)
                    .commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
