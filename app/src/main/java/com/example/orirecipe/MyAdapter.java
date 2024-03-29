package com.example.orirecipe;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.util.ArrayList;
import java.util.List;


public class MyAdapter extends RecyclerView.Adapter<FoodViewHolder>{

    private Context mContext;
    private List<FoodData> mFoodList;

    public MyAdapter(Context mContext, List<FoodData> mFoodList) {
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
        Glide.with(mContext)
                .load(mFoodList.get(position).getItemImage())
                .fitCenter()
                .into(holder.imageView);

        holder.mTitle.setText(mFoodList.get(position).getItemName());
        holder.mDesc.setText(mFoodList.get(position).getItemDesc());
        holder.authorName.setText(mFoodList.get(position).getUserName());

        holder.mCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, com.example.orirecipe.DetailActivity.class);
                intent.putExtra("image", mFoodList.get(holder.getAdapterPosition()).getItemImage());
                intent.putExtra("description", mFoodList.get(holder.getAdapterPosition()).getItemDesc());
                intent.putExtra("id", mFoodList.get(holder.getAdapterPosition()).getItemId());
                intent.putExtra("recipename", mFoodList.get(holder.getAdapterPosition()).getItemName());
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
    TextView mTitle, mDesc, authorName;
    CardView mCardView;


    public FoodViewHolder(@NonNull View itemView) {
        super(itemView);

        imageView = itemView.findViewById(R.id.ivImage);
        mTitle = itemView.findViewById(R.id.tvTitle);
        mDesc = itemView.findViewById(R.id.tvDesc);
        authorName = itemView.findViewById(R.id.authorName);

        mCardView = itemView.findViewById(R.id.myCardView);
    }
}
