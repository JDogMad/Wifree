package be.ehb.wifree_2.ui.dashboard;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import be.ehb.wifree_2.R;
import be.ehb.wifree_2.WifiPlace;

public class PlacesAdapter  extends RecyclerView.Adapter<PlacesAdapter.ViewHolder>{
    private ArrayList<WifiPlace> items; // arraylist of wifiplaces

    public PlacesAdapter(){
        items = new ArrayList<>(); // new arraylist
    }

    public void addItems(List<WifiPlace> items){
        this.items = new ArrayList<>(items); // adds items
        notifyDataSetChanged(); // notify when data has changed
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txt_title_place, txt_description_place;

        public ViewHolder(View itemView) {
            super(itemView);

            // Search for the title and description
            txt_title_place = itemView.findViewById(R.id.marker_title);
            txt_description_place = itemView.findViewById(R.id.marker_description);
        }
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // creates new view -> inflates the adapter
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_places, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // returns the name and description of the place
        WifiPlace currentItem = items.get(position);
        holder.txt_title_place.setText(currentItem.getTitle());
        holder.txt_description_place.setText(currentItem.getDescription());
    }

    @Override
    public int getItemCount() {
        return items.size(); // returns the number of items in the arraylist
    }
}
