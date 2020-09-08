package com.example.click.ui.messages.Presenter;

import com.example.click.ui.profile.Model.User;

import java.util.List;

public interface MessagesContract {

    interface MessagesView{
        void onSuccess(List<User> userList);
        void onFail(String message);
    }

    interface MessagesPresenter{
        void requestListMessages();
    }

    interface MessagesModel{
        void getMessagesList();
        void chatList();
    }

    interface MessagesListener{
        void onListComplete(List<User> userList);
        void onListUncompleted(String message);

    }
}
