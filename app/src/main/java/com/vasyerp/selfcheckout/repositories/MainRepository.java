package com.vasyerp.selfcheckout.repositories;

import android.util.Log;

import androidx.annotation.NonNull;

import com.vasyerp.selfcheckout.api.Api;
import com.vasyerp.selfcheckout.api.ApiResponse;
import com.vasyerp.selfcheckout.models.product.CompanySettingVo;
import com.vasyerp.selfcheckout.models.product.GetAllProducts;
import com.vasyerp.selfcheckout.models.product.Product;
import com.vasyerp.selfcheckout.models.product.ProductDto;
import com.vasyerp.selfcheckout.models.product.ProductStatus;
import com.vasyerp.selfcheckout.models.product.ProductVarientsDTO;

import java.util.List;
import java.util.stream.Collectors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainRepository {
    private Api api;
    private String TAG = "MainRepository";

    public MainRepository(Api api) {
        this.api = api;
    }

    public static MainRepository getInstance(Api api) {
        return new MainRepository(api);
    }

    public void getProductByBarcodeId(DataSource<ProductVarientsDTO> dataSource, String financialYear, String productId, boolean isSearchByBarcode, String branchId, String companyId) {

            /*Call<ApiResponse<List<GetAllProducts>>> callGetAllProducts = api.getAllProductList(String.valueOf(companyId));
        dataSource.loading(true);
        callGetAllProducts.enqueue(new Callback<ApiResponse<List<GetAllProducts>>>() {*/
        if (!isSearchByBarcode) {
            Call<ApiResponse<Product>> callProductByProductId = api.getProductByProductId(branchId, productId, companyId, financialYear);
            dataSource.loading(true);
            callProductByProductId.enqueue(new Callback<ApiResponse<Product>>() {
                @Override
                public void onResponse(@NonNull Call<ApiResponse<Product>> call, @NonNull Response<ApiResponse<Product>> response) {
                    ProductStatus status = new ProductStatus();
                    if (response.isSuccessful()) {
                        if (response.body() != null) {
                            if (response.body().getResponse() != null) {
                                if (response.body().isStatus()) {
                                    Product product = response.body().getResponse();
                                    if (product != null) {
                                        ProductDto productDto = product.getMposProductDTO();
                                        ProductVarientsDTO productVarientsDTO = product.getProductVarientsDTO();
                                        CompanySettingVo companySettingVo = product.getCompanySettingVo();
                                        productVarientsDTO.setProductStatus(status);
                                        if (companySettingVo.getValue() == 1) {
                                            if (productVarientsDTO.getActive() == 0) {
                                                productVarientsDTO.setStockMasterVos(productVarientsDTO.getStockMasterVos()
                                                        .stream()
                                                        .filter(stockMasterVo -> Double.valueOf(stockMasterVo.getQuantity()).doubleValue() >= 0.0)
                                                        .map(stockMasterVo -> {
                                                            stockMasterVo.setHasNegativeSelling(companySettingVo.getValue());
                                                            return stockMasterVo;
                                                        })
                                                        .collect(Collectors.toList()));
                                                if (productVarientsDTO.getStockMasterVos().size() > 0) {
                                                    productVarientsDTO.setProductDto(productDto);
                                                    status.setProductAddable(true);
                                                    status.setReason(null);
                                                    //_getProduct.postValue(productVarientsDTO);
                                                    dataSource.data(productVarientsDTO);
                                                    dataSource.loading(false);
                                                    dataSource.error(null);
                                                    Log.d("MainViewModel", "onResponse: added.");
                                                } else {
                                                    status.setProductAddable(false);
                                                    status.setReason("No, stock available.");
                                                    //_getProduct.postValue(productVarientsDTO);
                                                    dataSource.data(productVarientsDTO);
                                                    dataSource.loading(false);
                                                    dataSource.error("No, stock available.");
                                                    Log.d("MainViewModel", "onResponse: not added, no stock available.");
                                                }
                                            } else {
                                                status.setProductAddable(false);
                                                status.setReason("Selected product is inactive or disabled.");
                                                //_getProduct.postValue(productVarientsDTO);
                                                dataSource.data(productVarientsDTO);
                                                dataSource.loading(false);
                                                dataSource.error("Selected product is inactive or disabled.");
                                            }
                                        } else {
                                            if (productVarientsDTO.getActive() == 0) {
                                                productVarientsDTO.setStockMasterVos(productVarientsDTO.getStockMasterVos()
                                                        .stream()
                                                        .map(stockMasterVo -> {
                                                            stockMasterVo.setHasNegativeSelling(companySettingVo.getValue());
                                                            return stockMasterVo;
                                                        })
                                                        .collect(Collectors.toList()));
                                                if (productVarientsDTO.getStockMasterVos().size() > 0) {
                                                    productVarientsDTO.setProductDto(productDto);
                                                    status.setProductAddable(true);
                                                    status.setReason(null);
                                                    dataSource.data(productVarientsDTO);
                                                    dataSource.loading(false);
                                                    dataSource.error(null);
                                                    //_getProduct.postValue(productVarientsDTO);
                                                } else {
                                                    status.setProductAddable(false);
                                                    status.setReason("No, stock available.");
                                                    //_getProduct.postValue(productVarientsDTO);
                                                    dataSource.data(productVarientsDTO);
                                                    dataSource.loading(false);
                                                    dataSource.error("No, stock available.");
                                                }
                                            } else {
                                                status.setProductAddable(false);
                                                status.setReason("Selected product is inactive or disabled.");
                                                //_getProduct.postValue(productVarientsDTO);
                                                dataSource.data(productVarientsDTO);
                                                dataSource.loading(false);
                                                dataSource.error("Selected product is inactive or disabled.");
                                            }
                                        }
                                    }
                                } else {
                                    dataSource.data(null);
                                    dataSource.loading(false);
                                    dataSource.error("Product not found.");
                                }
                            } else {
                                Log.d(TAG, "onResponse: response is null.");
                                dataSource.loading(false);
                                dataSource.data(null);
                                dataSource.error("Invalid store Barcode.");
                            }
                        } else {
                            Log.d(TAG, "onResponse: response body null.");
                            dataSource.loading(false);
                            dataSource.data(null);
                            dataSource.error("Response body is null.");
                        }
                    } else {
                        Log.d(TAG, "onResponse: response fail.");
                        dataSource.loading(false);
                        dataSource.data(null);
                        dataSource.error("Response fail.");
                    }
                }

                @Override
                public void onFailure(@NonNull Call<ApiResponse<Product>> call, @NonNull Throwable t) {
                    dataSource.loading(false);
                    dataSource.data(null);
                    dataSource.error("fail to connect.");
                }
            });
        } else {
            Call<ApiResponse<Product>> callProductByBarcode = api.getProductByBarCode(branchId, productId, companyId, financialYear);
            dataSource.loading(true);
            callProductByBarcode.enqueue(new Callback<ApiResponse<Product>>() {
                @Override
                public void onResponse(@NonNull Call<ApiResponse<Product>> call, @NonNull Response<ApiResponse<Product>> response) {
                    ProductStatus status = new ProductStatus();
                    if (response.isSuccessful()) {
                        if (response.body() != null) {
                            if (response.body().getResponse() != null) {
                                if (response.body().isStatus()) {
                                    Product product = response.body().getResponse();
                                    if (product != null) {
                                        ProductDto productDto = product.getMposProductDTO();
                                        ProductVarientsDTO productVarientsDTO = product.getProductVarientsDTO();
                                        CompanySettingVo companySettingVo = product.getCompanySettingVo();
                                        productVarientsDTO.setProductStatus(status);
                                        if (companySettingVo.getValue() == 1) {
                                            if (productVarientsDTO.getActive() == 0) {
                                                productVarientsDTO.setStockMasterVos(productVarientsDTO.getStockMasterVos()
                                                        .stream()
                                                        .filter(stockMasterVo -> Double.valueOf(stockMasterVo.getQuantity()).doubleValue() >= 0.0)
                                                        .map(stockMasterVo -> {
                                                            stockMasterVo.setHasNegativeSelling(companySettingVo.getValue());
                                                            return stockMasterVo;
                                                        })
                                                        .collect(Collectors.toList()));
                                                if (productVarientsDTO.getStockMasterVos().size() > 0) {
                                                    productVarientsDTO.setProductDto(productDto);
                                                    status.setProductAddable(true);
                                                    status.setReason(null);
                                                    //_getProduct.postValue(productVarientsDTO);
                                                    dataSource.data(productVarientsDTO);
                                                    dataSource.loading(false);
                                                    dataSource.error(null);
                                                    Log.d("MainViewModel", "onResponse: added.");
                                                } else {
                                                    status.setProductAddable(false);
                                                    status.setReason("No, stock available.");
                                                    //_getProduct.postValue(productVarientsDTO);
                                                    dataSource.data(productVarientsDTO);
                                                    dataSource.loading(false);
                                                    dataSource.error("No, stock available.");
                                                    Log.d("MainViewModel", "onResponse: not added, no stock available.");
                                                }
                                            } else {
                                                status.setProductAddable(false);
                                                status.setReason("Selected product is inactive or disabled.");
                                                //_getProduct.postValue(productVarientsDTO);
                                                dataSource.data(productVarientsDTO);
                                                dataSource.loading(false);
                                                dataSource.error("Selected product is inactive or disabled.");
                                            }
                                        } else {
                                            if (productVarientsDTO.getActive() == 0) {
                                                productVarientsDTO.setStockMasterVos(productVarientsDTO.getStockMasterVos()
                                                        .stream()
                                                        .map(stockMasterVo -> {
                                                            stockMasterVo.setHasNegativeSelling(companySettingVo.getValue());
                                                            return stockMasterVo;
                                                        })
                                                        .collect(Collectors.toList()));
                                                if (productVarientsDTO.getStockMasterVos().size() > 0) {
                                                    productVarientsDTO.setProductDto(productDto);
                                                    status.setProductAddable(true);
                                                    status.setReason(null);
                                                    dataSource.data(productVarientsDTO);
                                                    dataSource.loading(false);
                                                    dataSource.error(null);
                                                    //_getProduct.postValue(productVarientsDTO);
                                                } else {
                                                    status.setProductAddable(false);
                                                    status.setReason("No, stock available.");
                                                    //_getProduct.postValue(productVarientsDTO);
                                                    dataSource.data(productVarientsDTO);
                                                    dataSource.loading(false);
                                                    dataSource.error("No, stock available.");
                                                }
                                            } else {
                                                status.setProductAddable(false);
                                                status.setReason("Selected product is inactive or disabled.");
                                                //_getProduct.postValue(productVarientsDTO);
                                                dataSource.data(productVarientsDTO);
                                                dataSource.loading(false);
                                                dataSource.error("Selected product is inactive or disabled.");
                                            }
                                        }
                                    }
                                } else {
                                    dataSource.data(null);
                                    dataSource.loading(false);
                                    dataSource.error("Product not found.");
                                }

                                Log.e(TAG, "onResponse: call set db data");
                            } else {
                                Log.d(TAG, "onResponse: response is null.");
                                dataSource.loading(false);
                                dataSource.data(null);
                                dataSource.error("Invalid store Barcode.");
                            }
                        } else {
                            Log.d(TAG, "onResponse: response body null.");
                            dataSource.loading(false);
                            dataSource.data(null);
                            dataSource.error("Response body is null.");
                        }
                    } else {
                        Log.d(TAG, "onResponse: response fail.");
                        dataSource.loading(false);
                        dataSource.data(null);
                        dataSource.error("Response fail.");
                    }
                }

                @Override
                public void onFailure(@NonNull Call<ApiResponse<Product>> call, @NonNull Throwable t) {
                    dataSource.loading(false);
                    dataSource.data(null);
                    dataSource.error("fail to connect.");
                }
            });
        }
    }

    public void getAllProductListApiCall(DataSource<List<GetAllProducts>> dataSource, int companyId) {
        Call<ApiResponse<List<GetAllProducts>>> callGetAllProducts = api.getAllProductList(String.valueOf(companyId));
        dataSource.loading(true);
        callGetAllProducts.enqueue(new Callback<ApiResponse<List<GetAllProducts>>>() {
            @Override
            public void onResponse(@NonNull Call<ApiResponse<List<GetAllProducts>>> call, @NonNull Response<ApiResponse<List<GetAllProducts>>> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        if (response.body().getResponse() != null) {
                            if (response.body().isStatus()) {
                                dataSource.data(response.body().getResponse());
                                dataSource.loading(false);
                                dataSource.error(null);
                            } else {
                                dataSource.data(response.body().getResponse());
                                dataSource.loading(false);
                                dataSource.error(response.message());
                            }
                            Log.e(TAG, "onResponse: call set db data");
                        } else {
                            Log.d(TAG, "onResponse: response is null.");
                            dataSource.loading(false);
                            dataSource.data(null);
                            dataSource.error("no list available");
                        }
                        dataSource.data(response.body().getResponse());
                    } else {
                        Log.d(TAG, "onResponse: response body null.");
                        dataSource.loading(false);
                        dataSource.data(null);
                        dataSource.error("Response body is null.");
                    }
                } else {
                    Log.d(TAG, "onResponse: response fail.");
                    dataSource.loading(false);
                    dataSource.data(null);
                    dataSource.error("Response fail.");
                }
            }

            @Override
            public void onFailure(@NonNull Call<ApiResponse<List<GetAllProducts>>> call, @NonNull Throwable t) {
                dataSource.loading(false);
                dataSource.data(null);
                dataSource.error("fail to connect.");
            }
        });
    }


}