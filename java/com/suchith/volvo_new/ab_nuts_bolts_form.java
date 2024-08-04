package com.suchith.volvo_new;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Calendar;

public class ab_nuts_bolts_form extends AppCompatActivity {

    Button btnBack;
    Button buttonSubmit;
    DatabaseReference databaseReference;

    EditText editTextEmpName, editTextAssetID, editTextQuantities;
    TextInputEditText Date_PickerEditText;
    TextInputLayout til_rack_location;
    AutoCompleteTextView act_rack_location;
    ArrayList<String> arrayList_rack_location;
    ArrayAdapter<String> arrayAdapter_rack_location;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ab_nuts_bolts_form);

        btnBack = findViewById(R.id.back_btn_filed_mapping);
        btnBack.setOnClickListener(view -> onBackPressed());

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }

        // Initialize Firebase database reference
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Nuts_Bolts_Form");

        editTextEmpName = findViewById(R.id.editTextEmpName);
        editTextAssetID = findViewById(R.id.editTextAsset_ID);
        editTextQuantities = findViewById(R.id.editTextQuantity);
        Date_PickerEditText = findViewById(R.id.datePickerEditText);
        Date_PickerEditText.setOnClickListener(v -> showDatePicker());
        buttonSubmit = findViewById(R.id.buttonSubmit);

        // DROP DOWN MENU rack_location SELECTOR START
        act_rack_location = findViewById(R.id.editTextRack);
        arrayList_rack_location = new ArrayList<>();
        arrayList_rack_location.add("Rack #1");
        arrayList_rack_location.add("Rack #2");
        arrayList_rack_location.add("Rack #3");
        arrayList_rack_location.add("Rack #4");
        arrayList_rack_location.add("Rack #5");

        arrayAdapter_rack_location = new ArrayAdapter<>(this, R.layout.ab_nuts_bolts_drop_down, arrayList_rack_location);
        act_rack_location.setAdapter(arrayAdapter_rack_location);
        act_rack_location.setThreshold(1);
        // DROP DOWN MENU rack_location SELECTOR END

        buttonSubmit.setOnClickListener(v -> {
            // Get the data from the input fields
            String Employee_Name = editTextEmpName.getText().toString().trim();
            String Asset_ID = editTextAssetID.getText().toString().trim();
            String Rack_Location = act_rack_location.getText().toString().trim();
            String Quantities = editTextQuantities.getText().toString().trim();
            String farmDate = Date_PickerEditText.getText().toString().trim();

            // Check if any field is empty
            if (Employee_Name.isEmpty() || Asset_ID.isEmpty() || Rack_Location.isEmpty() || Quantities.isEmpty() || farmDate.isEmpty()) {
                Toast.makeText(ab_nuts_bolts_form.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            } else {
                // Create a new Class_ab_nuts_bolts object with the entered data
                Class_ab_nuts_bolts nutsBolts = new Class_ab_nuts_bolts(Employee_Name, Asset_ID, Rack_Location, Quantities, farmDate);

                // Push the nutsBolts object to Firebase database
                databaseReference.push().setValue(nutsBolts);

                // Clear the input fields
                editTextEmpName.setText("");
                editTextAssetID.setText("");
                act_rack_location.setText("");
                editTextQuantities.setText("");
                Date_PickerEditText.setText("");

                Toast.makeText(ab_nuts_bolts_form.this, "Data submitted successfully", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showDatePicker() {
        final Calendar calendar = Calendar.getInstance();
        final int currentYear = calendar.get(Calendar.YEAR);
        final int currentMonth = calendar.get(Calendar.MONTH);
        final int currentDayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                (view, year, month, dayOfMonth) -> {
                    Calendar selectedDate = Calendar.getInstance();
                    selectedDate.set(year, month, dayOfMonth);
                    if (selectedDate.getTimeInMillis() > calendar.getTimeInMillis()) {
                        String date = dayOfMonth + "/" + (month + 1) + "/" + year;
                        Date_PickerEditText.setText(date);
                    } else {
                        Toast.makeText(ab_nuts_bolts_form.this, "Please select a future date", Toast.LENGTH_SHORT).show();
                    }
                }, currentYear, currentMonth, currentDayOfMonth);
        datePickerDialog.getDatePicker().setMinDate(calendar.getTimeInMillis());
        datePickerDialog.show();
    }
}