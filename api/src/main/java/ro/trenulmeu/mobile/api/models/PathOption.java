package ro.trenulmeu.mobile.api.models;

import java.util.List;

import lombok.Data;
import ro.trenulmeu.mobile.timespan.TimeSpan;

/**
 * Model for a Route Option JSON.
 */
@Data
public class PathOption {
    private TimeSpan StartTime;
    private TimeSpan EndTime;
    private TimeSpan TotalTime;
    private List<PathStep> Steps;
}
