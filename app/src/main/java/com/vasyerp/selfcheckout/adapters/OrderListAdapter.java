package com.vasyerp.selfcheckout.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.vasyerp.selfcheckout.R;
import com.vasyerp.selfcheckout.databinding.ItemOrderListBinding;
import com.vasyerp.selfcheckout.models.OrderListModel;

import java.util.ArrayList;

public class OrderListAdapter extends RecyclerView.Adapter<OrderListAdapter.MyViewHolder> {

    Context context;
    ArrayList<OrderListModel> orderListModelArrayList;

    public OrderListAdapter(Context context, ArrayList<OrderListModel> orderListModelArrayList) {
        this.context = context;
        this.orderListModelArrayList = orderListModelArrayList;
    }

    @NonNull
    @Override
    public OrderListAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemOrderListBinding itemOrderListBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_order_list, parent, false);
        return new MyViewHolder(itemOrderListBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderListAdapter.MyViewHolder holder, int position) {
        holder.itemOrderListBinding.tvItemOrderSrNo.setText(String.valueOf(position + 1));
        holder.itemOrderListBinding.tvItemOrderInv.setText(orderListModelArrayList.get(holder.getAbsoluteAdapterPosition()).getStrOrderInv());
        holder.itemOrderListBinding.tvItemOrderDate.setText(orderListModelArrayList.get(holder.getAbsoluteAdapterPosition()).getStrOrderDate());
        holder.itemOrderListBinding.tvItemOrderTotal.setText(String.valueOf(orderListModelArrayList.get(holder.getAbsoluteAdapterPosition()).getOrderTotal()));

        holder.itemOrderListBinding.llItemOrder.setOnClickListener(v -> {
            Toast.makeText(context, "Click on " + holder.itemOrderListBinding.tvItemOrderInv.getText().toString(), Toast.LENGTH_SHORT).show();
            Log.e("TAG", "onBindViewHolder: ");
        });
    }

    @Override
    public int getItemCount() {
        return orderListModelArrayList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        ItemOrderListBinding itemOrderListBinding;

        public MyViewHolder(@NonNull ItemOrderListBinding itemView) {
            super(itemView.getRoot());
            this.itemOrderListBinding = itemView;
        }
    }
}
