package com.vasyerp.selfcheckout.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.vasyerp.selfcheckout.R;
import com.vasyerp.selfcheckout.databinding.ItemCompanyListBinding;
import com.vasyerp.selfcheckout.models.CompanyDetailsModel;

import java.util.ArrayList;

public class CompanyListAdapter extends RecyclerView.Adapter<CompanyListAdapter.MyViewHolder> {

    Context context;
    ArrayList<CompanyDetailsModel> companyDetailsModelArrayList;


    public CompanyListAdapter(Context context, ArrayList<CompanyDetailsModel> companyDetailsModelArrayList) {
        this.context = context;
        this.companyDetailsModelArrayList = companyDetailsModelArrayList;
    }

    @NonNull
    @Override
    public CompanyListAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemCompanyListBinding itemCompanyListBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_company_list, parent, false);
        return new MyViewHolder(itemCompanyListBinding);
        //return null;
    }

    @Override
    public void onBindViewHolder(@NonNull CompanyListAdapter.MyViewHolder holder, int position) {
        holder.itemComListBinding.tvComNameItem.setText(companyDetailsModelArrayList.get(holder.getAbsoluteAdapterPosition()).getCompanyName());
        holder.itemComListBinding.tvComAddressItem.setText(companyDetailsModelArrayList.get(holder.getAbsoluteAdapterPosition()).getCompanyAddress());
        //Picasso.get().load(R.drawable.m1).into(holder.itemComListBinding.ivComLogoItem);
        Picasso.get()
                .load(companyDetailsModelArrayList.get(holder.getAbsoluteAdapterPosition()).getCompanyIV())
                .resize(2048, 2048)
                .onlyScaleDown()
                .into(holder.itemComListBinding.ivComLogoItem);
        //holder.itemComListBinding.tvComNameItem.setText(companyDetailsModelArrayList.get(holder.getAbsoluteAdapterPosition()).getCompanyName());
    }

    @Override
    public int getItemCount() {
        return companyDetailsModelArrayList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        ItemCompanyListBinding itemComListBinding;

        public MyViewHolder(@NonNull ItemCompanyListBinding itemView) {
            super(itemView.getRoot());
            this.itemComListBinding = itemView;
        }
    }
}
