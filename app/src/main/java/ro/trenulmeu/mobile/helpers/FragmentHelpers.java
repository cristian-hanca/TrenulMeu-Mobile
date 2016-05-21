package ro.trenulmeu.mobile.helpers;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import ro.trenulmeu.mobile.AppContext;
import ro.trenulmeu.mobile.R;

/**
 * Helper Class for Fragments Management.
 */
public class FragmentHelpers {

    /**
     * Pops all the Fragments up to and including the one of the specified Tag.
     */
    public static boolean popInclusive(String tag) {
        FragmentManager fm = getFM();
        if (fm.findFragmentByTag(tag) != null) {
            fm.popBackStackImmediate(tag, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            return true;
        }
        return false;
    }

    /**
     * Pops all the Fragments in the Back Stack.
     */
    public static void popAll() {
        FragmentManager fm = getFM();
        while (fm.getBackStackEntryCount() > 0) {
            fm.popBackStackImmediate();
        }
    }

    /**
     * Return the number of Items in the Back Stack.
     */
    public static int backCount() {
        return getFM().getBackStackEntryCount();
    }

    /**
     * Goto the specified Fragment using the given Tag.
     */
    public static <T extends Fragment> void goTo(T fragment, String tag) {
        FragmentTransaction ft = getFM().beginTransaction();
        ft.replace(R.id.fragment, fragment, tag);
        ft.addToBackStack(tag);
        ft.commit();
    }

    /**
     * Similar to goTo, only that it popsInclusive before going to the Fragment.
     * Guarantees that only one instance of the Fragment is on the back Stack (by Tag).
     */
    public static <T extends Fragment> void goToSingleton(T fragment, String tag) {
        popInclusive(tag);
        goTo(fragment, tag);
    }

    /**
     * Returns True if a Fragment of the given Tag was found, else False.
     */
    public static boolean existsByTag(String tag) {
        return getFM().findFragmentByTag(tag) != null;
    }

    /**
     * Returns the Fragment of the given Tag, or Null if it does not exist.
     */
    public static <T extends Fragment> T findByTag(String tag, Class<T> clazz) {
        Fragment f = getFM().findFragmentByTag(tag);
        if (f != null) {
            try {
                return clazz.cast(f);
            } catch (Exception e) {
                return null;
            }
        }
        return null;
    }

    /**
     * Returns the Fragment of the given Tag, or Default Value if it does not exist.
     */
    public static <T extends Fragment> T findByTag(String tag, Class<T> clazz, T defaultValue) {
        T f = findByTag(tag, clazz);
        return f == null ? defaultValue : f;
    }

    /**
     * Gets the Support Fragment Manager.
     */
    private static FragmentManager getFM() {
        return AppContext.activity.getSupportFragmentManager();
    }

    /**
     * Static Class.
     */
    private FragmentHelpers() {}

}
