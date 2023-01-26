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
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.maps.model.Dash;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

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
        //binding with NewWifi layout
        binding = FragmentNewWifiBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // Show a message to the user to make sure their location is on
        Toast.makeText(getContext(), "Please make sure your location is on.", Toast.LENGTH_SHORT).show();
        getUserLocation(); // method that gets the location

        // Search for the title and description
        txt_marker_title = root.findViewById(R.id.txt_newMarker_title);
        txt_marker_description = root.findViewById(R.id.txt_newMarker_description);

        // Search for the button and sets a onclick listener
        btn_add_marker = root.findViewById(R.id.btn_marker);
        btn_add_marker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Checks if the text-fields are left empty -> shows a toast with error message
                if(txt_marker_title.getText().toString().isEmpty() || txt_marker_description.getText().toString().isEmpty()){
                    Toast.makeText(getContext(), "Please make sure all fields are filled in.", Toast.LENGTH_LONG).show();
                } else { // the text-fields are not empty
                    if(latLng != null){ // Checks if the latlng (location) is null -> false: makes the place + add to db
                        // new place created with 3 attributes
                        WifiPlace place = new WifiPlace(txt_marker_title.getText().toString(), txt_marker_description.getText().toString(), latLng);
                        db.addMarker(place); // adds the new place to the database

                        DashboardFragment dashboardFragment = new DashboardFragment();  // new Fragment
                        FragmentManager fragmentManager = getChildFragmentManager();  // getting the fragmentManager of the activity
                        fragmentManager.popBackStack(); // removing the fragment from backstack, user can't go back

                        // Search for the action button
                        Button save = getView().findViewById(R.id.btn_marker);
                        save.setVisibility(View.GONE); // visibilty is set to gone -> is not shown in the new fragment

                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction(); // start transaction
                        fragmentTransaction.replace(R.id.const_new_wifi, dashboardFragment); // transaction action to change the layout
                        fragmentTransaction.addToBackStack(null); // the new fragment to backstack -> user can navigate back
                        fragmentTransaction.commit(); // the new fragment is shown

                    } else { // if latlng is null -> returns to dashboard with error message
                        DashboardFragment dashboardFragment = new DashboardFragment();  // new Fragment
                        FragmentManager fragmentManager = getChildFragmentManager();  // getting the fragmentManager of the activity
                        fragmentManager.popBackStack(); // removing the fragment from backstack, user can't go back

                        // Search for the action button
                        Button save = getView().findViewById(R.id.btn_marker);
                        save.setVisibility(View.GONE); // visibilty is set to gone -> is not shown in the new fragment

                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction(); // start transaction
                        fragmentTransaction.replace(R.id.const_new_wifi, dashboardFragment); // transaction action to change the layout
                        fragmentTransaction.addToBackStack(null); // the new fragment to backstack -> user can navigate back
                        fragmentTransaction.commit(); // the new fragment is shown
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

    // method that get's the location of the user
    private void getUserLocation() {
        // gets location manager
        locationManager = (LocationManager) requireActivity().getSystemService(Context.LOCATION_SERVICE); // access to GPS device info

        // adding listener for any updates of location
        LocationListener locationListener = new LocationListener() {
            public void onLocationChanged(Location location) {
                // Update the latitude and longitude when the location changes
                latitude = location.getLatitude();
                longitude = location.getLongitude();
                latLng = new LatLng(latitude, longitude);
            }

            public void onStatusChanged(String provider, int status, Bundle extras) {}

            public void onProviderEnabled(String provider) {}

            public void onProviderDisabled(String provider) {}
        };

        // checks if the app has permission to access the device's fine location and coarse location -> not the permissions ? ask
        if (ActivityCompat.checkSelfPermission(requireActivity(), Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // requests permission from the user via the requestPermissions method.
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_LOCATION_PERMISSION);
            return;
        }

        // gets list of all location providers that are currently enabled on the device
        List<String> providers = locationManager.getProviders(true);
        if (!providers.isEmpty()) { // checks if list is not empty
            String provider = providers.get(0); // gets first provider
            // requests updates
            // (provider = first provider in the list, min time between location updates, min distance between location updates, and the locationlistner from above)
            locationManager.requestLocationUpdates(provider, 0, 0, locationListener);
        }

        if (location != null) { // checks if location is null -> false: proceed
            latitude = location.getLatitude(); // gets the user's latitude
            longitude = location.getLongitude(); // get(s the user's longitude
            latLng = new LatLng(latitude, longitude); // We make a LatLng object with the latitude and longitude
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
        // called when the status of the provider changes
    }

    @Override
    public void onProviderEnabled(String provider) {
        // called when the provider is enabled
    }

    @Override
    public void onProviderDisabled(String provider) {
        // called when the provider is disabled
    }
}
