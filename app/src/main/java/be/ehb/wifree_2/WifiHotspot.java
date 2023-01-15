package be.ehb.wifree_2;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class WifiHotspot {
    @JsonProperty("datasetid")
    private String datasetId;
    @JsonProperty("recordid")
    private String recordId;
    @JsonProperty("fields")
    private HotspotFields hotspotFields;
    @JsonProperty("geometry")
    private HotspotGeometry hotspotGeometry;


    public WifiHotspot() {}

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

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class HotspotFields {
        @JsonProperty("longitude")
        private String longitude;
        @JsonProperty("latitude")
        private String latitude;
        @JsonProperty("emplacement")
        private String place;
        @JsonProperty("nom_site_nl")
        private String naam;

        //getters and setters
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
        @JsonProperty("type")
        private String type;
        @JsonProperty("coordinates")
        private double[] coordinates;

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
