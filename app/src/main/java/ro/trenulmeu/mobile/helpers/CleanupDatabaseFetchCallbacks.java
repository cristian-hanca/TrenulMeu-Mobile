package ro.trenulmeu.mobile.helpers;

import ro.trenulmeu.mobile.api.fetcher.DataBaseFetch;

/**
 * Same as CleanupFetchCallbacks, only for the Database Fetch.
 */
public abstract class CleanupDatabaseFetchCallbacks extends CleanupFetchCallbacks<Void> implements DataBaseFetch.Callbacks {

    protected CleanupDatabaseFetchCallbacks(String key) {
        super(key);
    }

}
