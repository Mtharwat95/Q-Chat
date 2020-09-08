package com.example.click.ui.profile.Model;

import android.app.Activity;
import android.app.ProgressDialog;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.click.Nutil.Util;
import com.example.click.ui.profile.Presenter.ProfileMainContract;
import com.example.click.ui.profile.View.ProfileFragment;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileModel implements ProfileMainContract.ProfileModel {

    private ProfileMainContract.Listener listener;
    private User user;
    FirebaseUser firebaseUser;
    DatabaseReference databaseReference;
    StorageReference storageReference;
    UploadTask uploadTask;
    FirebaseAuth firebaseAuth;

    public ProfileModel(ProfileMainContract.Listener listener) {
        this.listener = listener;

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        storageReference = FirebaseStorage.getInstance().getReference("uploads");
        databaseReference=FirebaseDatabase.getInstance().getReference("Users");

    }

    @Override
    public void uploadImage(Activity activity,Uri ImageUri) {

        final ProgressDialog progressDialog=new ProgressDialog(activity);
        progressDialog.setMessage("Uploading...");
        progressDialog.show();

        if (ImageUri != null){
            final StorageReference reference=storageReference.
                    child(System.currentTimeMillis()+"."+Util.getFileExtention(activity,ImageUri));
            uploadTask =reference.putFile(ImageUri);
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
                        databaseReference=FirebaseDatabase.getInstance().
                                getReference("Users").child(firebaseUser.getUid());

                        HashMap<String,Object> map=new HashMap<>();
                        map.put("imageURL",mUri);
                        databaseReference.updateChildren(map);
                        progressDialog.dismiss();
                        listener.imageUploaded();
                    }else {
//                        Util.addToast(activity,"Failed To Upload Image");
                        progressDialog.dismiss();
                        listener.onDataFailure("Failed To Upload Image");
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.d("ProfileModel", "onFailure: "+e.getMessage());
//                    Util.addToast(activity,"Check Your Network Connection");
                    progressDialog.dismiss();
                    listener.onDataFailure("Check Your Network Connection");
                }
            });
        }else {
//            Util.addToast(ProfileFragment.this.getContext(),"No Image Selected");
        }
    }

    @Override
    public void getProfileData(CircleImageView imageView,Activity activity) {
        assert firebaseUser != null;
        databaseReference = databaseReference.child(firebaseUser.getUid());
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                user = dataSnapshot.getValue(User.class);
                assert user != null;

//                Log.d("profile000", "onDataChange:---Presenter: " + dataSnapshot.getValue().toString()+"");
                listener.onDataSuccess(user,imageView,activity);
//                    updateView(user.getUserName(), user.getImageUrl());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                listener.onDataFailure(databaseError.getMessage());
//                Util.addToast(getContext(), "Some Thing Wrong Check Your Connection");
                Log.d("databaseError", "onCancelled: " + databaseError);
            }
        });
    }

    @Override
    public void ForgetPassword() {
        firebaseAuth =FirebaseAuth.getInstance();
        if (firebaseUser.getEmail() != null){
            firebaseAuth.sendPasswordResetEmail(firebaseUser.getEmail())
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                listener.resetPasswordSuccess("Check Yor E-Mail to Rest Password");
//                                Toast.makeText(getContext(),"Check Yor E-Mail to Rest Password",
//                                        Toast.LENGTH_LONG).show();
                            }else{
                                listener.resetPasswordFailed("Failed To Rest Password... Try Later");
//                                Util.addToast(getContext(), Objects.requireNonNull(task.getException()).getMessage());
                            }
                        }
                    });
        }
    }


}
