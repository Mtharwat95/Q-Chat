package com.example.click.ui.Registration.View;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.balysv.materialripple.MaterialRippleLayout;
import com.example.click.Home;
import com.example.click.Nutil.Util;
import com.example.click.R;
import com.example.click.ui.Registration.Presenter.RegistrationMainContract;
import com.example.click.ui.Registration.Presenter.MainPresenter;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class Login extends AppCompatActivity implements RegistrationMainContract.IView {

    @BindView(R.id.etEmailLogin)
    EditText etEmailLogin;
    @BindView(R.id.etPasswordLogin)
    EditText etPasswordLogin;
    @BindView(R.id.btnLogin)
    MaterialRippleLayout btnLogin;
    @BindView(R.id.btnSignUp)
    MaterialRippleLayout btnSignUp;
    @BindView(R.id.progressLogin)
    ProgressBar progressLogin;

    String Email, Password;
    private MainPresenter presenter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        presenter = new MainPresenter(this);
    }


    @OnClick(R.id.btnLogin)
    void onLogin() {
        Email = etEmailLogin.getText().toString();
        Password = etPasswordLogin.getText().toString();
        if (Util.validateLogin(etEmailLogin, etPasswordLogin)) {
            progressLogin.setVisibility(View.VISIBLE);
            presenter.onLoginBtnClick(this,Email, Password);
        } else {
            Toast.makeText(this, "Incorrect Email or password", Toast.LENGTH_SHORT).show();
        }
    }

    @OnClick(R.id.btnSignUp)
    void onSignUp(){
        Intent intent = new Intent(this, Register.class);
        startActivity(intent);
        finish();
    }


    @Override
    public void onLoginSuccess(String message) {
        progressLogin.setVisibility(View.GONE);
        Intent intent = new Intent(Login.this, Home.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onLoginFailed(String message) {
        progressLogin.setVisibility(View.GONE);
        Toast.makeText(Login.this, message, Toast.LENGTH_SHORT).show();
    }

}
