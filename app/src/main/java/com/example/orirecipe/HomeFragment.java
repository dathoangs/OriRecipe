package com.example.orirecipe;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Context;
import android.text.Editable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;
import android.text.TextWatcher;
import android.widget.EditText;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private RecyclerView mRecyclerView;
    private List<FoodData> mFoodList;
    private MyAdapter myAdapter;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private  EditText etSearchText;


    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ConstraintLayout cl = (ConstraintLayout) inflater.inflate(R.layout.fragment_home, container, false);

        mRecyclerView = (RecyclerView) cl.findViewById(R.id.recyclerView);
        etSearchText = (EditText) cl.findViewById(R.id.edt_Search);
        etSearchText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }
            @Override
            public void afterTextChanged(Editable editable) {
                filter(editable.toString());
            } //Sau khi text đã được thay đổi thì gọi tới hàm filter
        });

        readData(new MyCallback() {
            @Override
            public void onCallback(List<FoodData> foodList) {
                mFoodList = foodList;
                myAdapter = new MyAdapter(getActivity(), foodList);
                mRecyclerView.setAdapter(myAdapter);
            }
        });
        return cl;
    }


    public void filter(String string){
      ArrayList<FoodData> filterList = new ArrayList<>(); //Mảng chứa dữ liệu sau filter

       for (FoodData item: mFoodList) {
           if (item.getItemName().toLowerCase().contains(string.toString())) {
               filterList.add(item);
           }
       } //For qua tất cả công thức ban đầu
        // Công thức nào có tên gồm text mình gõ vào thì thêm vào filterList

        myAdapter.filteredList(filterList);
       //Gọi hàm đánh động tới Adapter để nó upd lại giao diện theo list công thức đã filter
    } //filter for search text

    public interface MyCallback {
        void onCallback(List<FoodData> foodList);
    }

    public void readData(MyCallback myCallback){
        mFoodList = new ArrayList<>();

        db.collection("Recipe").get()
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
                        Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_SHORT).show();
                    }
                });
    }


}