package com.vasyerp.selfcheckout.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.vasyerp.selfcheckout.R;
import com.vasyerp.selfcheckout.databinding.ItemOrdersDetailsListBinding;
import com.vasyerp.selfcheckout.models.ordersummary.SalesItems;

import java.util.Locale;

public class OrderProductsAdapter extends ListAdapter<SalesItems, OrderProductsViewHolder> {

    public OrderProductsAdapter() {
        super(OrderProductsAdapter.callback);
    }

    @NonNull
    @Override
    public OrderProductsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemOrdersDetailsListBinding ordersSummaryListBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext())
                , R.layout.item_orders_details_list
                , parent
                , false);
        return new OrderProductsViewHolder(ordersSummaryListBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderProductsViewHolder holder, int position) {
        holder.bind(getItem(position));
    }

    private static DiffUtil.ItemCallback<SalesItems> callback = new DiffUtil.ItemCallback<SalesItems>() {
        @Override
        public boolean areItemsTheSame(@NonNull SalesItems oldItem, @NonNull SalesItems newItem) {
            return ((SalesItems) oldItem) == ((SalesItems) newItem);
        }

        @Override
        public boolean areContentsTheSame(@NonNull SalesItems oldItem, @NonNull SalesItems newItem) {
            return ((SalesItems) oldItem).getProductVarientId() == ((SalesItems) oldItem).getProductVarientId();
        }
    };
}

class OrderProductsViewHolder extends RecyclerView.ViewHolder {
    ItemOrdersDetailsListBinding itemViewOrdersSummaryListBinding;

    public OrderProductsViewHolder(ItemOrdersDetailsListBinding binding) {
        super(binding.getRoot());
        this.itemViewOrdersSummaryListBinding = binding;
    }

    public void bind(SalesItems salesItems) {
        itemViewOrdersSummaryListBinding.productName.setText(salesItems.getProductName());
        itemViewOrdersSummaryListBinding.productQty.setText(String.valueOf(salesItems.getQty()));
        itemViewOrdersSummaryListBinding.productNetAmount.setText(String.format(Locale.getDefault(), "%.2f", salesItems.getNetAmount()));
        itemViewOrdersSummaryListBinding.productDiscount.setText(String.format(Locale.getDefault(), "%.2f", salesItems.getMrpToDiscount()));
    }
}
