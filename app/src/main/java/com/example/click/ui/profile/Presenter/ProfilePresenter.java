package com.example.click.ui.profile.Presenter;

import android.app.Activity;
import android.net.Uri;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.click.R;
import com.example.click.ui.profile.Model.ProfileModel;
import com.example.click.ui.profile.Model.User;
import com.example.click.ui.profile.View.ProfileFragment;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfilePresenter implements ProfileMainContract.ProfilePresenter, ProfileMainContract.Listener {

    ProfileMainContract.ProfileModel profileModel;
    ProfileMainContract.ProfileView profileView;

    public ProfilePresenter(ProfileMainContract.ProfileView profileView) {
        profileModel = new ProfileModel(this);
        this.profileView = profileView;
    }


    @Override
    public void requestUploadImage(Activity activity, Uri ImageUri) {
        profileModel.uploadImage(activity, ImageUri);
    }

    @Override
    public void requestData(CircleImageView imageView,Activity activity) {
        profileModel.getProfileData(imageView,activity);
    }

    @Override
    public void requesForgetPassword() {
        profileModel.ForgetPassword();
    }

    @Override
    public void onDataSuccess(User user, CircleImageView imageView, Activity activity) {
        profileView.onSuccess(user);
        if (user!=null){
            String url = user.getImageUrl();
            if (url.equals("default")) {
                imageView.setImageResource(R.drawable.ic_profile_24dp);
            }else {
                Glide.with(Objects.requireNonNull(activity))
                        .load(url)
                        .override(imageView.getWidth(), imageView.getHeight())
                        .into(imageView);
            }
        }else {
            Toast.makeText(activity,"Loading.....",Toast.LENGTH_SHORT).show();
        }


    }

    @Override
    public void onDataFailure(String message) {
        profileView.onFailed(message);
    }

    @Override
    public void imageUploaded() {
        profileView.imageUploadedSuccess();

    }

    @Override
    public void resetPasswordSuccess(String message) {
        profileView.onResetPasswordSuccess(message);
    }

    @Override
    public void resetPasswordFailed(String message) {
        profileView.onResetPasswordFailed(message);
    }


}
