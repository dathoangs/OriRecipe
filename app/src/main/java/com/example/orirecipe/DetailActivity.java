package com.example.orirecipe;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class DetailActivity extends AppCompatActivity {

    private ImageView ivImage_detail;
    private TextView tvDesc_detail, tvRecipeName;
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();;
    private String itemId;
    private ImageButton btnBack, btnFav;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private String uID = mAuth.getCurrentUser().getUid();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        firebaseAuth = FirebaseAuth.getInstance();

        Bundle bundle = getIntent().getExtras();

        tvRecipeName = (TextView) findViewById(R.id.tvRecipeName);
        tvDesc_detail = (TextView) findViewById(R.id.tvDesc_detail);
        ivImage_detail = (ImageView) findViewById(R.id.ivImage_detail);
        btnBack = (ImageButton) findViewById(R.id.btnBack);
        btnFav = (ImageButton) findViewById(R.id.btnAddFav);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        btnFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (firebaseAuth.getCurrentUser() == null){
                    Toast.makeText(DetailActivity.this, "Bạn chưa đăng nhập", Toast.LENGTH_LONG).show();
                } else {
                    favUpd(itemId);
                }
            }
        });

        if (bundle != null){
            tvRecipeName.setText(bundle.getString("recipename"));
            tvDesc_detail.setText(bundle.getString("description"));
            itemId = bundle.getString("id");
            Glide.with(this)
                    .load(bundle.getString("image"))
                    .into(ivImage_detail);
        }
    }

    void favUpd (String string){

        db.collection("Users").document(uID).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        User user = documentSnapshot.toObject(User.class);
                        if (user.getFavRecipe().contains(string)){
                            user.favRecipe.remove(string);
                        } else {
                            user.favRecipe.add(string);
                        }

                        db.collection("Users").document(uID).set(user)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Toast.makeText(DetailActivity.this,
                                                "Thành công!!!",
                                                Toast.LENGTH_LONG).show();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(DetailActivity.this,
                                                "Có lỗi rồi!",
                                                Toast.LENGTH_LONG).show();
                                    }
                                });
                    }
                });
    }
}