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

public class Adapter_forklifts extends FirebaseRecyclerAdapter<Model_Forklifts, Adapter_forklifts.SpareViewHolder> {
    private static final String TAG = "Adapter_forklifts";

    public Adapter_forklifts(@NonNull FirebaseRecyclerOptions<Model_Forklifts> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull Adapter_forklifts.SpareViewHolder holder, int position, @NonNull Model_Forklifts model) {
        try {
            holder.spareNameText.setText(model.getModel_name());
            holder.spareIdText.setText(model.getModel_id());
            holder.plantLocationText.setText(model.getPlant_location());
            holder.vendorInfoText.setText(model.getStart_Date());
            holder.costText.setText(model.getEnd_date());
            holder.availUnitsText.setText(model.getDuration());

            if ("true".equalsIgnoreCase(model.getActive())) {
                holder.statusText.setText("Active");
                holder.statusText.setTextColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.green));
            } else {
                holder.statusText.setText("Inactive");
                holder.statusText.setTextColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.red1));
            }

            holder.updateButton.setOnClickListener(v -> {
                Intent intent = new Intent(holder.itemView.getContext(), UpdateforkliftActivity.class);
                intent.putExtra("model_id", model.getModel_id());
                holder.itemView.getContext().startActivity(intent);
            });

            holder.deleteButton.setOnClickListener(v -> {
                DatabaseReference itemRef = getRef(position);
                itemRef.removeValue()
                        .addOnSuccessListener(aVoid -> Toast.makeText(holder.itemView.getContext(), "Item deleted successfully", Toast.LENGTH_SHORT).show())
                        .addOnFailureListener(e -> Toast.makeText(holder.itemView.getContext(), "Failed to delete item", Toast.LENGTH_SHORT).show());
            });

            Log.d(TAG, "Item bound: " + model.getModel_id());
        } catch (Exception e) {
            Log.e(TAG, "Error in onBindViewHolder: " + e.getMessage(), e);
        }
    }

    @NonNull
    @Override
    public Adapter_forklifts.SpareViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_forklifts, parent, false);
        return new Adapter_forklifts.SpareViewHolder(view);
    }

    static class SpareViewHolder extends RecyclerView.ViewHolder {
        TextView spareNameText, spareIdText, plantLocationText, vendorInfoText, costText, availUnitsText, statusText;
        Button updateButton, deleteButton;

        public SpareViewHolder(@NonNull View itemView) {
            super(itemView);
            spareNameText = itemView.findViewById(R.id.model_name_text_aa1_mm);
            spareIdText = itemView.findViewById(R.id.model_id_text_aa2_mm);
            plantLocationText = itemView.findViewById(R.id.plant_location_text_aa3_mm);
            vendorInfoText = itemView.findViewById(R.id.start_Date_text_aa4_mm);
            costText = itemView.findViewById(R.id.end_date_aa5_mm);
            availUnitsText = itemView.findViewById(R.id.duration_text_naa6_mm);
            statusText = itemView.findViewById(R.id.status_text_mm);
            updateButton = itemView.findViewById(R.id.btn_update_mm);
            deleteButton = itemView.findViewById(R.id.btn_delete_mm);
        }
    }
}