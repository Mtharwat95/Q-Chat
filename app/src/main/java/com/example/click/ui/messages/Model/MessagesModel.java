package com.example.click.ui.messages.Model;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.click.ui.ChatVew.Model.ChatList;
import com.example.click.ui.messages.Presenter.MessagesContract;
import com.example.click.ui.messages.View.MessagesAdapter;
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

public class MessagesModel implements MessagesContract.MessagesModel {

   FirebaseUser firebaseUser;
   DatabaseReference databaseReference;
   List<ChatList> userChatList;
   MessagesContract.MessagesListener messagesListener;
   List<User> userList;


    public MessagesModel(MessagesContract.MessagesListener messagesListener) {
        this.messagesListener = messagesListener;
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance()
                .getReference("ChatList").child(firebaseUser.getUid());
    }

    @Override
    public void getMessagesList() {
        assert firebaseUser != null;
        userChatList = new ArrayList<>();
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                userChatList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    ChatList chatList =snapshot.getValue(ChatList.class);
                    userChatList.add(chatList);
                }
                chatList();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                messagesListener.onListUncompleted(databaseError.getMessage());
//                progressBarMessages.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public void chatList() {
        userList = new ArrayList<>();
        databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                userList.clear();
                for (DataSnapshot snapshot:dataSnapshot.getChildren()){
                    User user = snapshot.getValue(User.class);
                    assert user != null;
                    for (ChatList chatList : userChatList){
                        if (user.getId().equals(chatList.getId())){
                            userList.add(user);
                        }
                    }
                }
                messagesListener.onListComplete(userList);
//                messagesAdapter = new MessagesAdapter(getContext(),userList);
//                rvAllChat.setLayoutManager(new LinearLayoutManager(getContext()));
//                rvAllChat.setAdapter(messagesAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
//                progressBarMessages.setVisibility(View.GONE);
                messagesListener.onListUncompleted(databaseError.getMessage());
            }
        });
    }
}
