package be.ehb.wifree_2;

import com.google.android.gms.maps.model.LatLng;

public class WifiPlace {
    // WifiPlace attributes
    private String title, description;
    private LatLng location;

    // WifiPlace constructors
    public WifiPlace() {}

    public WifiPlace(String title, String description, LatLng location) {
        this.title = title;
        this.description = description;
        this.location = location;
    }

    // WifiPlace getters and setters
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LatLng getLocation() {
        return location;
    }

    public void setLocation(LatLng location) {
        this.location = location;
    }
}
