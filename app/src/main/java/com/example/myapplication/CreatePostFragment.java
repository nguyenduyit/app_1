package com.example.myapplication;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CreatePostFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CreatePostFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    ImageView imageView_post;
    EditText title,price;
    Button uploadPost,cancelPost;
    Spinner category;

    ArrayList<String> arrayList_category;
    ArrayAdapter<String> arrayAdapter;
    ArrayList<DocumentReference> postArrayList;
    MyAdapter myAdapter;
    View view;
    SharedPreferences sharedPreferences;
    FirebaseStorage store;
    FirebaseFirestore db;

    String result;
    Uri imageUrl;
    AlertDialog.Builder builder;


    public CreatePostFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CreatePostFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CreatePostFragment newInstance(String param1, String param2) {
        CreatePostFragment fragment = new CreatePostFragment();
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
        view= inflater.inflate(R.layout.fragment_create_post, container, false);
        sharedPreferences=getActivity().getSharedPreferences("key", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferences.edit();

        String id=sharedPreferences.getString("id",null);
        String name=sharedPreferences.getString("name",null);
        String code=sharedPreferences.getString("code",null);
        imageView_post=view.findViewById(R.id.upload_image_post);
        title=view.findViewById(R.id.Title);
        price=view.findViewById(R.id.Price);
        category=view.findViewById(R.id.spinner_category);
        uploadPost=view.findViewById(R.id.btnCreate_post);
        cancelPost=view.findViewById(R.id.btnCancel);
        store=FirebaseStorage.getInstance();
        db=FirebaseFirestore.getInstance();
        arrayList_category = new ArrayList<>();
        arrayList_category.add("Tài liệu");
        arrayList_category.add("Đồ dùng cá nhân");
        arrayAdapter= new ArrayAdapter<>(requireActivity(), android.R.layout.simple_spinner_item,arrayList_category);
        category.setAdapter(arrayAdapter);

        imageView_post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mGetContent.launch("image/*");
            }
        });

        uploadPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(imageUrl!=null){
                   String file_name=UUID.randomUUID().toString();

                   StorageReference storageReference=store.getReference().child("image/"+file_name);
                   storageReference.putFile(imageUrl).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                       @Override
                       public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                           if(!task.isSuccessful()) throw  task.getException();
                           return storageReference.getDownloadUrl();
                       }
                   }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                       @Override
                       public void onComplete(@NonNull Task<Uri> task) {
                           if(task.isSuccessful()){
                               Map<String,Object> post= new HashMap<>();
                               post.put("name_owner",name);
                               post.put("image",task.getResult().toString());
                               post.put("title",title.getText().toString().toUpperCase().trim());
                               post.put("price",price.getText().toString().trim());
                               post.put("category",category.getSelectedItem().toString());
                               post.put("code_owner",code);
                               post.put("status","1");
                               post.put("time",new Timestamp(new Date()));

                               if(title.getText().toString().isEmpty()){
                                   Toast.makeText(getActivity(), "No title entered", Toast.LENGTH_SHORT).show();
                               }else {

                                   db.collection("post").add(post)
                                           .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                               @Override
                                               public void onSuccess(DocumentReference documentReference) {
                                                   Toast.makeText(getActivity(), "Success", Toast.LENGTH_SHORT).show();
                                                   Fragment homeFragment= new HomeFragment();
                                                   FragmentTransaction transaction=getFragmentManager().beginTransaction();
                                                   transaction.replace(R.id.frameLayout,homeFragment,"homeFragment");
                                                   transaction.addToBackStack("homeFragment");
                                                   transaction.commit();
                                               }
                                           }).addOnFailureListener(new OnFailureListener() {
                                               @Override
                                               public void onFailure(@NonNull Exception e) {
                                                   Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                                               }
                                           });
                               }
                           }
                       }
                   });

                }
            }
        });

        cancelPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                builder = new AlertDialog.Builder(getActivity());
                builder
                        .setMessage("Do you want to cancel").setTitle("Alert")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Fragment homeFragment = new HomeFragment();
                                FragmentTransaction transaction=getFragmentManager().beginTransaction();
                                transaction.replace(R.id.frameLayout,homeFragment,"homeFragment");
                                transaction.addToBackStack("homeFragment");
                                transaction.commit();
                            }
                        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                            }
                        });
                builder.create();
                builder.show();
            }
        });

        return view;
    }


    ActivityResultLauncher<String> mGetContent= registerForActivityResult(new ActivityResultContracts.GetContent(),
            new ActivityResultCallback<Uri>() {
        @Override
        public void onActivityResult(Uri result) {
            if(result!=null){
                imageView_post.setImageURI(result);
                imageUrl=result;
            }
        }
    });

}