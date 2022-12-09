package com.example.myapplication;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private EditText code;
    private EditText password;
    private Button login;
    private Button signup;
    private Button forget_password;
    private FirebaseFirestore db;
    SharedPreferences sharedPreferences;
    AlertDialog.Builder builder;

    Fragment homeFragment= new HomeFragment();
    public  static  final String KEY="key";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        code=findViewById(R.id.txtCode);
        password=findViewById(R.id.txtPassword);
        login=findViewById(R.id.btnLogin);
        signup=findViewById(R.id.btnSignup);
        forget_password=findViewById(R.id.btnForgetPassword);

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,Signup.class);
                startActivity(intent);
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String code_input= code.getText().toString().toUpperCase().trim();
                db=FirebaseFirestore.getInstance();
                db.collection("student").whereEqualTo("code",code_input)
                        .whereEqualTo("password",password.getText().toString().trim()).get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if(task.isSuccessful()){
                                    QuerySnapshot q=task.getResult();
//                                    Log.d("Sucess", "onComplete: "+task.getResult().getDocuments());
                                    if(task.getResult().getDocuments().isEmpty()){
                                        builder = new AlertDialog.Builder(MainActivity.this);
                                        builder.setTitle("Something is wrong")
                                                .setMessage("Please, enter your code and password again!!")
                                                .setNegativeButton("OK", new DialogInterface.OnClickListener() {
                                                       @Override
                                                       public void onClick(DialogInterface dialogInterface, int i) {
                                                           dialogInterface.cancel();
                                                       }
                                                });
                                        builder.create();
                                        builder.show();
                                    }else{
                                        for(QueryDocumentSnapshot doc : task.getResult()){
                                            String code= (String) doc.getData().get("code");
                                            String username= (String) doc.getData().get("name");
                                            String course= (String) doc.getData().get("course");
                                            String faculty= (String) doc.getData().get("faculty");
                                            String major= (String) doc.getData().get("major");
                                            String district = (String)doc.getData().get("district");
                                            String ward = (String)doc.getData().get("ward");
                                            String id= (String)doc.getId();
                                            sharedPreferences = getSharedPreferences("key", MODE_PRIVATE);
                                            SharedPreferences.Editor editor=sharedPreferences.edit();

                                            editor.putString("code",code);
                                            editor.putString("name",username);
                                            editor.putString("course",course);
                                            editor.putString("faculty",faculty);
                                            editor.putString("major",major);
                                            editor.putString("district",district);
                                            editor.putString("ward",ward);
                                            editor.putString("id",id);
                                            editor.commit();
                                            Intent intent = new Intent(MainActivity.this,HomeActivity.class);

                                            startActivity(intent);

                                            Log.d("Sucess", "onComplete: "+ username.toString());
                                        }

                                    }
                                }
                            }
                        });
            }
        });

        forget_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,ForgetPasswordActivity.class);
                startActivity(intent);
            }
        });
    }
}