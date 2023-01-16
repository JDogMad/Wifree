package be.ehb.wifree_2.ui.dashboard;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.gms.maps.model.LatLng;

import java.util.List;

import be.ehb.wifree_2.R;
import be.ehb.wifree_2.WifiPlace;
import be.ehb.wifree_2.data.FirebaseDatabase;
import be.ehb.wifree_2.databinding.FragmentNewWifiBinding;

public class NewWifiFragment extends Fragment implements LocationListener {
    private static final int REQUEST_LOCATION_PERMISSION = 1;
    private FirebaseDatabase db = new FirebaseDatabase();

    FragmentNewWifiBinding binding;

    EditText txt_marker_title, txt_marker_description;
    Button btn_add_marker;

    LocationManager locationManager;
    Location location;
    LatLng latLng;
    private double latitude, longitude;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentNewWifiBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // Show a message before calling the method
        Toast.makeText(getContext(), "Please make sure your location is on.", Toast.LENGTH_SHORT).show();
        getUserLocation();

        // Search fragment
        txt_marker_title = root.findViewById(R.id.txt_newMarker_title);
        txt_marker_description = root.findViewById(R.id.txt_newMarker_description);
        btn_add_marker = root.findViewById(R.id.btn_marker);
        btn_add_marker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(txt_marker_title.getText().toString().isEmpty() || txt_marker_description.getText().toString().isEmpty()){
                    Toast.makeText(getContext(), "Please make sure all fields are filled in.", Toast.LENGTH_LONG).show();
                } else {
                    if(latLng != null){
                        WifiPlace place = new WifiPlace(txt_marker_title.getText().toString(), txt_marker_description.getText().toString(), latLng);
                        db.addMarker(place);

                        FragmentManager fragmentManager = getFragmentManager();
                        if (fragmentManager != null) {
                            fragmentManager.beginTransaction().replace(R.id.const_new_wifi, new DashboardFragment()).commit();
                        }
                    } else {
                        FragmentManager fragmentManager = getFragmentManager();
                        if (fragmentManager != null) {
                            fragmentManager.beginTransaction().replace(R.id.const_new_wifi, new DashboardFragment()).commit();
                        }
                        Toast.makeText(getContext(), "Something went wrong try again later", Toast.LENGTH_LONG).show();
                    }

                }
            }
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void getUserLocation() {
        locationManager = (LocationManager) requireActivity().getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        String bestProvider = locationManager.getBestProvider(criteria, true);

        // A listener for any updates of location
        LocationListener locationListener = new LocationListener() {
            public void onLocationChanged(Location location) {
                // Is changed when there is a new provider
                latitude = location.getLatitude();
                longitude = location.getLongitude();
                latLng = new LatLng(latitude, longitude);
            }

            public void onStatusChanged(String provider, int status, Bundle extras) {}

            public void onProviderEnabled(String provider) {}

            public void onProviderDisabled(String provider) {}
        };

        if (ActivityCompat.checkSelfPermission(requireActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_LOCATION_PERMISSION);
            return;
        }

        List<String> providers = locationManager.getProviders(true);
        if (!providers.isEmpty()) {
            String provider = providers.get(0);
            locationManager.requestLocationUpdates(provider, 0, 0, locationListener);
        }

        if (location != null) {
            latitude = location.getLatitude();
            longitude = location.getLongitude();
            latLng = new LatLng(latitude, longitude);
            System.out.println("Test de locatie: " + latLng);
        }
    }


    @Override
    public void onLocationChanged(Location location) {
        // Update the latitude and longitude when the location changes
        latitude = location.getLatitude();
        longitude = location.getLongitude();
        latLng = new LatLng(latitude, longitude);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}
