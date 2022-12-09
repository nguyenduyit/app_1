package com.example.myapplication;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AlertFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AlertFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    View view;
    FirebaseFirestore db;
    RecyclerView alertRecycler;
    AlertAdapter alertAdapter;
    ArrayList<ConfirmPost> confirmPostArrayList;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    String code;

    public AlertFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AlertFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AlertFragment newInstance(String param1, String param2) {
        AlertFragment fragment = new AlertFragment();
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
        view= inflater.inflate(R.layout.fragment_alert, container, false);
        db=FirebaseFirestore.getInstance();
        alertRecycler = view.findViewById(R.id.myalert);

        sharedPreferences=getActivity().getSharedPreferences("key",getContext().MODE_PRIVATE);
        editor=sharedPreferences.edit();
        code=sharedPreferences.getString("code",null);

        alertRecycler.setHasFixedSize(true);

        alertRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));

        db=FirebaseFirestore.getInstance();
        confirmPostArrayList = new ArrayList<ConfirmPost>();
        alertAdapter = new AlertAdapter(getContext(),confirmPostArrayList);

        alertRecycler.setAdapter(alertAdapter);
        EventChangeListener();

        return view;
    }

    private void EventChangeListener() {
        db.collection("confirmPost")

                .whereEqualTo("ownerPost",code)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for(DocumentSnapshot doc: queryDocumentSnapshots){
                            ConfirmPost confirmPost = doc.toObject(ConfirmPost.class);
                            confirmPost.setDocId(doc.getId());
                            confirmPostArrayList.add(confirmPost);
                        }
                        alertAdapter.notifyDataSetChanged();
                    }
                });
    }
}