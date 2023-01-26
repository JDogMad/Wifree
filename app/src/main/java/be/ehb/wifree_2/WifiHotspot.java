package be.ehb.wifree_2;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true) // ignore all the rest of properties of the api
public class WifiHotspot {
    // WifiHotspot attributes
    @JsonProperty("datasetid")
    private String datasetId;
    @JsonProperty("recordid")
    private String recordId;
    @JsonProperty("fields")
    private HotspotFields hotspotFields;
    @JsonProperty("geometry")
    private HotspotGeometry hotspotGeometry;

    // WifiHotspot constructor
    public WifiHotspot() {}

    // WifiHotspot getters and setters
    public HotspotFields getHotspotFields() {
        return hotspotFields;
    }

    public void setHotspotFields(HotspotFields hotspotFields) {
        this.hotspotFields = hotspotFields;
    }

    public HotspotGeometry getHotspotGeometry() {
        return hotspotGeometry;
    }

    public void setHotspotGeometry(HotspotGeometry hotspotGeometry) {
        this.hotspotGeometry = hotspotGeometry;
    }

    public String getDatasetId() {
        return datasetId;
    }

    public void setDatasetId(String datasetId) {
        this.datasetId = datasetId;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)  // ignore all the rest of properties of the api
    public static class HotspotFields {
        // HotspotFields attributes
        @JsonProperty("longitude")
        private String longitude;
        @JsonProperty("latitude")
        private String latitude;
        @JsonProperty("emplacement")
        private String place;
        @JsonProperty("nom_site_nl")
        private String naam;

        // HotspotFields getters and setters
        public String getLongitude() {
            return longitude;
        }
        public void setLongitude(String longitude) {
            this.longitude = longitude;
        }

        public String getLatitude() {
            return latitude;
        }

        public void setLatitude(String latitude) {
            this.latitude = latitude;
        }

        public String getPlace() {
            return place;
        }

        public void setPlace(String place) {
            this.place = place;
        }

        public String getNaam() {
            return naam;
        }

        public void setNaam(String naam) {
            this.naam = naam;
        }
    }

    public static class HotspotGeometry{
        // HotspotGeometry attributes
        @JsonProperty("type")
        private String type;
        @JsonProperty("coordinates")
        private double[] coordinates;

        // HotspotGeometry getters and setters
        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public double[] getCoordinates() {
            return coordinates;
        }

        public void setCoordinates(double[] coordinates) {
            this.coordinates = coordinates;
        }
    }
}
