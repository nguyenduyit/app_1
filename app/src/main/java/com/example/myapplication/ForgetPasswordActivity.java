package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class ForgetPasswordActivity extends AppCompatActivity {
    private EditText code,password;
    private Button update_password;
    FirebaseFirestore db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);

        code=findViewById(R.id.txtCode_Forget);
        password=findViewById(R.id.txtPassword_Forget);
        update_password=findViewById(R.id.btnUpdate_Password);

        update_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(code.getText().toString().isEmpty() || password.getText().toString().isEmpty()){
                    Toast.makeText(ForgetPasswordActivity.this, "Your input is not empty", Toast.LENGTH_SHORT).show();
                }else{
                    String code_forget=code.getText().toString().toUpperCase().trim();
                    db=FirebaseFirestore.getInstance();
//                Log.d("Success", "onClick: "+code_forget);
                    db.collection("student").whereEqualTo("code",code_forget)
                            .get()
                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if(task.isSuccessful()){
                                        if(task.getResult().getDocuments().size()==0){

                                            Toast.makeText(ForgetPasswordActivity.this, "Không tồn tại tài khoản của bạn", Toast.LENGTH_SHORT).show();
                                        }
                                        else {
                                            for(QueryDocumentSnapshot doc : task.getResult()){
                                                String id=doc.getId().toString();
//                                            Log.d("ID", "onComplete: "+id);
                                                String new_password=password.getText().toString().trim();

                                                Map<String,Object> student= new HashMap<>();
                                                student.put("password",new_password);

                                                db.collection("student").document(id).update(student)
                                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                            @Override
                                                            public void onSuccess(Void unused) {
                                                                Toast.makeText(ForgetPasswordActivity.this, "Thành công", Toast.LENGTH_SHORT).show();
                                                                Intent intent = new Intent(ForgetPasswordActivity.this,MainActivity.class);
                                                                startActivity(intent);
                                                            }
                                                        }).addOnFailureListener(new OnFailureListener() {
                                                            @Override
                                                            public void onFailure(@NonNull Exception e) {
                                                                Toast.makeText(ForgetPasswordActivity.this, "Thất bại", Toast.LENGTH_SHORT).show();
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

    }
}