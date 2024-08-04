package com.suchith.volvo_new;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;

public class UpdateVolvoXC90Activity extends AppCompatActivity {

    private TextInputEditText spareNameEditText, plantLocationEditText, costEditText, availableUnitsEditText;
    private AutoCompleteTextView vendorInfoAutoComplete;
    private SwitchMaterial statusSwitch;
    private DatabaseReference databaseReference;
    private String spareId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.update_volvo_xc90);

        spareNameEditText = findViewById(R.id.asset_name_input_spm);
        plantLocationEditText = findViewById(R.id.plant_location_input_spm);
        vendorInfoAutoComplete = findViewById(R.id.rack_location_input_spm);
        costEditText = findViewById(R.id.cost_input_spm);
        availableUnitsEditText = findViewById(R.id.available_units_input_spm);
        statusSwitch = findViewById(R.id.status_switch_spm);

        spareId = getIntent().getStringExtra("spare_id");
        if (spareId == null || spareId.isEmpty()) {
            Toast.makeText(this, "Error: No spare ID provided", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        databaseReference = FirebaseDatabase.getInstance().getReference().child("spare_parts_management");

        fetchAndPopulateData();

        // Set up vendor information dropdown
        ArrayList<String> vendorList = new ArrayList<>(Arrays.asList("Vendor 1", "Vendor 2", "Vendor 3", "Vendor 4", "Vendor 5"));
        ArrayAdapter<String> vendorAdapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, vendorList);
        vendorInfoAutoComplete.setAdapter(vendorAdapter);

        // Set up update button click listener
        Button updateButton = findViewById(R.id.update_button_spm);
        updateButton.setOnClickListener(v -> updateData());
    }

    private void fetchAndPopulateData() {
        databaseReference.orderByChild("spare_id").equalTo(spareId).limitToFirst(1)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                Model_volvo_xc90 item = snapshot.getValue(Model_volvo_xc90.class);
                                if (item != null) {
                                    spareNameEditText.setText(item.getSpare_name());
                                    plantLocationEditText.setText(item.getPlant_location());
                                    vendorInfoAutoComplete.setText(item.getvendor_information());
                                    costEditText.setText(item.getCost());
                                    availableUnitsEditText.setText(item.getAvailable_units());
                                    statusSwitch.setChecked(Boolean.parseBoolean(item.getActive()));
                                }
                            }
                        } else {
                            Toast.makeText(UpdateVolvoXC90Activity.this, "Item not found", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(UpdateVolvoXC90Activity.this, "Error fetching data", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });
    }

    private void updateData() {
        String spareName = spareNameEditText.getText().toString().trim();
        String plantLocation = plantLocationEditText.getText().toString().trim();
        String vendorInfo = vendorInfoAutoComplete.getText().toString().trim();
        String cost = costEditText.getText().toString().trim();
        String availableUnits = availableUnitsEditText.getText().toString().trim();
        boolean isActive = statusSwitch.isChecked();

        if (spareName.isEmpty()) {
            Toast.makeText(this, "Spare Name is required", Toast.LENGTH_SHORT).show();
            return;
        }

        Model_volvo_xc90 updatedItem = new Model_volvo_xc90(spareName, spareId, plantLocation, vendorInfo, cost, availableUnits, String.valueOf(isActive), "false");

        databaseReference.orderByChild("spare_id").equalTo(spareId).limitToFirst(1)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                snapshot.getRef().setValue(updatedItem)
                                        .addOnSuccessListener(aVoid -> {
                                            Toast.makeText(UpdateVolvoXC90Activity.this, "Data updated successfully", Toast.LENGTH_SHORT).show();
                                            finish();
                                        })
                                        .addOnFailureListener(e -> {
                                            Toast.makeText(UpdateVolvoXC90Activity.this, "Error updating data", Toast.LENGTH_SHORT).show();
                                        });
                            }
                        } else {
                            Toast.makeText(UpdateVolvoXC90Activity.this, "Item not found", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(UpdateVolvoXC90Activity.this, "Error updating data", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}