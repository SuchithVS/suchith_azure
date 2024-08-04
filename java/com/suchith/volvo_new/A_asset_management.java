package com.suchith.volvo_new;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class A_asset_management extends AppCompatActivity {

    private CardView nuts_bolts;
    Button btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a_asset_management);
        // Add your Asset Management functionality here

        nuts_bolts = findViewById(R.id.am_card1);

        // Set click listeners
        nuts_bolts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(A_asset_management.this, Aa_nuts_bolts.class));
            }
        });

        btnBack = findViewById(R.id.back_btn_nearest_workers);
        btnBack.setOnClickListener(view -> onBackPressed());

    }


}