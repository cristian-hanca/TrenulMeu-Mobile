package ro.trenulmeu.mobile.api.models;

import lombok.Data;

/**
 * Model for a Named Train JSON.
 */
@Data
public class NamedTrain {
    private int TrainId;
    private int OperatorId;
    private String OperatorName;
    private int TypeId;
    private String TypeName;
    private int ServiceId;
    private String ServiceName;
    private String Name;
    private String OriginalName;
    private String StartName;
    private String EndName;
}
