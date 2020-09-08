package com.example.click.Nutil;

import android.util.Log;

import com.example.click.ui.Registration.Model.Country;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class StorageRepository {


    public static List<Country> getAllCountries(String json) {
        List<Country> result = new ArrayList<>();
        try {
            JSONArray arr = new JSONArray(json);


            for (int i = 0; i < arr.length(); i++) {
                JSONObject countryItem = arr.getJSONObject(i);
                List<String> latlng= Collections.singletonList(countryItem.getString("latlng"));
                Log.d("StorageRepository", "getAllCountries: "+countryItem.getString("latlng")+"");
                Country item = new Country(countryItem.getString("country_code"),
                        countryItem.getString("capital"),
                        Collections.singletonList(countryItem.getString("timezones")),
                        countryItem.getString("name"),
                        Collections.singletonList(countryItem.getString("latlng")));
                /*Country item = new Country(countryItem.getString("country_code"),
                        countryItem.getString("capital"),
                        countryItem.getString("name"));*/
                result.add(item);

            }
        } catch (JSONException e) {
            return result;
        }

        return result;
    }
}
