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
            etName.setError("Bạn chưa nhập Tên!");
            etName.requestFocus();
            return;
        }

        if (email.isEmpty()){
            etEmail.setError("Bạn chưa nhập Email!");
            etEmail.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            etEmail.setError("Email không đúng định dạng!");
            etEmail.requestFocus();
            return;
        }

        if (pass.isEmpty()){
            etPassword.setError("Bạn chưa nhập Mật Khẩu!");
            etPassword.requestFocus();
            return;
        }

        if (pass.length() < 6){
            etPassword.setError("Mật khẩu cần ít nhất 6 kí tự!");
            etPassword.requestFocus();
            return;
        }

        if (pass_chk.isEmpty()){
            etPass_check.setError("Bạn chưa nhập lại mật khẩu!");
            etPass_check.requestFocus();
            return;
        }

        if (!pass_chk.equals(pass)){
            etPass_check.setError("Không giống mật khẩu ban đầu!");
            etPass_check.requestFocus();
            return;
        }

        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Vui lòng đợi...");

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
                                                "Đăng ký thành công",
                                                Toast.LENGTH_LONG).show();
                                        progressDialog.dismiss();
                                        finish();
                                    } else {
                                        Toast.makeText(DangKyActivity.this,
                                                "Đăng ký không thành công!",
                                                Toast.LENGTH_LONG).show();
                                        progressDialog.dismiss();
                                    }

                                }
                            });

                        } else {
                            Toast.makeText(DangKyActivity.this,
                                    "Đăng ký không thành công!",
                                    Toast.LENGTH_LONG).show();
                            progressDialog.dismiss();
                        }

                    }
                });

    }


}