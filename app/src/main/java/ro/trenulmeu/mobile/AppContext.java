package ro.trenulmeu.mobile;

import ro.trenulmeu.mobile.helpers.DataCache;
import ro.trenulmeu.mobile.models.DaoSession;

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
     * Data Cache Singleton.
     */
    public static final DataCache cache = new DataCache();

    /**
     * Static Class.
     */
    private AppContext() {}

}
