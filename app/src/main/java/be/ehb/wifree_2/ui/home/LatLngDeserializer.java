package be.ehb.wifree_2.ui.home;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.Map;

// Custom class to deserialize
public class LatLngDeserializer extends JsonDeserializer<LatLng> {

    // Method that will deserialze -> two parameters a jsonparser and a context
    @Override
    public LatLng deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        // converts JSON data from the JsonParser into a Map object
        Map<String, Object> map = p.readValueAs(new TypeReference<Map<String, Object>>() {});
        if (map != null) { // map can't be null
            double lat = (double) map.get("latitude"); // get the latitude
            double lng = (double) map.get("longitude"); // get the longitude
            return new LatLng(lat, lng); // returns a LatLng object
        }
        return null;
    }
}
