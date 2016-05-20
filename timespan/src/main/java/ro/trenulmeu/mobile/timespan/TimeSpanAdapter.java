package ro.trenulmeu.mobile.timespan;

import de.greenrobot.dao.converter.PropertyConverter;

/**
 * Adapter for TimeSpan from GreenDAO.
 */
public class TimeSpanAdapter implements PropertyConverter<TimeSpan, Short> {

    public TimeSpan convertToEntityProperty(Short databaseValue) {
        return new TimeSpan(databaseValue);
    }

    public Short convertToDatabaseValue(TimeSpan entityProperty) {
        return entityProperty.getTicks();
    }

}
