package com.example.myapplication;

import android.app.Dialog;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.MyViewHolder> {
    Context context;
    ArrayList<Comment> commentArrayList;
    Dialog dialog;
    EditText content_update;

    public CommentAdapter(Context context, ArrayList<Comment> commentArrayList) {
        this.context = context;
        this.commentArrayList = commentArrayList;
    }

    @NonNull
    @Override
    public CommentAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(context).inflate(R.layout.item_comment,parent,false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentAdapter.MyViewHolder holder, int position) {
        Comment comment=commentArrayList.get(position);
        holder.ownerComment.setText(comment.owner_comment);
        holder.contentComment.setText(comment.content);
        SharedPreferences sharedPreferences= holder.sharedPreferences;
        SharedPreferences.Editor editor= holder.editor;
        Button updateComment=holder.updateComment.findViewById(R.id.btnUpdate_Comment);
        if(sharedPreferences.getString("code",null).equals(comment.owner_comment)){
            updateComment.setVisibility(View.VISIBLE);
        }
        else {
            updateComment.setVisibility(View.INVISIBLE);
        }


        updateComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DialogUpdate(comment.getOwner_comment(),holder.contentComment.getText().toString());


            }
        });



    }

    @Override
    public int getItemCount() {
        return commentArrayList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView ownerComment,contentComment;
        Button updateComment;
        SharedPreferences sharedPreferences=itemView.getContext().getSharedPreferences("key",itemView.getContext().MODE_PRIVATE);
        SharedPreferences.Editor editor= sharedPreferences.edit();

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            ownerComment=itemView.findViewById(R.id.owner_comment);
            contentComment=itemView.findViewById(R.id.content_comment);
            updateComment=itemView.findViewById(R.id.btnUpdate_Comment);

            ownerComment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(sharedPreferences.getString("code",null).compareTo(ownerComment.getText().toString())==0){
                        editor.putString("code",ownerComment.getText().toString());
                        HomeActivity activity= (HomeActivity) itemView.getContext();
                        Fragment profileFragment = new ProfileFragment();
                        activity.getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.frameLayout,profileFragment)
                                .addToBackStack(null)
                                .commit();
                        editor.commit();
                        editor.apply();


                    }else {
                        editor.putString("code",ownerComment.getText().toString());
                        HomeActivity activity= (HomeActivity) itemView.getContext();
                        Fragment otherFragment = new OtherFragment();
                        activity.getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.frameLayout,otherFragment)
                                .addToBackStack(null)
                                .commit();
                        editor.commit();
                        editor.apply();

                    }

                }
            });


        }
    }
    public void DialogUpdate(String owner,String text){
        dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_update_comment);
        content_update=dialog.findViewById(R.id.content_update);
        content_update.setText(text);
        FirebaseFirestore db=FirebaseFirestore.getInstance();
        dialog.show();
        Button cancel_update=dialog.findViewById(R.id.btnCancel_Update);
        cancel_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        Button update_comment=dialog.findViewById(R.id.btnUpdate);
        update_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                db.collection("comment").whereEqualTo("owner_comment",owner)
                        .whereEqualTo("content",text).get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if(task.isSuccessful()){
                                    for(QueryDocumentSnapshot doc:task.getResult()){
                                        String id=doc.getId();
                                        Map<String,Object> comment= new HashMap<>();
                                        comment.put("content",content_update.getText().toString().trim());
                                        db.collection("comment").document(id)
                                                .update(comment)
                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void unused) {
                                                        Toast.makeText(context, "Success", Toast.LENGTH_SHORT).show();
                                                        dialog.dismiss();


                                                    }
                                                }).addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                    }
                                }
                            }
                        });

            }
        });


    }
}
