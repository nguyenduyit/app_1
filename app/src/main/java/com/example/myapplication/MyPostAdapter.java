package com.example.myapplication;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.ContentInfo;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MyPostAdapter extends RecyclerView.Adapter<MyPostAdapter.MyViewHolder> {

    Context context;
    ArrayList<Post> postArrayList;

    public MyPostAdapter(@NonNull Context context, ArrayList<Post> postArrayList) {
        this.context = context;
        this.postArrayList = postArrayList;
    }

    @NonNull
    @Override
    public MyPostAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.item,parent,false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyPostAdapter.MyViewHolder holder, int position) {
        Post post=postArrayList.get(position);
        String code;
        holder.name_owner.setText(post.name_owner);
        holder.title.setText(post.title);
        holder.price.setText(post.price);
        String uri=null;
        holder.category.setText(post.category);
        FirebaseFirestore db=FirebaseFirestore.getInstance();
        db.collection("confirmPost")
                .whereEqualTo("idPost",post.docId)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                       for(DocumentSnapshot documentSnapshot: queryDocumentSnapshots){
                           holder.idPost.setText("Người xác nhận:"+documentSnapshot.getString("studentConfirm"));
                           holder.idPost.setOnClickListener(new View.OnClickListener() {
                               @Override
                               public void onClick(View view) {
                                   OtherFragment otherFragment = new OtherFragment();
                                   Bundle bundle=new Bundle();
                                   bundle.putString("student_confirm",documentSnapshot.getString("studentConfirm"));
                                   otherFragment.setArguments(bundle);
                                   ((HomeActivity)context).getSupportFragmentManager()
                                           .beginTransaction()
                                           .replace(R.id.frameLayout, otherFragment).addToBackStack(null)
                                           .commit();
                               }
                           });
                       }
                    }
                });

        holder.code_owner.setText(post.code_owner);

        Picasso.get().load(post.image).resize(500,500).into(holder.imageView);





    }

    @Override
    public int getItemCount() {
        return postArrayList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView name_owner,price,category,title,idPost,code_owner;
        ImageView imageView;
        Button confirm,comment;
        SharedPreferences sharedPreferences=itemView.getContext().getSharedPreferences("key",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        FirebaseFirestore db=FirebaseFirestore.getInstance();
        String code=sharedPreferences.getString("code",null);
        AlertDialog.Builder builder;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            name_owner=itemView.findViewById(R.id.name_ownerPost);
            price=itemView.findViewById(R.id.pricePost);
            title=itemView.findViewById(R.id.titlePost);
            category=itemView.findViewById(R.id.categoryPost);
            imageView=itemView.findViewById(R.id.imagePost);
            idPost=itemView.findViewById(R.id.idPost);
//            idPost.setVisibility((itemView.INVISIBLE));
            code_owner=itemView.findViewById(R.id.code_ownerPost);
            confirm=itemView.findViewById(R.id.btnConfirm);
            comment=itemView.findViewById(R.id.btnComment);

            confirm.setVisibility(itemView.INVISIBLE);
            comment.setVisibility(itemView.INVISIBLE);



        }



    }

}
