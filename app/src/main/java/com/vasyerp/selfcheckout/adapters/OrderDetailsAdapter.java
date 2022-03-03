package com.vasyerp.selfcheckout.adapters;

import android.content.Context;
import android.view.LayoutInflater;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.vasyerp.selfcheckout.R;
import com.vasyerp.selfcheckout.databinding.ItemOrdersDetailsListBinding;
import com.vasyerp.selfcheckout.models.OrderDetailsModel;

import java.util.ArrayList;
import java.util.Locale;

public class OrderDetailsAdapter extends RecyclerView.Adapter<OrderDetailsAdapter.MyViewHolder> {

    Context context;
    ArrayList<OrderDetailsModel> orderDetailsModelArrayList;

    public OrderDetailsAdapter(Context context, ArrayList<OrderDetailsModel> orderDetailsModelArrayList) {
        this.context = context;
        this.orderDetailsModelArrayList = orderDetailsModelArrayList;
    }

    @NonNull
    @Override
    public OrderDetailsAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemOrdersDetailsListBinding itemOrdersDetailsListBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_orders_details_list, parent, false);
        return new MyViewHolder(itemOrdersDetailsListBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderDetailsAdapter.MyViewHolder holder, int position) {
        //holder.itemOrderListBinding.tvItemOrderSrNo.setText(String.valueOf(position + 1));
        holder.ordersDetailsListBinding.productName.setText(orderDetailsModelArrayList.get(holder.getAbsoluteAdapterPosition()).getProName());
        holder.ordersDetailsListBinding.productQty.setText(String.format(Locale.getDefault(), "%.2f", orderDetailsModelArrayList.get(holder.getAbsoluteAdapterPosition()).getProQty()));
        holder.ordersDetailsListBinding.productDiscount.setText(String.format(Locale.getDefault(), "%.2f", orderDetailsModelArrayList.get(holder.getAbsoluteAdapterPosition()).getProDisc()));
        holder.ordersDetailsListBinding.productNetAmount.setText(String.format(Locale.getDefault(), "%.2f", orderDetailsModelArrayList.get(holder.getAbsoluteAdapterPosition()).getProPrice()));
    }

    @Override
    public int getItemCount() {
        return orderDetailsModelArrayList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        ItemOrdersDetailsListBinding ordersDetailsListBinding;

        public MyViewHolder(@NonNull ItemOrdersDetailsListBinding itemView) {
            super(itemView.getRoot());
            this.ordersDetailsListBinding = itemView;
        }
    }
}
