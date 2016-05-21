package ro.trenulmeu.mobile.helpers;

import android.view.Menu;
import android.view.MenuItem;

import ro.trenulmeu.mobile.R;

public class MenuHandler {

    private final Menu menu;
    private final Actions actions;

    private MenuItem routes;
    private MenuItem trains;
    private MenuItem stations;
    private MenuItem status;

    private static final int routesId = R.id.menu_routes;
    private static final int trainsId = R.id.menu_trains;
    private static final int stationsId = R.id.menu_stations;
    private static final int statusId = R.id.menu_status;

    public MenuHandler(Menu menu, Actions actions) {
        this.menu = menu;
        this.actions = actions;

        routes = menu.findItem(routesId);
        trains = menu.findItem(trainsId);
        stations = menu.findItem(stationsId);
        status = menu.findItem(statusId);
    }

    public Menu getMenu() {
        return menu;
    }

    public boolean handle(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case routesId :
                actions.onRoutes();
                break;
            case trainsId :
                actions.onTrains();
                break;
            case stationsId :
                actions.onStations();
                break;
            case statusId :
                actions.onStatus();
                break;
            default:
                return false;
        }

        return true;
    }

    public interface Actions {
        void onRoutes();
        void onTrains();
        void onStations();
        void onStatus();
    }

}
