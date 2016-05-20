package ro.trenulmeu.mobile.helpers;

import retrofit2.Response;
import ro.trenulmeu.mobile.AppContext;
import ro.trenulmeu.mobile.api.fetcher.FetchUnit;

/**
 * A special implementation of the FetchCallbacks that cleans up after itself by removing the Fetch
 * action from the Cache in AppContext.
 */
public abstract class CleanupFetchCallbacks<T> implements FetchUnit.FetchCallbacks<T> {

    private final String key;

    protected CleanupFetchCallbacks(String key) {
        this.key = key;
    }

    @Override
    public abstract void onStart();

    @Override
    public void onException(Exception e) {
        cleanUp();
        onFail(e);
    }

    @Override
    public void onCancel() {
        cleanUp();
        onFail(new RuntimeException("Canceled"));
    }

    @Override
    public void onError(Response response) {
        cleanUp();
        onFail(new RuntimeException(response.code() + " | " + response.message()));
    }

    @Override
    public void onDone(T result) {
        cleanUp();
        onSuccess(result);
    }

    private void cleanUp() {
        AppContext.cache.delete(key);
    }

    public abstract void onFail(Exception e);

    public abstract void onSuccess(T result);

}
