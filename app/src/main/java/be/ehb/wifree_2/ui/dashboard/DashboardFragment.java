package be.ehb.wifree_2.ui.dashboard;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
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
        binding = FragmentDashboardBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        HomeViewModel viewModel = new ViewModelProvider(getActivity()).get(HomeViewModel.class);
        PlacesAdapter adapter = new PlacesAdapter();
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        binding.rcPlaces.setAdapter(adapter);
        binding.rcPlaces.setLayoutManager(layoutManager);
        viewModel.getAllLocationsDB().observe(getViewLifecycleOwner(), new Observer<List<WifiPlace>>() {
            @Override
            public void onChanged(List<WifiPlace> places) {
                adapter.addItems(places);
            }
        });


        btn_new_marker = root.findViewById(R.id.btn_new_marker);
        btn_new_marker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NewWifiFragment jobFragment = new NewWifiFragment();
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.const_dashboard, jobFragment);

                FloatingActionButton button4 = getView().findViewById(R.id.btn_new_marker);
                button4.setVisibility(View.GONE);

                ft.addToBackStack(null);
                ft.commit();
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
