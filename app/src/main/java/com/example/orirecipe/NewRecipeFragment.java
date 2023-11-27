package com.example.orirecipe;

import static android.app.Activity.RESULT_OK;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NewRecipeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NewRecipeFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public NewRecipeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment NewRecipeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static NewRecipeFragment newInstance(String param1, String param2) {
        NewRecipeFragment fragment = new NewRecipeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    private Uri uri;
    private ImageView recipeImage;
    private EditText txtName, txtDesc;
    private String imageUrl, key;
    private Button btnSave, btnDel;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private User user = new User();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        db.collection("Users").whereEqualTo("id", mAuth.getCurrentUser().getUid()).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        for (QueryDocumentSnapshot doc:task.getResult()){
                            user = doc.toObject(User.class);
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ConstraintLayout cl = (ConstraintLayout) inflater.inflate(R.layout.fragment_new_recipe, container, false);

        recipeImage = (ImageView) cl.findViewById(R.id.ivUploadImage);
        txtName = (EditText) cl.findViewById(R.id.etUploadName);
        txtDesc = (EditText) cl.findViewById(R.id.etUploadDesc);
        btnDel = (Button) cl.findViewById(R.id.btnDel);
        btnSave = (Button) cl.findViewById(R.id.btnSave);

        recipeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imagePicker.launch("image/*");
            }
        });

        btnDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                txtName.setText("");
                txtDesc.setText("");
                recipeImage.setImageURI(null);
            }
        });

        btnSave.setOnClickListener(uploadImage);

        return cl;
    }

    ActivityResultLauncher imagePicker = registerForActivityResult(new ActivityResultContracts.GetContent(),
        new ActivityResultCallback<Uri>() {
            @Override
            public void onActivityResult(Uri result) {
                if (result != null){
                    recipeImage.setImageURI(result);
                    uri = result;
                } else {
                    Toast.makeText(getActivity(), "Bạn chưa chọn ảnh đúng không?", Toast.LENGTH_LONG).show();
                }
            }
        }
    );

    View.OnClickListener uploadImage = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            StorageReference storageReference = FirebaseStorage.getInstance()
                    .getReference().child("recipeImage").child(UUID.randomUUID().toString());

            ProgressDialog progressDialog = new ProgressDialog(getActivity());
            progressDialog.setMessage("Đang tải lên công thức...");
            progressDialog.show();

            storageReference.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                    while (!uriTask.isComplete());
                    Uri urlImage = uriTask.getResult();
                    imageUrl = urlImage.toString();
                    uploadRecipe();
                    progressDialog.dismiss();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    progressDialog.dismiss();
                }
            });
        }
    };

    public void uploadRecipe(){
        final String ID = db.collection("Recipe").document().getId();

        com.example.orirecipe.FoodData foodData = new com.example.orirecipe.FoodData(
                ID,
                txtName.getText().toString(),
                txtDesc.getText().toString(),
                imageUrl,
                mAuth.getCurrentUser().getUid(),
                user.getName(),
                0
        );

        db.collection("Recipe").document(ID)
                        .set(foodData)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(getActivity(), "Tải lên thành công", Toast.LENGTH_LONG).show();
                                recipeImage.setImageURI(null);
                                txtDesc.setText("");
                                txtName.setText("");
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getActivity(), e.getMessage().toString(), Toast.LENGTH_LONG).show();
                    }
                });
    }





}