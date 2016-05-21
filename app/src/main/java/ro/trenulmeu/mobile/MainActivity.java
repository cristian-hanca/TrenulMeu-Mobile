package ro.trenulmeu.mobile;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Toast;

import ro.trenulmeu.mobile.fragments.SplashFragment;
import ro.trenulmeu.mobile.fragments.TrainsFragment;
import ro.trenulmeu.mobile.helpers.FragmentHelpers;
import ro.trenulmeu.mobile.helpers.MenuHandler;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    DrawerLayout drawer;
    ActionBarDrawerToggle toggle;
    NavigationView navigationView;
    MenuHandler menuHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppContext.activity = this;

        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        assert navigationView != null;
        navigationView.setNavigationItemSelectedListener(this);

        menuHandler = new MenuHandler(navigationView.getMenu(), new MenuHandler.Actions() {
            @Override
            public void onRoutes() {

            }

            @Override
            public void onTrains() {
                FragmentHelpers.goToSingleton(new TrainsFragment(), Constants.gotoTrains);
            }

            @Override
            public void onStations() {

            }

            @Override
            public void onStatus() {

            }
        });

        if (savedInstanceState == null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.add(R.id.fragment, new SplashFragment(), Constants.gotoSplash);
            ft.addToBackStack(Constants.gotoSplash);
            ft.commit();
        }
    }

    public void returnToMain() {
        FragmentHelpers.popInclusive(Constants.gotoSplash);
        if (AppContext.db == null) {
            exit();
        } else {
            FragmentHelpers.goToSingleton(new TrainsFragment(), Constants.gotoTrains);
            menuHandler.getMenu().getItem(1).setChecked(true);
        }
    }

    public void exit() {
        FragmentHelpers.popAll();
        finish();
    }

    @Override
    public void onBackPressed() {
        if (AppContext.db == null) {
            exit();
        }

        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if (FragmentHelpers.backCount() == 1) {
            exit();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        drawer.closeDrawer(GravityCompat.START);

        return menuHandler.handle(item);
    }

}
