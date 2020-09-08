package com.example.click.ui.Registration.Model;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class Country {

	@SerializedName("country_code")
	private String countryCode;

	@SerializedName("capital")
	private String capital;

	@SerializedName("timezones")
	private List<String> timezones;

	@SerializedName("name")
	private String name;

	@SerializedName("latlng")
	private List<String> latlng;

	public Country(String countryCode, String capital, List<String> timezones, String name, List<String> latlng) {
		this.countryCode = countryCode;
		this.capital = capital;
		this.timezones = timezones;
		this.name = name;
		this.latlng = latlng;
	}

	public Country(String countryCode, String capital, List<String> timezones, String name) {
		this.countryCode = countryCode;
		this.capital = capital;
		this.timezones = timezones;
		this.name = name;
	}


	public Country(String countryCode, String capital, String name) {
		this.countryCode = countryCode;
		this.capital = capital;
		this.name = name;
	}

	public void setCountryCode(String countryCode){
		this.countryCode = countryCode;
	}

	public String getCountryCode(){
		return countryCode;
	}

	public void setCapital(String capital){
		this.capital = capital;
	}

	public String getCapital(){
		return capital;
	}

	public void setTimezones(List<String> timezones){
		this.timezones = timezones;
	}

	public List<String> getTimezones(){
		return timezones;
	}

	public void setName(String name){
		this.name = name;
	}

	public String getName(){
		return name;
	}

	public List<String> getLatlng() {
		return latlng;
	}

	public void setLatlng(List<String> latlng) {
		this.latlng = latlng;
	}

	@Override
 	public String toString(){
		return 
			"Country{" +
			"country_code = '" + countryCode + '\'' + 
			",capital = '" + capital + '\'' + 
			",timezones = '" + timezones + '\'' + 
			",name = '" + name + '\'' + 
			",latlng = '" + latlng + '\'' + 
			"}";
		}
}