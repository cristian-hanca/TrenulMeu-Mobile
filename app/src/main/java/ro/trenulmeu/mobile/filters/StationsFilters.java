package ro.trenulmeu.mobile.filters;

import android.support.v7.app.AlertDialog;

import com.annimon.stream.Collectors;
import com.annimon.stream.Stream;
import com.annimon.stream.function.Predicate;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;

import org.joda.time.DateTime;

import java.util.List;

import ro.trenulmeu.mobile.AppContext;
import ro.trenulmeu.mobile.models.Station;
import ro.trenulmeu.mobile.models.StationDao;

/**
 * Managed Filter for the Trains view.
 */
public class StationsFilters {

    private final List<Station> original;
    private final ImmutableMap<Long, String> namesMap;

    private List<Station> filtered;

    private String search;

    private boolean invalidate = false;

    public StationsFilters() {
        this.original = AppContext.db.getStationDao().queryBuilder().list();
        this.filtered = Lists.newArrayList(this.original);

        search = "";
        this.invalidate = false;

        ImmutableMap.Builder<Long, String> builder = ImmutableMap.builder();
        for (Station s : this.original) {
            builder.put(s.getId(), removeChr(s.getName()));
        }
        namesMap = builder.build();
    }

    public List<Station> getFiltered(boolean invalidate) {
        if (invalidate) {
            this.invalidate = true;
        }
        return getFiltered();
    }

    public List<Station> getFiltered() {
        if (invalidate) {
            final String searchTerm = removeChr(search);
            filtered = Stream.of(original).filter(new Predicate<Station>() {
                @Override
                public boolean test(Station value) {
                    return namesMap.get(value.getId()).startsWith(searchTerm);
                }
            }).collect(Collectors.<Station>toList());
        }
        return filtered;
    }

    private String removeChr(String str) {
        return str.toLowerCase()
                .replaceAll(".+ăâá", "a")
                .replaceAll("é", "e")
                .replaceAll(".+îí", "i")
                .replaceAll("ş", "s")
                .replaceAll(".+öóő", "o")
                .replaceAll(".+üúű", "u")
                .replaceAll("ţ", "t");
    }

    public String getSearch() {
        return search;
    }

    public void setSearch(String search) {
        this.invalidate = true;
        this.search = search;
    }

}
