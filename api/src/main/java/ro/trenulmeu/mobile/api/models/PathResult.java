package ro.trenulmeu.mobile.api.models;

import java.util.List;

import lombok.Data;

/**
 * Model for the Routing Result JSON.
 */
@Data
public class PathResult {
    private NamedStation Start;
    private NamedStation End;
    private List<PathOption> Options;
}
