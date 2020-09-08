package com.example.click.ui.messages.View;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.click.Home;
import com.example.click.R;
import com.example.click.ui.ChatVew.Model.ChatList;
import com.example.click.ui.Notifications.Token;
import com.example.click.ui.messages.Presenter.MessagesContract;
import com.example.click.ui.messages.Presenter.MessagesPresenter;
import com.example.click.ui.profile.Model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MessagesFragment extends Fragment implements MessagesContract.MessagesView {

    @BindView(R.id.SearchView) SearchView ivSearch;
    @BindView(R.id.rvAllChat) RecyclerView rvAllChat;
    @BindView(R.id.progressBarMessages) ProgressBar progressBarMessages;
    @BindView(R.id.etSearchView) EditText etSearchView;
    private MessagesAdapter messagesAdapter;
    private List<ChatList> userChatList;
    private List<String> userListString;
    private List<User> userList;
    String TAG = "MessageFragment";
    View root;

    private FirebaseUser firebaseUser;
//    private DatabaseReference databaseReference;
    MessagesPresenter messagesPresenter;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_messages, container, false);
        ButterKnife.bind(this, root);
        progressBarMessages.setVisibility(View.VISIBLE);
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        messagesPresenter = new MessagesPresenter(this);
        messagesPresenter.requestListMessages();
        updateToken(FirebaseInstanceId.getInstance().getToken());

        return root;
    }


    @Override
    public void onStart() {
        super.onStart();
        Home.userStatus("online");
    }

    @Override
    public void onResume() {
        super.onResume();
        Home.userStatus("online");
    }



    private void updateToken(String token){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Tokens");
        Token token1 = new Token(token);
        reference.child(firebaseUser.getUid()).setValue(token1);
    }

    @OnClick(R.id.SearchView)
    void serchClick() {
        etSearchView.setVisibility(View.VISIBLE);
        ivSearch.setVisibility(View.GONE);
        etSearchView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                searchUser(charSequence.toString().toLowerCase());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private void searchUser(String s) {
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        Query query = FirebaseDatabase.getInstance().
                getReference("Users").orderByChild("username").startAt(s).endAt(s + "\uf8ff");

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                userList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    User user = snapshot.getValue(User.class);

                    assert user != null;
                    assert firebaseUser != null;
                    if (!user.getId().equals(firebaseUser.getUid())) {
                        userList.add(user);
                    }
                }

                messagesAdapter = new MessagesAdapter(getContext(), userList);
                rvAllChat.setAdapter(messagesAdapter);
                ivSearch.setVisibility(View.VISIBLE);
                etSearchView.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onSuccess(List<User> userList) {
        progressBarMessages.setVisibility(View.GONE);
        messagesAdapter = new MessagesAdapter(getContext(),userList);
        rvAllChat.setLayoutManager(new LinearLayoutManager(getContext()));
        rvAllChat.setAdapter(messagesAdapter);
    }

    @Override
    public void onFail(String message) {
        progressBarMessages.setVisibility(View.GONE);
        Toast.makeText(getContext(),message,Toast.LENGTH_LONG).show();
    }


/*    private void getUsersMessages() {
        progressBarMessages.setVisibility(View.VISIBLE);
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        userListString = new ArrayList<>();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Chats");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                userListString.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    ChatDataModel chatModel = snapshot.getValue(ChatDataModel.class);
                    assert chatModel != null;
                    assert firebaseUser != null;
                    if (chatModel.getSender().equals(firebaseUser.getUid())) {
                        userListString.add(chatModel.getReceiver());
                    } else if (chatModel.getReceiver().equals(firebaseUser.getUid())) {
                        userListString.add(chatModel.getSender());
                    }
                    readChats();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void readChats() {
        List<User> userList = new ArrayList<>();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                userList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    User user = snapshot.getValue(User.class);
                    // Display 1 user from Chats
                    for (String id : userListString) {
                        assert user != null;
                        if (user.getId().equals(id)) {
                            if (userList.size() != 0) {
                                for (User user1 : userList) {
                                    if (!user.getId().equals(user1.getId())) {
                                        userList.add(user);
                                    }
                                }
                            } else {
                                userList.add(user);
                            }
                        }
                    }
                }
                progressBarMessages.setVisibility(View.GONE);
                messagesAdapter = new MessagesAdapter(getContext(), userList);
                rvAllChat.setLayoutManager(new LinearLayoutManager(getContext()));
                rvAllChat.setAdapter(messagesAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d(TAG, "onCancelled: " + databaseError.getDetails());
            }
        });
    }*/


}