package com.suchith.volvo_new;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;

public class Home_Page extends AppCompatActivity {

    private CardView assetManagementCard, spareManagementCard, maintenanceCard,
            analyticsCard, customerSupportCard, settingsCard;
    private FirebaseAuth mAuth;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_page);



        // Initialize CardViews
        assetManagementCard = findViewById(R.id.hp_card1);
        spareManagementCard = findViewById(R.id.hp_card2);
        maintenanceCard = findViewById(R.id.hp_card3);
        analyticsCard = findViewById(R.id.hp_card4);
        settingsCard = findViewById(R.id.hp_card5);
        customerSupportCard = findViewById(R.id.hp_card6);






        // Set click listeners
        assetManagementCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Home_Page.this, Aa_nuts_bolts.class));
            }
        });

        spareManagementCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Home_Page.this, ba_volvo_xc90.class));
            }
        });

        maintenanceCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Home_Page.this, Ca_Forklifts.class));
            }
        });

        analyticsCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Home_Page.this, Analytics.class));
            }
        });

        customerSupportCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Home_Page.this, Customer_support.class));
            }
        });

        settingsCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Home_Page.this, Settings.class));
            }
        });


    }
}


