package com.example.myapplication;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class AlertAdapter extends RecyclerView.Adapter<AlertAdapter.MyViewHolder> {
    Context context;
    ArrayList<ConfirmPost> confirmPostArrayList;


    public AlertAdapter(Context context, ArrayList<ConfirmPost> confirmPostArrayList) {
        this.context = context;
        this.confirmPostArrayList = confirmPostArrayList;
    }

    @NonNull
    @Override
    public AlertAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v=  LayoutInflater.from(parent.getContext()).inflate(R.layout.item_myalert,parent,false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull AlertAdapter.MyViewHolder holder, int position) {
        ConfirmPost confirmPost = confirmPostArrayList.get(position);
        holder.alert.setText(confirmPost.studentConfirm +" đã xác nhận bài viết");
        holder.alert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OtherFragment otherFragment = new OtherFragment();
                Bundle bundle=new Bundle();
                bundle.putString("student_confirm",confirmPost.studentConfirm);
                otherFragment.setArguments(bundle);
                ((HomeActivity)context).getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.frameLayout, otherFragment).addToBackStack(null)
                                .commit();
            }
        });

    }

    @Override
    public int getItemCount() {
        return confirmPostArrayList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView alert;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            alert=itemView.findViewById(R.id.alertForMe);
        }
    }
}
