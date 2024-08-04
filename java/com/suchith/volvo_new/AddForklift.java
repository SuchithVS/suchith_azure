package com.suchith.volvo_new;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class AddForklift extends AppCompatActivity {

    private static final String TAG = "AddForklift";

    private EditText modelNameEditText, modelIdEditText, plantLocationEditText, startDateEditText, endDateEditText, durationEditText;
    private SwitchMaterial activeSwitch;
    private Button saveButton;
    private DatabaseReference databaseReference;
    private Calendar calendar;
    private SimpleDateFormat dateFormat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_forklift);

        initializeViews();
        setupDatePickers();
        setupListeners();

        databaseReference = FirebaseDatabase.getInstance().getReference().child("maintenance_management");
    }

    private void initializeViews() {
        modelNameEditText = findViewById(R.id.asset_name_input_mm);
        modelIdEditText = findViewById(R.id.asset_id_input_mm);
        plantLocationEditText = findViewById(R.id.plant_location_input_mm);
        startDateEditText = findViewById(R.id.start_date_input);
        endDateEditText = findViewById(R.id.end_date_input);
        durationEditText = findViewById(R.id.duration_input);
        activeSwitch = findViewById(R.id.status_switch_mm);
        saveButton = findViewById(R.id.save_button_mm);

        if (modelNameEditText == null || modelIdEditText == null || plantLocationEditText == null ||
                startDateEditText == null || endDateEditText == null || durationEditText == null ||
                activeSwitch == null || saveButton == null) {
            throw new IllegalStateException("One or more views not found in layout");
        }
    }

    private void setupDatePickers() {
        calendar = Calendar.getInstance();
        dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.US);

        DatePickerDialog.OnDateSetListener startDateListener = (view, year, month, dayOfMonth) -> {
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, month);
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            startDateEditText.setText(dateFormat.format(calendar.getTime()));
        };

        DatePickerDialog.OnDateSetListener endDateListener = (view, year, month, dayOfMonth) -> {
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, month);
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            endDateEditText.setText(dateFormat.format(calendar.getTime()));
        };

        startDateEditText.setOnClickListener(v -> new DatePickerDialog(AddForklift.this, startDateListener,
                calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show());

        endDateEditText.setOnClickListener(v -> new DatePickerDialog(AddForklift.this, endDateListener,
                calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show());
    }

    private void setupListeners() {
        saveButton.setOnClickListener(v -> saveForklift());
    }

    private void saveForklift() {
        String modelName = modelNameEditText.getText().toString().trim();
        String modelId = modelIdEditText.getText().toString().trim();
        String plantLocation = plantLocationEditText.getText().toString().trim();
        String startDate = startDateEditText.getText().toString().trim();
        String endDate = endDateEditText.getText().toString().trim();
        String duration = durationEditText.getText().toString().trim();
        boolean active = activeSwitch.isChecked();

        if (modelName.isEmpty() || modelId.isEmpty() || plantLocation.isEmpty() ||
                startDate.isEmpty() || endDate.isEmpty() || duration.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        String activeString = active ? "true" : "false";
        Model_Forklifts forklift = new Model_Forklifts(modelName, modelId, plantLocation, startDate, endDate, duration, activeString, "false");

        databaseReference.child(modelId).setValue(forklift)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(AddForklift.this, "Forklift added successfully", Toast.LENGTH_SHORT).show();
                    finish();
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Error adding forklift", e);
                    Toast.makeText(AddForklift.this, "Failed to add forklift: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }
}