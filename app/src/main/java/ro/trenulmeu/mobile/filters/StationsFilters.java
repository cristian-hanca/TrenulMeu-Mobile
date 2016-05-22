package ro.trenulmeu.mobile.filters;

import com.annimon.stream.Collectors;
import com.annimon.stream.Stream;
import com.annimon.stream.function.Predicate;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;

import java.util.List;

import ro.trenulmeu.mobile.AppContext;
import ro.trenulmeu.mobile.helpers.StringHelpers;
import ro.trenulmeu.mobile.models.Station;

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
            builder.put(s.getId(), StringHelpers.normalize(s.getName()));
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
            final String searchTerm = StringHelpers.normalize(search);
            filtered = Stream.of(original).filter(new Predicate<Station>() {
                @Override
                public boolean test(Station value) {
                    return namesMap.get(value.getId()).startsWith(searchTerm);
                }
            }).collect(Collectors.<Station>toList());
        }
        return filtered;
    }

    public String getSearch() {
        return search;
    }

    public void setSearch(String search) {
        this.invalidate = true;
        this.search = search;
    }

}
