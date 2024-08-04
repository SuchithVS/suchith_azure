package com.suchith.volvo_new;

import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.database.DataSnapshot;

import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class UpdateNutsBoltsActivity extends AppCompatActivity {

    private TextInputEditText assetNameInput, plantLocationInput, costInput, availableUnitsInput;
    private AutoCompleteTextView rackLocationInput;
    private SwitchMaterial statusSwitch;
    private DatabaseReference databaseReference;
    private String assetId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.update_nuts_bolts);

        assetNameInput = findViewById(R.id.asset_name_input);
        plantLocationInput = findViewById(R.id.plant_location_input);
        rackLocationInput = findViewById(R.id.rack_location_input);
        costInput = findViewById(R.id.cost_input);
        availableUnitsInput = findViewById(R.id.available_units_input);
        statusSwitch = findViewById(R.id.status_switch);
        MaterialButton updateButton = findViewById(R.id.update_button);
        MaterialButton backButton = findViewById(R.id.back_btn_add_new_item);

        setupRackLocationDropdown();

        assetId = getIntent().getStringExtra("asset_id");
        if (assetId == null || assetId.isEmpty()) {
            Toast.makeText(this, "Error: No asset ID provided", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        databaseReference = FirebaseDatabase.getInstance().getReference().child("nuts_bolts");

        fetchAndPopulateData();

        updateButton.setOnClickListener(v -> updateData());
        backButton.setOnClickListener(v -> finish());
    }

    private void fetchAndPopulateData() {
        Log.d("UpdateNutsBolts", "Fetching data for asset ID: " + assetId);
        databaseReference.orderByChild("asset_id").equalTo(assetId).get().addOnSuccessListener(dataSnapshot -> {
            if (!dataSnapshot.exists()) {
                Log.e("UpdateNutsBolts", "Asset not found");
                Toast.makeText(this, "Error: Asset not found", Toast.LENGTH_LONG).show();
                finish();
                return;
            }

            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                Model_aa_nuts_bolts model = snapshot.getValue(Model_aa_nuts_bolts.class);
                if (model != null) {
                    Log.d("UpdateNutsBolts", "Fetched data: " + model.toString());
                    assetNameInput.setText(model.getAsset_name());
                    plantLocationInput.setText(model.getPlant_location());
                    rackLocationInput.setText(model.getRack_location());
                    costInput.setText(model.getCost());
                    availableUnitsInput.setText(model.getAvailable_units());
                    statusSwitch.setChecked(Boolean.parseBoolean(model.getActive()));
                    return;  // Exit after finding the first match
                }
            }
            Log.e("UpdateNutsBolts", "Fetched data is null");
        }).addOnFailureListener(e -> {
            Log.e("UpdateNutsBolts", "Error fetching data", e);
            Toast.makeText(this, "Error fetching data: " + e.getMessage(), Toast.LENGTH_LONG).show();
            finish();
        });
    }



    private void updateData() {
        String assetName = assetNameInput.getText().toString().trim();
        String plantLocation = plantLocationInput.getText().toString().trim();
        String rackLocation = rackLocationInput.getText().toString().trim();
        String cost = costInput.getText().toString().trim();
        String availableUnits = availableUnitsInput.getText().toString().trim();
        String active = String.valueOf(statusSwitch.isChecked());

        if (assetName.isEmpty() || plantLocation.isEmpty() || rackLocation.isEmpty() || cost.isEmpty() || availableUnits.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        Map<String, Object> updates = new HashMap<>();
        updates.put("asset_name", assetName);
        updates.put("plant_location", plantLocation);
        updates.put("rack_location", rackLocation);
        updates.put("cost", cost);
        updates.put("available_units", availableUnits);
        updates.put("active", active);

        Log.d("UpdateNutsBolts", "Updating asset with ID: " + assetId);
        Log.d("UpdateNutsBolts", "Update data: " + updates.toString());

        databaseReference.orderByChild("asset_id").equalTo(assetId).get().addOnSuccessListener(dataSnapshot -> {
            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                snapshot.getRef().updateChildren(updates)
                        .addOnSuccessListener(aVoid -> {
                            Log.d("UpdateNutsBolts", "Update successful");
                            Toast.makeText(UpdateNutsBoltsActivity.this, "Data updated successfully", Toast.LENGTH_SHORT).show();
                            finish();
                        })
                        .addOnFailureListener(e -> {
                            Log.e("UpdateNutsBolts", "Update failed", e);
                            Toast.makeText(UpdateNutsBoltsActivity.this, "Failed to update data: " + e.getMessage(), Toast.LENGTH_LONG).show();
                        });
                return;  // Exit after updating the first match
            }
        }).addOnFailureListener(e -> {
            Log.e("UpdateNutsBolts", "Error finding asset to update", e);
            Toast.makeText(this, "Error finding asset to update: " + e.getMessage(), Toast.LENGTH_LONG).show();
        });
    }

    private void setupRackLocationDropdown() {
        ArrayList<String> rackLocations = new ArrayList<>(Arrays.asList("Rack #1", "Rack #2", "Rack #3", "Rack #4", "Rack #5"));
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, rackLocations);
        rackLocationInput.setAdapter(adapter);
    }
}