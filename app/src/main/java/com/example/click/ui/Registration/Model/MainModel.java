package com.example.click.ui.Registration.Model;

import android.app.Activity;
import android.app.ProgressDialog;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.click.Nutil.Util;
import com.example.click.ui.Registration.Presenter.RegistrationMainContract;
import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Objects;

public class MainModel implements RegistrationMainContract.IModel {

    private RegistrationMainContract.Listener onLoginListener;
    private RegistrationMainContract.RegisterListener registerListener;

    private FirebaseUser firebaseUser;

    private String userId;
    private String TAG = "LoginModel";
    private DatabaseReference refrence;
    private StorageReference storageReference;
    private UploadTask uploadTask;

    public MainModel(RegistrationMainContract.Listener onLoginListener) {
        this.onLoginListener = onLoginListener;
    }

    public MainModel(RegistrationMainContract.RegisterListener registerListener) {
        this.registerListener = registerListener;
        refrence =FirebaseDatabase.getInstance().getReference("Users");
        storageReference = FirebaseStorage.getInstance().getReference("uploads");

    }

    @Override
    public void LoginGetData(Activity activity, String Email, String Password) {
        FirebaseAuth auth=FirebaseAuth.getInstance();
        auth.signInWithEmailAndPassword(Email,Password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()){
                        onLoginListener.onSuccess(Objects.requireNonNull(task.getResult()).toString());
                    }else {
                        onLoginListener.onFailure("User Dosen't Exist.... Pleas SignUp First");
                        Log.d(TAG, "LoginGetData: "+task.getException().toString());
                    }
                });
    }

    @Override
    public void Register(Activity activity, String userName, String Email, String Password,Uri imageUri,String gender,String country,String bio) {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        auth.createUserWithEmailAndPassword(Email, Password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        assert firebaseUser != null;
                        firebaseUser = auth.getCurrentUser();
                        assert firebaseUser != null;
                        userId = firebaseUser.getUid();
                        refrence = FirebaseDatabase.getInstance().
                                getReference("Users").child(userId);
                        HashMap<String, String> hashMap = new HashMap<>();
                        hashMap.put("id", userId);
                        hashMap.put("username", userName);
                        hashMap.put("imageURL", "default");
                        hashMap.put("status", "offline");
                        hashMap.put("gender", gender);
                        hashMap.put("country", country);
                        hashMap.put("bio", bio);
//                        GeoFire location = new GeoFire(refrence);
//                        location.setLocation(userId,new GeoLocation("27","30"));
                        refrence.setValue(hashMap).addOnCompleteListener(task1 -> {
                            if (task1.isSuccessful()) {
                                //TODO Go To Home
                                Log.d("ProfileModel", "Register: "+task1.getResult()+" ");
                                registerListener.Registered(activity,imageUri);
                            }else {
                                registerListener.NotRegistered();
                            }
                        });
                    } else {
                        registerListener.NotRegistered();
                        Log.d("task false: ", task.toString());
                    }
                });
    }

    @Override
    public void UploadImage(Activity activity, Uri image) {
        final ProgressDialog progressDialog=new ProgressDialog(activity);
        progressDialog.setMessage("Uploading...");
        progressDialog.show();

        if (image != null){
            final StorageReference reference=storageReference.
                    child(System.currentTimeMillis()+"."+ Util.getFileExtention(activity,image));
            uploadTask =reference.putFile(image);
            uploadTask.continueWithTask((Continuation<UploadTask.TaskSnapshot, Task<Uri>>) task -> {
                if (!task.isSuccessful()){
                    throw Objects.requireNonNull(task.getException());
                }

                return reference.getDownloadUrl();
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()){
                        Uri downloadUri=task.getResult();
                        assert downloadUri != null;
                        String mUri=downloadUri.toString();
                        refrence=FirebaseDatabase.getInstance().
                                getReference("Users").child(firebaseUser.getUid());

                        HashMap<String,Object> map=new HashMap<>();
                        map.put("imageURL",mUri);
                        refrence.updateChildren(map);
                        progressDialog.dismiss();
                        registerListener.uploadedSusses("Image Uploaded Success");
                    }else {
                        progressDialog.dismiss();
                        registerListener.uploadedFailed("Failed To Upload Image");
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.d("ProfileModel", "onFailure: "+e.getMessage());
//                    Util.addToast(activity,"Check Your Network Connection");
                    progressDialog.dismiss();
                    registerListener.uploadedFailed("Check Your Network Connection");
                }
            });
        }else {
            registerListener.uploadedSusses("No Image Selected");
        }
    }
}
