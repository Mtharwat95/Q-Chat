package com.example.click.ui.ChatVew.View;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.click.Home;
import com.example.click.Nutil.Util;
import com.example.click.R;
import com.example.click.ui.ChatVew.Model.ChatDataModel;
import com.example.click.ui.ChatVew.Model.ChatModel;
import com.example.click.ui.ChatVew.Presenter.ChatContract;
import com.example.click.ui.ChatVew.Presenter.ChatPresenter;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChatActivity extends AppCompatActivity implements ChatContract.ChatView {

    @BindView(R.id.ivBackChatView) ImageView ivBack;
    @BindView(R.id.ivChatUSerImage) CircleImageView ivChatUSerImage;
    @BindView(R.id.tvChatUserName) TextView tvChatUserName;
    @BindView(R.id.rvChatView) RecyclerView rvChatView;
    @BindView(R.id.chatProgressLoad) ProgressBar chatProgressLoad;
    @BindView(R.id.etMessage) EditText etMessage;
    @BindView(R.id.ivSendMessage) ImageView ivSendMessage;

    FirebaseUser firebaseUser;
    DatabaseReference databaseReference;
    Intent intent;
    String userId;
    String TAG="ChatActivity -->: ";

    ChatAdapter chatAdapter;
    List<ChatDataModel> chatDataModelList;

    ValueEventListener seenListener;


    Boolean notify =false;
    ChatPresenter chatPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        ButterKnife.bind(this);
        intent=getIntent();
        userId=intent.getStringExtra("userId");
        firebaseUser=FirebaseAuth.getInstance().getCurrentUser();
        chatProgressLoad.setVisibility(View.VISIBLE);

        chatPresenter =new ChatPresenter(this);
        prepareView();
        chatPresenter.messageStatus(userId);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
    }

    void prepareView(){
        setRvChatView();
        chatProgressLoad.setVisibility(View.INVISIBLE);
        chatPresenter.UserData(userId);
        onSend();
    }

    @OnClick(R.id.ivBackChatView)
    void onBack(){
        Intent intent=new Intent(this,Home.class);
        startActivity(intent);
        finish();
    }

    @OnClick(R.id.ivSendMessage)
    void onSend() {
        if (!etMessage.getText().toString().equals("")){
            ChatModel.notify =true;
            chatPresenter.requestMessageSend(firebaseUser.getUid(),userId,etMessage.getText().toString(),userId);
            etMessage.setText("");
        }

    }

    void setRvChatView(){
        rvChatView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setStackFromEnd(true);
        rvChatView.setLayoutManager(linearLayoutManager);

    }

    @Override
    protected void onResume() {
        super.onResume();
        Home.userStatus("online");
        Util.saveInSharedPref("currentuser",userId,getApplicationContext());
    }

    @Override
    protected void onPause() {
        super.onPause();
        Home.userStatus("offline");
//        databaseReference.removeEventListener(seenListener);
    }

    @Override
    public void onUserDataGet(String userName, String Url) {

        tvChatUserName.setText(userName);
                if (!Url.equals("default")){
                    Glide.with(getApplicationContext())
                            .load(Url)
                            .into(ivChatUSerImage);
                }else {
                    ivChatUSerImage.setImageResource(R.drawable.ic_profile_24dp);
                }
    }

    @Override
    public void onUserDataFail(String message) {
        Toast.makeText(this,message,Toast.LENGTH_LONG).show();
    }

    @Override
    public void onReadMessageSuccess(List<ChatDataModel> chatDataModelList) {
        chatAdapter=new ChatAdapter(ChatActivity.this, chatDataModelList);
        rvChatView.setAdapter(chatAdapter);
    }
}
