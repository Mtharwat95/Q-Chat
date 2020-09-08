package com.example.click.ui.ChatVew.Presenter;

import com.example.click.ui.ChatVew.Model.ChatDataModel;
import com.example.click.ui.ChatVew.Model.ChatModel;

import java.util.List;

public class ChatPresenter implements ChatContract.ChatPresenter,ChatContract.ChatListener {

    ChatContract.ChatModel chatModel;
    ChatContract.ChatView chatView;

    public ChatPresenter(ChatContract.ChatView chatView) {
        this.chatView = chatView;
        chatModel = new ChatModel(this);
    }

    @Override
    public void UserData(String userId) {
        chatModel.getUserData(userId);
    }

    @Override
    public void requestMessageSend(String sender,String receiver,String message,String userId) {
        chatModel.sendMessage(sender,receiver,message,userId);
    }

    @Override
    public void messageStatus(String userId) {
        chatModel.getMessageStatus(userId);
    }

    @Override
    public void ChatListenerSuccess(String userName,String Url) {
        chatView.onUserDataGet(userName,Url);
    }

    @Override
    public void ChatListenerFailed(String message) {
        chatView.onUserDataFail(message);
    }

    @Override
    public void readMessageSuccess(List<ChatDataModel> chatDataModelList) {
        chatView.onReadMessageSuccess(chatDataModelList);
    }
}
