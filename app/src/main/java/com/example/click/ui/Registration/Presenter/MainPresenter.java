package com.example.click.ui.Registration.Presenter;

import android.app.Activity;
import android.net.Uri;

import com.example.click.ui.Registration.Model.MainModel;

public class MainPresenter implements RegistrationMainContract.IPresenter , RegistrationMainContract.Listener , RegistrationMainContract.RegisterListener {

    private RegistrationMainContract.IModel imodel;
    private RegistrationMainContract.IView iview;
    private RegistrationMainContract.RegisterView registerView;

    public MainPresenter(RegistrationMainContract.IView iview) {
        imodel = new MainModel((RegistrationMainContract.Listener) this);
        this.iview = iview;
    }

    public MainPresenter(RegistrationMainContract.RegisterView registerView) {
        imodel = new MainModel((RegistrationMainContract.RegisterListener) this);
        this.registerView = registerView;
    }

    @Override
    public void onLoginBtnClick(Activity activity, String Email, String Password) {
        imodel.LoginGetData(activity,Email,Password);
    }

    @Override
    public void onRegisterClick(Activity activity, String userName, String Email, String Password,Uri imageUri,String gender,String country,String bio) {
        imodel.Register(activity,userName,Email,Password,imageUri,gender,country,bio);
    }

    @Override
    public void onSuccess(String message) {
        iview.onLoginSuccess(message);
    }

    @Override
    public void onFailure(String message) {
        iview.onLoginFailed(message);
    }

    @Override
    public void Registered(Activity activity,Uri imageUri) {
        registerView.onRegisterSuccess("Registration Success");
        imodel.UploadImage(activity,imageUri);
    }

    @Override
    public void NotRegistered() {
        registerView.onRegisterFailed("Registration Failed");
    }

    @Override
    public void uploadedSusses(String message) {
        registerView.onRegisterSuccess(message);
    }

    @Override
    public void uploadedFailed(String message) {
        registerView.onRegisterSuccess(message);
    }
}
