package com.vasyerp.selfcheckout.adapters.company_list;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.vasyerp.selfcheckout.R;
import com.vasyerp.selfcheckout.adapters.listeners.CompanyListClickListener;
import com.vasyerp.selfcheckout.databinding.ItemCompanyListBinding;
import com.vasyerp.selfcheckout.models.login.LogIn;

import java.util.ArrayList;

public class CompanyListAdapter extends RecyclerView.Adapter<CompanyListAdapter.MyViewHolder> {

    Context context;
    ArrayList<LogIn> logInArrayList;
    private CompanyListClickListener companyListClickListener;
    //private CartQtyFocusCallback cartQtyFocusCallback

    public CompanyListAdapter(Context context, ArrayList<LogIn> logInArrayList) {
        this.context = context;
        this.logInArrayList = logInArrayList;
    }

    public CompanyListClickListener getCompanyListClickListener() {
        return companyListClickListener;
    }

    public void setCompanyListClickListener(CompanyListClickListener companyListClickListener) {
        this.companyListClickListener = companyListClickListener;
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
        holder.itemComListBinding.tvComNameItem.setText(logInArrayList.get(holder.getAbsoluteAdapterPosition()).getBranchName());
        holder.itemComListBinding.tvComAddressItem.setText(logInArrayList.get(holder.getAbsoluteAdapterPosition()).getAddress());
        //Picasso.get().load(R.drawable.m1).into(holder.itemComListBinding.ivComLogoItem);
        String imgPath = logInArrayList.get(holder.getAbsoluteAdapterPosition()).getLogoPrefix() + logInArrayList.get(holder.getAbsoluteAdapterPosition()).getLogo();
        Picasso.get()
                .load(imgPath)
                .resize(2048, 2048)
                .onlyScaleDown()
                .into(holder.itemComListBinding.ivComLogoItem);
        holder.itemComListBinding.tvComBranchId.setText(logInArrayList.get(holder.getAbsoluteAdapterPosition()).getBranchId());
        holder.itemComListBinding.tvComCompanyId.setText(logInArrayList.get(holder.getAbsoluteAdapterPosition()).getCompanyId());
        //holder.itemComListBinding.tvComNameItem.setText(logInArrayList.get(holder.getAbsoluteAdapterPosition()).getCompanyName());

        holder.itemComListBinding.itemCardView.setOnClickListener(v -> {
            Log.e("TAG", "onClick: " + holder.itemComListBinding.tvComCompanyId.getText().toString());
            Log.e("TAG", "onClick: " + holder.itemComListBinding.tvComBranchId.getText().toString());
            companyListClickListener.setLoginData(holder.itemComListBinding.tvComCompanyId.getText().toString(), holder.itemComListBinding.tvComBranchId.getText().toString());
        });
    }

    @Override
    public int getItemCount() {
        return logInArrayList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        ItemCompanyListBinding itemComListBinding;

        public MyViewHolder(@NonNull ItemCompanyListBinding itemView) {
            super(itemView.getRoot());
            this.itemComListBinding = itemView;
        }
    }
}
