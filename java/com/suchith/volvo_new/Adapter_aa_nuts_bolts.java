package com.suchith.volvo_new;

import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;

public class Adapter_aa_nuts_bolts extends FirebaseRecyclerAdapter<Model_aa_nuts_bolts, Adapter_aa_nuts_bolts.NutsBoltsViewHolder> {
    private static final String TAG = "Adapter_aa_nuts_bolts";

    public Adapter_aa_nuts_bolts(@NonNull FirebaseRecyclerOptions<Model_aa_nuts_bolts> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull NutsBoltsViewHolder holder, int position, @NonNull Model_aa_nuts_bolts model) {
        try {
            holder.assetNameText.setText(model.getAsset_name());
            holder.assetIdText.setText(model.getAsset_id());
            holder.plantLocationText.setText(model.getPlant_location());
            holder.rackLocationText.setText(model.getRack_location());
            holder.costText.setText(model.getCost());
            holder.availUnitsText.setText(model.getAvailable_units());

            if ("true".equalsIgnoreCase(model.getActive())) {
                holder.statusText.setText("Active");
                holder.statusText.setTextColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.green));
            } else {
                holder.statusText.setText("Inactive");
                holder.statusText.setTextColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.red1));
            }

            holder.updateButton.setOnClickListener(v -> {
                Intent intent = new Intent(holder.itemView.getContext(), UpdateNutsBoltsActivity.class);
                intent.putExtra("asset_id", model.getAsset_id());
                holder.itemView.getContext().startActivity(intent);
            });

            holder.deleteButton.setOnClickListener(v -> {
                DatabaseReference itemRef = getRef(position);
                itemRef.removeValue()
                        .addOnSuccessListener(aVoid -> Toast.makeText(holder.itemView.getContext(), "Item deleted successfully", Toast.LENGTH_SHORT).show())
                        .addOnFailureListener(e -> Toast.makeText(holder.itemView.getContext(), "Failed to delete item", Toast.LENGTH_SHORT).show());
            });

            Log.d(TAG, "Item bound: " + model.getAsset_id());
        } catch (Exception e) {
            Log.e(TAG, "Error in onBindViewHolder: " + e.getMessage(), e);
        }
    }

    @NonNull
    @Override
    public NutsBoltsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_aa_nuts_bolts, parent, false);
        return new NutsBoltsViewHolder(view);
    }

    static class NutsBoltsViewHolder extends RecyclerView.ViewHolder {
        TextView assetNameText, assetIdText, plantLocationText, rackLocationText, costText, availUnitsText, statusText;
        Button updateButton, deleteButton;

        public NutsBoltsViewHolder(@NonNull View itemView) {
            super(itemView);
            assetNameText = itemView.findViewById(R.id.asset_name_text_aa1);
            assetIdText = itemView.findViewById(R.id.asset_id_text_aa2);
            plantLocationText = itemView.findViewById(R.id.plant_location_text_aa3);
            rackLocationText = itemView.findViewById(R.id.rack_location_text_aa4);
            costText = itemView.findViewById(R.id.cost_text_aa5);
            availUnitsText = itemView.findViewById(R.id.avail_units_text_naa6);
            statusText = itemView.findViewById(R.id.status_text);
            updateButton = itemView.findViewById(R.id.btn_update);
            deleteButton = itemView.findViewById(R.id.btn_delete);
        }
    }
}