package com.example.myapplication;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link OtherFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class OtherFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    View v;
    TextView othername,othercode,otheraddress,othercoursemajor,otherphone;
    RecyclerView otherPostRecyler;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    FirebaseFirestore db;
    ArrayList<Post> postArrayList;
    MyAdapter myAdapter;
    String code;
    Bundle bundle;
    Button backToAlert;

    public OtherFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment OtherFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static OtherFragment newInstance(String param1, String param2) {
        OtherFragment fragment = new OtherFragment();
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
        v= inflater.inflate(R.layout.fragment_other, container, false);
        othercode=v.findViewById(R.id.otherCode);
        othername=v.findViewById(R.id.otherName);
        otheraddress=v.findViewById(R.id.otherAddress);
        othercoursemajor=v.findViewById(R.id.otherCourseMajor);
        otherphone=v.findViewById(R.id.otherPhone);
        backToAlert=v.findViewById(R.id.btnBackToAlert);
//        sharedPreferences=getActivity().getSharedPreferences("key", Context.MODE_PRIVATE);
//        editor= sharedPreferences.edit();
        bundle=this.getArguments();
        if(bundle !=null){
            code= bundle.getString("student_confirm",null);
        }

//
        db=FirebaseFirestore.getInstance();
        db.collection("student").whereEqualTo("code",code).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        for(DocumentSnapshot doc: task.getResult()){
                            Student student=doc.toObject(Student.class);
                            othercode.setText(student.code);
                            othername.setText(student.name);
                            otheraddress.setText(student.district+" - "+student.ward);
                            othercoursemajor.setText(student.course+" : "+student.major);
                            otherphone.setText(student.numberPhone);
                        }
                    }
                });
        otherPostRecyler=v.findViewById(R.id.otherPostRecycler);
        otherPostRecyler.setHasFixedSize(true);
        otherPostRecyler.setLayoutManager(new LinearLayoutManager(getActivity()));
        postArrayList = new ArrayList<Post>();
        myAdapter = new MyAdapter(getContext(),postArrayList);
        otherPostRecyler.setAdapter(myAdapter);
        EventChangeListener();
        backToAlert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              FragmentTransaction transaction = getFragmentManager().beginTransaction();
              transaction.replace(R.id.frameLayout,new AlertFragment());
              transaction.addToBackStack(null);
              transaction.commit();
            }
        });

        return v;
    }
    private void EventChangeListener() {

        db.collection("post")
                .whereEqualTo("code_owner",code)
                .whereEqualTo("status","1")
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


}