package com.example.click.ui.Registration.Presenter;

import android.app.Activity;
import android.net.Uri;
import android.widget.ProgressBar;

public interface RegistrationMainContract {

    interface IView{
        void onLoginSuccess(String message);
        void onLoginFailed(String message);
    }

    interface RegisterView{
        void onRegisterSuccess(String message);
        void onRegisterFailed(String message);
        void onUploadSuccess(String message);
        void onUploadFailed(String message);
    }


    interface IPresenter{
        void onLoginBtnClick(Activity activity,String Email, String Password);
        void onRegisterClick(Activity activity,String userName,String Email,String Password,Uri imageUri,String gender,String country,String bio);
    }


    interface IModel{
        void LoginGetData(Activity activity,String Email,String Password);
        void Register(Activity activity,String userName,String Email,String Password,Uri imageUri,String gender,String country,String bio);
        void UploadImage(Activity activity, Uri image);
    }

    interface Listener{
        void onSuccess(String message);
        void onFailure(String message);
    }

    interface RegisterListener{
        void Registered(Activity activity,Uri imageUri);
        void NotRegistered();
        void uploadedSusses(String message);
        void uploadedFailed(String message);
    }
}
