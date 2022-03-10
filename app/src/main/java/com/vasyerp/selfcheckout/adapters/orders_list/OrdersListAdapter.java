package com.vasyerp.selfcheckout.adapters.orders_list;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.vasyerp.selfcheckout.R;
import com.vasyerp.selfcheckout.databinding.ItemOrderListBinding;
import com.vasyerp.selfcheckout.models.orderlist.OrderModel;
import com.vasyerp.selfcheckout.ui.orders_ui.OrderDetailsActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import lombok.SneakyThrows;

public class OrdersListAdapter extends RecyclerView.Adapter<OrdersListAdapter.MyViewHolder> {

    Context context;
    ArrayList<OrderModel> orderModelArrayList;

    public OrdersListAdapter(Context context, ArrayList<OrderModel> orderModelArrayList) {
        this.context = context;
        this.orderModelArrayList = orderModelArrayList;
    }

    @NonNull
    @Override
    public OrdersListAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemOrderListBinding itemOrderListBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_order_list, parent, false);
        return new MyViewHolder(itemOrderListBinding);
    }

    @SneakyThrows
    @Override
    public void onBindViewHolder(@NonNull OrdersListAdapter.MyViewHolder holder, int position) {
        holder.itemOrderListBinding.tvItemOrderSrNo.setText(String.valueOf(position + 1));
        String salesNo = orderModelArrayList.get(holder.getAbsoluteAdapterPosition()).getPrefix() + String.valueOf(orderModelArrayList.get(holder.getAbsoluteAdapterPosition()).getSalesNo());
        holder.itemOrderListBinding.tvItemOrderInv.setText(salesNo);
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");
        Date oldDate = sdf2.parse(orderModelArrayList.get(holder.getAbsoluteAdapterPosition()).getSalesDate());
        String oldFormattedDate = sdf.format(oldDate.getTime());
        holder.itemOrderListBinding.tvItemOrderDate.setText(oldFormattedDate);
        holder.itemOrderListBinding.tvItemOrderTotal.setText(String.valueOf(orderModelArrayList.get(holder.getAbsoluteAdapterPosition()).getTotal()));

        holder.itemOrderListBinding.llItemOrder.setOnClickListener(v -> {
            Toast.makeText(context, "Click on " + holder.itemOrderListBinding.tvItemOrderInv.getText().toString(), Toast.LENGTH_SHORT).show();
            Intent intentOrderDetails = new Intent(context, OrderDetailsActivity.class);
            intentOrderDetails.putExtra("checkStatus", 0);
            intentOrderDetails.putExtra("salesId", orderModelArrayList.get(holder.getAbsoluteAdapterPosition()).getSalesId());
            //orderLists.get(position).getSalesId())
            context.startActivity(intentOrderDetails);
            Log.e("TAG", "onBindViewHolder: ");
        });
    }

    @Override
    public int getItemCount() {
        return orderModelArrayList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        ItemOrderListBinding itemOrderListBinding;

        public MyViewHolder(@NonNull ItemOrderListBinding itemView) {
            super(itemView.getRoot());
            this.itemOrderListBinding = itemView;
        }
    }
}