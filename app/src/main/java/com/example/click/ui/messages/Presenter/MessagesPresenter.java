package com.example.click.ui.messages.Presenter;

import com.example.click.ui.messages.Model.MessagesModel;
import com.example.click.ui.profile.Model.User;

import java.util.List;

public class MessagesPresenter implements MessagesContract.MessagesPresenter , MessagesContract.MessagesListener {

    MessagesContract.MessagesModel messagesModel;
    MessagesContract.MessagesView messagesView;

    public MessagesPresenter(MessagesContract.MessagesView messagesView) {
        this.messagesView = messagesView;
        messagesModel =new MessagesModel(this);
    }

    @Override
    public void requestListMessages() {
        messagesModel.getMessagesList();
    }

    @Override
    public void onListComplete(List<User> userList) {
        messagesView.onSuccess(userList);
    }

    @Override
    public void onListUncompleted(String message) {
        messagesView.onFail(message);
    }
}
