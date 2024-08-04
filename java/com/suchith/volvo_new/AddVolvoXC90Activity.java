package com.suchith.volvo_new;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Arrays;

public class AddVolvoXC90Activity extends AppCompatActivity {

    private TextInputEditText spareNameEditText, spareIdEditText, plantLocationEditText, costEditText, availableUnitsEditText;
    private AutoCompleteTextView vendorInfoAutoComplete;
    private DatabaseReference databaseReference;
    private SwitchMaterial statusSwitch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_volvo_xc90);

        spareNameEditText = findViewById(R.id.asset_name_input_spm);
        spareIdEditText = findViewById(R.id.asset_id_input_spm);
        plantLocationEditText = findViewById(R.id.plant_location_input_spm);
        vendorInfoAutoComplete = findViewById(R.id.rack_location_input_spm);
        costEditText = findViewById(R.id.cost_input_spm);
        availableUnitsEditText = findViewById(R.id.available_units_input_spm);
        statusSwitch = findViewById(R.id.status_switch_spm);

        Button saveButton = findViewById(R.id.save_button_spm);
        Button backButton = findViewById(R.id.back_btn_add_new_item_spm);

        databaseReference = FirebaseDatabase.getInstance().getReference().child("spare_parts_management");

        saveButton.setOnClickListener(v -> saveVolvoXC90Data());
        backButton.setOnClickListener(v -> finish());

        plantLocationEditText.setText("Bangalore");
        plantLocationEditText.setEnabled(false);

        // Set up vendor information dropdown
        ArrayList<String> vendorList = new ArrayList<>(Arrays.asList("Vendor 1", "Vendor 2", "Vendor 3", "Vendor 4", "Vendor 5"));
        ArrayAdapter<String> vendorAdapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, vendorList);
        vendorInfoAutoComplete.setAdapter(vendorAdapter);
    }

    private void saveVolvoXC90Data() {
        String spareName = spareNameEditText.getText().toString().trim();
        String spareId = spareIdEditText.getText().toString().trim();
        String plantLocation = plantLocationEditText.getText().toString().trim();
        String vendorInfo = vendorInfoAutoComplete.getText().toString().trim();
        String cost = costEditText.getText().toString().trim();
        String availableUnits = availableUnitsEditText.getText().toString().trim();
        boolean isActive = statusSwitch.isChecked();

        Model_volvo_xc90 newItem = new Model_volvo_xc90(spareName, spareId, plantLocation, vendorInfo, cost, availableUnits, String.valueOf(isActive), "false");

        if (spareName.isEmpty() || spareId.isEmpty()) {
            Toast.makeText(this, "Spare Name and ID are required", Toast.LENGTH_SHORT).show();
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