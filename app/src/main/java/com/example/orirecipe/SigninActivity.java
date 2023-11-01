package com.example.orirecipe;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SigninActivity extends AppCompatActivity {

    private EditText etEmail, etPass;
    private Button btnDangnhap;
    private TextView tvSignUp;
    private FirebaseAuth firebaseAuth;
    Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);

        tvSignUp = (TextView) findViewById(R.id.tvSignUp);
        tvSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(context, LoginActivity.class);
                startActivity(myIntent);
                finish();
            }
        });

        firebaseAuth = FirebaseAuth.getInstance();
        etEmail = (EditText) findViewById(R.id.edtEmail);
        etPass = (EditText) findViewById(R.id.edtPass);

        btnDangnhap = (Button) findViewById(R.id.btnDangnhap);
        btnDangnhap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userLogin();
            }
        });

    }

    private void userLogin(){
        String email = etEmail.getText().toString().trim(),
                pass = etPass.getText().toString().trim();

        if (email.isEmpty()){
            etEmail.setError("Chưa nhập Email!");
            etEmail.requestFocus();
            return;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            etEmail.setError("Email không đúng định dạng!");
            etEmail.requestFocus();
            return;
        }

        if (pass.isEmpty() || pass.length() < 6){
            etPass.setError("Điền lại mật khẩu!");
            etPass.requestFocus();
            return;
        }

        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Vui lòng đợi...");

        firebaseAuth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    //Chuyển sang activity User Profile
                    //Temporary code for testing
                    Toast.makeText(context, "Đăng nhập thành công.", Toast.LENGTH_LONG).show();
                    Intent myIntent = new Intent(context, MainActivity.class);
                    startActivity(myIntent);
                    finish();
                } else {
                    Toast.makeText(context, "Đăng nhập không thành công.\nVui lòng thử lại.", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}