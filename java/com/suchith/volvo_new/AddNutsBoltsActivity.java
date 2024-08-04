package com.suchith.volvo_new;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class AddNutsBoltsActivity extends AppCompatActivity {

    private TextInputEditText assetNameEditText, assetIdEditText, plantLocationEditText, costEditText, availableUnitsEditText;
    private AutoCompleteTextView rackLocationAutoComplete;
    private DatabaseReference databaseReference;

    private SwitchMaterial statusSwitch;

    private Button btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_nuts_bolts);

        assetNameEditText = findViewById(R.id.asset_name_input);
        assetIdEditText = findViewById(R.id.asset_id_input);
        plantLocationEditText = findViewById(R.id.plant_location_input);
        rackLocationAutoComplete = findViewById(R.id.rack_location_input);
        costEditText = findViewById(R.id.cost_input);
        availableUnitsEditText = findViewById(R.id.available_units_input);

        Button saveButton = findViewById(R.id.save_button);
        btnBack = findViewById(R.id.back_btn_add_new_item);

        statusSwitch = findViewById(R.id.status_switch);

        databaseReference = FirebaseDatabase.getInstance().getReference().child("nuts_bolts");

        saveButton.setOnClickListener(v -> saveNutsBoltsData());
        btnBack.setOnClickListener(v -> finish());

        plantLocationEditText.setText("Bangalore");
        plantLocationEditText.setEnabled(false);

        // Set up rack location dropdown
        ArrayList<String> rackLocations = new ArrayList<>();
        rackLocations.add("Rack #1");
        rackLocations.add("Rack #2");
        rackLocations.add("Rack #3");
        rackLocations.add("Rack #4");
        rackLocations.add("Rack #5");

        ArrayAdapter<String> rackAdapter = new ArrayAdapter<>(this, R.layout.ab_nuts_bolts_drop_down, rackLocations);
        rackLocationAutoComplete.setAdapter(rackAdapter);
        rackLocationAutoComplete.setThreshold(1);
    }

    private void saveNutsBoltsData() {
        String assetName = assetNameEditText.getText().toString().trim();
        String assetId = assetIdEditText.getText().toString().trim();
        String plantLocation = plantLocationEditText.getText().toString().trim();
        String rackLocation = rackLocationAutoComplete.getText().toString().trim();
        String cost = costEditText.getText().toString().trim();
        String availableUnits = availableUnitsEditText.getText().toString().trim();
        boolean isActive = statusSwitch.isChecked();

        Model_aa_nuts_bolts newItem = new Model_aa_nuts_bolts(assetName, assetId, plantLocation, rackLocation, cost, availableUnits, String.valueOf(isActive), "false");


        if (assetName.isEmpty() || assetId.isEmpty()) {
            Toast.makeText(this, "Asset Name and ID are required", Toast.LENGTH_SHORT).show();
            return;
        }


        databaseReference.push().setValue(newItem)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(this, "Data saved successfully", Toast.LENGTH_SHORT).show();
                    finish();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Error saving data", Toast.LENGTH_SHORT).show();
                });
    }
}