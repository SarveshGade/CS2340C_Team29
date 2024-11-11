package com.example.sprintproject.view.location;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sprintproject.R;
import com.example.sprintproject.model.Destination;

import java.util.List;

public class DestinationWrapper extends
        RecyclerView.Adapter<DestinationWrapper.DestinationViewHolder> {
    private final List<Destination> destinations;

    public DestinationWrapper(List<Destination> destinations) {
        this.destinations = destinations;
    }

    @NonNull
    @Override
    public DestinationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.item_location, parent, false);
        return new DestinationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DestinationViewHolder holder, int position) {
        Destination destination = destinations.get(position);
        holder.destinationTitle.setText(destination.getLocation()); // Set destination name
        holder.daysPlanned.setText(destination.getDuration() + " days planned"); // Set duration
    }

    @Override
    public int getItemCount() {
        return destinations.size();
    }

    // ViewHolder class with appropriate visibility and field initialization
    public static class DestinationViewHolder extends RecyclerView.ViewHolder {
        private final TextView destinationTitle;
        private final TextView daysPlanned;

        public DestinationViewHolder(@NonNull View itemView) {
            super(itemView);
            destinationTitle = itemView.findViewById(R.id.destinationTitle);
            daysPlanned = itemView.findViewById(R.id.daysPlanned);
        }
    }
}