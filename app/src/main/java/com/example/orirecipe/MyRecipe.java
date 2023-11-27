package com.example.orirecipe;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class MyRecipe extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private List<FoodData> mFoodList;
    private MyAdapter myAdapter;
    private FirebaseFirestore db ;
    private FirebaseAuth auth = FirebaseAuth.getInstance();

    private Context context = this;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_recipe);
        mRecyclerView = (RecyclerView) findViewById(R.id.rclView);
        db = FirebaseFirestore.getInstance();
        readData(new MyCallback() {
            @Override
            public void onCallback(List<FoodData> foodList) {
                myAdapter = new MyAdapter(context, foodList);
                mRecyclerView.setAdapter(myAdapter);

            }
        });
    }

    public interface MyCallback {
        void onCallback(List<FoodData> foodList);
    }

    public void readData(MyCallback myCallback){
        mFoodList = new ArrayList<>();

        db.collection("Recipe").whereEqualTo("userID", auth.getCurrentUser().getUid()).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        for (QueryDocumentSnapshot doc:task.getResult()){
                            mFoodList.add(doc.toObject(FoodData.class));
                            myCallback.onCallback(mFoodList);
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}