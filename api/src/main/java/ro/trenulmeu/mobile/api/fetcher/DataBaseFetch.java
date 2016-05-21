package ro.trenulmeu.mobile.api.fetcher;

import android.os.AsyncTask;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import retrofit2.Response;

/**
 * Special Class designed to download the SQLite Database File.
 * We cannot use Retrofit2 since it cannot be used to download files with Progress.
 */
public class DataBaseFetch {

    private File destination;
    private String address;

    private AsyncTask<Void, Integer, Void> task;

    private Callbacks callbacks;
    private final BaseCallbacks baseCallbacks;

    public DataBaseFetch(String address, File destination) {
        this.address = address;
        this.destination = destination;

        this.task = null;

        this.baseCallbacks = new BaseCallbacks();
        this.callbacks = this.baseCallbacks;
    }

    public void fetch() {
        task = new AsyncTask<Void, Integer, Void>() {
            boolean failed = false;

            @Override
            protected Void doInBackground(Void... params) {
                InputStream input = null;
                OutputStream output = null;
                HttpURLConnection connection = null;
                try {
                    URL url = new URL(address);
                    connection = (HttpURLConnection) url.openConnection();
                    connection.connect();

                    if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                        failed = true;
                        callbacks.onException(new RuntimeException("Server error! (" +
                                connection.getResponseCode() + ") " + connection.getResponseMessage()));
                        return null;
                    }

                    int fileLength = connection.getContentLength();
                    input = connection.getInputStream();
                    output = new FileOutputStream(destination);

                    byte data[] = new byte[4096];
                    long total = 0;
                    int count;
                    while ((count = input.read(data)) != -1) {
                        if (isCancelled()) {
                            input.close();
                            return null;
                        }
                        total += count;
                        if (fileLength > 0) {
                            publishProgress((int) (total * 100 / fileLength));
                        }
                        output.write(data, 0, count);
                    }
                } catch (Exception e) {
                    failed = true;
                    callbacks.onException(e);
                } finally {
                    try {
                        if (output != null)
                            output.close();
                        if (input != null)
                            input.close();
                    } catch (Exception ignored) { }

                    if (connection != null)
                        connection.disconnect();
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
            protected void onProgressUpdate(Integer... values) {
                if (!failed) callbacks.onProgress(values[0]);
                super.onProgressUpdate(values);
            }

            @Override
            protected void onPostExecute(Void result) {
                if (!failed) callbacks.onDone(null);
                super.onPostExecute(result);
            }
        };
        task.execute();
    }

    public void setCallbacks(Callbacks callbacks) {
        this.callbacks = callbacks == null ? baseCallbacks : callbacks;
    }

    public AsyncTask<Void, Integer, Void> getTask() {
        return task;
    }

    public interface Callbacks extends FetchUnit.FetchCallbacks<Void> {
        void onProgress(int progress);
    }

    private class BaseCallbacks implements Callbacks {
        @Override
        public void onProgress(int progress) { }
        @Override
        public void onStart() { }
        @Override
        public void onException(Exception e) { }
        @Override
        public void onCancel() { }
        @Override
        public void onError(Response<Void> response) { }
        @Override
        public void onDone(Void result) { }
    }

}
