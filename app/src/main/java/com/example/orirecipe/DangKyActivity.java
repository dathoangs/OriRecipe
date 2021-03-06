package com.example.orirecipe;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Layout;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.regex.Pattern;

public class DangKyActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText etName, etEmail, etPassword, etPass_check;
    private Button btnDk;
    private View tvLogin;


    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dang_ky);

        mAuth = FirebaseAuth.getInstance();

        etEmail = (EditText) findViewById(R.id.etEmail);
        etName = (EditText) findViewById(R.id.etuser);
        etPassword = (EditText) findViewById(R.id.etpassword);
        etPass_check = (EditText) findViewById(R.id.etpasswordcheck);

        btnDk = (Button) findViewById(R.id.btnDk);
        btnDk.setOnClickListener(this);

        tvLogin = findViewById(R.id.login);
        tvLogin.setOnClickListener(this);


    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.login:{
                startActivity(new Intent(DangKyActivity.this, com.example.orirecipe.DangNhapActivity.class));
                break;
            }

            case R.id.btnDk:{
                registerUser();
                break;
            }

        }
    }

    private void registerUser() {
        String email = etEmail.getText().toString().trim();
        String name = etName.getText().toString().trim();
        String pass = etPassword.getText().toString().trim();
        String pass_chk = etPass_check.getText().toString().trim();

        if (name.isEmpty()){
            etName.setError("B???n ch??a nh???p T??n!");
            etName.requestFocus();
            return;
        }

        if (email.isEmpty()){
            etEmail.setError("B???n ch??a nh???p Email!");
            etEmail.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            etEmail.setError("Email kh??ng ????ng ?????nh d???ng!");
            etEmail.requestFocus();
            return;
        }

        if (pass.isEmpty()){
            etPassword.setError("B???n ch??a nh???p M???t Kh???u!");
            etPassword.requestFocus();
            return;
        }

        if (pass.length() < 6){
            etPassword.setError("M???t kh???u c???n ??t nh???t 6 k?? t???!");
            etPassword.requestFocus();
            return;
        }

        if (pass_chk.isEmpty()){
            etPass_check.setError("B???n ch??a nh???p l???i m???t kh???u!");
            etPass_check.requestFocus();
            return;
        }

        if (!pass_chk.equals(pass)){
            etPass_check.setError("Kh??ng gi???ng m???t kh???u ban ?????u!");
            etPass_check.requestFocus();
            return;
        }

        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Vui l??ng ?????i...");

        mAuth.createUserWithEmailAndPassword(email, pass)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()){
                            ArrayList <String> tmp = new ArrayList<>();
                            tmp.add("10000000007");
                            String id = FirebaseAuth.getInstance().getUid();
                            User user = new User(id, name, email, tmp);

                            FirebaseDatabase.getInstance().getReference("Users")
                                    .child(id)
                                    .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()){
                                        Toast.makeText(DangKyActivity.this,
                                                "????ng k?? th??nh c??ng",
                                                Toast.LENGTH_LONG).show();
                                        progressDialog.dismiss();
                                        finish();
                                    } else {
                                        Toast.makeText(DangKyActivity.this,
                                                "????ng k?? kh??ng th??nh c??ng!",
                                                Toast.LENGTH_LONG).show();
                                        progressDialog.dismiss();
                                    }

                                }
                            });

                        } else {
                            Toast.makeText(DangKyActivity.this,
                                    "????ng k?? kh??ng th??nh c??ng!",
                                    Toast.LENGTH_LONG).show();
                            progressDialog.dismiss();
                        }

                    }
                });

    }


}