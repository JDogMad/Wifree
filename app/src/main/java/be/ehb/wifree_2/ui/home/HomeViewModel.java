package be.ehb.wifree_2.ui.home;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import be.ehb.wifree_2.WifiPlace;

public class HomeViewModel extends AndroidViewModel {

    public FirebaseFirestore db = FirebaseFirestore.getInstance(); // initializing instance of Firebase Firestore

    public HomeViewModel(@NonNull Application application) {
        super(application);
    }

    public MutableLiveData<List<WifiPlace>> getAllLocationsDB(){
        MutableLiveData<List<WifiPlace>> places = new MutableLiveData<>();
        CollectionReference placesRef = db.collection("Places");
        placesRef.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                List<WifiPlace> wifiPlaces = new ArrayList<>(); // new arraylist
                for (DocumentSnapshot documentSnapshot: queryDocumentSnapshots.getDocuments()) {
                    Map<String, Object> data = documentSnapshot.getData();
                    WifiPlace place = new WifiPlace(); // new wifiplace
                    // set all the attributes of the new wifiplace
                    place.setTitle((String) data.get("title")); // set the title -> with the title from the database
                    place.setDescription((String) data.get("description")); // set the description -> with the description from the database

                    Object locationData = data.get("location"); // get the location from the database
                    if (locationData != null) { // locationData can't be null
                        ObjectMapper mapper = new ObjectMapper(); // new mapper (part of Jackson library)
                        Map<String, Object> locationMap = (Map<String, Object>) data.get("location");
                        String locationJson = null;
                        try {
                            locationJson = mapper.writeValueAsString(locationMap); // convert map location to JSON string
                        } catch (JsonProcessingException e) {
                            e.printStackTrace();
                        }

                        LatLng location = null;
                        try {
                            location = mapper.readValue(locationJson, LatLng.class); // sets the location to the LatLng object
                        } catch (JsonProcessingException e) {
                            e.printStackTrace();
                        }
                        place.setLocation(location); // sets the attribute of place loaction to the new location that we got
                    }

                    wifiPlaces.add(place); // adds it to the arraylist
                }
                places.setValue(wifiPlaces); // adds it to the MutableLiveData
            }
        });
        return places; // returns it
    }

}