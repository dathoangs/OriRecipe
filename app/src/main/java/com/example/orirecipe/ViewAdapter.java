package com.example.orirecipe;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;


public class ViewAdapter extends RecyclerView.Adapter<FoodViewHolder>{

    private Context mContext;
    private List<FoodData> mFoodList;

    public ViewAdapter(Context mContext, List<FoodData> mFoodList) {
        this.mContext = mContext;
        this.mFoodList = mFoodList;
    }

    @NonNull
    @Override
    public FoodViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View mView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_row_item, parent, false);

        return new FoodViewHolder(mView);
    }

    @Override
    public void onBindViewHolder(@NonNull FoodViewHolder holder, int position) {

        System.out.println(holder.imageView);

        Glide.with(mContext)
                .load(mFoodList.get(position).getItemImage())
                .into(holder.imageView);

        holder.mTitle.setText(mFoodList.get(position).getItemName());
        holder.mDesc.setText(mFoodList.get(position).getItemDesc());

        holder.mCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, com.example.orirecipe.DetailActivity.class);
                intent.putExtra("image", mFoodList.get(holder.getAdapterPosition()).getItemImage());
                intent.putExtra("description", mFoodList.get(holder.getAdapterPosition()).getItemDesc());
                intent.putExtra("id", mFoodList.get(holder.getAdapterPosition()).getItemId());
                mContext.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return mFoodList.size();
    }

    public void filteredList(ArrayList<FoodData> filterList) {
        mFoodList = filterList;
        notifyDataSetChanged();
    }
}

class FoodViewHolder extends RecyclerView.ViewHolder{

    ImageView imageView;
    TextView mTitle, mDesc;
    CardView mCardView;


    public FoodViewHolder(@NonNull View itemView) {
        super(itemView);

        imageView = itemView.findViewById(R.id.ivImage);
        mTitle = itemView.findViewById(R.id.tvTitle);
        mDesc = itemView.findViewById(R.id.tvDesc);

        mCardView = itemView.findViewById(R.id.myCardView);
    }
}
