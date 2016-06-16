package ro.trenulmeu.mobile.api.models;

import lombok.Data;
import ro.trenulmeu.mobile.timespan.TimeSpan;

/**
 * Model for a Step in a Route.
 */
@Data
public class PathStep {
    private NamedTrain Train;
    private NamedStation Start;
    private NamedStation End;
    private int Stops;
    private TimeSpan StartTime;
    private TimeSpan EndTime;
}
