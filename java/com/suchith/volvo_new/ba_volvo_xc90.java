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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class ba_volvo_xc90 extends AppCompatActivity {

    private static final String TAG = "ba_volvo_xc90";
    private static final int PERMISSION_REQUEST_CODE = 1;

    private RecyclerView recyclerView;
    private Adapter_volvo_xc90 adapter;
    private DatabaseReference databaseReference;
    private List<Model_volvo_xc90> dataList = new ArrayList<>();
    private FloatingActionButton fabOptions;
    private Handler searchHandler = new Handler();
    private EditText searchEditText;

    private String active, inactive;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ba_volvo_xc90);
        Log.d(TAG, "onCreate: Started");

        recyclerView = findViewById(R.id.ba_spares_recycle_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        MaterialButton backButton = findViewById(R.id.back_btn_spm);
        fabOptions = findViewById(R.id.fab_options_spm);
        searchEditText = findViewById(R.id.search_edit_text_spm);

        databaseReference = FirebaseDatabase.getInstance().getReference().child("spare_parts_management");
        Log.d(TAG, "onCreate: Database reference initialized");

        setupFirebaseAdapter(databaseReference);

        backButton.setOnClickListener(v -> finish());

        fabOptions.setOnClickListener(v -> showOptionsDialog());

        setupSearch();
    }

    private void performSearch(String query) {
        Query searchQuery;
        if (query.isEmpty()) {
            searchQuery = databaseReference;
            Log.d(TAG, "performSearch: Empty query, showing all items");
        } else {
            searchQuery = databaseReference.orderByChild("spare_id")
                    .startAt(query.toLowerCase())
                    .endAt(query.toLowerCase() + "\uf8ff");
            Log.d(TAG, "performSearch: Searching for query: " + query);
        }

        setupFirebaseAdapter(searchQuery);
    }

    private void setupFirebaseAdapter(Query query) {
        FirebaseRecyclerOptions<Model_volvo_xc90> options =
                new FirebaseRecyclerOptions.Builder<Model_volvo_xc90>()
                        .setQuery(query, Model_volvo_xc90.class)
                        .build();

        adapter = new Adapter_volvo_xc90(options);
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


    private void showOptionsDialog() {
        String[] options = {"Add New Item", "Export Data"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choose an option")
                .setItems(options, (dialog, which) -> {
                    if (which == 0) {
                        Intent intent = new Intent(ba_volvo_xc90.this, AddVolvoXC90Activity.class);
                        startActivity(intent);
                    } else {
                        checkPermissionAndExport();
                    }
                });
        builder.create().show();
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



    private void sortData(String sortBy) {
        Query query = databaseReference.orderByChild(sortBy);
        FirebaseRecyclerOptions<Model_volvo_xc90> newOptions =
                new FirebaseRecyclerOptions.Builder<Model_volvo_xc90>()
                        .setQuery(query, Model_volvo_xc90.class)
                        .build();
        adapter.updateOptions(newOptions);
        Log.d(TAG, "sortData: Sorting applied by " + sortBy);
    }

    private void filterData(String field, String value) {
        Query query = databaseReference.orderByChild(field).equalTo(value);
        FirebaseRecyclerOptions<Model_volvo_xc90> newOptions =
                new FirebaseRecyclerOptions.Builder<Model_volvo_xc90>()
                        .setQuery(query, Model_volvo_xc90.class)
                        .build();
        adapter.updateOptions(newOptions);
        Log.d(TAG, "filterData: Filter applied on " + field + " with value " + value);
    }

    private void exportToCSV() {
        databaseReference.get().addOnSuccessListener(dataSnapshot -> {
            StringBuilder data = new StringBuilder();
            data.append("Spare Name,Spare ID,Plant Location,Vendor Information,Cost,Available Units,Active\n");

            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                Model_volvo_xc90 item = snapshot.getValue(Model_volvo_xc90.class);
                if (item != null) {
                    data.append(String.format("%s,%s,%s,%s,%s,%s,%s\n",
                            item.getSpare_name(),
                            item.getSpare_id(),
                            item.getPlant_location(),
                            item.getvendor_information(),
                            item.getCost(),
                            item.getAvailable_units(),
                            item.getActive()));
                }
            }

            String fileName = "spare_parts.csv";
            saveFile(fileName, data.toString(), "text/csv");
        }).addOnFailureListener(e -> {
            Toast.makeText(this, "Failed to export data: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        });
    }

    private void exportToPDF() {
        databaseReference.get().addOnSuccessListener(dataSnapshot -> {
            StringBuilder data = new StringBuilder();
            data.append("Spare Parts Inventory\n\n");

            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                Model_volvo_xc90 item = snapshot.getValue(Model_volvo_xc90.class);
                if (item != null) {
                    data.append(String.format("Spare Name: %s\nSpare ID: %s\nPlant Location: %s\nVendor Information: %s\nCost: %s\nAvailable Units: %s\nActive: %s\n\n",
                            item.getSpare_name(),
                            item.getSpare_id(),
                            item.getPlant_location(),
                            item.getvendor_information(),
                            item.getCost(),
                            item.getAvailable_units(),
                            item.getActive()));
                }
            }

            String fileName = "spares_management.pdf";
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
                File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
                File file = new File(path, fileName);
                outputStream = new FileOutputStream(file);
            }

            outputStream.write(data.getBytes());
            outputStream.close();
            Toast.makeText(this, fileName + " saved to Downloads folder", Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Error saving file: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void showFilterDialog() {
        String[] options = {"All", "Active", "Inactive", "Low Stock"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Filter by")
                .setItems(options, (dialog, which) -> {
                    switch (which) {
                        case 0: // All
                            resetFilter();
                            break;
                        case 1: // Active
                            filterData("active", "true");
                            break;
                        case 2: // Inactive
                            filterData("active", "false");
                            break;
                        case 3: // Low Stock
                            filterLowStock();
                            break;
                    }
                });
        builder.create().show();
    }

    private void resetFilter() {
        FirebaseRecyclerOptions<Model_volvo_xc90> options =
                new FirebaseRecyclerOptions.Builder<Model_volvo_xc90>()
                        .setQuery(databaseReference, Model_volvo_xc90.class)
                        .build();
        adapter.updateOptions(options);
    }

    private void filterLowStock() {
        Query query = databaseReference.orderByChild("available_units").endAt("10");
        FirebaseRecyclerOptions<Model_volvo_xc90> options =
                new FirebaseRecyclerOptions.Builder<Model_volvo_xc90>()
                        .setQuery(query, Model_volvo_xc90.class)
                        .build();
        adapter.updateOptions(options);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.volvo_xc90_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.sort_name_spm) {
            sortData("spare_name");
            return true;
        } else if (itemId == R.id.sort_id_spm) {
            sortData("spare_id");
            return true;
        } else if (itemId == R.id.filter_options_spm) {
            showFilterDialog();
            return true;
        }
        return super.onOptionsItemSelected(item);
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
        Log.d(TAG, "onStart: Called");
        if (adapter != null) {
            adapter.startListening();
        }
    }


}