package com.example.myapplication;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PostFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PostFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    View view;
    Bundle bundle;
    ImageView imageView_history;
    TextView code_ownerPost_history,name_ownerPost_history,categoryPost_history,
                pricePost_history,titlePost_history;
    String idPost;
    FirebaseFirestore db;
    Button backToProfile;

    public PostFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PostFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PostFragment newInstance(String param1, String param2) {
        PostFragment fragment = new PostFragment();
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
        view= inflater.inflate(R.layout.fragment_post, container, false);
        bundle=this.getArguments();
        if(bundle!=null){
           idPost=bundle.getString("idPost",null);
        }
        db=FirebaseFirestore.getInstance();
        imageView_history=view.findViewById(R.id.image_history);
        code_ownerPost_history=view.findViewById(R.id.code_ownerPost_history);
        name_ownerPost_history=view.findViewById(R.id.name_ownerPost_history);
        pricePost_history=view.findViewById(R.id.pricePost_history);
        titlePost_history=view.findViewById(R.id.titlePost_history);
        categoryPost_history=view.findViewById(R.id.categoryPost_history);
        backToProfile=view.findViewById(R.id.btnBackToProfile);

        db.collection("post").document(idPost)
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        Picasso.get().load(documentSnapshot.getString("image")).resize(400,400).into(imageView_history);
                        code_ownerPost_history.setText(documentSnapshot.getString("code_owner"));
                        name_ownerPost_history.setText(documentSnapshot.getString("name_owner"));
                        pricePost_history.setText(documentSnapshot.getString("price"));
                        categoryPost_history.setText(documentSnapshot.getString("category"));
                        titlePost_history.setText(documentSnapshot.getString("title"));
                    }
                });

        code_ownerPost_history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bundle.putString("student_confirm",code_ownerPost_history.getText().toString());
                OtherFragment otherFragment = new OtherFragment();
                ((HomeActivity)getContext()).getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.frameLayout, otherFragment).addToBackStack(null)
                        .commit();
            }
        });

        backToProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ProfileFragment profileFragment = new ProfileFragment();
                ((HomeActivity)getContext()).getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.frameLayout, profileFragment).addToBackStack(null)
                        .commit();
            }
        });

        return view;
    }
}