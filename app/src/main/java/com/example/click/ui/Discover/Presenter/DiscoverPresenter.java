package com.example.click.ui.Discover.Presenter;

import com.example.click.ui.Discover.Model.DiscoverModel;
import com.example.click.ui.profile.Model.User;

import java.util.List;

public class DiscoverPresenter implements DiscoverContract.UsersPresenter, DiscoverContract.UsersListener {

    private DiscoverContract.UsersModel usersModel;
    private DiscoverContract.UsersView usersView;

    public DiscoverPresenter(DiscoverContract.UsersView usersView) {
        usersModel =new DiscoverModel(this);
        this.usersView =usersView;
    }

    @Override
    public void requestAllUsers() {
        usersModel.displayUsers();
    }

    @Override
    public void requestSomeUser(String onlineState, String selectedCountry, String gender) {
        usersModel.displaySomeUser(onlineState,selectedCountry,gender);
    }

    @Override
    public void requestOnlineUser(String state) {
        usersModel.displayOnlineUsers(state);
    }

    @Override
    public void onListComplete(List<User> userList) {
        usersView.onSuccess(userList);
    }

    @Override
    public void onFilterComplete(List<User> userList) {
        usersView.onFilterSuccess(userList);
    }

    @Override
    public void onListUncompleted(String message) {
        usersView.onFailed(message);
    }
}
