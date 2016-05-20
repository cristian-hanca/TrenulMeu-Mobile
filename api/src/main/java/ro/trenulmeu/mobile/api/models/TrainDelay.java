package ro.trenulmeu.mobile.api.models;

import lombok.Data;

/**
 * Train Delay JSON.
 */
@Data
public class TrainDelay {
    private int TrainId;
    private int Delay;
    private boolean Error;
}
