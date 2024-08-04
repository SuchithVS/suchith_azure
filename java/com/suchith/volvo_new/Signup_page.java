package com.suchith.volvo_new;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Signup_page extends AppCompatActivity {

    TextView txtSignIn;
    EditText edtFullName, edtEmail, edtMobile, edtPassword, edtConfirmPassword;
    ProgressBar progressBar;
    Button btnSignUp;
    String txtFullName, txtEmail, txtMobile, txtPassword, txtConfirmPassword;
    final String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    final String passwordPattern = ("^" +
            "(?=.*[@#$%^&+=])" +     // at least 1 special character
            "(?=\\S+$)" +            // no white spaces
            ".{4,}" +                // at least 4 characters
            "$");

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup_page);




        TextView privacyPolicyButton = findViewById(R.id.Terms_and_conditions_text);
        privacyPolicyButton.setOnClickListener(v -> {
            String url = "https://www.freeprivacypolicy.com/live/3582ff6e-0cd8-416b-bd5d-87f43a53aff8";
            // TERMS AND CONDITION

            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(intent);
        });




        //Initializing Variables
        txtSignIn = findViewById(R.id.txtSignIn);
        edtFullName = findViewById(R.id.edtSignUpFullName);
        edtEmail = findViewById(R.id.edtSignUpEmail);
        edtMobile = findViewById(R.id.edtSignUpMobile);
        edtPassword = findViewById(R.id.edtSignUpPassword);
        edtConfirmPassword = findViewById(R.id.edtSignUpConfirmPassword);
        progressBar = findViewById(R.id.signInProgressBar);
        btnSignUp = findViewById(R.id.sup_btn_Signup);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        //Initialize Firebase Fire store
        db = FirebaseFirestore.getInstance();

        txtSignIn.setOnClickListener(view -> {
            Intent intent = new Intent(Signup_page.this, Login_page.class);
            startActivity(intent);
            finish();
        });

        btnSignUp.setOnClickListener(view -> {
                    //converting edit text data to string Start
                    txtFullName = edtFullName.getText().toString();
                    txtEmail = edtEmail.getText().toString().trim();
                    txtMobile = edtMobile.getText().toString().trim();
                    txtPassword = edtPassword.getText().toString().trim();
                    txtConfirmPassword = edtConfirmPassword.getText().toString().trim();
                    //converting edit text data to string End


                    //Validations Start
                    if (!TextUtils.isEmpty(txtFullName)) {
                        if (!TextUtils.isEmpty(txtEmail)) {
                            if (txtEmail.matches(emailPattern)) {
                                if (!TextUtils.isEmpty(txtMobile)) {
                                    if (txtMobile.length() == 10) {
                                        if (!TextUtils.isEmpty(txtPassword)) {
                                            if (txtPassword.matches(passwordPattern)) {
                                                if (!TextUtils.isEmpty(txtConfirmPassword)) {
                                                    if (txtConfirmPassword.equals(txtPassword)) {
                                                        SignUpUser();
                                                        // Sign up Method
                                                    } else {
                                                        edtConfirmPassword.setError("Confirm Password and Password should be same.");
                                                    }
                                                } else {
                                                    edtConfirmPassword.setError("Confirm Password Field can't be empty");
                                                }
                                            } else {
                                                edtPassword.setError("Enter a valid Password with Special Character & one Upper Case");
                                            }
                                        } else {
                                            edtPassword.setError("Password Field can't be empty");
                                        }
                                    } else {
                                        edtMobile.setError("Enter a valid Mobile");
                                    }
                                } else {
                                    edtMobile.setError("Mobile Number Field can't be empty");
                                }
                            } else {
                                edtEmail.setError("Enter a valid Email Address");
                            }
                        } else {
                            edtEmail.setError("Email Field can't be empty");
                        }
                    } else {
                        edtFullName.setError("Full Name Field can't be empty");
                    }

                }

        );
        //Validations End
    }

    private void SignUpUser() {
        progressBar.setVisibility(View.VISIBLE);
        btnSignUp.setVisibility(View.INVISIBLE);


        mAuth.createUserWithEmailAndPassword(txtEmail, txtPassword).addOnSuccessListener(authResult -> {
            //new
            Map<String, Object> user = new HashMap<>();
            user.put("FullName", txtFullName);
            user.put("Email", txtEmail);
            user.put("MobileNumber", txtMobile);
            user.put("Password", txtPassword);
            db.collection("users")
                    .add(user)
                    .addOnSuccessListener(documentReference -> {
                        Toast.makeText(Signup_page.this, "Account Created Successfully", Toast.LENGTH_SHORT).show();
//                        Toasty.success(d_Signup_Page.this, "Verified", Toast.LENGTH_SHORT).show();
                        Transfer_Data_to_OTP();
                    })
                    .addOnFailureListener(e ->  Toast.makeText(Signup_page.this, "Error - " + e.getMessage(), Toast.LENGTH_LONG).show());

        }).addOnFailureListener(e -> {
            Toast.makeText(Signup_page.this, "Error", Toast.LENGTH_SHORT).show();
            progressBar.setVisibility(View.INVISIBLE);
            btnSignUp.setVisibility(View.VISIBLE);
        });

    }

    private void Transfer_Data_to_OTP() {
        Intent intent = new Intent(Signup_page.this, Home_Page.class);
//        Intent intent = new Intent(d_Signup_Page.this, e_OTP_send.class);
//        intent.putExtra("Number_trf", txtMobile);
//        intent.putExtra("FullName_trf", txtFullName); OTP
        startActivity(intent);
        finish();
    }

}