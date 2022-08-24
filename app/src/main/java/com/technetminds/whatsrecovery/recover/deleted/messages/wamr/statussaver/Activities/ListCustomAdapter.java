package com.technetminds.whatsrecovery.recover.deleted.messages.wamr.statussaver.Activities;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.technetminds.whatsrecovery.recover.deleted.messages.wamr.statussaver.R;

import java.util.ArrayList;

public class ListCustomAdapter extends BaseAdapter implements Filterable {

    ArrayList<CountryCodeModel> CountryCodeList;
    ItemClickListener itemClickListener;
    private Context context;
    private LayoutInflater inflater;
    private ArrayList<CountryCodeModel> filterList;
    private CustomFilter filter;

    ListCustomAdapter(Context c, ArrayList<CountryCodeModel> codelist, ItemClickListener itemClickListener) {
        this.context = c;
        this.CountryCodeList = codelist;
        this.filterList = CountryCodeList;
        this.itemClickListener = itemClickListener;
    }

    //TOTLA NUM OF MOVIES
    @Override
    public int getCount() {
        return CountryCodeList.size();
    }

    //GET A SINGLE MOVIE
    @Override
    public Object getItem(int position) {
        return CountryCodeList.get(position);
    }

    //IDENTITDIER
    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        if (inflater == null) {
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        //PERFORM INFLATION
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.code_search_model, null);
        }

        //BIND DATA TO VIEWS
        final ListViewHolder holder = new ListViewHolder(convertView);
        holder.country_code.setText(CountryCodeList.get(position).CountryCode);
        holder.country_name.setText(CountryCodeList.get(position).CountryName);

        holder.parent_lay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemClickListener.onItemClick(v, CountryCodeList.get(position).CountryCode);
            }
        });

        //RETURN A ROW
        return convertView;
    }

    @Override
    public Filter getFilter() {

        if (filter == null) {
            filter = new CustomFilter(filterList, this);
        }

        return filter;
    }

    public interface ItemClickListener {

        void onItemClick(View v, String Position);
    }

    public class ListViewHolder {

        TextView country_code, country_name;
        LinearLayout parent_lay;

        public ListViewHolder(View v) {

            country_code = v.findViewById(R.id.country_code);
            country_name = v.findViewById(R.id.country_name);
            parent_lay = v.findViewById(R.id.parent_lay);
        }

    }
}
