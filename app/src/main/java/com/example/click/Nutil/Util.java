package com.example.click.Nutil;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.click.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;

public class Util {

    public static int IMAGE_REQUEST = 1;


    public static boolean validateRegister(EditText userName, EditText Email, EditText password) {
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

        if (userName.getText().toString().isEmpty() || userName.getText().toString().length() < 3) {
            userName.requestFocus();
            userName.setError("Required");
            return false;
        } else if (Email.getText().toString().trim().isEmpty() || Email.getText().toString().length() < 5 ||
                !Email.getText().toString().trim().matches(emailPattern)) {
            Email.requestFocus();
            Email.setError("Your E-mail is incorrect");
            return false;
        } else if (password.getText().toString().isEmpty() || password.getText().toString().length() < 6) {
            Email.requestFocus();
            Email.setError("Password must be at least 6 character");
            return false;
        } else
            return true;

    }

    public static boolean validateLogin(EditText Email, EditText password) {
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

        if (Email.getText().toString().trim().isEmpty() ||
                Email.getText().toString().length() < 5 ||
                !Email.getText().toString().trim().matches(emailPattern)) {
            Email.requestFocus();
            Email.setError("Your E-mail is incorrect");
            return false;
        } else if (password.getText().toString().isEmpty() ||
                password.getText().toString().length() < 6) {
            Email.requestFocus();
            Email.setError("Password must be at least 6 character");
            return false;
        } else
            return true;

    }

    public static void startFragment(Fragment fragment, String tag, AppCompatActivity activity, int parent) {
        FragmentManager manager = activity.getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.add(parent, fragment, tag);
        transaction.commit();
    }

    public static void clearBackStack(AppCompatActivity activity) {
        FragmentManager manager = activity.getSupportFragmentManager();
        if (manager.getBackStackEntryCount() > 0) {
            FragmentManager.BackStackEntry first = manager.getBackStackEntryAt(0);
            manager.popBackStack(first.getId(), FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }
    }

    public static void clearBackStack(Fragment activity) {
        FragmentManager manager = activity.getFragmentManager();
        if (manager.getBackStackEntryCount() > 0) {
            FragmentManager.BackStackEntry first = manager.getBackStackEntryAt(0);
            manager.popBackStack(first.getId(), FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }
    }

    public static void startActivityUtil(Activity activity, Class destination) {
        Intent intent = new Intent(activity, destination);
        activity.startActivity(intent);
        activity.finish();
    }

    public static void addToast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }

    //TODO /* ================================================================= */
    public static void saveInSharedPref(String key, String value, Context context) {
        SharedPreferences appSharedPrefs = PreferenceManager
                .getDefaultSharedPreferences(context.getApplicationContext());
        SharedPreferences.Editor prefsEditor = appSharedPrefs.edit();
        prefsEditor.putString(key, value);
        prefsEditor.apply();
    }

    //----------------------------------------------------------------- //
    public static String getFromSharedPref(String key, Context context) {
        SharedPreferences appSharedPrefs = PreferenceManager
                .getDefaultSharedPreferences(context.getApplicationContext());

        String value = appSharedPrefs.getString(key, "");
        if (value != null && !value.isEmpty())
            return value;
        else
            return null;
    }

    //----------------------------------------------------------------------------------//
    /* ================================================================= */
    public static void saveListInShared(String key, List<String> value, Context context) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor mEdit1 = sp.edit();
        /* sKey is an array */
        mEdit1.putInt("Status_size", value.size());

        for (int i = 0; i < value.size(); i++) {
            mEdit1.putString(key, value.get(i));
        }

        mEdit1.apply();
    }

    //----------------------------------------------------------------- //
    public static List<String> getListFromSharedPref(String key, Context context) {
        SharedPreferences mSharedPreference1 = PreferenceManager.getDefaultSharedPreferences(context);
        List<String> dd = new ArrayList<>();

        int size = mSharedPreference1.getInt("Status_size", 0);

        for (int i = 0; i < size; i++) {
            dd.add(mSharedPreference1.getString(key, null));
        }
        return dd;
    } /* ============================================================== */

