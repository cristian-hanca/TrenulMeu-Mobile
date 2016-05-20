package ro.trenulmeu.mobile.helpers;

import android.database.sqlite.SQLiteDatabase;

import java.io.File;

import ro.trenulmeu.mobile.AppContext;
import ro.trenulmeu.mobile.Constants;
import ro.trenulmeu.mobile.models.DaoMaster;
import ro.trenulmeu.mobile.models.DaoSession;

/**
 * Helper Class for Database Management.
 */
public class DatabaseHelpers {

    /**
     * Returns the Database File.
     */
    private static File getFile() {
        return AppContext.activity.getFileStreamPath(Constants.dbName);
    }

    /**
     * Determines if the Database file exists.
     */
    public static boolean fileExists() {
        return getFile().exists();
    }

    /**
     * Open the Database and return a GreenDAO Session.
     */
    public static DaoSession open() {
        return new DaoMaster(SQLiteDatabase.openDatabase(
                getFile().getAbsolutePath(), null, SQLiteDatabase.OPEN_READONLY)).newSession();
    }

    /**
     * Deletes the Database File.
     * @return True if deleted, else False.
     */
    public static boolean delete() {
        return getFile().delete();
    }

    /**
     * SStatic Class.
     */
    private DatabaseHelpers() { }

}
