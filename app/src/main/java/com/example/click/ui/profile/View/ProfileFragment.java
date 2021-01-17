package com.example.click.ui.profile.View;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.example.click.Home;
import com.example.click.Nutil.Util;
import com.example.click.R;
import com.example.click.Splash;
import com.example.click.ui.profile.Model.User;
import com.example.click.ui.profile.Presenter.ProfileMainContract;
import com.example.click.ui.profile.Presenter.ProfilePresenter;
import com.fxn.pix.Options;
import com.fxn.pix.Pix;
import com.fxn.utility.PermUtil;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.StorageTask;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;


public class ProfileFragment extends Fragment implements ProfileMainContract.ProfileView {

    @BindView(R.id.ivProfileImage)
    CircleImageView ivProfileImage;
    @BindView(R.id.tvUserName)
    TextView tvUserName;
    @BindView(R.id.tvEmail)
    TextView tvEmail;
    @BindView(R.id.toolbarProfile)
    Toolbar toolbarProfile;
    @BindView(R.id.profileProgressBar)
    ProgressBar profileProgressBar;
    @BindView(R.id.tvBio)
    TextView tvBio;
    @BindView(R.id.tvGender)
    TextView tvGender;
    @BindView(R.id.tvCountry)
    TextView tvCountry;
    private User user;
    private View root;
    private String TAG = "profile";

    private StorageTask uploadTask;
    FirebaseAuth firebaseAuth;
    private Uri ImageUri;
    FirebaseUser firebaseUser;
    private ProfilePresenter profilePresenter;

    @SuppressLint("FragmentLiveDataObserve")
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_profile, container, false);

        ButterKnife.bind(this, root);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        prepareMenu();
        profileProgressBar.setVisibility(View.VISIBLE);
        profilePresenter = new ProfilePresenter(this);
        Home.userStatus("online");
        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        profilePresenter.requestData(ivProfileImage, getActivity());
        Home.userStatus("online");
    }


//    @OnClick(R.id.forgetPassword)
//    void forgetPassword() {
//        profileProgressBar.setVisibility(View.VISIBLE);
//        profilePresenter.requesForgetPassword();
//
//    }


    private void onLogout(Activity activity) {
        FirebaseAuth.getInstance().signOut();
        Util.startActivityUtil(activity, Splash.class);
    }


    private void prepareMenu() {
        toolbarProfile.inflateMenu(R.menu.profile_menu);
        toolbarProfile.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.logout) {
                onLogout(getActivity());
            } else if (item.getItemId() == R.id.editProfile) {

            }
            return false;
        });

    }

    @OnClick(R.id.ivProfileImage)
    void onProfileImageClick() {
        Pix.start(ProfileFragment.this, Options.init().setRequestCode(Util.IMAGE_REQUEST));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        assert data != null;
        if (resultCode == Activity.RESULT_OK && requestCode == Util.IMAGE_REQUEST) {
            ArrayList<String> returnValue = data.getStringArrayListExtra(Pix.IMAGE_RESULTS);
            assert returnValue != null;
            ImageUri = Uri.fromFile(new File(returnValue.get(0)));
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                Boolean ImageAdded = Util.setImageBitmap(ImageUri, getContext(), ivProfileImage);
                if (ImageAdded) {
                    if (uploadTask != null && uploadTask.isInProgress()) {
                        Util.addToast(ProfileFragment.this.getContext(), "Upload In Progressing");
                    } else {
                        profilePresenter.requestUploadImage(getActivity(), ImageUri);
//                      uploadImage();
                    }
                }
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NotNull String[] permissions, @NotNull int[] grantResults) {
        Log.d(TAG, "onActivityResult: " + "4050");
        if (requestCode == PermUtil.REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS) {
            // If request is cancelled, the result arrays are empty.
            Log.d(TAG, "onActivityResult: " + "6070");
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Pix.start(ProfileFragment.this, Options.init().setRequestCode(Util.IMAGE_REQUEST));
            } else {
                Util.addToast(getContext(), "Approve permissions to open Pix ImagePicker");
            }
        } else {
            Log.d(TAG, "onActivityResult: " + "100000");
        }
    }


    @Override
    public void onSuccess(User user) {
        if (user!= null){
            profileProgressBar.setVisibility(View.GONE);
            tvUserName.setText(user.getUserName());
            tvEmail.setText(firebaseUser.getEmail());
            tvBio.setText(user.getBio());
            tvGender.setText(user.getGender());
            tvCountry.setText(user.getCountry());
        }
    }

    @Override
    public void onFailed(String message) {
        profileProgressBar.setVisibility(View.GONE);
        Log.d(TAG, "onFailed: " + message);
        Toast.makeText(getContext(), "Some Thing Wrong Check Your Connection", Toast.LENGTH_LONG).show();
    }

    @Override
    public void imageUploadedSuccess() {
        Toast.makeText(getContext(), "Image Uploaded Successfully", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onResetPasswordSuccess(String message) {
        profileProgressBar.setVisibility(View.GONE);
        Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onResetPasswordFailed(String message) {
        profileProgressBar.setVisibility(View.GONE);
        Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
    }


}