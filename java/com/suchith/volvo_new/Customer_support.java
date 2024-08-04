package com.suchith.volvo_new;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Customer_support extends AppCompatActivity {
    EditText edtFeedback;
    Button btnSubmit;
    Button btnBack;
    Button btnWhatsApp;

    private FirebaseFirestore db;

    @SuppressLint("CutPasteId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.customer_support);

        edtFeedback = findViewById(R.id.edtFeedback);
        btnSubmit = findViewById(R.id.btn_submit_feedback_Page);
        btnBack = findViewById(R.id.back_btn_feedback);
        btnWhatsApp = findViewById(R.id.btn_whatsapp_feedback);

        btnBack.setOnClickListener(view -> onBackPressed());

        // Initialize Firebase Firestore
        db = FirebaseFirestore.getInstance();

        btnSubmit.setOnClickListener(view -> {
            // Convert EditText data to string
            String txtFeedback = edtFeedback.getText().toString();

            // Check if feedback field is empty
            if (TextUtils.isEmpty(txtFeedback)) {
                edtFeedback.setError("Please enter your feedback");
                return;
            }

            // Send feedback to Firestore
            Map<String, Object> feed = new HashMap<>();
            feed.put("Feedback", txtFeedback);

            db.collection("feedback")
                    .add(feed)
                    .addOnSuccessListener(documentReference -> {
                        Toast.makeText(Customer_support.this, "Feedback Submitted Successfully!", Toast.LENGTH_SHORT).show();
                        sendEmail(txtFeedback);
                    })
                    .addOnFailureListener(e -> Toast.makeText(Customer_support.this, "Error - " + e.getMessage(), Toast.LENGTH_SHORT).show());
        });

        btnWhatsApp.setOnClickListener(view -> {
            String txtFeedback = edtFeedback.getText().toString();

            // Check if feedback field is empty
            if (TextUtils.isEmpty(txtFeedback)) {
                edtFeedback.setError("Please enter your feedback");
                return;
            }

            sendWhatsApp(txtFeedback);
        });
    }

    private void sendEmail(String feedback) {
        Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                "mailto", "suchithvs2001@gmail.com", null));
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Customer Support Feedback");
        emailIntent.putExtra(Intent.EXTRA_TEXT, feedback);

        try {
            startActivity(Intent.createChooser(emailIntent, "Send email..."));
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(Customer_support.this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
        }
    }

    private void sendWhatsApp(String feedback) {
        Intent sendIntent = new Intent(Intent.ACTION_VIEW);
        String url = "https://api.whatsapp.com/send?phone=" + "+918660591153" + "&text=" + Uri.encode(feedback);
        sendIntent.setData(Uri.parse(url));

        try {
            startActivity(sendIntent);
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(Customer_support.this, "WhatsApp is not installed.", Toast.LENGTH_SHORT).show();
        }
    }
}