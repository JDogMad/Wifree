package be.ehb.wifree_2.ui.dashboard;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import be.ehb.wifree_2.R;
import be.ehb.wifree_2.WifiPlace;
import be.ehb.wifree_2.databinding.FragmentDashboardBinding;
import be.ehb.wifree_2.ui.home.HomeViewModel;

public class DashboardFragment extends Fragment {
    FragmentDashboardBinding binding;
    FloatingActionButton btn_new_marker;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        //binding with Dashboard layout
        binding = FragmentDashboardBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // Search for the button and sets a onclick listener
        btn_new_marker = root.findViewById(R.id.btn_new_marker);
        btn_new_marker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NewWifiFragment newWifiFragment = new NewWifiFragment();  // new Fragment
                FragmentManager fragmentManager = getChildFragmentManager();  // getting the fragmentManager of the activity
                fragmentManager.popBackStack(); // removing the fragment from backstack, user can't go back

                // Search for the action button
                FloatingActionButton addbutton = getView().findViewById(R.id.btn_new_marker);
                addbutton.setVisibility(View.GONE); // visibilty is set to gone -> is not shown in the new fragment

                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction(); // start transaction
                fragmentTransaction.replace(R.id.const_dashboard, newWifiFragment); // transaction action to change the layout
                fragmentTransaction.addToBackStack(null); // the new fragment to backstack -> user can navigate back
                fragmentTransaction.commit(); // the new fragment is shown
            }
        });

        HomeViewModel viewModel = new ViewModelProvider(getActivity()).get(HomeViewModel.class); // new viewmodel
        PlacesAdapter adapter = new PlacesAdapter(); // new adapter
        // new layoutmanager that will display the list vertically
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        binding.rcPlaces.setAdapter(adapter); // sets adapter to rcPlaces (my recyclerview)
        binding.rcPlaces.setLayoutManager(layoutManager); // sets layoutmanager to rcPlaces (my recyclerview)
        // the viewmodel uses the method getAllLoactionsDB to return a list of places
        viewModel.getAllLocationsDB().observe(getViewLifecycleOwner(), new Observer<List<WifiPlace>>() {
            @Override
            public void onChanged(List<WifiPlace> places) {
                adapter.addItems(places); // the places are added to the adapter
            }
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
