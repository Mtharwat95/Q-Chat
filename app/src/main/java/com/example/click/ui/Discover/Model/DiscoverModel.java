package com.example.click.ui.Discover.Model;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.click.ui.Discover.Presenter.DiscoverContract;
import com.example.click.ui.profile.Model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class DiscoverModel implements DiscoverContract.UsersModel {
    private List<User> userList;
    private DiscoverContract.UsersListener usersListener;
    private FirebaseUser firebaseUser;
    private DatabaseReference databaseReference;

    public DiscoverModel(DiscoverContract.UsersListener usersListener) {
        this.usersListener = usersListener;
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference("Users");
    }

    @Override
    public void displayUsers() {
        userList = new ArrayList<>();
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                userList.clear();
                for (DataSnapshot Snapshot : dataSnapshot.getChildren()) {
                    User user = Snapshot.getValue(User.class);
                    assert user != null;
                    assert firebaseUser != null;
                    if (user.getId() != null && !user.getId().equals(firebaseUser.getUid())) {
                        userList.add(user);

                    }
                }
                usersListener.onListComplete(userList);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                usersListener.onListUncompleted(databaseError.getMessage());
            }
        });
    }

    @Override
    public void displaySomeUser(String onlineState, String selectedCountry, String gender) {
        userList = new ArrayList<>();
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                userList.clear();
                for (DataSnapshot Snapshot : dataSnapshot.getChildren()) {
                    User user = Snapshot.getValue(User.class);
                    assert user != null;
                    assert firebaseUser != null;
                    if (user.getId() != null&&!user.getId().equals(firebaseUser.getUid())) {
                        if (onlineState.equals("online")) {

                            if (user.getStatus().equals(onlineState)
                                    && selectedCountry.equals("AllCountries")
                                    && gender.equals("Both")) {
                                userList.add(user);
                                Log.d("Casse:", "onDataChange: "+1);
                            } else if (user.getStatus().equals(onlineState)
                                    && selectedCountry.equals("AllCountries")
                                    && user.getGender().equals(gender)) {
                                userList.add(user);
                                Log.d("Casse:", "onDataChange: "+2);
                            } else if (user.getStatus().equals(onlineState)
                                    && user.getCountry().equals(selectedCountry)
                                    && gender.equals("Both")) {
                                userList.add(user);
                            } else if (user.getStatus().equals(onlineState)
                                    && user.getCountry().equals(selectedCountry)
                                    && user.getGender().equals(gender)) {
                                userList.add(user);
                                Log.d("Casse:", "onDataChange: "+3);
                            }
                        } else if (onlineState.equals("offline")) {
                            if (user.getStatus().equals(onlineState)
                                    && selectedCountry.equals("AllCountries")
                                    && gender.equals("Both")) {
                                userList.add(user);
                                Log.d("Casse:", "onDataChange: "+4);
                            } else if (user.getStatus().equals(onlineState)
                                    && selectedCountry.equals("AllCountries")
                                    && user.getGender().equals(gender)) {
                                userList.add(user);
                                Log.d("Casse:", "onDataChange: "+5);
                            } else if (user.getStatus().equals(onlineState)
                                    && user.getCountry().equals(selectedCountry)
                                    && gender.equals("Both")) {
                                userList.add(user);
                                Log.d("Casse:", "onDataChange: "+6);
                            } else if (user.getStatus().equals(onlineState)
                                    && user.getCountry().equals(selectedCountry)
                                    && user.getGender().equals(gender)) {
                                userList.add(user);
                                Log.d("Casse:", "onDataChange: "+7);
                            }
                        }
                    }
                }
                usersListener.onFilterComplete(userList);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                usersListener.onListUncompleted(databaseError.getMessage());
            }
        });
    }

    @Override
    public void displayOnlineUsers(String state) {
        userList = new ArrayList<>();
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                userList.clear();
                for (DataSnapshot Snapshot : dataSnapshot.getChildren()) {
                    User user = Snapshot.getValue(User.class);
                    assert user != null;
                    assert firebaseUser != null;
                    if (user.getId() != null) {
                        if (user.getStatus().equals(state)) {
                            userList.add(user);
                        }
//                        userList.add(user);
                   /*     if (!user.getId().equals(firebaseUser.getUid())) {
                            userList.add(user);
                        }*/
                    }
                }
                usersListener.onListComplete(userList);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                usersListener.onListUncompleted(databaseError.getMessage());
            }
        });
    }
}
