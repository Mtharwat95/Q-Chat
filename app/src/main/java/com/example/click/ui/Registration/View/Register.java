package com.example.click.ui.Registration.View;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.example.click.Home;
import com.example.click.Nutil.Constants;
import com.example.click.Nutil.Util;
import com.example.click.R;
import com.example.click.ui.Registration.Adapters.CountriesAdapter;
import com.example.click.ui.Registration.Model.Country;
import com.example.click.ui.Registration.Presenter.MainPresenter;
import com.example.click.ui.Registration.Presenter.RegistrationMainContract;
import com.fxn.pix.Options;
import com.fxn.pix.Pix;
import com.fxn.utility.PermUtil;
import com.google.firebase.storage.StorageTask;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;


public class Register extends AppCompatActivity implements RegistrationMainContract.RegisterView, CountriesAdapter.CountryAdapterListener {

    @BindView(R.id.etNameRegister)
    EditText etNameRegister;
    @BindView(R.id.etEmailRegister)
    EditText etEmailRegister;
    @BindView(R.id.etPasswordRegister)
    EditText etPasswordRegister;
    @BindView(R.id.ProgressRegister)
    ProgressBar progressRegister;
    @BindView(R.id.ivRegisterProfileImage)
    CircleImageView ivRegisterProfileImage;
    @BindView(R.id.tvSelectedCountry)
    TextView tvSelectedCountry;
    @BindView(R.id.ivShowDialog)
    ImageView ivShowDialog;
    @BindView(R.id.radioGroupGender)
    RadioGroup radioGroupGender;

    RadioButton radioSex;
    String userName, email, password, gender, country,bio;
    @BindView(R.id.etBioRegister)
    EditText etBioRegister;
    private Uri ImageUri;
    StorageTask uploadTask;
    private MainPresenter presenter;
    MaterialDialog dialog;

    String TAG = "RegisterView";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        ButterKnife.bind(this);
        presenter = new MainPresenter(this);
    }

    @OnClick(R.id.ivShowDialog)
    void onShowDialg() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            showCountryDialog();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this, Login.class);
        startActivity(intent);
        finish();
    }

    @OnClick(R.id.btnJoinUs)
    void onRegister() {
        int selectedId;
        selectedId = radioGroupGender.getCheckedRadioButtonId();
        radioSex = findViewById(selectedId);
        if (Util.validateRegister(etNameRegister, etEmailRegister, etPasswordRegister)) {
            userName = etNameRegister.getText().toString();
            email = etEmailRegister.getText().toString();
            password = etPasswordRegister.getText().toString();
            gender = radioSex.getText().toString();
            country = tvSelectedCountry.getText().toString();
            bio = etBioRegister.getText().toString();
            progressRegister.setVisibility(View.VISIBLE);

            presenter.onRegisterClick(this, userName, email, password, ImageUri
                    , gender, country,bio);
        } else {
            Toast.makeText(Register.this, "All fields required", Toast.LENGTH_SHORT).show();
        }
    }

    @OnClick(R.id.ivRegisterProfileImage)
    void onImage() {
        Pix.start(Register.this, Options.init().setRequestCode(Util.IMAGE_REQUEST));
    }

    @OnClick(R.id.tvLogin)
    void onLogin() {
        Intent intent = new Intent(this, Login.class);
        startActivity(intent);
        finish();
    }

    @OnClick(R.id.ivBackLogin)
    void back() {
        Intent intent = new Intent(this, Login.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        assert data != null;
        if (resultCode == Activity.RESULT_OK && requestCode == Util.IMAGE_REQUEST) {
            ArrayList<String> returnValue = data.getStringArrayListExtra(Pix.IMAGE_RESULTS);
            assert returnValue != null;
            ImageUri = Uri.fromFile(new File(returnValue.get(0)));
            Log.d(TAG, "onActivityResult: "+ImageUri+" ");
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                Boolean ImageAdded = Util.setImageBitmap(ImageUri, this, ivRegisterProfileImage);
                if (ImageAdded) {
                    if (uploadTask != null && uploadTask.isInProgress()) {
                        Util.addToast(this, "Upload In Progressing");
                    }
                }
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NotNull String[] permissions, @NotNull int[] grantResults) {
        Log.d(TAG, "onActivityResult: " + "4050");
        if (requestCode == PermUtil.REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS) {
            // If request is cancelled, the result arrays are empty.
            Log.d(TAG, "onActivityResult: " + "6070");
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Pix.start(this, Options.init().setRequestCode(Util.IMAGE_REQUEST));
            } else {
                Util.addToast(this, "Approve permissions to open Pix ImagePicker");
            }
        } else {
            Log.d(TAG, "onActivityResult: " + "100000");
        }
    }


    @Override
    public void onRegisterSuccess(String message) {
        progressRegister.setVisibility(View.GONE);
        Intent intent = new Intent(this, Home.class);
        startActivity(intent);
        finish();
        Toast.makeText(Register.this, "User Created Successfully", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRegisterFailed(String message) {
        progressRegister.setVisibility(View.GONE);
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
//        Toast.makeText(Register.this,
//                "User Can't Created with this E-mail", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onUploadSuccess(String message) {
        Toast.makeText(Register.this, message, Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onUploadFailed(String message) {
        Toast.makeText(Register.this, message, Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onCountrySelected(Country country) {
        Log.d(TAG, "onCountrySelected: " + country.getName());
        Log.d(TAG, "onCountrySelected:Lat: " + country.getLatlng() + " ");
        tvSelectedCountry.setText(country.getName());
        dialog.dismiss();
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void showCountryDialog() {
        dialog = new MaterialDialog.Builder(this)
                .customView(R.layout.dialog_country, false)
                .negativeText(R.string.select_country)
                .show();
        View view = dialog.getCustomView();
        assert view != null;
        RecyclerView recyclerView = view.findViewById(R.id.country_recycler);
        SearchView searchEditText = view.findViewById(R.id.et_search);
        searchEditText.setOnClickListener(v -> searchEditText.setIconified(false));

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
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
    public String loadJSONFromAsset() {
        String json = null;
        try {
            InputStream is = getAssets().open("json.json");
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
}
