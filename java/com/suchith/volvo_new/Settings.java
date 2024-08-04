package com.suchith.volvo_new;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Switch;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.graphics.Insets;
import androidx.core.view.GravityCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Locale;

public class Settings extends AppCompatActivity {
    @SuppressLint("UseSwitchCompatOrMaterialCode")
    private Switch a_Switch;

    private FirebaseAuth mAuth;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);







        Button Button2 = findViewById(R.id.back_btn_Settings);
        Button2.setOnClickListener(v -> {
            Intent intent1 = new Intent(Settings.this, Home_Page.class);
            startActivity(intent1);
        });






        // Logout Button Start
        mAuth = FirebaseAuth.getInstance();
        Button btn_Logout = findViewById(R.id.Log_out_btn);
        btn_Logout.setOnClickListener(v -> {
            // Show dialog box asking user to confirm logout
            new AlertDialog.Builder(Settings.this)
                    .setTitle("Logout")
                    .setMessage("Are you sure you want to logout?")
                    .setPositiveButton(android.R.string.yes, (dialog, which) -> {
                        mAuth.signOut();
                        Intent intent = new Intent(Settings.this, Welcome_screen.class);
                        startActivity(intent);
                        finish();
                        Toast.makeText(Settings.this, "Logout Successful !", Toast.LENGTH_SHORT).show();
                    })
                    .setNegativeButton(android.R.string.no, null)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        });


    }






}