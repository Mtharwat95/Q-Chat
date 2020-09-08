package com.example.click.ui.ChatVew.Presenter;

import com.example.click.ui.ChatVew.Model.ChatDataModel;

import java.util.List;

public interface ChatContract {

    interface ChatView{
        void onUserDataGet(String userName,String Url);
        void onUserDataFail(String message);
        void onReadMessageSuccess(List<ChatDataModel> chatDataModelList);
    }

    interface ChatPresenter{
        void UserData(String userId);
        void requestMessageSend(String sender,String receiver,String message,String userId);
        void messageStatus(String userId);
    }

    interface ChatModel{
        void getUserData(String userId);
        void readMessage(String myId,String userId);
        void sendMessage(String sender,String receiver,String message,String userId);
        void sendNotification(String receiver, String userName, String msg,String userId);
        void getMessageStatus(String userId);
    }

    interface ChatListener{
        void ChatListenerSuccess(String userName,String Url);
        void ChatListenerFailed(String message);
        void readMessageSuccess(List<ChatDataModel> chatDataModelList);
    }
}
