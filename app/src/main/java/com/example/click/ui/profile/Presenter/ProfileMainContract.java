package com.example.click.ui.profile.Presenter;

import android.app.Activity;
import android.net.Uri;

import com.example.click.ui.profile.Model.User;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import de.hdodenhof.circleimageview.CircleImageView;

public interface ProfileMainContract {

    interface ProfileView{
        void onSuccess(User user);
        void onFailed(String message);
        void imageUploadedSuccess();
        void onResetPasswordSuccess(String message);
        void onResetPasswordFailed(String message);
    }

    interface ProfilePresenter{
        void requestUploadImage(Activity activity,Uri ImageUri);
        void requestData(CircleImageView imageView,Activity activity);
        void requesForgetPassword();
    }

    interface ProfileModel{
        void uploadImage(Activity activity, Uri ImageUri);
        void getProfileData(CircleImageView imageView,Activity activity);
        void ForgetPassword();
    }

    interface Listener{
        void onDataSuccess(User user, CircleImageView imageView, Activity activity);
        void onDataFailure(String message);
        void imageUploaded();
        void resetPasswordSuccess(String message);
        void resetPasswordFailed(String message);
    }
}
