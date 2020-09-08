package com.example.click;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;

import com.example.click.ui.Registration.View.Login;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Start extends AppCompatActivity {

    static FirebaseUser firebaseUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_start);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();


        new Handler().postDelayed(() -> {

            if (firebaseUser != null){
                Intent intent=new Intent(Start.this, Home.class);
                startActivity(intent);
                finish();
            }else {
                Intent intent=new Intent(Start.this, Login.class);
                startActivity(intent);
                finish();
            }
        }, 2000);
    }


}
