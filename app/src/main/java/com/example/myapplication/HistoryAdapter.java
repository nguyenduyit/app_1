package com.example.myapplication;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.MyViewHolder> {
    Context context;
    ArrayList<ConfirmPost> confirmPostArrayList;

    public HistoryAdapter(Context context, ArrayList<ConfirmPost> confirmPostArrayList) {
        this.context = context;
        this.confirmPostArrayList = confirmPostArrayList;
    }

    @NonNull
    @Override
    public HistoryAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_myalert,parent,false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryAdapter.MyViewHolder holder, int position) {
        ConfirmPost confirmPost=confirmPostArrayList.get(position);
        holder.textView.setText("Bạn đã xác nhận bài viết của "+ confirmPost.ownerPost);
        holder.textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment postFragment = new PostFragment();
                Bundle bundle = new Bundle();
                bundle.putString("idPost",confirmPost.idPost);
                postFragment.setArguments(bundle);
                ((HomeActivity)context).getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.frameLayout,postFragment)
                        .commit();
            }
        });

    }

    @Override
    public int getItemCount() {
        return confirmPostArrayList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView textView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            textView=itemView.findViewById(R.id.alertForMe);
        }


    }
}
