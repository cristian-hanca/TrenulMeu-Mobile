package ro.trenulmeu.mobile.api.models;

import java.util.List;

import lombok.Data;
import ro.trenulmeu.mobile.models.Station;

/**
 * Model for the Routing Result JSON.
 */
@Data
public class PathResult {
    private Station Start;
    private Station End;
    private List<PathOption> Options;
}
