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

public class C_Maintnance extends AppCompatActivity {

    private CardView maintenance;
    Button btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.c_maintnance);
        // Add your Asset Management functionality here

        maintenance = findViewById(R.id.pm_card1);

        // Set click listeners
        maintenance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(C_Maintnance.this, Ca_Forklifts.class));
            }
        });

        btnBack = findViewById(R.id.back_btn_nearest_workers);
        btnBack.setOnClickListener(view -> onBackPressed());

    }


}