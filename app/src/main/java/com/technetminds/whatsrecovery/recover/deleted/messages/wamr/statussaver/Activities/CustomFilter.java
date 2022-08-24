package com.technetminds.whatsrecovery.recover.deleted.messages.wamr.statussaver.Activities;

import android.widget.Filter;

import java.util.ArrayList;

public class CustomFilter extends Filter {

    ArrayList<CountryCodeModel> filterList;
    ListCustomAdapter adapter;

    public CustomFilter(ArrayList<CountryCodeModel> filterList, ListCustomAdapter adapter) {
        this.filterList = filterList;
        this.adapter = adapter;
    }

    //FILTERING
    @Override
    protected FilterResults performFiltering(CharSequence constraint) {

        //RESULTS
        FilterResults results=new FilterResults();

        //VALIDATION
        if(constraint != null && constraint.length()>0)
        {

            //CHANGE TO UPPER FOR CONSISTENCY
            constraint=constraint.toString().toUpperCase();

            ArrayList<CountryCodeModel> filteredMovies=new ArrayList<>();

            //LOOP THRU FILTER LIST
            for(int i=0;i<filterList.size();i++)
            {
                //FILTER
                if(filterList.get(i).CountryName.toUpperCase().contains(constraint))
                {
                    filteredMovies.add(filterList.get(i));
                }
            }

            results.count=filteredMovies.size();
            results.values=filteredMovies;
        }else
        {
            results.count=filterList.size();
            results.values=filterList;
        }

        return results;
    }

    //PUBLISH RESULTS

    @Override
    protected void publishResults(CharSequence constraint, FilterResults results) {

        adapter.CountryCodeList = (ArrayList<CountryCodeModel>) results.values;
        adapter.notifyDataSetChanged();

    }
}
