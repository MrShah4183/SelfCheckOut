package com.vasyerp.selfcheckout.adapters.getproductdata;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.vasyerp.selfcheckout.R;
import com.vasyerp.selfcheckout.models.product.GetAllProducts;

import java.util.ArrayList;

public class AdapterGetProductData extends ArrayAdapter<GetAllProducts> {
    //extends ArrayAdapter<Customer>{
    //private ArrayList<Customer> customersList;
    ArrayList<GetAllProducts> getAllProducts;

    public AdapterGetProductData(@NonNull Context context, ArrayList<GetAllProducts> getAllProducts) {
        super(context, 0);
        this.getAllProducts = getAllProducts;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.raw_autocomplete_product, parent, false);
        }
        TextView tvProductName = convertView.findViewById(R.id.raw_tv_product_name);
        TextView tvProductId = convertView.findViewById(R.id.raw_tv_product_id);

        tvProductName.setText(getItem(position).getValue());
        tvProductId.setText(String.valueOf(getItem(position).getId()));

        return convertView;
        //return super.getView(position, convertView, parent);
    }

    @NonNull
    @Override
    public Filter getFilter() {
        //return super.getFilter();
        return filter;
    }

    private Filter filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();
            ArrayList<GetAllProducts> tempList = new ArrayList<>();

            if (constraint == null || constraint.length() == 0) {
                tempList.addAll(getAllProducts);
            } else {
                String filterPatter = constraint.toString().toLowerCase().trim();
                for (GetAllProducts allProductList : getAllProducts) {
                    if (allProductList.getValue().toLowerCase().trim().contains(filterPatter)) {
                        tempList.add(allProductList);
                    }
                    //else if (filterPatter.contains(String.valueOf(allProductList.getId()))) {
                    else if (String.valueOf(allProductList.getId()).contains(filterPatter)) {
                        tempList.add(allProductList);
                    }
                }
            }

            results.count = tempList.size();
            results.values = tempList;
            return results;
            //return null;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            /*clear();
            Log.d("TAG", "publishResults: "+((ArrayList<Customer>)results.values).size());
            if(((ArrayList<Customer>) results.values).get(0).getAdapterCallbackString().equals("ADD_NEW_USER_VIEW")){
                setItemType("ADD_NEW_USER_VIEW");
            }
            addAll(((ArrayList)results.values));
            notifyDataSetChanged();*/
            clear();
            Log.d("TAG", "publishResults: " + ((ArrayList<GetAllProducts>) results.values).size());
            addAll(((ArrayList) results.values));
            notifyDataSetChanged();
        }

        @Override
        public CharSequence convertResultToString(Object resultValue) {
            return resultValue.toString();
        }

    };
}
