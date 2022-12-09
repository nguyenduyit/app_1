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

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HistoryFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HistoryFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    View view;
    FirebaseFirestore db;
    RecyclerView historyRecycler;
    HistoryAdapter historyAdapter;
    ArrayList<ConfirmPost> confirmPostArrayList;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    String code;

    public HistoryFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HistoryFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HistoryFragment newInstance(String param1, String param2) {
        HistoryFragment fragment = new HistoryFragment();
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
        view= inflater.inflate(R.layout.fragment_history, container, false);
        sharedPreferences=getContext().getSharedPreferences("key", Context.MODE_PRIVATE);
        code=sharedPreferences.getString("code",null);
        db=FirebaseFirestore.getInstance();
        historyRecycler=view.findViewById(R.id.RecyclerHistory);
        historyRecycler.setHasFixedSize(true);

        historyRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        confirmPostArrayList = new ArrayList<ConfirmPost>();
        historyAdapter = new HistoryAdapter(getContext(),confirmPostArrayList);
        historyRecycler.setAdapter(historyAdapter);
        EventChangeListener();

        return view;
    }

    private void EventChangeListener() {
        db.collection("confirmPost")

                .whereEqualTo("studentConfirm",code)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for(DocumentSnapshot doc: queryDocumentSnapshots){
                            ConfirmPost confirmPost = doc.toObject(ConfirmPost.class);
                            confirmPost.setDocId(doc.getId());
                            confirmPostArrayList.add(confirmPost);
                        }
                        historyAdapter.notifyDataSetChanged();
                    }
                });
    }
}