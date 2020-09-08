package com.example.click.ui.Discover.Presenter;

import com.example.click.ui.profile.Model.User;

import java.util.List;

public interface DiscoverContract {

    interface UsersView{
        void onSuccess(List<User> userList);
        void onFilterSuccess(List<User> userList);
        void onFailed(String message);
    }

    interface UsersPresenter{
        void requestAllUsers();
        void requestSomeUser(String onlineState,String selectedCountry,String gender);
        void requestOnlineUser(String state);
    }

    interface UsersModel{
        void displayUsers();
        void displaySomeUser(String onlineState,String selectedCountry,String gender);
        void displayOnlineUsers(String state);
    }

    interface UsersListener{
        void onListComplete(List<User> userList);
        void onFilterComplete(List<User> userList);
        void onListUncompleted(String message);
    }
}
