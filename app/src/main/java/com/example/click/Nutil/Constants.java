package com.example.click.Nutil;

import com.example.click.ui.Registration.Model.Country;

import java.util.List;

public class Constants {
    public static Country selectedCountry;
    public static List<Country> getAllCountries(String json) {
        return StorageRepository.getAllCountries(json);
    }

}
