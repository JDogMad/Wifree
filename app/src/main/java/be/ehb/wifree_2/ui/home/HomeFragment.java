package be.ehb.wifree_2.ui.home;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import be.ehb.wifree_2.R;
import be.ehb.wifree_2.RegisterActivity;
import be.ehb.wifree_2.User;
import be.ehb.wifree_2.WifiHotspot;
import be.ehb.wifree_2.WifiHotspots;
import be.ehb.wifree_2.WifiPlace;
import be.ehb.wifree_2.data.FirebaseDatabase;
import be.ehb.wifree_2.databinding.FragmentHomeBinding;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class HomeFragment extends Fragment {
    public FirebaseFirestore db = FirebaseFirestore.getInstance();

    private FragmentHomeBinding binding;
    private GoogleMap map;
    private FirebaseAuth mAuth;

    TextView txt_username;
    Button btn_logout;

    private OnMapReadyCallback onMapReadyCallback = new OnMapReadyCallback() {
        @Override
        public void onMapReady(@NonNull GoogleMap googleMap) {
            map = googleMap;

            drawAnnotations();
            fetchMarkers();
        }
    };

    private void drawAnnotations() {
        db.collection("Places").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Map<String, Object> data = document.getData();
                        WifiPlace place = new WifiPlace();
                        // Set the fields for the Post object
                        place.setTitle((String) data.get("title"));
                        place.setDescription((String) data.get("description"));

                        // Check if there is a location in post => avoids errors
                        if (data.containsKey("location")) {
                            // Deserialize the JSON object stored in the "location" field
                            ObjectMapper mapper = new ObjectMapper();
                            String locationJson = null;
                            try {
                                locationJson = mapper.writeValueAsString(data.get("location"));
                            } catch (JsonProcessingException e) {
                                e.printStackTrace();
                            }
                            JsonParser parser = null;
                            try {
                                parser = mapper.getFactory().createParser(locationJson);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            LatLngDeserializer latLngDeserializer = new LatLngDeserializer();
                            LatLng location = null;
                            try {
                                location = latLngDeserializer.deserialize(parser, null);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                            // Add a marker to the map using the deserialized LatLng object
                            map.addMarker(new MarkerOptions().position(location)
                                    .title(place.getTitle())
                                    .snippet(place.getDescription()));
                            map.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 10));
                        }
                    }
                } else {
                    Log.d(TAG, Objects.requireNonNull(task.getException()).getMessage());
                }
            }
        });
    }

    private void fetchMarkers(){
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("https://opendata.brussels.be/api/records/1.0/search/?dataset=wifi&rows=10&facet=latitude&facet=longitude")
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                // Handle failure
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseString = response.body().string();
                ObjectMapper mapper = new ObjectMapper();
                WifiHotspots wifiHotspots = mapper.readValue(responseString, WifiHotspots.class);

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // Code to add markers to the map here
                        List<WifiHotspot> hotspots = wifiHotspots.getRecords();
                        for(WifiHotspot hotspot: hotspots){
                            double latitude = Double.parseDouble(hotspot.getHotspotFields().getLatitude());
                            double longitude = Double.parseDouble(hotspot.getHotspotFields().getLongitude());
                            LatLng location = new LatLng(latitude, longitude);

                            MarkerOptions markerOptions = new MarkerOptions()
                                    .position(location)
                                    .title(hotspot.getHotspotFields().getNaam())
                                    .snippet(hotspot.getHotspotFields().getPlace());

                            map.addMarker(markerOptions);
                        }
                    }
                });
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        // Last check to make sure that user is logged in
        mAuth = FirebaseAuth.getInstance();
        if(mAuth.getCurrentUser() != null){
            // Redirect it to the Register activity
            Intent intent = new Intent(getActivity(), RegisterActivity.class);
            startActivity(intent);
        }

        // Search the activity for the textview
        txt_username = binding.getRoot().findViewById(R.id.txt_greeting);
        // Call the method to get met the username
        getUserUsername(txt_username);

        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Als mijn scherm getekend is, ga kijken naar de view
        // Dit gebeurd async, dus in de achtergrond
        binding.mapView2.getMapAsync(onMapReadyCallback);
        binding.mapView2.onCreate(savedInstanceState);
    }

    @Override
    public void onPause(){
        super.onPause();
        binding.mapView2.onPause();
    }

    // Als je terug gaat naar de activiteit
    @Override
    public void onResume(){
        super.onResume();
        binding.mapView2.onResume();
    }

    // Als ik klaar ben dan gebruik ik de ondestroy methode
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding.mapView2.onDestroy();
        binding = null;
    }

    public void getUserUsername(TextView txt_greeting){
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseDatabase database = new FirebaseDatabase();
        Task<User> userTask = database.getUserFromDbByUidTask(mAuth.getUid());
        userTask.addOnCompleteListener(new OnCompleteListener<User>() {
            @Override
            public void onComplete(@NonNull Task<User> task) {
                if (task.isSuccessful()) {
                    User user = task.getResult();
                    if(user != null){
                        txt_greeting.setText("Hi " + user.getUsername());
                    } else {
                        System.out.println("User is null");
                    }
                } else {
                    // TODO: Error handling
                    System.out.println("error");
                }
            }
        });
    }

}