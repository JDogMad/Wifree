package be.ehb.wifree_2.ui.home;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.Map;

public class LatLngDeserializer extends JsonDeserializer<LatLng> {
    @Override
    public LatLng deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        Map<String, Object> map = p.readValueAs(new TypeReference<Map<String, Object>>() {});
        if (map != null) {
            double lat = (double) map.get("latitude");
            double lng = (double) map.get("longitude");
            return new LatLng(lat, lng);
        }
        return null;
    }
}
