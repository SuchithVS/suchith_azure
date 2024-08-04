package com.suchith.volvo_new;

import android.Manifest;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.io.IOException;
import java.io.OutputStream;

public class Ca_Forklifts extends AppCompatActivity {

    private static final String TAG = "Ca_Forklifts";
    private static final int PERMISSION_REQUEST_CODE = 1;

    private RecyclerView recyclerView;
    private Adapter_forklifts adapter;
    private DatabaseReference databaseReference;
    private FloatingActionButton fabOptions;
    private Handler searchHandler = new Handler();
    private EditText searchEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ca_forklifts);
        Log.d(TAG, "onCreate: Started");

        recyclerView = findViewById(R.id.ca_mm_recycle_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        MaterialButton backButton = findViewById(R.id.back_btn_mm);
        fabOptions = findViewById(R.id.fab_options_mm);
        searchEditText = findViewById(R.id.search_edit_text_mm);

        databaseReference = FirebaseDatabase.getInstance().getReference().child("maintenance_management");
        Log.d(TAG, "onCreate: Database reference initialized");

        setupFirebaseAdapter(databaseReference);

        backButton.setOnClickListener(v -> finish());

        fabOptions.setOnClickListener(v -> showOptionsDialog());

        setupSearch();
    }

    private void setupFirebaseAdapter(Query query) {
        FirebaseRecyclerOptions<Model_Forklifts> options =
                new FirebaseRecyclerOptions.Builder<Model_Forklifts>()
                        .setQuery(query, Model_Forklifts.class)
                        .build();

        adapter = new Adapter_forklifts(options);
        recyclerView.setAdapter(adapter);
        adapter.startListening();
        Log.d(TAG, "setupFirebaseAdapter: New adapter set to RecyclerView");
    }

    private void setupSearch() {
        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                searchHandler.removeCallbacksAndMessages(null);
                searchHandler.postDelayed(() -> performSearch(s.toString()), 300);
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    private void performSearch(String query) {
        Query searchQuery;
        if (query.isEmpty()) {
            searchQuery = databaseReference;
            Log.d(TAG, "performSearch: Empty query, showing all items");
        } else {
            searchQuery = databaseReference.orderByChild("model_id")
                    .startAt(query.toLowerCase())
                    .endAt(query.toLowerCase() + "\uf8ff");
            Log.d(TAG, "performSearch: Searching for query: " + query);
        }

        setupFirebaseAdapter(searchQuery);
    }

    private void showOptionsDialog() {
        try {
            String[] options = {"Add New Item", "Export Data"};
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Choose an option")
                    .setItems(options, (dialog, which) -> {
                        if (which == 0) {
                            try {
                                Intent intent = new Intent(Ca_Forklifts.this, AddForklift.class);
                                startActivity(intent);
                            } catch (Exception e) {
                                Log.e(TAG, "Error starting AddForklift activity", e);
                                Toast.makeText(this, "Error opening Add New Item", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            checkPermissionAndExport();
                        }
                    });
            builder.create().show();
        } catch (Exception e) {
            Log.e(TAG, "Error showing options dialog", e);
            Toast.makeText(this, "Error showing options", Toast.LENGTH_SHORT).show();
        }
    }

    private void checkPermissionAndExport() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            showExportDialog();
        } else {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        PERMISSION_REQUEST_CODE);
            } else {
                showExportDialog();
            }
        }
    }

    private void showExportDialog() {
        String[] options = {"CSV", "PDF"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choose export format")
                .setItems(options, (dialog, which) -> {
                    if (which == 0) {
                        exportToCSV();
                    } else {
                        exportToPDF();
                    }
                });
        builder.create().show();
    }

    private void exportToCSV() {
        databaseReference.get().addOnSuccessListener(dataSnapshot -> {
            StringBuilder data = new StringBuilder();
            data.append("Model Name,Model ID,Plant Location,Start Date,End Date,Duration,Active\n");

            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                Model_Forklifts item = snapshot.getValue(Model_Forklifts.class);
                if (item != null) {
                    data.append(String.format("%s,%s,%s,%s,%s,%s,%s\n",
                            item.getModel_name(),
                            item.getModel_id(),
                            item.getPlant_location(),
                            item.getStart_Date(),
                            item.getEnd_date(),
                            item.getDuration(),
                            item.getActive()));
                }
            }

            String fileName = "forklifts.csv";
            saveFile(fileName, data.toString(), "text/csv");
        }).addOnFailureListener(e -> {
            Toast.makeText(this, "Failed to export data: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        });
    }

    private void exportToPDF() {
        databaseReference.get().addOnSuccessListener(dataSnapshot -> {
            StringBuilder data = new StringBuilder();
            data.append("Forklifts Data\n\n");

            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                Model_Forklifts item = snapshot.getValue(Model_Forklifts.class);
                if (item != null) {
                    data.append(String.format("Model Name: %s\nModel ID: %s\nPlant Location: %s\nStart Date: %s\nEnd Date: %s\nDuration: %s\nActive: %s\n\n",
                            item.getModel_name(),
                            item.getModel_id(),
                            item.getPlant_location(),
                            item.getStart_Date(),
                            item.getEnd_date(),
                            item.getDuration(),
                            item.getActive()));
                }
            }

            String fileName = "forklifts.pdf";
            saveFile(fileName, data.toString(), "application/pdf");
        }).addOnFailureListener(e -> {
            Toast.makeText(this, "Failed to export data: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        });
    }

    private void saveFile(String fileName, String data, String mimeType) {
        try {
            OutputStream outputStream;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                ContentResolver resolver = getContentResolver();
                ContentValues contentValues = new ContentValues();
                contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, fileName);
                contentValues.put(MediaStore.MediaColumns.MIME_TYPE, mimeType);
                contentValues.put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS);
                Uri uri = resolver.insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, contentValues);
                outputStream = resolver.openOutputStream(uri);
            } else {
                // Implement for older Android versions
                return;
            }

            outputStream.write(data.getBytes());
            outputStream.close();
            Toast.makeText(this, fileName + " saved to Downloads folder", Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Error saving file: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.forklifts_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.sort_name_mm) {
            sortData("model_name");
            return true;
        } else if (itemId == R.id.sort_id_mm) {
            sortData("model_id");
            return true;
        } else if (itemId == R.id.filter_options_mm) {
            showFilterDialog();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void sortData(String sortBy) {
        Query query = databaseReference.orderByChild(sortBy);
        setupFirebaseAdapter(query);
    }

    private void showFilterDialog() {
        String[] options = {"All", "Active", "Inactive"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Filter by")
                .setItems(options, (dialog, which) -> {
                    switch (which) {
                        case 0: // All
                            setupFirebaseAdapter(databaseReference);
                            break;
                        case 1: // Active
                            filterData("active", "true");
                            break;
                        case 2: // Inactive
                            filterData("active", "false");
                            break;
                    }
                });
        builder.create().show();
    }

    private void filterData(String field, String value) {
        Query query = databaseReference.orderByChild(field).equalTo(value);
        setupFirebaseAdapter(query);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                showExportDialog();
            } else {
                Toast.makeText(this, "Permission denied. Cannot export file.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (adapter != null) {
            adapter.startListening();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (adapter != null) {
            adapter.stopListening();
        }
    }
}