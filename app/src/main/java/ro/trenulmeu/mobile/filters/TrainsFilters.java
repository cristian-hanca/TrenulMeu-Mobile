package ro.trenulmeu.mobile.filters;

import com.annimon.stream.Collectors;
import com.annimon.stream.Stream;
import com.annimon.stream.function.Predicate;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import de.greenrobot.dao.query.WhereCondition;
import ro.trenulmeu.mobile.AppContext;
import ro.trenulmeu.mobile.filters.models.CheckItem;
import ro.trenulmeu.mobile.models.Train;
import ro.trenulmeu.mobile.models.TrainDao;
import ro.trenulmeu.mobile.models.TrainOperator;
import ro.trenulmeu.mobile.models.TrainType;

/**
 * Managed Filter for the Trains view.
 */
public class TrainsFilters {

    private final List<Train> original;

    private List<Train> filtered;

    private List<CheckItem<TrainOperator>> operators;

    private List<CheckItem<TrainType>> types;

    private boolean onlyRunning;

    private String search;

    private boolean invalidate = false;

    public TrainsFilters() {
        this.original = AppContext.db.getTrainDao().queryBuilder().list();

        operators = new ArrayList<>();
        for (TrainOperator op : AppContext.db.getTrainOperatorDao().loadAll()) {
            operators.add(new CheckItem<>(op, op.getName(), true));
        }
        types = new ArrayList<>();
        for (TrainType ty : AppContext.db.getTrainTypeDao().loadAll()) {
            types.add(new CheckItem<>(ty, ty.getLongName(), true));
        }
        onlyRunning = false;
        search = "";

        this.filtered = Lists.newArrayList(this.original);
        this.invalidate = false;
    }

    public List<Train> getOriginal() {
        return original;
    }

    public List<Train> getFiltered(boolean invalidate) {
        if (invalidate) {
            this.invalidate = true;
        }
        return getFiltered();
    }

    public List<Train> getFiltered() {
        if (invalidate) {
            if (onlyRunning) {
                filtered = Stream.of(AppContext.db.getTrainDao().queryBuilder()
                        .where(TrainDao.Properties.OperatorId.in(extractSelectedOperators()))
                        .where(TrainDao.Properties.TypeId.in(extractSelectedTypes()))
                        .orderAsc(TrainDao.Properties.Id)
                        .list())
                        .filter(new Predicate<Train>() {
                            @Override
                            public boolean test(Train value) {
                                return value.runsOnDate(new Date());
                            }
                        })
                        .collect(Collectors.<Train>toList());
            } else {
                filtered = AppContext.db.getTrainDao().queryBuilder()
                        .where(TrainDao.Properties.OperatorId.in(extractSelectedOperators()))
                        .where(TrainDao.Properties.TypeId.in(extractSelectedTypes()))
                        .orderAsc(TrainDao.Properties.Id)
                        .list();
            }
        }

        if (Strings.isNullOrEmpty(search)) {
            return filtered;
        }
        return Stream.of(filtered).filter(new Predicate<Train>() {
            @Override
            public boolean test(Train value) {
                return value.getName().startsWith(search);
            }
        }).collect(Collectors.<Train>toList());
    }

    public String getSearch() {
        return search;
    }

    public void setSearch(String search) {
        this.search = search;
    }

    public boolean getOnlyRunning() {
        return onlyRunning;
    }

    public void setOnlyRunning(boolean onlyRunning) {
        this.onlyRunning = onlyRunning;
        this.invalidate = true;
    }

    public List<CheckItem<TrainOperator>> getOperators() {
        return operators;
    }

    public void setOperators(List<CheckItem<TrainOperator>> operators) {
        this.operators = operators;
        this.invalidate = true;
    }

    public List<CheckItem<TrainType>> getTypes() {
        return types;
    }

    public void setTypes(List<CheckItem<TrainType>> types) {
        this.types = types;
        this.invalidate = true;
    }

    private List<Long> extractSelectedOperators() {
        List<Long> result = new ArrayList<>();
        for (CheckItem<TrainOperator> item : operators) {
            if (item.isCheck()) {
                result.add(item.getItem().getId());
            }
        }
        return result;
    }

    private List<Long> extractSelectedTypes() {
        List<Long> result = new ArrayList<>();
        for (CheckItem<TrainType> item : types) {
            if (item.isCheck()) {
                result.add(item.getItem().getId());
            }
        }
        return result;
    }

}
