package com.example.orirecipe;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class UserActivity extends AppCompatActivity {

    private TextView tvLogout;
    private TextView tvmyrecipe;
    private FirebaseAuth mAuth;
    private Context context = this;
    private FirebaseFirestore db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        tvmyrecipe = (TextView) findViewById(R.id.txtMyrecipe);
        db = FirebaseFirestore.getInstance();
        tvmyrecipe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(context, MyRecipeFragment.class);
                startActivity(myIntent);
                finish();
            }
        });
    }
}