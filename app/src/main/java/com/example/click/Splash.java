package com.example.click;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.click.ui.Registration.View.Login;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Splash extends AppCompatActivity {

    static FirebaseUser firebaseUser;
    public static int SPLASH_SCREEN = 3000;
    Animation topAnim,bottomAnim,rightAnim,leftAnim;
    ImageView splashImage;
    TextView appName,guide1,guide2,guide3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_start);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        topAnim = AnimationUtils.loadAnimation(this,R.anim.top_anim);
        bottomAnim = AnimationUtils.loadAnimation(this,R.anim.bottom_anim);
        rightAnim = AnimationUtils.loadAnimation(this,R.anim.right_anim);
        leftAnim = AnimationUtils.loadAnimation(this,R.anim.left_anim);

        splashImage = findViewById(R.id.splashImage);
        appName = findViewById(R.id.splashAppName);
        guide1 = findViewById(R.id.splashGuide1);
        guide2 = findViewById(R.id.splashGuide2);
        guide3 = findViewById(R.id.splashGuide3);


        splashImage.setAnimation(topAnim);

        appName.setAnimation(bottomAnim);

        guide1.setAnimation(rightAnim);
        guide2.setAnimation(leftAnim);
        guide3.setAnimation(bottomAnim);


        new Handler().postDelayed(() -> {

            if (firebaseUser != null){
                Intent intent=new Intent(Splash.this, Home.class);
                startActivity(intent);
                finish();
            }else {
                Intent intent=new Intent(Splash.this, Login.class);
                startActivity(intent);
                finish();
            }
        }, SPLASH_SCREEN);
    }


}
