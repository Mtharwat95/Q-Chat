package com.example.click.ui.Discover.View;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.afollestad.materialdialogs.MaterialDialog;
import com.example.click.Home;
import com.example.click.Nutil.Constants;
import com.example.click.R;
import com.example.click.ui.Discover.Presenter.DiscoverContract;
import com.example.click.ui.Discover.Presenter.DiscoverPresenter;
import com.example.click.ui.Registration.Adapters.CountriesAdapter;
import com.example.click.ui.Registration.Model.Country;
import com.example.click.ui.profile.Model.User;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DiscoverFragment extends Fragment implements DiscoverContract.UsersView, CountriesAdapter.CountryAdapterListener {

    @BindView(R.id.progressBarUser)
    ProgressBar progressBarUser;
    @BindView(R.id.rvAllChatUsers)
    RecyclerView rvAllChatUsers;
    @BindView(R.id.filter)
    ImageView filter;
    @BindView(R.id.rlTopDiscoverBar)
    RelativeLayout rlTopDiscoverBar;

    private DiscoverAdapter discoverAdapter;
    private DiscoverPresenter discoverPresenter;
    private View root;
    private PopupWindow mPopupWindow;
    private CheckBox noFilter, onlineFilter, allCountryFilter;
    private TextView tvselectedCountry;
    private ImageView countrydialog;
    private RadioGroup genderFilter;
    private RadioButton sex;
    private ImageView search;
    private String onlineState,selectedCountry,gender;
    private MaterialDialog dialog;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_discover, container, false);
        ButterKnife.bind(this, root);

        discoverPresenter = new DiscoverPresenter(this);
        progressBarUser.setVisibility(View.VISIBLE);
        discoverPresenter.requestAllUsers();
        Home.userStatus("online");


        return root;
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mPopupWindow!=null){
            mPopupWindow.dismiss();
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        Home.userStatus("online");
    }

    @OnClick(R.id.filter)
    void onFilter() {
//        filter(Objects.requireNonNull(getActivity()), root);

        intiPoupWindow(Objects.requireNonNull(getActivity()));
    }

    @Override
    public void onSuccess(List<User> userList) {
        progressBarUser.setVisibility(View.GONE);
        discoverAdapter = new DiscoverAdapter(getContext(), userList);
        rvAllChatUsers.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));

        rvAllChatUsers.setAdapter(discoverAdapter);
    }

    @Override
    public void onFilterSuccess(List<User> userList) {
        progressBarUser.setVisibility(View.GONE);
        mPopupWindow.dismiss();
        discoverAdapter = new DiscoverAdapter(getContext(), userList);
        rvAllChatUsers.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));

        rvAllChatUsers.setAdapter(discoverAdapter);
    }


    @Override
    public void onFailed(String message) {
        progressBarUser.setVisibility(View.GONE);
        Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
    }

    @SuppressLint("RtlHardcoded")
    private void filter(final Activity context, View root) {
//        intiPoupWindow(context);
        noFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (noFilter.isChecked()){
                    onlineFilter.setChecked(false);
                    allCountryFilter.setChecked(false);
                    genderFilter.clearCheck();
//                    discoverPresenter.requestAllUsers();
//                    progressBarUser.setVisibility(View.VISIBLE);
                }
            }
        });
        countrydialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    showCountryDialog(context);
                }
            }
        });
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBarUser.setVisibility(View.VISIBLE);
                onlineState = "offline";
                selectedCountry = "AllCountries";
                gender = "Both";
                if (noFilter.isChecked()){
                    discoverPresenter.requestAllUsers();
                    mPopupWindow.dismiss();
                }else {

                    if (onlineFilter.isChecked()){
                        onlineState = "online";
                    }else {
                        onlineState = "offline";
                    }

                    if (allCountryFilter.isChecked()){
                        selectedCountry = "AllCountries";
                    }else if (!allCountryFilter.isChecked() && !tvselectedCountry.getText().equals("Select Country")){
                        selectedCountry = tvselectedCountry.getText().toString();
                    }

                    int id;
                    id = genderFilter.getCheckedRadioButtonId();
                    Log.d("dataaaId", "onClick: "+id);
                    if (id != -1){
                        if (id==2131230980){
                            gender = "Both";
                        }else {
                            sex = root.findViewById(id);
                            gender = sex.getText().toString();
                        }
                    }


                    Log.d("Dataaaa", "onClick: "+onlineState+"--"+selectedCountry+"--"+gender);
                    discoverPresenter.requestSomeUser(onlineState,selectedCountry,gender);
                    mPopupWindow.dismiss();
                }


            }
        });

    }

    private void intiPoupWindow(Activity context){
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        assert inflater != null;
        @SuppressLint("InflateParams")
        View customView = inflater.inflate(R.layout.filter_layout, null);
        mPopupWindow = new PopupWindow(
                customView,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        mPopupWindow.showAtLocation(root, Gravity.NO_GRAVITY, Gravity.END, rlTopDiscoverBar.getHeight()+35);

        if (Build.VERSION.SDK_INT >= 21) {
            mPopupWindow.setElevation(5.0f);
        }

        noFilter = customView.findViewById(R.id.NoFilter);
        onlineFilter =customView.findViewById(R.id.onlineFilter);
        allCountryFilter = customView.findViewById(R.id.countryFilterAll);
        genderFilter = customView.findViewById(R.id.radioGroupGender);
        search = customView.findViewById(R.id.searchFilter);
        tvselectedCountry = customView.findViewById(R.id.tvSelectedCountry);
        countrydialog = customView.findViewById(R.id.ivShowDialog);

        filter(context,customView);
    }


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void showCountryDialog(Context context) {
        dialog = new MaterialDialog.Builder(context)
                .customView(R.layout.dialog_country, false)
                .negativeText(R.string.select_country)
                .show();
        View view = dialog.getCustomView();
        assert view != null;
        RecyclerView recyclerView = view.findViewById(R.id.country_recycler);
        SearchView searchEditText = view.findViewById(R.id.et_search);
        searchEditText.setOnClickListener(v -> searchEditText.setIconified(false));

        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.addItemDecoration(new DividerItemDecoration(context, DividerItemDecoration.VERTICAL));
        CountriesAdapter adapter = new CountriesAdapter(Constants.getAllCountries(loadJSONFromAsset()), dialog, this);
        recyclerView.setAdapter(adapter);

        searchEditText.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                adapter.getFilter().filter(query);

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                adapter.getFilter().filter(newText);
                return true;
            }
        });
        dialog.getBuilder().onNegative((dialog1, which) ->
                dialog.dismiss()
        );

    }


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private String loadJSONFromAsset() {
        String json = null;
        try {
            InputStream is = getActivity().getAssets().open("json.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, StandardCharsets.UTF_8);
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }





    @Override
    public void onCountrySelected(Country country) {
        tvselectedCountry.setText(country.getName());
        dialog.dismiss();
    }
}