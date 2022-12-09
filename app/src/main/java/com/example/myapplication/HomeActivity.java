package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class HomeActivity extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;
    HomeFragment homeFragment = new HomeFragment();
    ProfileFragment profileFragment = new ProfileFragment();
    OtherFragment otherFragment = new OtherFragment();
    MyPostFragment myPostFragment= new MyPostFragment();
    AlertFragment alertFragment = new AlertFragment();
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        bottomNavigationView=findViewById(R.id.nav_bottom);
        getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout,homeFragment);
        sharedPreferences=getSharedPreferences("key", MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        String name,code,course,major,faculty,district,ward,id;
        name = sharedPreferences.getString("name",null);
        code= sharedPreferences.getString("code",null);
        faculty =sharedPreferences.getString("faculty",null);
        major= sharedPreferences.getString("major",null);
        course= sharedPreferences.getString("course",null);
        district=sharedPreferences.getString("district",null);
        ward=sharedPreferences.getString("ward",null);
        id=sharedPreferences.getString("id",null);


       bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
           @Override
           public boolean onNavigationItemSelected(@NonNull MenuItem item) {
               Fragment selectedFragment=null;
               switch (item.getItemId()){
                   case R.id.home:
                        editor.putString("id",id);
                        editor.putString("code",code);
                        editor.putString("name",name);
                        editor.commit();
                       selectedFragment =homeFragment;

                       break;
                   case R.id.profile:

                       editor.putString("name",name);
                       editor.putString("code",code);
                       editor.putString("faculty",faculty);
                       editor.putString("major",major);
                       editor.putString("course",course);
                       editor.putString("district",district);
                       editor.putString("ward",ward);
                       editor.commit();
                       selectedFragment =profileFragment;



                       break;
//                   case R.id.setting:
//                       selectedFragment =otherFragment;
//                       break;
                   case R.id.MyPost:
                       selectedFragment =myPostFragment;
                       editor.putString("code",code);
                       editor.commit();
                       editor.apply();
                       break;
                   case R.id.Alert:
                       selectedFragment =alertFragment;
                       editor.putString("code",code);
                       editor.commit();
                       editor.apply();
                       break;
               }
               getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout,selectedFragment).commit();
               return true;
           }
       });



    }
}