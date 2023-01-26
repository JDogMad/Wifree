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
    public FirebaseFirestore db = FirebaseFirestore.getInstance(); // initializing instance of Firebase Firestore

    private FragmentHomeBinding binding;
    private GoogleMap map;
    private FirebaseAuth mAuth;

    TextView txt_username;

    private OnMapReadyCallback onMapReadyCallback = new OnMapReadyCallback() {
        @Override
        public void onMapReady(@NonNull GoogleMap googleMap) {
            map = googleMap;

            drawAnnotations(); // draws the markers that users have made
            fetchMarkers(); // draws the markers from the OpenData Brussels api
        }
    };

    // Method to get the annotations made by the users stored in Firebase
    private void drawAnnotations() {
        // get all the places from the database
        db.collection("Places").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) { // task is successful
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Map<String, Object> data = document.getData();
                        WifiPlace place = new WifiPlace(); // new wifiplace
                        // set all the attributes of the new wifiplace
                        place.setTitle((String) data.get("title")); // set the title -> with the title from the database
                        place.setDescription((String) data.get("description")); // set the description -> with the description from the database

                        if (data.containsKey("location")) { // checks if location field exists in the document data
                            ObjectMapper mapper = new ObjectMapper(); // new mapper (part of Jackson library)
                            String locationJson = null;
                            try {
                                locationJson = mapper.writeValueAsString(data.get("location")); // convert map location to JSON string
                            } catch (JsonProcessingException e) { // catches any errors
                                e.printStackTrace();
                            }

                            JsonParser parser = null;
                            try {
                                parser = mapper.getFactory().createParser(locationJson); // parses the locationJson
                            } catch (IOException e) { // catches any errors
                                e.printStackTrace();
                            }

                            LatLngDeserializer latLngDeserializer = new LatLngDeserializer(); // new deserializer (custom class)
                            LatLng location = null;
                            try {
                                location = latLngDeserializer.deserialize(parser, null); // deserialize the location so I can get the latitude and longitude
                            } catch (IOException e) { // catches any errors
                                e.printStackTrace();
                            }

                            // Add a marker to the map using the deserialized LatLng object
                            map.addMarker(new MarkerOptions().position(location)
                                    .title(place.getTitle())
                                    .snippet(place.getDescription()));
                            map.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 10));
                        }
                    }
                } else { // logs error
                    Log.d(TAG, Objects.requireNonNull(task.getException()).getMessage());
                }
            }
        });
    }

    // Method to get the annotations from the OpenData Brussels api
    private void fetchMarkers(){
        OkHttpClient client = new OkHttpClient(); // new Client -> handles the HTTP request
        Request request = new Request.Builder() // new request builder -> on basis of the url -> build
                .url("https://opendata.brussels.be/api/records/1.0/search/?dataset=wifi&rows=10&facet=latitude&facet=longitude")
                .build();

        client.newCall(request).enqueue(new Callback() { // new call of the request
            @Override
            public void onFailure(Call call, IOException e) {
                // Handle failure
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseString = response.body().string(); // converts the request body
                ObjectMapper mapper = new ObjectMapper(); // new mapper (part of Jackson library)
                WifiHotspots wifiHotspots = mapper.readValue(responseString, WifiHotspots.class); // deserializes the response to a wifiHotspot

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        List<WifiHotspot> hotspots = wifiHotspots.getRecords(); // new list where you add the hotspots
                        for(WifiHotspot hotspot: hotspots){ // loop trough the hotspots
                            double latitude = Double.parseDouble(hotspot.getHotspotFields().getLatitude()); // get the latitude
                            double longitude = Double.parseDouble(hotspot.getHotspotFields().getLongitude()); // get the longitude
                            LatLng location = new LatLng(latitude, longitude); // new LatLng object

                            // Add a marker to the map using the deserialized LatLng object
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
        //binding with Home layout
        binding = FragmentHomeBinding.inflate(inflater, container, false);

        // check to make sure that user is logged in
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

        // get map async (background) and create
        binding.mapView2.getMapAsync(onMapReadyCallback);
        binding.mapView2.onCreate(savedInstanceState);
    }

    @Override
    public void onPause(){
        super.onPause();
        binding.mapView2.onPause();
    }

    @Override
    public void onResume(){
        super.onResume();
        binding.mapView2.onResume();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding.mapView2.onDestroy();
        binding = null;
    }

    public void getUserUsername(TextView txt_greeting){
        FirebaseAuth mAuth = FirebaseAuth.getInstance(); // initializing instance of Firebase Firestore
        FirebaseDatabase database = new FirebaseDatabase(); // new FirebaseDatabase

        Task<User> userTask = database.getUserFromDbByUidTask(mAuth.getUid()); // get current user
        userTask.addOnCompleteListener(new OnCompleteListener<User>() { // when user is fetched
            @Override
            public void onComplete(@NonNull Task<User> task) {
                if (task.isSuccessful()) { // checks if the task is successful
                    User user = task.getResult();
                    if(user != null){ // user must not be null
                        txt_greeting.setText("Hi " + user.getUsername()); // displays greeting
                    } else {
                        System.out.println("User is null");
                    }
                } else { // error handling
                    System.out.println("error");
                }
            }
        });
    }

}