package com.example.click.ui.Registration.Adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.example.click.Nutil.Constants;
import com.example.click.R;
import com.example.click.ui.Registration.Model.Country;

import java.util.ArrayList;
import java.util.List;

public class CountriesAdapter extends RecyclerView.Adapter<CountriesAdapter.MyViewHolder> implements Filterable {

    private List<Country> allCountries;
    private MaterialDialog dialog;
    private CountryAdapterListener countryFilterlistener;
    private List<Country> mFilteredList;
    String TAG="CountriesAdapter";

    public CountriesAdapter(List<Country> countries, MaterialDialog dialog, CountryAdapterListener countryFilterlistener) {
        this.allCountries = countries;
        this.dialog = dialog;
        this.countryFilterlistener = countryFilterlistener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.country_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.name.setText(getItem(position).getName());

/*
        holder.llCountryRow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onBindViewHolder: "+getItem(position).getName()+" ");
                //            Constants.selectedCountry.set(allCountries.get(position));
                Constants.selectedCountry.setName(getItem(position).getName());
//            Constants.selectedCountry.setDialCode(getItem(position).getDialCode());
//            Constants.selectedCountry.setCode(getItem(position).getCode());
//            Constants.selectedCountry.setCoor(getItem(position).getCoor());

                dialog.dismiss();
            }
        });
*/

        holder.itemView.setOnClickListener(v -> {
            Log.d(TAG, "onBindViewHolder: "+getItem(position).getName()+" ");
//            Constants.selectedCountry.set(allCountries.get(position));
//            Constants.selectedCountry.setName(getItem(position).getName());
//            Constants.selectedCountry.setDialCode(getItem(position).getDialCode());
//            Constants.selectedCountry.setCode(getItem(position).getCode());
//            Constants.selectedCountry.setCoor(getItem(position).getCoor());
            dialog.dismiss();
        });
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        LinearLayout llCountryRow;

        MyViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.country_name);
            llCountryRow = itemView.findViewById(R.id.llCountryRow);
            name.setOnClickListener(
                    view -> {
                        countryFilterlistener.onCountrySelected(getItem(getAdapterPosition()));
                    });

        }
    }

    /*   @Override
       public Filter getFilter() {
          // if(filter==null)
          // {
               filter=new CustomFilter(filteredCountry,this);
           Log.d("mmmmmmmmm", "getFilter: ");
           //}

           return filter;
       }
       */

    @Override
    public Filter getFilter() {
        return new Filter() {

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                final FilterResults oReturn = new FilterResults();
                final ArrayList<Country> results = new ArrayList<>();

                if (mFilteredList == null)
                    mFilteredList = allCountries;

                if (constraint != null) {
                    if ((mFilteredList != null) && (mFilteredList.size() > 0)) {
                        for (final Country country : mFilteredList) {
                            if ((country.getName().toLowerCase().contains(constraint.toString().toLowerCase())))
                                results.add(country);
                        }
                    }
                    oReturn.count = results.size();
                    oReturn.values = results;
                } else {
                    oReturn.count = allCountries.size();
                    oReturn.values = allCountries;
                }
                return oReturn;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                allCountries = (List<Country>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    public interface CountryAdapterListener {
        void onCountrySelected(Country country);
    }

    @Override
    public int getItemCount() {
        return allCountries.size();
    }

    public Country getItem(int position) {
        return allCountries.get(position);
    }

}
