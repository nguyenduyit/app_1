package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityOptionsCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private EditText  editText_code, editText_name, editText_course,
            editText_faculty, editText_major,editText_district,editText_ward,editText_numberphone;
    private Spinner spinnerDistrict,spinnerWard;
    private  ArrayList<String> arrayList_District, arrayList_NK,arrayList_CR;
    private  ArrayAdapter<String> arrayAdapter_District,arrayAdapter_Ward;
    private ImageView imageView;
    private View view;
    private Button update_info,logout,history;
    FirebaseFirestore db;
    SharedPreferences sharedPreferences;
    FirebaseStorage store;
    Student student;
    Uri imageUrl;
    String result;



    public ProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
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
        view=inflater.inflate(R.layout.fragment_profile, container, false);
        editText_code=view.findViewById(R.id.textCode);
        editText_name=view.findViewById(R.id.textName);
        editText_course=view.findViewById(R.id.textCourse);
        imageView=view.findViewById(R.id.imageView_Avatar);
        update_info=view.findViewById(R.id.button_update_info);
        editText_district=view.findViewById(R.id.textDistrict);
        editText_numberphone=view.findViewById(R.id.numberphone);
        history=view.findViewById(R.id.History);
        sharedPreferences= getActivity().getSharedPreferences("key", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferences.edit();

        db=FirebaseFirestore.getInstance();
        store=FirebaseStorage.getInstance();
        spinnerDistrict=view.findViewById(R.id.spinner_district);
        spinnerWard=view.findViewById(R.id.spinner_ward);



        db.collection("student").whereEqualTo("code",sharedPreferences.getString("code",null)).get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                             for(DocumentSnapshot doc:queryDocumentSnapshots){
                                 student=doc.toObject(Student.class);
                                 student.setDocId(doc.getId());
                                 editText_code.setText(student.code);
                                 editText_course.setText(student.course + ":" + student.major);
                                 editText_district.setText(student.district + " - " + student.ward);
                                 editText_name.setText(student.name);
                                 editText_numberphone.setText(student.numberPhone);
                                 if(student.avatar!=null){
                                     Picasso.get().load(student.avatar).resize(500,600).into(imageView);
                                 }
                                 else {
                                     imageView.setBackgroundResource(R.mipmap.ic_useravatar_background);
                                 }
                             }


                    }});

       arrayList_District = new ArrayList<>();
       arrayList_District.add("Ninh Kiều");
       arrayList_District.add("Cái Răng");
       arrayList_CR = new ArrayList<>();
       arrayList_CR.add("Lê Bình");
       arrayList_CR.add("Phú Thứ");
       arrayList_NK = new ArrayList<>();
       arrayList_NK.add("Xuân Khánh");
       arrayList_NK.add("Hưng Lợi");
       arrayAdapter_District = new ArrayAdapter<String>(this.getActivity(),android.R.layout.simple_spinner_item,arrayList_District);
       spinnerDistrict.setAdapter(arrayAdapter_District);

       spinnerDistrict.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
           @Override
           public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(i==0){
                   arrayAdapter_Ward= new ArrayAdapter<>(requireActivity(), android.R.layout.simple_spinner_item,arrayList_NK);
                }
                if(i==1){
                    arrayAdapter_Ward= new ArrayAdapter<>(requireActivity(), android.R.layout.simple_spinner_item,arrayList_CR);
                }
               
                spinnerWard.setAdapter(arrayAdapter_Ward);
           }

           @Override
           public void onNothingSelected(AdapterView<?> adapterView) {
                arrayAdapter_Ward =new ArrayAdapter<>(requireActivity(), android.R.layout.simple_spinner_item,arrayList_NK);
                spinnerWard.setAdapter(arrayAdapter_Ward);
           }
       });
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mGetContent.launch("image/*");
            }
        });



        update_info.setOnClickListener(new View.OnClickListener() {
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

                            String code=sharedPreferences.getString("code",null);

                            String district=spinnerDistrict.getSelectedItem().toString();
                            String ward=spinnerWard.getSelectedItem().toString();

                            editText_district.setText(district+" - "+ward);
                            String avatar=task.getResult().toString();
                            db=FirebaseFirestore.getInstance();
                            db.collection("student").whereEqualTo("code",code)
                                    .get()
                                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                            if(task.isSuccessful()){
                                                if(task.getResult().getDocuments().size()>0){
                                                    for(QueryDocumentSnapshot doc : task.getResult()){
                                                        Map<String,Object> student= new HashMap<>();

                                                        student.put("district",district);
                                                        student.put("ward",ward);
                                                        student.put("avatar",avatar);
                                                        db.collection("student").document(doc.getId()).update(student)
                                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                    @Override
                                                                    public void onSuccess(Void unused) {
                                                                        Toast.makeText(requireActivity(), "Success", Toast.LENGTH_SHORT).show();

                                                                    }
                                                                })
                                                                .addOnFailureListener(new OnFailureListener() {
                                                                    @Override
                                                                    public void onFailure(@NonNull Exception e) {
                                                                        Toast.makeText(requireActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                                                                    }
                                                                });
                                                    }
                                                }
                                            }
                                        }
                                    });
                        }
                    });
                }else {
                    String code=sharedPreferences.getString("code",null);

                    String district=spinnerDistrict.getSelectedItem().toString();
                    String ward=spinnerWard.getSelectedItem().toString();

                    editText_district.setText(district+" - "+ward);

                    db=FirebaseFirestore.getInstance();
                    db.collection("student").whereEqualTo("code",code)
                            .get()
                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if(task.isSuccessful()){
                                        if(task.getResult().getDocuments().size()>0){
                                            for(QueryDocumentSnapshot doc : task.getResult()){
                                                Map<String,Object> student= new HashMap<>();

                                                student.put("district",district);
                                                student.put("ward",ward);

                                                db.collection("student").document(doc.getId()).update(student)
                                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                            @Override
                                                            public void onSuccess(Void unused) {
                                                                Toast.makeText(requireActivity(), "Success", Toast.LENGTH_SHORT).show();

                                                            }
                                                        })
                                                        .addOnFailureListener(new OnFailureListener() {
                                                            @Override
                                                            public void onFailure(@NonNull Exception e) {
                                                                Toast.makeText(requireActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                                                            }
                                                        });
                                            }
                                        }
                                    }
                                }
                            });
                }

            }
        });


        logout=view.findViewById(R.id.Logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editor.clear();
                editor.apply();
                Intent intent = new Intent(getActivity(),MainActivity.class);
                startActivity(intent);
            }
        });

        history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editor.putString("code",editText_code.getText().toString());
                Fragment historyFragment = new HistoryFragment();
                HomeActivity activity = (HomeActivity) view.getContext();
                activity.getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.frameLayout,historyFragment)
                        .addToBackStack(null)
                        .commit();
                editor.apply();
                editor.commit();

            }
        });




        return view;
    }

    ActivityResultLauncher<String> mGetContent= registerForActivityResult(new ActivityResultContracts.GetContent(),
            new ActivityResultCallback<Uri>() {
                @Override
                public void onActivityResult(Uri result) {
                    if(result!=null){
                        imageView.setImageURI(result);
                        imageUrl=result;
                    }
                }
            });

}