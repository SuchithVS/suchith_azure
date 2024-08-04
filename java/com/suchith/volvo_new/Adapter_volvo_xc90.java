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

public class Adapter_volvo_xc90 extends FirebaseRecyclerAdapter<Model_volvo_xc90, Adapter_volvo_xc90.SpareViewHolder> {
    private static final String TAG = "Adapter_volvo_xc90";

    public Adapter_volvo_xc90(@NonNull FirebaseRecyclerOptions<Model_volvo_xc90> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull Adapter_volvo_xc90.SpareViewHolder holder, int position, @NonNull Model_volvo_xc90 model) {
        try {
            holder.spareNameText.setText(model.getSpare_name());
            holder.spareIdText.setText(model.getSpare_id());
            holder.plantLocationText.setText(model.getPlant_location());
            holder.vendorInfoText.setText(model.getvendor_information());
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
                Intent intent = new Intent(holder.itemView.getContext(), UpdateVolvoXC90Activity.class);
                intent.putExtra("spare_id", model.getSpare_id());
                holder.itemView.getContext().startActivity(intent);
            });

            holder.deleteButton.setOnClickListener(v -> {
                DatabaseReference itemRef = getRef(position);
                itemRef.removeValue()
                        .addOnSuccessListener(aVoid -> Toast.makeText(holder.itemView.getContext(), "Item deleted successfully", Toast.LENGTH_SHORT).show())
                        .addOnFailureListener(e -> Toast.makeText(holder.itemView.getContext(), "Failed to delete item", Toast.LENGTH_SHORT).show());
            });

            Log.d(TAG, "Item bound: " + model.getSpare_id());
        } catch (Exception e) {
            Log.e(TAG, "Error in onBindViewHolder: " + e.getMessage(), e);
        }
    }

    @NonNull
    @Override
    public Adapter_volvo_xc90.SpareViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_xc90, parent, false);
        return new Adapter_volvo_xc90.SpareViewHolder(view);
    }

    static class SpareViewHolder extends RecyclerView.ViewHolder {
        TextView spareNameText, spareIdText, plantLocationText, vendorInfoText, costText, availUnitsText, statusText;
        Button updateButton, deleteButton;

        public SpareViewHolder(@NonNull View itemView) {
            super(itemView);
            spareNameText = itemView.findViewById(R.id.spare_name_text_aa1);
            spareIdText = itemView.findViewById(R.id.spare_id_text_aa2_spm);
            plantLocationText = itemView.findViewById(R.id.plant_location_text_aa3_spm);
            vendorInfoText = itemView.findViewById(R.id.vendor_information_text_aa4_spm);
            costText = itemView.findViewById(R.id.cost_text_aa5_spm);
            availUnitsText = itemView.findViewById(R.id.avail_units_text_naa6_spm);
            statusText = itemView.findViewById(R.id.status_text_spm);
            updateButton = itemView.findViewById(R.id.btn_update_spm);
            deleteButton = itemView.findViewById(R.id.btn_delete_spm);
        }
    }
}