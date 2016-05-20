package ro.trenulmeu.mobile.api.fetcher;

import android.os.AsyncTask;

import java.util.concurrent.Callable;

import retrofit2.Call;
import retrofit2.Response;

/**
 * Generic API caller using Retrofit2.
 * Allows for a managed AsyncTask with Callbacks.
 * @param <T> Expected Result.
 */
public class FetchUnit<T> {

    private AsyncTask<Callable<Call<T>>, Void, T> task;

    private FetchCallbacks<T> callbacks;
    private final BaseCallbacks<T> baseCallbacks;

    public FetchUnit() {
        this.task = null;
        this.baseCallbacks = new BaseCallbacks<>();
    }

    public FetchUnit(FetchCallbacks<T> callbacks) {
        this();
        this.callbacks = callbacks;
    }

    public FetchCallbacks<T> getCallbacks() {
        return callbacks;
    }

    public void setCallbacks(FetchCallbacks<T> callbacks) {
        this.callbacks = callbacks == null ? baseCallbacks : callbacks;
    }

    public AsyncTask<Callable<Call<T>>, Void, T> getTask() {
        return task;
    }

    public void fetch(Callable<Call<T>> toExecute) {
        task = new AsyncTask<Callable<Call<T>>, Void, T>() {
            boolean failed = false;

            @Override
            @SafeVarargs
            protected final T doInBackground(Callable<Call<T>>... params) {
                try {
                    Call<T> call = params[0].call();
                    Response<T> response = call.execute();
                    if (response.isSuccess()) {
                        return response.body();
                    } else {
                        failed = true;
                        callbacks.onError(response);
                    }
                } catch (Exception e) {
                    failed = true;
                    callbacks.onException(e);
                }
                return null;
            }

            @Override
            protected void onPreExecute() {
                if (!failed) callbacks.onStart();
                super.onPreExecute();
            }

            @Override
            protected void onCancelled() {
                if (!failed) callbacks.onCancel();
                super.onCancelled();
            }

            @Override
            protected void onPostExecute(T rResponse) {
                if (!failed) callbacks.onDone(rResponse);
                super.onPostExecute(rResponse);
            }
        };
        task.execute(toExecute);
    }

    public interface FetchCallbacks<T> {
        void onStart();
        void onException(final Exception e);
        void onCancel();
        void onError(final Response<T> response);
        void onDone(final T result);
    }

    private class BaseCallbacks<T2> implements FetchCallbacks<T2> {
        @Override
        public void onStart() { }
        @Override
        public void onException(final Exception e) { }
        @Override
        public void onCancel() { }
        @Override
        public void onError(final Response<T2> response) { }
        @Override
        public void onDone(final T2 result) { }
    }

}