    //----------------------------------------------------------------------------------//
    public static void saveIntegerSharedPref(String key, Integer value, Context context) {
        SharedPreferences appSharedPrefs = PreferenceManager
                .getDefaultSharedPreferences(context.getApplicationContext());
        SharedPreferences.Editor prefsEditor = appSharedPrefs.edit();
        prefsEditor.putInt(key, value);
        prefsEditor.apply();
    }

    //----------------------------------------------------------------------------------//
    public static Integer getIntegerFromSharedPref(String key, Context context) {
        SharedPreferences appSharedPrefs = PreferenceManager
                .getDefaultSharedPreferences(context.getApplicationContext());

        Integer value = appSharedPrefs.getInt(key, 0);
        if (value != null)
            return value;
        else
            return 0;
    }

    /* ============================================================== */

    public static void clear(Context context, String key) {
        SharedPreferences appSharedPrefs = PreferenceManager
                .getDefaultSharedPreferences(context.getApplicationContext());
        SharedPreferences.Editor prefsEditor = appSharedPrefs.edit();
        prefsEditor.remove(key);
        prefsEditor.apply();
    }

    public static void clearAllSharedPref(Context context) {
        SharedPreferences appSharedPrefs = PreferenceManager
                .getDefaultSharedPreferences(context.getApplicationContext());
        SharedPreferences.Editor prefsEditor = appSharedPrefs.edit();
        prefsEditor.clear().apply();
    }

    public static void updateInSharedPref(String key, String value, Context context) {
        SharedPreferences appSharedPrefs = PreferenceManager
                .getDefaultSharedPreferences(context.getApplicationContext());
        SharedPreferences.Editor prefsEditor = appSharedPrefs.edit();
        prefsEditor = PreferenceManager.getDefaultSharedPreferences(context).edit();
        prefsEditor.putString(key, value);
        prefsEditor.apply();
    }

    public static void updateIntegerInSharedPref(String key, Integer value, Context context) {
        SharedPreferences appSharedPrefs = PreferenceManager
                .getDefaultSharedPreferences(context.getApplicationContext());
        SharedPreferences.Editor prefsEditor = appSharedPrefs.edit();
        prefsEditor = PreferenceManager.getDefaultSharedPreferences(context).edit();
        prefsEditor.putInt(key, value);
        prefsEditor.apply();
    }


    //----------------------------------------------------------------------------------//
    //TODO /* ============================================================== */


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static Boolean setImageBitmap(Uri imageUri, Context context, ImageView imageView) {
        if (!Objects.requireNonNull(imageUri.getPath()).isEmpty()) {
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), imageUri);
                imageView.setImageBitmap(Bitmap.createScaledBitmap(bitmap, imageView.getWidth(), imageView.getHeight(), false));
                return true;
            } catch (IOException e) {
                Log.d("openCamera", Objects.requireNonNull(e.getMessage()));
                e.printStackTrace();
                return false;
            }
        } else {
            Log.d("openCamera", ": 2");
            return false;
        }
    }

    public static String getFileExtention(Activity activity, Uri uri) {
        ContentResolver contentResolver = Objects.requireNonNull(activity).getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();

        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    //TODO //////////////////////////////////////////////////////////////////////
    @SuppressLint("RtlHardcoded")
    public static void showSortPopup(final Activity context, View root) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        assert inflater != null;
        View customView = inflater.inflate(R.layout.filter_layout,null);

        PopupWindow  mPopupWindow = new PopupWindow(
                customView,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );

        RelativeLayout rlTopDiscoverBar = context.findViewById(R.id.rlTopDiscoverBar);

        mPopupWindow.showAtLocation(root, Gravity.NO_GRAVITY,Gravity.END,rlTopDiscoverBar.getHeight());

        if(Build.VERSION.SDK_INT>=21){
            mPopupWindow.setElevation(5.0f);
        }

        CheckBox NoFilter,Online,Country;
        NoFilter = customView.findViewById(R.id.NoFilter);

        NoFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context,"No Filter",Toast.LENGTH_LONG).show();
                mPopupWindow.dismiss();
            }
        });
    }



}
