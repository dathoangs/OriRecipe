package com.example.orirecipe;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentContainerView;
import androidx.fragment.app.FragmentManager;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ImageButton btnHome, btnFavourite, btnAccount, btnAdd;
    private EditText edt_search;
    private FragmentContainerView fCV;
    private FragmentManager fragmentManager = getSupportFragmentManager();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    Context context = this;
    List <FoodData> mFoodList = new ArrayList<>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(FirebaseAuth.getInstance().getCurrentUser() == null){
            Intent myIntent = new Intent(context, SigninActivity.class);
            startActivity(myIntent);
            finish();
        }

        btnHome = (ImageButton) findViewById(R.id.btnHome);
        btnFavourite = (ImageButton) findViewById(R.id.btnFavourite);
        btnAdd  = (ImageButton) findViewById(R.id.btnAdd);
        btnAccount = (ImageButton) findViewById(R.id.btnAccount);
        fCV = (FragmentContainerView) findViewById(R.id.fragment_container_view);

        btnHome.setOnClickListener(btnHomeClick);
        btnFavourite.setOnClickListener(btnfavouriteClick);
        btnAdd.setOnClickListener(btnAddClick);
        btnAccount.setOnClickListener(btnAccountClick);

//        try {
//            PackageInfo info = getPackageManager().getPackageInfo("com.example.orirecipe", PackageManager.GET_SIGNATURES);
//            for (Signature signature : info.signatures){
//                MessageDigest md = MessageDigest.getInstance("SHA");
//                md.update(signature.toByteArray());
//                Log.d("KeyHash", Base64.encodeToString(md.digest(), Base64.DEFAULT));
//            }
//        } catch (PackageManager.NameNotFoundException e) {
//            throw new RuntimeException(e);
//        } catch (NoSuchAlgorithmException e) {
//            throw new RuntimeException(e);
//        } Lấy hashKey

    }

    private View.OnClickListener btnHomeClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            fragmentManager.beginTransaction()
                    .setCustomAnimations(
                            R.anim.slide_in,  // enter
                            R.anim.fade_out,  // exit
                            R.anim.fade_in,   // popEnter
                            R.anim.slide_out  // popExit
                    )
                    .replace(R.id.fragment_container_view, HomeFragment.class, null)
                    .setReorderingAllowed(true)
                    .addToBackStack("HomeFragment")
                    .commit();
        }
    };

    private View.OnClickListener btnfavouriteClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            fragmentManager.beginTransaction()
                    .setCustomAnimations(
                            R.anim.slide_in,  // enter
                            R.anim.fade_out,  // exit
                            R.anim.fade_in,   // popEnter
                            R.anim.slide_out  // popExit
                    )
                    .replace(R.id.fragment_container_view, FavouriteFragment.class, null)
                    .setReorderingAllowed(true)
                    .addToBackStack("FavouriteFragment")
                    .commit();
        }
    };

    private View.OnClickListener btnAddClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
                  fragmentManager.beginTransaction()
                          .setCustomAnimations(
                                  R.anim.slide_in,  // enter
                                  R.anim.fade_out,  // exit
                                  R.anim.fade_in,   // popEnter
                                  R.anim.slide_out  // popExit
                          )
                          .replace(R.id.fragment_container_view, NewRecipeFragment.class, null)
                          .setReorderingAllowed(true)
                          .addToBackStack("NewRecipeFragment")
                          .commit();
        }
    };

    private View.OnClickListener btnAccountClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            fragmentManager.beginTransaction()
                    .setCustomAnimations(
                            R.anim.slide_in,  // enter
                            R.anim.fade_out,  // exit
                            R.anim.fade_in,   // popEnter
                            R.anim.slide_out  // popExit
                    )
                    .replace(R.id.fragment_container_view, AccountFragment.class, null)
                    .setReorderingAllowed(true)
                    .addToBackStack("AccountFragment")
                    .commit();
        }
    };



}