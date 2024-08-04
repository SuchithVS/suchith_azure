package com.suchith.volvo_new;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class UpdateforkliftActivity extends AppCompatActivity {

    private static final String TAG = "UpdateforkliftActivity";
    private SwitchMaterial activeSwitch;
    private EditText modelNameEditText, plantLocationEditText, startDateEditText, endDateEditText, durationEditText;
    private DatabaseReference databaseReference;
    private String modelId;
    private Calendar calendar;
    private SimpleDateFormat dateFormat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.updateforklift);

        try {
            initializeViews();
            getModelIdFromIntent();
            initializeDatabaseReference();
            setupUpdateButton();
            setupDatePickers();
            fetchForkliftData();
        } catch (Exception e) {
            Log.e(TAG, "Error in onCreate: " + e.getMessage(), e);
            Toast.makeText(this, "Error initializing activity: " + e.getMessage(), Toast.LENGTH_LONG).show();
            finish();
        }
    }

    private void initializeViews() {
        modelNameEditText = findViewById(R.id.asset_name_input_mm);
        plantLocationEditText = findViewById(R.id.plant_location_input_mm);
        startDateEditText = findViewById(R.id.rack_location_input_mm);
        endDateEditText = findViewById(R.id.cost_input_mm);
        durationEditText = findViewById(R.id.available_units_input_mm);
        activeSwitch = findViewById(R.id.status_switch_mm);

        if (modelNameEditText == null || plantLocationEditText == null || startDateEditText == null ||
                endDateEditText == null || durationEditText == null || activeSwitch == null) {
            throw new IllegalStateException("One or more views not found in layout");
        }
    }

    private void getModelIdFromIntent() {
        modelId = getIntent().getStringExtra("model_id");
        if (modelId == null || modelId.isEmpty()) {
            throw new IllegalArgumentException("Invalid model ID");
        }
        Log.d(TAG, "Model ID: " + modelId);
    }

    private void initializeDatabaseReference() {
        databaseReference = FirebaseDatabase.getInstance().getReference().child("maintenance_management");
        if (databaseReference == null) {
            throw new IllegalStateException("Failed to initialize database reference");
        }
    }

    private void setupUpdateButton() {
        Button updateButton = findViewById(R.id.update_button_mm);
        if (updateButton == null) {
            throw new IllegalStateException("Update button not found in layout");
        }
        updateButton.setOnClickListener(v -> updateForklift());
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

        startDateEditText.setOnClickListener(v -> new DatePickerDialog(UpdateforkliftActivity.this, startDateListener,
                calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show());

        endDateEditText.setOnClickListener(v -> new DatePickerDialog(UpdateforkliftActivity.this, endDateListener,
                calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show());
    }

    private void fetchForkliftData() {
        Log.d(TAG, "Fetching data for model ID: " + modelId);
        databaseReference.orderByChild("model_id").equalTo(modelId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Model_Forklifts forklift = snapshot.getValue(Model_Forklifts.class);
                        if (forklift != null) {
                            populateViews(forklift);
                        } else {
                            Log.e(TAG, "Forklift data is null");
                            showToast("Error: Forklift data is null");
                        }
                    }
                } else {
                    Log.e(TAG, "Forklift not found for model ID: " + modelId);
                    showToast("Forklift not found");
                    finish();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e(TAG, "Database error: " + databaseError.getMessage());
                showToast("Failed to fetch forklift data: " + databaseError.getMessage());
                finish();
            }
        });
    }

    private void populateViews(Model_Forklifts forklift) {
        modelNameEditText.setText(forklift.getModel_name());
        plantLocationEditText.setText(forklift.getPlant_location());
        startDateEditText.setText(forklift.getStart_Date());
        endDateEditText.setText(forklift.getEnd_date());
        durationEditText.setText(forklift.getDuration());
        activeSwitch.setChecked(Boolean.parseBoolean(forklift.getActive()));
        Log.d(TAG, "Views populated with forklift data");
    }

    private void updateForklift() {
        Log.d(TAG, "Attempting to update forklift");
        String modelName = modelNameEditText.getText().toString().trim();
        String plantLocation = plantLocationEditText.getText().toString().trim();
        String startDate = startDateEditText.getText().toString().trim();
        String endDate = endDateEditText.getText().toString().trim();
        String duration = durationEditText.getText().toString().trim();
        boolean active = activeSwitch.isChecked();

        if (modelName.isEmpty() || plantLocation.isEmpty() || startDate.isEmpty() || endDate.isEmpty() || duration.isEmpty()) {
            showToast("Please fill in all fields");
            return;
        }

        String activeString = active ? "true" : "false";
        Model_Forklifts updatedForklift = new Model_Forklifts(modelName, modelId, plantLocation, startDate, endDate, duration, activeString, "false");

        databaseReference.child(modelId).setValue(updatedForklift)
                .addOnSuccessListener(aVoid -> {
                    Log.d(TAG, "Forklift updated successfully");
                    showToast("Forklift updated successfully");
                    finish();
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Failed to update forklift: " + e.getMessage());
                    showToast("Failed to update forklift: " + e.getMessage());
                });
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}