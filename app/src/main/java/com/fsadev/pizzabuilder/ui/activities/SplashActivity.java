package com.fsadev.pizzabuilder.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;

import androidx.appcompat.app.AppCompatActivity;

import com.fsadev.pizzabuilder.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SplashActivity extends AppCompatActivity {
    private static final int COUNT_DOWN_TIME =3500;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        //-----------------------------------------
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        VerifyUser(user);

    }
    //espera 3 seg y comprueba si la aplicación tiene una instancia activa de usuario
    private void VerifyUser(FirebaseUser user) {
        new CountDownTimer(COUNT_DOWN_TIME,1000){
            @Override
            public void onTick(long millisUntilFinished) { }
            @Override
            public void onFinish() {
                if (user!=null){
                    GoToHome();
                }else{
                    GoToLogin();
                }
            }
        }.start();


    }

    //Lleva al login
    private void GoToLogin() {
        startActivity(new Intent(SplashActivity.this, LoginActivity.class ));
        overridePendingTransition(R.anim.left_in, R.anim.left_out);
        finish();
    }

    //Lleva al home
    private void GoToHome() {

        startActivity(new Intent(SplashActivity.this, HomeActivity.class ));
        overridePendingTransition(R.anim.zoomin, R.anim.fadeout);
        finish();
    }
}