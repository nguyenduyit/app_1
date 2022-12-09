package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Signup extends AppCompatActivity {
    private Spinner spinnerCourse;
    ArrayList<String> arrayList_course;
    ArrayAdapter<String> arrayAdapter_course;
    private EditText code_signup;
    private EditText name;

    private Spinner spinnerFaculty;
    ArrayList<String> arrayList_faculty;
    ArrayAdapter<String> arrayAdapter_faculty;

    ArrayList<String> arrayList_CNTT,arrayList_KT,arrayList_CN;
    ArrayAdapter<String> arrayAdapter_Major;

    private Spinner spinnerMajor;
    private  Button submit_signup;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        code_signup=findViewById(R.id.txtCode_Signup);
        spinnerCourse=findViewById(R.id.spinnerCourse);
        spinnerFaculty=findViewById(R.id.spinnerFaculty);
        spinnerMajor=findViewById(R.id.spinnerMajor);
        submit_signup=findViewById(R.id.btnSignup_Signup);
        name=findViewById(R.id.txtName);
        arrayList_course=new ArrayList<>();
        arrayList_course.add("44");
        arrayList_course.add("45");
        arrayList_course.add("46");
        arrayList_course.add("47");
        arrayList_course.add("48");
        arrayAdapter_course= new ArrayAdapter<>(this,android.R.layout.simple_spinner_item,arrayList_course);

        spinnerCourse.setAdapter(arrayAdapter_course);



        arrayList_faculty=new ArrayList<>();
        arrayList_faculty.add("Công nghệ thông tin và truyền thông");
        arrayList_faculty.add("Kinh tế");
        arrayList_faculty.add("Công nghệ");

        arrayAdapter_faculty = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item,arrayList_faculty);
        spinnerFaculty.setAdapter(arrayAdapter_faculty);

        arrayList_CNTT=new ArrayList<>();
        arrayList_CNTT.add("Công nghệ thông tin");
        arrayList_CNTT.add("Kỹ thuật phần mềm");

        arrayList_KT=new ArrayList<>();
        arrayList_KT.add("Kế toán");
        arrayList_KT.add("Kiểm toán");

        arrayList_CN=new ArrayList<>();
        arrayList_CN.add("Kỹ thuật ô tô");
        arrayList_CN.add("Cơ điện tử");



        spinnerFaculty.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(i==0){
                    arrayAdapter_Major=new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_item,arrayList_CNTT);
                }
                if(i==1){
                    arrayAdapter_Major=new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_item,arrayList_KT);
                }
                if(i==2){
                    arrayAdapter_Major=new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_item,arrayList_CN);
                }
                String txtFaculty=adapterView.getItemAtPosition(i).toString();
//                Toast.makeText(Signup.this, txtFaculty, Toast.LENGTH_SHORT).show();

                spinnerMajor.setAdapter(arrayAdapter_Major);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                String txtFaculty=adapterView.getItemAtPosition(0).toString();
//                Toast.makeText(Signup.this, txtFaculty, Toast.LENGTH_SHORT).show();
                arrayAdapter_Major=new ArrayAdapter<>(getApplicationContext(),android.R.layout.simple_spinner_item,arrayList_CNTT);
                spinnerMajor.setAdapter(arrayAdapter_Major);
            }
        });

        spinnerMajor.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String txtMajor=adapterView.getItemAtPosition(i).toString();
//                Toast.makeText(Signup.this, txtMajor, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                String txtMajor=adapterView.getItemAtPosition(0).toString();
//                Toast.makeText(Signup.this, txtMajor, Toast.LENGTH_SHORT).show();
            }
        });
        submit_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                db=FirebaseFirestore.getInstance();
                Map<String,Object> student=new HashMap<>();
                student.put("code",code_signup.getText().toString().toUpperCase().trim());
                student.put("name",name.getText().toString().trim());
                student.put("course",spinnerCourse.getSelectedItem().toString());
                student.put("faculty",spinnerFaculty.getSelectedItem().toString());
                student.put("major",spinnerMajor.getSelectedItem().toString());
                student.put("password","123456");
                student.put("district","");
                student.put("ward","");
                db.collection("student").whereEqualTo("code",code_signup.getText().toString().trim()).get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if(!task.getResult().isEmpty()){
                                    Toast.makeText(Signup.this, code_signup.getText().toString().trim() + " đã tồn tại", Toast.LENGTH_SHORT).show();
                                }else {
                                    db.collection("student").add(student)
                                            .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                                @Override
                                                public void onSuccess(DocumentReference documentReference) {
                                                    Toast.makeText(Signup.this, "Success", Toast.LENGTH_SHORT).show();
                                                    Intent intent = new Intent(Signup.this, MainActivity.class);
                                                    startActivity(intent);
                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Toast.makeText(Signup.this, "Failure", Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                }
                            }
                        });


            }
        });

    }


}