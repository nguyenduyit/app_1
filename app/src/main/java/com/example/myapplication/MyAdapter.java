package com.example.myapplication;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Transaction;
import com.squareup.picasso.Picasso;

import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder>  {


    Context context;
    ArrayList<Post> postArrayList;


    public MyAdapter(Context context, ArrayList<Post> postArrayList) {
        this.context = context;
        this.postArrayList = postArrayList;

    }

    @NonNull
    @Override
    public MyAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.item,parent,false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyAdapter.MyViewHolder holder, int position) {

        Post post=postArrayList.get(position);
        holder.name_owner.setText(post.name_owner);
        holder.title.setText(post.title);
        holder.price.setText(post.price);
        String uri=null;
        holder.category.setText(post.category);
        holder.idPost.setText(post.docId);
        holder.code_owner.setText(post.code_owner);

        Picasso.get().load(post.image).resize(500,600).into(holder.image);



    }

    @Override
    public int getItemCount() {
        return postArrayList.size();
    }

    public void filterList(ArrayList<Post> filteredList) {
        postArrayList = filteredList;
        notifyDataSetChanged();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView name_owner,title,price,category,idPost,code_owner;
        ImageView image;
        Button confirm,commment;
        AlertDialog.Builder builder;
        FirebaseFirestore db=FirebaseFirestore.getInstance();
        SharedPreferences sharedPreferences=itemView.getContext().getSharedPreferences("key",itemView.getContext().MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferences.edit();

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            name_owner= itemView.findViewById(R.id.name_ownerPost);
            title= itemView.findViewById(R.id.titlePost);
            price= itemView.findViewById(R.id.pricePost);
            category=itemView.findViewById(R.id.categoryPost);
            image=itemView.findViewById(R.id.imagePost);
            idPost=itemView.findViewById(R.id.idPost);
            idPost.setVisibility((itemView.INVISIBLE));
            code_owner=itemView.findViewById(R.id.code_ownerPost);
            confirm=itemView.findViewById(R.id.btnConfirm);
            commment=itemView.findViewById(R.id.btnComment);
            sharedPreferences= itemView.getContext().getSharedPreferences("key",itemView.getContext().MODE_PRIVATE);
            editor=sharedPreferences.edit();
            Fragment homeFragment= new HomeFragment();
            String code=sharedPreferences.getString("code",null);
            confirm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                   if(code.equals(code_owner.getText())){
                       builder = new AlertDialog.Builder(itemView.getContext());
                       builder.setMessage("This post is yours")
                               .setTitle("Info")
                               .setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                                   @Override
                                   public void onClick(DialogInterface dialogInterface, int i) {
                                       dialogInterface.cancel();
                                   }
                               });
                       builder.create();
                       builder.show();
                   }
                   else {
                       builder = new AlertDialog.Builder(itemView.getContext());
                       builder.setTitle("Information")
                               .setMessage("Are you sure to confirm this post")
                               .setCancelable(false)
                               .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                   @Override
                                   public void onClick(DialogInterface dialogInterface, int i) {
                                       Map<String,Object> confirmPost = new HashMap<>();
                                       confirmPost.put("ownerPost",code_owner.getText().toString());
                                       confirmPost.put("idPost",idPost.getText().toString());
                                       confirmPost.put("studentConfirm",code.trim());
                                       confirmPost.put("time", new Timestamp(new Date()));
                                       db.collection("confirmPost").add(confirmPost)
                                               .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                                   @Override
                                                   public void onSuccess(DocumentReference documentReference) {
                                                       Map<String,Object> post= new HashMap<>();
                                                       post.put("status","0");
                                                       db.collection("post").document(idPost.getText().toString())
                                                               .update(post)
                                                               .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                   @Override
                                                                   public void onSuccess(Void unused) {
                                                                       Toast.makeText(itemView.getContext(), "Success", Toast.LENGTH_SHORT).show();
                                                                   }
                                                               }).addOnFailureListener(new OnFailureListener() {
                                                                   @Override
                                                                   public void onFailure(@NonNull Exception e) {
                                                                       Toast.makeText(itemView.getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                                                                   }
                                                               });
                                                   }
                                               });
                                   }
                               }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                                   @Override
                                   public void onClick(DialogInterface dialogInterface, int i) {
                                       dialogInterface.cancel();
                                   }
                               });
                       builder.create().show();
                   }

                }
            });

            commment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    editor.putString("idPost",idPost.getText().toString());
                    editor.putString("code",code);

                    HomeActivity activity = (HomeActivity) view.getContext();
                    Fragment commentFragment = new CommentFragment();
                    activity.getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.frameLayout,commentFragment)
                            .addToBackStack(null)
                            .commit();
                    editor.commit();

                }
            });

        }
    }
}
