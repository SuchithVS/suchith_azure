package com.suchith.volvo_new;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

public class Forgot_password extends AppCompatActivity {
    private EditText TxtEmail;
    private ProgressBar progressBar;

    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forgot_password);



        auth = FirebaseAuth.getInstance();

        TxtEmail = findViewById(R.id.forget_Email);
        Button reset_Btn = findViewById(R.id.fgt_btn_Reset_Password);
        progressBar= findViewById(R.id.forgot_Pass_ProgressBar);

        auth = FirebaseAuth.getInstance();

        reset_Btn.setOnClickListener(view -> validateData());


    }
    private void validateData() {
        String email = TxtEmail.getText().toString().trim();
        if (email.isEmpty()) {
            TxtEmail.setError("Email is Required !");
            TxtEmail.requestFocus();
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            TxtEmail.setError("Please Provide valid Email");
            TxtEmail.requestFocus();
            return;
        }
        progressBar.setVisibility(View.VISIBLE);
        auth.sendPasswordResetEmail(email).addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                //  Toast.makeText(g_Forgot_password.this, "Check Your Email", Toast.LENGTH_SHORT).show();
                Toast.makeText(Forgot_password.this, "Check Your Email", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(Forgot_password.this , Welcome_screen.class));
                finish();
            }else {
                //Toast.makeText(g_Forgot_password.this, "Error: "+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                Toast.makeText(Forgot_password.this, "Error: "+ Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_LONG).show();
            }
        });

    }


}