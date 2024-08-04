package com.suchith.volvo_new;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;

public class B_Spares_Managament extends AppCompatActivity {

    private CardView volvo_Xc90, volvo_Xc60, volvo_Xc40, volvo_S90;
    Button btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.b_spares_managament);
        // Add your Asset Management functionality here

        volvo_Xc90 = findViewById(R.id.sm_card1);
        volvo_Xc60 = findViewById(R.id.sm_card2);
        volvo_Xc40 = findViewById(R.id.sm_card3);
        volvo_S90 = findViewById(R.id.sm_card4);

        // Set click listeners
        volvo_Xc90.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(B_Spares_Managament.this, ba_volvo_xc90.class));
            }
        });

        volvo_Xc60.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(B_Spares_Managament.this, ba_volvo_xc90.class));
            }
        });

        volvo_Xc40.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(B_Spares_Managament.this, ba_volvo_xc90.class));
            }
        });

        volvo_S90.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(B_Spares_Managament.this, ba_volvo_xc90.class));
            }
        });

        btnBack = findViewById(R.id.back_btn_nearest_workers);
        btnBack.setOnClickListener(view -> onBackPressed());

    }


}