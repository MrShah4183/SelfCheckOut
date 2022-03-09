package com.vasyerp.selfcheckout.adapters.batch;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.vasyerp.selfcheckout.R;
import com.vasyerp.selfcheckout.models.product.StockMasterVo;
import com.vasyerp.selfcheckout.utils.CommonUtil;

import java.util.ArrayList;

public class BatchSelectionArrayAdapter extends ArrayAdapter<StockMasterVo> {

    public BatchSelectionArrayAdapter(@NonNull Context context, ArrayList<StockMasterVo> stockMasterVos) {
        super(context, 0, stockMasterVos);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return initView(position, convertView, parent);
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return initView(position, convertView, parent);
    }

    public void notifyBatchChanges() {
        notifyDataSetChanged();
    }

    private View initView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.dialog_batch_selection_spinner_itemview, parent, false);
        }

        TextView mrpTextView = convertView.findViewById(R.id.mrpTextView);
        TextView sellingPriceTextView = convertView.findViewById(R.id.sellingPriceTextView);
        TextView landingCost = convertView.findViewById(R.id.landingCostTextView);

        landingCost.setText(TextUtils.concat("L.D: " + CommonUtil.getDoubleDecimalValue(getItem(position).getLandingcost())).toString());
        mrpTextView.setText(TextUtils.concat("MRP: " + CommonUtil.getDoubleDecimalValue(getItem(position).getMrp())).toString());
        sellingPriceTextView.setText(TextUtils.concat("S.P: " + CommonUtil.getDoubleDecimalValue(getItem(position).getSellingPrice())).toString());

        return convertView;
    }

}
