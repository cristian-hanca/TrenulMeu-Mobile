package ro.trenulmeu.mobile;

import java.util.Map;

import ro.trenulmeu.mobile.filters.StationsFilters;
import ro.trenulmeu.mobile.filters.TrainsFilters;
import ro.trenulmeu.mobile.helpers.DataCache;
import ro.trenulmeu.mobile.models.DaoSession;
import ro.trenulmeu.mobile.models.Station;
import ro.trenulmeu.mobile.models.Train;

/**
 * Application-wide Context.
 * Stores important information that will live for the life of the Application.
 */
public class AppContext {

    /**
     * Stores an up-to-date reference to the MainActivity.
     */
    public static MainActivity activity = null;

    /**
     * Open Database Secession.
     */
    public static DaoSession db = null;

    /**
     * Selected Train to be viewed.
     */
    public static Train selectedTrain;

    /**
     * Selected Station to be viewed.
     */
    public static Station selectedStation;

    /**
     * Train Filter to be used in the TrainsFragment.
     */
    public static TrainsFilters trainFilter = null;

    /**
     * Station Filter to be used in the StationsFragment.
     */
    public static StationsFilters stationFilter = null;

    /**
     * Map between Stations and their Normalized Station Name.
     */
    public static Map<Station, String> stationNameMap = null;

    /**
     * Data Cache Singleton.
     */
    public static final DataCache cache = new DataCache();

    /**
     * Static Class.
     */
    private AppContext() {}

}
