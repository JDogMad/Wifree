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

    public FirebaseFirestore db = FirebaseFirestore.getInstance();

    public HomeViewModel(@NonNull Application application) {
        super(application);
    }

    public MutableLiveData<List<WifiPlace>> getAllLocationsDB(){
        MutableLiveData<List<WifiPlace>> places = new MutableLiveData<>();
        CollectionReference placesRef = db.collection("Places");
        placesRef.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                List<WifiPlace> postList = new ArrayList<>();
                for (DocumentSnapshot documentSnapshot: queryDocumentSnapshots.getDocuments()) {
                    Map<String, Object> data = documentSnapshot.getData();
                    WifiPlace place = new WifiPlace();
                    place.setTitle((String) data.get("title"));
                    place.setDescription((String) data.get("description"));

                    Object locationData = data.get("location");
                    if (locationData != null) {
                        ObjectMapper mapper = new ObjectMapper();
                        Map<String, Object> locationMap = (Map<String, Object>) data.get("location");
                        String locationJson = null;
                        try {
                            locationJson = mapper.writeValueAsString(locationMap);
                        } catch (JsonProcessingException e) {
                            e.printStackTrace();
                        }
                        LatLng location = null;
                        try {
                            location = mapper.readValue(locationJson, LatLng.class);
                        } catch (JsonProcessingException e) {
                            e.printStackTrace();
                        }
                        place.setLocation(location);
                    }

                    postList.add(place);
                }
                places.setValue(postList);
            }
        });
        return places;
    }

}