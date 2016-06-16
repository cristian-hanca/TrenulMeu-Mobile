package ro.trenulmeu.mobile.api.models;

import lombok.Data;

/**
 * Model for a Named Station JSON.
 */
@Data
public class NamedStation {
    private int Id;
    private int OriginalId;
    private String Name;
    private float Lat;
    private float Lon;
    private byte TimeOffset;
}
