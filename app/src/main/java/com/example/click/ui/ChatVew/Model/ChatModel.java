package com.example.click.ui.ChatVew.Model;

import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.example.click.R;
import com.example.click.ui.ChatVew.Presenter.ChatContract;
import com.example.click.ui.ChatVew.View.ChatActivity;
import com.example.click.ui.ChatVew.View.ChatAdapter;
import com.example.click.ui.Notifications.Client;
import com.example.click.ui.Notifications.Data;
import com.example.click.ui.Notifications.MyResponse;
import com.example.click.ui.Notifications.Sender;
import com.example.click.ui.Notifications.Token;
import com.example.click.ui.messages.ApiServiese;
import com.example.click.ui.profile.Model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChatModel implements ChatContract.ChatModel {
    private FirebaseUser firebaseUser;
    private DatabaseReference databaseReference;
    private ChatContract.ChatListener chatListener;
    private List<ChatDataModel> chatDataModelList;
    public static Boolean notify =false;
    private ApiServiese apiServiese;
    private ValueEventListener seenListener;

    public ChatModel(ChatContract.ChatListener chatListener) {
        this.chatListener = chatListener;
        apiServiese = Client.getClient("https://fcm.googleapis.com/").create(ApiServiese.class);

    }

    @Override
    public void getUserData(String userId) {
        firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        databaseReference= FirebaseDatabase.getInstance().getReference("Users").child(userId);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user=dataSnapshot.getValue(User.class);
                assert user != null;
                //TODO fix error
                chatListener.ChatListenerSuccess(user.getUserName(),user.getImageUrl());
                readMessage(firebaseUser.getUid(),userId);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                chatListener.ChatListenerFailed(databaseError.getMessage());
            }
        });

    }

    @Override
    public void readMessage(String myId, String userId) {
        chatDataModelList =new ArrayList<>();
        databaseReference = FirebaseDatabase.getInstance().getReference("Chats");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                chatDataModelList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    ChatDataModel chatDataModel =snapshot.getValue(ChatDataModel.class);
                    assert chatDataModel != null;
                    if (chatDataModel.getReceiver().equals(myId) && chatDataModel.getSender().equals(userId) ||
                            chatDataModel.getReceiver().equals(userId) && chatDataModel.getSender().equals(myId)){
                        chatDataModelList.add(chatDataModel);
                    }
                    chatListener.readMessageSuccess(chatDataModelList);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                chatListener.ChatListenerFailed(databaseError.getMessage());
//                Log.d(TAG, "onCancelled Read Message: "+databaseError);
            }
        });
    }

    @Override
    public void sendMessage(String sender,String receiver,String message,String userId) {
        DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference();

        HashMap<String,Object> hashMap=new HashMap<>();
        hashMap.put("sender",sender);
        hashMap.put("receiver",receiver);
        hashMap.put("message",message);
        hashMap.put("isseen",false);
        databaseReference.child("Chats").push().setValue(hashMap);
//        etMessage.setText("");


        //add user to chat view Activity
        final DatabaseReference reference = FirebaseDatabase.getInstance()
                .getReference("ChatList").child(firebaseUser.getUid()).child(userId);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (!dataSnapshot.exists()){
                    reference.child("id").setValue(userId);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        final String msg=message;

        databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user=dataSnapshot.getValue(User.class);
                if (notify){
                    assert user != null;
                    sendNotification(receiver,user.getUserName(),msg,userId);
                }
                notify=false;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void sendNotification(String receiver, String userName, String msg,String userId) {
        DatabaseReference tokens = FirebaseDatabase.getInstance().getReference("Tokens");
        Query query =tokens.orderByKey().equalTo(receiver);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Token token =snapshot.getValue(Token.class);
                    Data data =new Data(firebaseUser.getUid(),R.drawable.ic_person_24dp,
                            userName+": "+msg,"New Message",userId);

                    assert token != null;
                    Sender sender = new Sender(data,token.getToken());

                    apiServiese.sendNotifcation(sender)
                            .enqueue(new Callback<MyResponse>() {
                                @Override
                                public void onResponse(@NotNull Call<MyResponse> call, Response<MyResponse> response) {
                                    if (response.code() == 200){
                                        assert response.body() != null;
                                        Log.d("sendNotificatoin", "onResponse: "+response.body().success+"");
                                        if (response.body().success!=1){
                                            chatListener.ChatListenerFailed("Failed...");
                                        }
                                    }
                                }

                                @Override
                                public void onFailure(@NotNull Call<MyResponse> call, @NotNull Throwable t) {
                                    Log.d("sendNotificatoin", "onFail: "+t.getMessage());
                                }
                            });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d("sendNotificatoin", "onCancel: "+databaseError);
            }
        });
    }

    @Override
    public void getMessageStatus(String userId) {
        databaseReference = FirebaseDatabase.getInstance().getReference("Chats");
        seenListener = databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    ChatDataModel chatDataModel =snapshot.getValue(ChatDataModel.class);
                    assert chatDataModel != null;
                    if (chatDataModel.getReceiver().equals(firebaseUser.getUid()) &&
                            chatDataModel.getSender().equals(userId)){
                        HashMap<String,Object> hashMap =new HashMap<>();
                        hashMap.put("isseen",true);
                        snapshot.getRef().updateChildren(hashMap);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
