package com.vasyerp.selfcheckout.adapters.cart_product;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.vasyerp.selfcheckout.R;
import com.vasyerp.selfcheckout.databinding.ItemCartListBinding;
import com.vasyerp.selfcheckout.adapters.listeners.CartQtyFocusCallback;
import com.vasyerp.selfcheckout.adapters.listeners.CartViewHolderCallbacks;
import com.vasyerp.selfcheckout.adapters.listeners.ItemQtyCallback;
import com.vasyerp.selfcheckout.models.product.StockMasterVo;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {
    Context context;
    public List<StockMasterVo> productList;
    float cartQty = 0;
    float total = 0, initialTotal = 0.0f;
    String roundOffStr;
    NumberFormat numberFormat;
    String strQuantityPattern = "^\\d+(\\.\\d+)?(?=$)|(?<=^)\\.\\d+(?=$)";
    Pattern pattern, quantityPattern;
    private ItemQtyCallback callback;
    private CartQtyFocusCallback cartQtyFocusCallback;
    private static final String TAG = "CartAdapter";

    public CartAdapter(Context context, List<StockMasterVo> productList) {
        this.context = context;
        this.productList = productList;
        this.numberFormat = NumberFormat.getInstance();
        this.quantityPattern = Pattern.compile(strQuantityPattern, Pattern.DOTALL);
    }

    public CartQtyFocusCallback getCartQtyFocusCallback() {
        return cartQtyFocusCallback;
    }

    public void setCartQtyFocusCallback(CartQtyFocusCallback cartQtyFocusCallback) {
        this.cartQtyFocusCallback = cartQtyFocusCallback;
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemCartListBinding rawCartBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_cart_list, parent, false);
        return new CartViewHolder(rawCartBinding, new CartEditTextWatcher());
    }

    @SuppressLint("DefaultLocale")
    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        holder.binding.tvRawProMrp.setText(String.valueOf(String.format(Locale.getDefault(), "%.2f", productList.get(holder.getAbsoluteAdapterPosition()).getDisplayMrp())));
        holder.cartEditTextWatcher.updatePosition(holder.getAbsoluteAdapterPosition());
        holder.cartEditTextWatcher.getBindingInstance(holder.binding.tvRawProQty);
        holder.binding.tvRawProQty.setText(String.format(Locale.getDefault(), "%.2f", Double.valueOf(productList.get(holder.getAbsoluteAdapterPosition()).getQuantity())));
        holder.binding.tvRawProName.setText(productList.get(holder.getAbsoluteAdapterPosition()).getProductDto().getDisplay_name());
        holder.binding.tvRawSrNo.setText(String.valueOf(holder.getAbsoluteAdapterPosition() + 1));

        holder.cartEditTextWatcher.setCartViewHolderCallbacks(new CartViewHolderCallbacks() {
            @Override
            public void changedItem(int position) {
                holder.binding.tvRawProMrp.setText(String.valueOf(String.format(Locale.getDefault(), "%.2f", productList.get(holder.getAbsoluteAdapterPosition()).getDisplayMrp())));
                callback.setLatestCount(
                        productList.stream().mapToDouble(StockMasterVo::getDisplayMrp).sum(),
                        productList.stream().mapToDouble(value -> Double.parseDouble(value.getQuantity())).sum());
            }
        });

        holder.binding.tvRawProQty.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                cartQtyFocusCallback.setHasFocus(hasFocus);
            }
        });
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    @Override
    public void onViewAttachedToWindow(@NonNull CartViewHolder holder) {
        holder.enableTextWatcher();
    }

    @Override
    public void onViewDetachedFromWindow(@NonNull CartViewHolder holder) {
        holder.disableTextWatcher();
    }

    public void removeItem(int position) {
        productList.remove(position);
        notifyDataSetChanged();
        callback.setLatestCount(
                productList.stream().mapToDouble(StockMasterVo::getDisplayMrp).sum(),
                productList.stream().mapToDouble(value -> Double.parseDouble(value.getQuantity())).sum());
    }

    public void restoreItem(StockMasterVo item, int position) {
        productList.add(position, item);
        notifyItemInserted(position);
        callback.setLatestCount(
                productList.stream().mapToDouble(StockMasterVo::getDisplayMrp).sum(),
                productList.stream().mapToDouble(value -> Double.parseDouble(value.getQuantity())).sum());
    }

    public void setItemQtyCallback(ItemQtyCallback itemQtyCallback) {
        this.callback = itemQtyCallback;
    }

    static class CartViewHolder extends RecyclerView.ViewHolder {
        private CartEditTextWatcher cartEditTextWatcher;
        private ItemCartListBinding binding;

        public CartViewHolder(ItemCartListBinding binding, CartEditTextWatcher cartEditTextWatcher) {
            super(binding.getRoot());
            this.binding = binding;
            this.cartEditTextWatcher = cartEditTextWatcher;
            binding.tvRawProQty.addTextChangedListener(cartEditTextWatcher);
        }

        public void enableTextWatcher() {
            binding.tvRawProQty.addTextChangedListener(cartEditTextWatcher);
        }

        public void disableTextWatcher() {
            binding.tvRawProQty.removeTextChangedListener(cartEditTextWatcher);
            Log.d("CartViewHolder", "disableTextWatcher: detached..");
        }
    }

    class CartEditTextWatcher implements TextWatcher {
        private int position;
        private CartViewHolderCallbacks cartViewHolderCallbacks;
        private EditText editText;

        public void updatePosition(int position) {
            this.position = position;
        }

        public void getBindingInstance(EditText editText) {
            this.editText = editText;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            Log.d("CartAdapter", "onTextChanged: called...");
            if (!s.toString().isEmpty()) {
                if (quantityPattern.matcher(s.toString()).matches()) {
                    double qty = Double.parseDouble(String.format(Locale.getDefault(), "%.2f", Double.parseDouble(s.toString())));
                    double oldQty = Double.parseDouble(String.format(Locale.getDefault(), "%.2f", Double.parseDouble(productList.get(position).getOldQuantity())));

                    if (productList.get(position).getHasNegativeSelling() == 1) {
                        if (qty > oldQty) {
                            if (oldQty != 0.0) {
                                if (editText.getText().hashCode() == s.hashCode()) {
                                    editText.setText(String.valueOf(oldQty));
                                    editText.setError(null);
                                }
                                productList.get(position).setQuantity(String.valueOf(oldQty));
                                double proPrice = productList.get(position).getTaxableAmount() + productList.get(position).getTaxPrice();
                                double displayPrice = Double.parseDouble(productList.get(position).getQuantity()) * proPrice;
                                double totalTax = Double.parseDouble(productList.get(position).getQuantity()) * productList.get(position).getTaxPrice();
                                productList.get(position).setTotalTaxPrice(totalTax);
                                productList.get(position).setDisplayMrp(displayPrice);
                                Toast.makeText(this.editText.getContext(), "Quantity must be less or equal to " + String.valueOf(oldQty) + " for " + productList.get(position).getProductDto().getDisplay_name(), Toast.LENGTH_LONG).show();
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        if (cartViewHolderCallbacks != null) {
                                            cartViewHolderCallbacks.changedItem(position);
                                        }
                                    }
                                }, 200);
                            }
                        } else {
                            if (qty != 0.0) {
                                productList.get(position).setQuantity(String.valueOf(qty));
                                double proPrice = productList.get(position).getTaxableAmount() + productList.get(position).getTaxPrice();
                                double displayPrice = Double.parseDouble(productList.get(position).getQuantity()) * proPrice;
                                double totalTax = Double.parseDouble(productList.get(position).getQuantity()) * productList.get(position).getTaxPrice();
                                productList.get(position).setTotalTaxPrice(totalTax);
                                productList.get(position).setDisplayMrp(displayPrice);
                                if (editText.getText().hashCode() == s.hashCode()) {
                                    editText.setError(null);
                                }
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        if (cartViewHolderCallbacks != null) {
                                            cartViewHolderCallbacks.changedItem(position);
                                        }
                                    }
                                }, 200);
                            } else {
                                if (editText.getText().hashCode() == s.hashCode()) {
                                    editText.setText("1.00");
                                    editText.setError(null);
                                }
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        if (cartViewHolderCallbacks != null) {
                                            cartViewHolderCallbacks.changedItem(position);
                                        }
                                    }
                                }, 200);
                            }
                        }
                    } else {
                        if (qty != 0.0) {
                            Log.d("CartAdapter", "onTextChanged: " + oldQty);
                            productList.get(position).setQuantity(s.toString());
                            double proPrice = productList.get(position).getTaxableAmount() + productList.get(position).getTaxPrice();
                            double displayPrice = Double.parseDouble(productList.get(position).getQuantity()) * proPrice;
                            double totalTax = Double.parseDouble(productList.get(position).getQuantity()) * productList.get(position).getTaxPrice();
                            productList.get(position).setTotalTaxPrice(totalTax);
                            productList.get(position).setDisplayMrp(displayPrice);
                            if (editText.getText().hashCode() == s.hashCode()) {
                                editText.setError(null);
                            }
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    if (cartViewHolderCallbacks != null) {
                                        cartViewHolderCallbacks.changedItem(position);
                                    }
                                }
                            }, 200);
                        } else {
                            Log.d("CartAdapter", "onTextChanged: within else.");
                            qty = 1.00;
                            if (editText.getText().hashCode() == s.hashCode()) {
                                editText.setText(String.valueOf(qty));
                            }
                            productList.get(position).setQuantity(String.valueOf(qty));
                            productList.get(position).setTotalTaxPrice(productList.get(position).getTaxPrice());
                            double displayPrice = productList.get(position).getTaxableAmount() + productList.get(position).getTaxPrice();
                            productList.get(position).setDisplayMrp(displayPrice);
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    if (cartViewHolderCallbacks != null) {
                                        cartViewHolderCallbacks.changedItem(position);
                                    }
                                }
                            }, 200);
                        }
                    }
                } else {
                    productList.get(position).setQuantity(String.valueOf("1.00"));
                    if (editText.getText().hashCode() == s.hashCode()) {
                        editText.setError("Please enter valid qty.");
                    }
                    productList.get(position).setTotalTaxPrice(productList.get(position).getTaxPrice());
                    double displayPrice = productList.get(position).getTaxableAmount() + productList.get(position).getTaxPrice();
                    productList.get(position).setDisplayMrp(displayPrice);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (cartViewHolderCallbacks != null) {
                                cartViewHolderCallbacks.changedItem(position);
                            }
                        }
                    }, 200);
                }
                if (editText.getText().hashCode() == s.hashCode()) {
                    editText.setError(null);
                }
            } else {
                productList.get(position).setQuantity(String.valueOf("1.00"));
                if (editText.getText().hashCode() == s.hashCode()) {
                    editText.setError("Please enter qty.");
                }
                productList.get(position).setTotalTaxPrice(productList.get(position).getTaxPrice());
                double displayPrice = productList.get(position).getTaxableAmount() + productList.get(position).getTaxPrice();
                productList.get(position).setDisplayMrp(displayPrice);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (cartViewHolderCallbacks != null) {
                            cartViewHolderCallbacks.changedItem(position);
                        }
                    }
                }, 200);
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }

        public void setCartViewHolderCallbacks(CartViewHolderCallbacks cartViewHolderCallbacks) {
            this.cartViewHolderCallbacks = cartViewHolderCallbacks;
        }
    }
}

