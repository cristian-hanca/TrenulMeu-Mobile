package ro.trenulmeu.mobile.models;

// THIS CODE IS GENERATED BY greenDAO, EDIT ONLY INSIDE THE "KEEP"-SECTIONS

// KEEP INCLUDES - put your custom includes here
// KEEP INCLUDES END
/**
 * Entity mapped to table "Station".
 */
public class Station {

    private Long id;
    private String Name;
    private Float Lat;
    private Float Lon;
    private Byte TimeOffset;

    // KEEP FIELDS - put your custom fields here
    // KEEP FIELDS END

    public Station() {
    }

    public Station(Long id) {
        this.id = id;
    }

    public Station(Long id, String Name, Float Lat, Float Lon, Byte TimeOffset) {
        this.id = id;
        this.Name = Name;
        this.Lat = Lat;
        this.Lon = Lon;
        this.TimeOffset = TimeOffset;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String Name) {
        this.Name = Name;
    }

    public Float getLat() {
        return Lat;
    }

    public void setLat(Float Lat) {
        this.Lat = Lat;
    }

    public Float getLon() {
        return Lon;
    }

    public void setLon(Float Lon) {
        this.Lon = Lon;
    }

    public Byte getTimeOffset() {
        return TimeOffset;
    }

    public void setTimeOffset(Byte TimeOffset) {
        this.TimeOffset = TimeOffset;
    }

    // KEEP METHODS - put your custom methods here
    // KEEP METHODS END

}
