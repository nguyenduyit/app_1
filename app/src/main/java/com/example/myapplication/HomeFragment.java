package com.example.myapplication;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment<timestamp> extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    String mParam1;
    String mParam2;
    FirebaseFirestore db;
    View view;
    String id,name,code;
    Button create_post;
    SharedPreferences sharedPreferences;
    Fragment createPostFragment = new CreatePostFragment();
    RecyclerView recyclerView;
    ArrayList<Post> postArrayList;
    MyAdapter myAdapter;
    EditText search;


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
        view= inflater.inflate(R.layout.fragment_home, container, false);
        sharedPreferences=getActivity().getSharedPreferences("key", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        id=sharedPreferences.getString("id",null);
        name=sharedPreferences.getString("name",null);
        code=sharedPreferences.getString("code",null);
        create_post= view.findViewById(R.id.btnCreate_post);
        search=view.findViewById(R.id.editTextSearch);

        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                    filter(editable.toString());
            }
        });
        create_post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editor.putString("id",id);
                editor.putString("code",code);
                editor.putString("name",name);
                editor.commit();
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout,createPostFragment).commit();


            }
        });

        recyclerView=view.findViewById(R.id.postRecycler);
        recyclerView.setHasFixedSize(true);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        db=FirebaseFirestore.getInstance();
        postArrayList = new ArrayList<Post>();
        myAdapter = new MyAdapter(getActivity(),postArrayList);

        recyclerView.setAdapter(myAdapter);
        EventChangeListener();



        return  view;
    }

    private void EventChangeListener() {

       db.collection("post")

               .whereEqualTo("status","1")
               .orderBy(FieldPath.documentId(), Query.Direction.DESCENDING)
               .get()
               .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                   @Override
                   public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                       for(DocumentSnapshot documentSnapshot : queryDocumentSnapshots){
                          Post post=documentSnapshot.toObject(Post.class);
                          post.setDocId(documentSnapshot.getId());
                          postArrayList.add(post);
                       }
                       myAdapter.notifyDataSetChanged();
                   }
               });
    }

    private void filter(String text){
        ArrayList<Post> filteredList = new ArrayList<>();

        for (Post post : postArrayList) {
            if (post.getTitle().toLowerCase().contains(text.toLowerCase()) || post.getName_owner().toLowerCase().contains(text.toLowerCase()) ) {
                filteredList.add(post);
            }
        }
        myAdapter.filterList(filteredList);
    }


}