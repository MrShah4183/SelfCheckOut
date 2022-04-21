package com.vasyerp.selfcheckout.ui.login;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Toast;

import com.kaopiz.kprogresshud.KProgressHUD;
import com.vasyerp.selfcheckout.api.Api;
import com.vasyerp.selfcheckout.api.ApiGenerator;
import com.vasyerp.selfcheckout.databinding.ActivityRegistrationBinding;
import com.vasyerp.selfcheckout.models.customer.City;
import com.vasyerp.selfcheckout.models.customer.Country;
import com.vasyerp.selfcheckout.models.customer.CreateCustomerBody;
import com.vasyerp.selfcheckout.models.customer.CustomerDetails;
import com.vasyerp.selfcheckout.models.customer.State;
import com.vasyerp.selfcheckout.repositories.LoginRegistrationRepository;
import com.vasyerp.selfcheckout.ui.orders_ui.OrdersListActivity;
import com.vasyerp.selfcheckout.utils.CommonUtil;
import com.vasyerp.selfcheckout.utils.ConnectivityStatus;
import com.vasyerp.selfcheckout.utils.PreferenceManager;
import com.vasyerp.selfcheckout.viewmodels.login_registration.LoginRegistrationViewModel;
import com.vasyerp.selfcheckout.viewmodels.login_registration.LoginRegistrationViewModelFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class RegistrationActivity extends AppCompatActivity {
    String TAG = RegistrationActivity.this.getClass().getSimpleName();
    ActivityRegistrationBinding registrationBinding;
    KProgressHUD kProgressHUD;
    private boolean isInternetConnected;

    private ArrayList<String> countryNames = new ArrayList<>();
    private ArrayList<String> countryCode = new ArrayList<>();
    private ArrayAdapter<String> countryAdapter;


    private ArrayList<String> stateNames = new ArrayList<>();
    private ArrayList<String> stateCode = new ArrayList<>();
    private ArrayAdapter<String> stateAdapter;


    private ArrayList<String> cityNames = new ArrayList<>();
    private ArrayList<String> cityCode = new ArrayList<>();
    private ArrayAdapter<String> cityAdapter;

    private int selectedCountryCode = -1;
    private int selectedStateCode = -1;
    private int selectedCityCode = -1;


    private int companyId;
    private int userId;
    private int branchId;
    private String domainName;

    LoginRegistrationViewModel viewModel;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        registrationBinding = ActivityRegistrationBinding.inflate(getLayoutInflater());
        setContentView(registrationBinding.getRoot());

        /*companyId = Integer.parseInt(PreferenceManager.getCompanyId(this));
        branchId = Integer.parseInt(PreferenceManager.getBranchId(this));
        userId = Integer.parseInt(PreferenceManager.getUserId(this));*/

        companyId = 64;
        branchId = 64;
        userId = 64;

        domainName = PreferenceManager.getDomain(this);

        kProgressHUD = CommonUtil.getKProgressHud(RegistrationActivity.this);
        kProgressHUD.setCancellable(false);
        kProgressHUD.setDimAmount(0.25f);

        countryAdapter = new ArrayAdapter<String>(RegistrationActivity.this, android.R.layout.simple_list_item_1, countryNames);
        //countryAdapter.setNotifyOnChange(true);
        registrationBinding.spinnerCountry.setAdapter(countryAdapter);

        stateAdapter = new ArrayAdapter<String>(RegistrationActivity.this, android.R.layout.simple_list_item_1, stateNames);
        //stateAdapter.setNotifyOnChange(true);
        registrationBinding.spinnerState.setAdapter(stateAdapter);

        cityAdapter = new ArrayAdapter<String>(RegistrationActivity.this, android.R.layout.simple_list_item_1, cityNames);
        //cityAdapter.setNotifyOnChange(true);
        registrationBinding.spinnerCity.setAdapter(cityAdapter);

        initViewModelAndRepository();

        ConnectivityStatus connectivityStatus = new ConnectivityStatus(RegistrationActivity.this);
        connectivityStatus.observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                isInternetConnected = aBoolean;
                if (aBoolean) {
                    viewModel.getCountryListApiCall();
                } else {
                    CommonUtil.showSnackBar(registrationBinding.llRegistrationSnackBar, registrationBinding.llRegistrationSnackBar, "Check Internet Connection");
                }
            }
        });


        viewObserverCollection();

        registrationBinding.btnRegistration.setOnClickListener(v -> {
            //Toast.makeText(RegistrationActivity.this, "Registration Done", Toast.LENGTH_SHORT).show();
            CreateCustomerBody customerBody = new CreateCustomerBody();
            customerBody.setFirstName(registrationBinding.etFullName.getText().toString());
            customerBody.setMobileNo(registrationBinding.etMobile.getText().toString());
            customerBody.setAddressLine1(registrationBinding.etAddress.getText().toString());
            customerBody.setGstin(null);
            customerBody.setGstType("UnRegistered");
            //String tempCityCode = "", tempStateCode = "", tempCountryCode = "";

            for (int i = 0; i < countryCode.size(); i++) {
                if (countryCode.get(i).split("-")[0].equals(countryNames.get(selectedCountryCode))) {
                    //tempCountryCode = countryCode.get(i).split("-")[1];
                    customerBody.setCountriesCode(countryCode.get(i).split("-")[1]);
                }
            }

            for (int i = 0; i < stateCode.size(); i++) {
                if (stateCode.get(i).split("-")[0].equals(stateNames.get(selectedStateCode))) {
                    //tempCountryCode = stateCode.get(i).split("-")[1];
                    customerBody.setCountriesCode(stateCode.get(i).split("-")[1]);
                }
            }

            for (int i = 0; i < cityCode.size(); i++) {
                if (cityCode.get(i).split("-")[0].equals(cityNames.get(selectedCityCode))) {
                    //tempCountryCode = cityCode.get(i).split("-")[1];
                    customerBody.setCountriesCode(cityCode.get(i).split("-")[1]);
                }
            }


            //customerBody.setCityCode(tempCityCode);
            //customerBody.setStateCode(tempStateCode);
            //customerBody.setCountriesCode(tempCountryCode);

            viewModel.createCustomerApiCall(customerBody);
            kProgressHUD.show();
        });

        registrationBinding.tvRegistrationToLogin.setOnClickListener(v -> {
            Intent intentLogin = new Intent(RegistrationActivity.this, LoginActivity.class);
            startActivity(intentLogin);
            RegistrationActivity.this.finish();
        });

        registrationBinding.spinnerCountry.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String countryName = (String) parent.getItemAtPosition(position);
                Log.d(TAG, "onItemSelected: " + countryName + " Position: " + position + " " + countryCode.get(position));
                for (int i = 0; i < countryCode.size(); i++) {
                    if (countryCode.get(i).split("-")[0].equals(countryName)) {
                        Log.d(TAG, "onItemClick: Country code: " + countryCode.get(i));
                        if (isInternetConnected) {
                            viewModel.getStateListApiCall(countryCode.get(i).split("-")[1]);
                        } else {
                            CommonUtil.showSnackBar(registrationBinding.llRegistrationSnackBar, registrationBinding.llRegistrationSnackBar, "Check internet connectivity.");
                        }
                        //loadStateListBasedOnSelectedCountry(countryCode.get(i).split("-")[1]);

                        selectedCountryCode = i;
                        selectedStateCode = -1;
                        selectedCityCode = -1;

                        stateCode.clear();
                        stateNames.clear();
                        cityCode.clear();
                        cityNames.clear();

                        stateAdapter.notifyDataSetChanged();
                        cityAdapter.notifyDataSetChanged();

                        /*if (cityNames.size() > 0) {
                            cityNames.clear();
                            cityCode.clear();
                        }*/
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        registrationBinding.spinnerState.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String stateName = (String) parent.getItemAtPosition(position);
                Log.d(TAG, "onItemSelected: " + stateName + " Position: " + position);

                for (int i = 0; i < stateCode.size(); i++) {
                    if (stateCode.get(i).split("-")[0].equals(stateName)) {
                        Log.d(TAG, "onItemClick: Country code: " + stateCode.get(i));
                        //loadCityBasedOnSelectedState(stateCode.get(i).split("-")[1]);
                        if (isInternetConnected) {
                            viewModel.getCityListApiCall(stateCode.get(i).split("-")[1]);
                        } else {
                            CommonUtil.showSnackBar(registrationBinding.llRegistrationSnackBar, registrationBinding.llRegistrationSnackBar, "Check internet connectivity.");
                        }

                        selectedStateCode = i;
                        selectedCityCode = -1;
                        if (cityCode.size() > 0) {
                            cityCode.clear();
                            cityNames.clear();
                            cityAdapter.notifyDataSetChanged();
                        }
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        registrationBinding.spinnerCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String cityName = (String) parent.getItemAtPosition(position);
                Log.d(TAG, "onItemSelected: " + cityName + " Position: " + position);
                for (int i = 0; i < cityCode.size(); i++) {
                    if (cityCode.get(i).split("-")[0].equals(cityName)) {
                        Log.d(TAG, "onItemClick: Country code: " + cityCode.get(i));
                        selectedCityCode = i;
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void viewObserverCollection() {

        viewModel.getCustomerResponse.observe(this, customerDetails -> {
            Log.e(TAG, "onChanged: customer is created" + customerDetails.toString());
            //Log.e(TAG, "onChanged: customer is created" + customerDetails.toString());
        });

        viewModel.country.observe(this, countries -> {
            countries.sort(Comparator.comparing(Country::getCountriesName));
            //Collections.sort(countries);
            countryNames.clear();
            countryCode.clear();
            countryAdapter.clear();
            for (Country country : countries) {
                countryNames.add(country.getCountriesName());
                countryCode.add(country.getCountriesName() + "-" + country.getCountriesCode());
            }
            String tempCountryName = "India";
            int tempPos = -1;
            for (int i = 0; i < countryNames.size(); i++) {
                if (countryNames.get(i).trim().toLowerCase().equals(tempCountryName.trim().toLowerCase())) {
                    tempPos = i;
                }
            }
            if (tempPos != -1) {
                registrationBinding.spinnerCountry.setSelection(tempPos);
            }
            countryAdapter.notifyDataSetChanged();
        });

        viewModel.state.observe(this, states -> {
            states.sort(Comparator.comparing(State::getStateName));
            stateNames.clear();
            stateCode.clear();
            stateAdapter.clear();
            for (State state : states) {
                stateNames.add(state.getStateName());
                stateCode.add(state.getStateName() + "-" + state.getStateCode());
            }

            if (stateNames.size() > 0) {
                stateAdapter.notifyDataSetChanged();
            } else {
                CommonUtil.showSnackBar(registrationBinding.llRegistrationSnackBar, registrationBinding.llRegistrationSnackBar, "No State Available in " + countryNames.get(selectedCountryCode));
            }

        });

        viewModel.city.observe(this, cities -> {
            cities.sort(Comparator.comparing(City::getCityName));
            cityNames.clear();
            cityCode.clear();
            cityAdapter.clear();
            for (City city : cities) {
                cityNames.add(city.getCityName());
                cityCode.add(city.getCityName() + "-" + city.getCityCode());
            }

            if (cityNames.size() > 0) {
                cityAdapter.notifyDataSetChanged();
            } else {
                CommonUtil.showSnackBar(registrationBinding.llRegistrationSnackBar, registrationBinding.llRegistrationSnackBar, "No City Available in " + stateNames.get(selectedStateCode));
            }
        });

        viewModel.error.observe(this, s -> {
            if (s != null) {
                CommonUtil.showSnackBar(registrationBinding.llRegistrationSnackBar, registrationBinding.llRegistrationSnackBar, "" + s);
            }
        });

        viewModel.isLoading.observe(this, aBoolean -> {
            if (aBoolean) {
                kProgressHUD.show();
            } else {
                kProgressHUD.dismiss();
            }
        });
    }

    private void initViewModelAndRepository() {
        Api apiInterface = ApiGenerator.getApi(CommonUtil.tempBaseUrl).create(Api.class);
        viewModel = new ViewModelProvider(this, new LoginRegistrationViewModelFactory(LoginRegistrationRepository.getInstance(apiInterface), companyId, branchId, userId)).get(LoginRegistrationViewModel.class);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (v instanceof EditText) {
                Rect outRect = new Rect();
                v.getGlobalVisibleRect(outRect);
                if (!outRect.contains((int) event.getRawX(), (int) event.getRawY())) {
                    v.clearFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
        }
        return super.dispatchTouchEvent(event);
    }
}