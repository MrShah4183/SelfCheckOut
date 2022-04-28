package com.vasyerp.selfcheckout.ui.login;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
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
import com.vasyerp.selfcheckout.models.customer.State;
import com.vasyerp.selfcheckout.repositories.LoginRegistrationRepository;
import com.vasyerp.selfcheckout.ui.main.MainActivity;
import com.vasyerp.selfcheckout.utils.CommonUtil;
import com.vasyerp.selfcheckout.utils.ConnectivityStatus;
import com.vasyerp.selfcheckout.utils.PreferenceManager;
import com.vasyerp.selfcheckout.viewmodels.login_registration.LoginRegistrationViewModel;
import com.vasyerp.selfcheckout.viewmodels.login_registration.LoginRegistrationViewModelFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Objects;

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

        companyId = Integer.parseInt(PreferenceManager.getCompanyId(this));
        branchId = Integer.parseInt(PreferenceManager.getBranchId(this));
        userId = Integer.parseInt(PreferenceManager.getUserId(this));

        /*companyId = 64;
        branchId = 64;
        userId = 64;*/

        domainName = PreferenceManager.getDomain(this);

        kProgressHUD = CommonUtil.getKProgressHud(RegistrationActivity.this);
        kProgressHUD.setCancellable(false);
        kProgressHUD.setDimAmount(0.25f);

        countryAdapter = new ArrayAdapter<>(RegistrationActivity.this, android.R.layout.simple_list_item_1, countryNames);
        //countryAdapter.setNotifyOnChange(true);
        registrationBinding.spinnerCountry.setAdapter(countryAdapter);

        stateAdapter = new ArrayAdapter<>(RegistrationActivity.this, android.R.layout.simple_list_item_1, stateNames);
        //stateAdapter.setNotifyOnChange(true);
        registrationBinding.spinnerState.setAdapter(stateAdapter);

        cityAdapter = new ArrayAdapter<>(RegistrationActivity.this, android.R.layout.simple_list_item_1, cityNames);
        //cityAdapter.setNotifyOnChange(true);
        registrationBinding.spinnerCity.setAdapter(cityAdapter);

        initViewModelAndRepository();

        ConnectivityStatus connectivityStatus = new ConnectivityStatus(RegistrationActivity.this);
        connectivityStatus.observe(this, aBoolean -> {
            isInternetConnected = aBoolean;
            if (aBoolean) {
                viewModel.getCountryListApiCall();
            }
        });

        viewObserverCollection();

        registrationBinding.btnRegistration.setOnClickListener(v -> {
            if (isInternetConnected) {
                kProgressHUD.show();
                boolean checkFullName = checkRegistrationValidationFullName();
                boolean checkMobileNo = checkRegistrationValidationMobileNo();
                boolean checkAddress = checkRegistrationValidationAddress();
                boolean checkSpinner = checkRegistrationValidationSpinner();
                if (checkFullName && checkMobileNo && checkAddress && checkSpinner) {
                    //viewModel.userLoginCompanyApiCall(Objects.requireNonNull(loginBinding.etMobile.getText()).toString());
                    Log.e(TAG, "onCreate: selectedCountryCode1 - " + selectedCountryCode);
                    Log.e(TAG, "onCreate: selectedStateCode1 - " + selectedStateCode);
                    Log.e(TAG, "onCreate: selectedCityCode1 - " + selectedCityCode);
                    Log.e(TAG, "-----------------------------------------------------------");

                    for (int i = 0; i < countryCode.size(); i++) {
                        if (countryCode.get(i).split("-")[0].equals(countryNames.get(selectedCountryCode))) {
                            //tempCountryCode = countryCode.get(i).split("-")[1];
                            Log.e(TAG, "onCreate: selectedCountryCode - " + countryCode.get(i));
                        }
                    }

                    for (int i = 0; i < stateCode.size(); i++) {
                        if (stateCode.get(i).split("-")[0].equals(stateNames.get(selectedStateCode))) {
                            Log.e(TAG, "onCreate: selectedCountryCode - " + stateCode.get(i));
                        }
                    }

                    for (int i = 0; i < cityCode.size(); i++) {
                        if (cityCode.get(i).split("-")[0].equals(cityNames.get(selectedCityCode))) {
                            Log.e(TAG, "onCreate: selectedCountryCode - " + cityCode.get(i));
                        }
                    }
                    Toast.makeText(RegistrationActivity.this, "save data", Toast.LENGTH_SHORT).show();

                    CreateCustomerBody customerBody = new CreateCustomerBody();
                    customerBody.setFirstName(registrationBinding.etFullName.getText().toString());
                    customerBody.setMobileNo(registrationBinding.etMobile.getText().toString());
                    customerBody.setAddressLine1(registrationBinding.etAddress.getText().toString());
                    customerBody.setGstin(null);
                    customerBody.setGstType("UnRegistered");

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

                    /*customerBody.setCityCode(String.valueOf(selectedCityCode));
                    customerBody.setStateCode(String.valueOf(selectedStateCode));
                    customerBody.setCountriesCode(String.valueOf(selectedCountryCode));*/

                    viewModel.createCustomerApiCall(customerBody);
                    kProgressHUD.show();

                } else {
                    Log.e(TAG, "onCreate: selectedCountryCode1 - " + selectedCountryCode);
                    Log.e(TAG, "onCreate: selectedStateCode1 - " + selectedStateCode);
                    Log.e(TAG, "onCreate: selectedCityCode1 - " + selectedCityCode);
                }
                kProgressHUD.dismiss();
            } else {
                CommonUtil.showSnackBar(registrationBinding.llRegistrationSnackBar, registrationBinding.llRegistrationSnackBar, "Check internet Connection.");
            }
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
                        selectedCountryCode = i;
                        selectedStateCode = -1;
                        selectedCityCode = -1;

                        stateCode.clear();
                        stateNames.clear();
                        cityCode.clear();
                        cityNames.clear();

                        stateAdapter.notifyDataSetChanged();
                        cityAdapter.notifyDataSetChanged();

                        if (isInternetConnected) {
                            viewModel.getStateListApiCall(countryCode.get(i).split("-")[1]);
                        } else {
                            CommonUtil.showSnackBar(registrationBinding.llRegistrationSnackBar, registrationBinding.llRegistrationSnackBar, "Check internet connectivity.");
                        }
                        //loadStateListBasedOnSelectedCountry(countryCode.get(i).split("-")[1]);
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
                        selectedStateCode = i;
                        selectedCityCode = -1;
                        if (cityCode.size() > 0) {
                            cityCode.clear();
                            cityNames.clear();
                            cityAdapter.notifyDataSetChanged();
                        }
                        if (isInternetConnected) {
                            viewModel.getCityListApiCall(stateCode.get(i).split("-")[1]);
                        } else {
                            CommonUtil.showSnackBar(registrationBinding.llRegistrationSnackBar, registrationBinding.llRegistrationSnackBar, "Check internet connectivity.");
                        }
                    }
                }

                /*String tempStateName = "Gujarat";
                String tempCityName = "Anand";

                for (int i = 0; i < stateNames.size(); i++) {
                    if (stateNames.get(i).trim().equalsIgnoreCase(tempStateName)) {
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                int tempPos = -1;
                                if (cityNames.size() != 0) {
                                    for (int j = 0; j < cityNames.size(); j++) {
                                        if (cityNames.get(j).trim().equalsIgnoreCase(tempCityName)) {
                                            tempPos = j;
                                        }
                                    }
                                    if (tempPos != -1) {
                                        registrationBinding.spinnerCity.setSelection(tempPos);
                                    }
                                }
                            }
                        }, 2000);
                    }
                }*/

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

        registrationBinding.etFullName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().isEmpty()) {
                    registrationBinding.tilFullName.setErrorEnabled(false);
                    registrationBinding.tilFullName.setError(null);
                } else {
                    registrationBinding.tilFullName.setErrorEnabled(true);
                    registrationBinding.tilFullName.setError("Please, enter name");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        registrationBinding.etMobile.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().isEmpty()) {
                    registrationBinding.tilMobile.setErrorEnabled(false);
                    registrationBinding.tilMobile.setError(null);
                } else {
                    registrationBinding.tilMobile.setErrorEnabled(true);
                    registrationBinding.tilMobile.setError("Please, enter mobile no");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        registrationBinding.etAddress.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().isEmpty()) {
                    registrationBinding.tilAddress.setErrorEnabled(false);
                    registrationBinding.tilAddress.setError(null);
                } else {
                    registrationBinding.tilAddress.setErrorEnabled(true);
                    registrationBinding.tilAddress.setError("Please, enter address");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private boolean checkRegistrationValidationSpinner() {
        return selectedStateCode != -1 && selectedCityCode != -1 && selectedCountryCode != -1;
    }

    private boolean checkRegistrationValidationFullName() {
        if (Objects.requireNonNull(registrationBinding.etFullName.getText()).toString().trim().isEmpty()) {
            registrationBinding.tilFullName.setErrorEnabled(true);
            registrationBinding.tilFullName.setError("Please, enter name");
            registrationBinding.etFullName.requestFocus();
            return false;
        } else if (!registrationBinding.etFullName.getText().toString().trim().isEmpty() && registrationBinding.etFullName.getText().toString().trim().length() < 3) {
            registrationBinding.tilFullName.setErrorEnabled(true);
            registrationBinding.tilFullName.setError("Name is too short.");
            registrationBinding.etFullName.requestFocus();
            return false;
        } else {
            registrationBinding.tilFullName.setErrorEnabled(false);
            registrationBinding.tilFullName.setError(null);
            return true;
        }
    }

    private boolean checkRegistrationValidationMobileNo() {
        if (Objects.requireNonNull(registrationBinding.etMobile.getText()).toString().trim().isEmpty()) {
            registrationBinding.tilMobile.setErrorEnabled(true);
            registrationBinding.tilMobile.setError("Please, enter mobile no");
            /*if (registrationBinding.tilFullName.getError() == null) {
                registrationBinding.etMobile.requestFocus();
            }*/
            return false;
        } else if (!registrationBinding.etMobile.getText().toString().trim().isEmpty() && registrationBinding.etMobile.getText().toString().trim().length() != 10) {
            registrationBinding.tilMobile.setErrorEnabled(true);
            registrationBinding.tilMobile.setError("Please, enter 10 digit mobile no");
            /*if (registrationBinding.tilFullName.getError() == null) {
                registrationBinding.etMobile.requestFocus();
            }*/
            return false;
        } else {
            //if (!registrationBinding.etMobile.getText().toString().isEmpty() && registrationBinding.etMobile.getText().toString().length() == 10) {
            registrationBinding.tilMobile.setErrorEnabled(false);
            registrationBinding.tilMobile.setError(null);
            return true;
        }
    }

    private boolean checkRegistrationValidationAddress() {
        if (Objects.requireNonNull(registrationBinding.etAddress.getText()).toString().trim().isEmpty()) {
            registrationBinding.tilAddress.setErrorEnabled(true);
            registrationBinding.tilAddress.setError("Please, enter address");
            /*if (registrationBinding.tilAddress.getError() == null) {
                registrationBinding.etAddress.requestFocus();
            }*/
            return false;
        } else if (registrationBinding.etAddress.getText().toString().trim().length() < 4) {
            registrationBinding.tilAddress.setErrorEnabled(true);
            registrationBinding.tilAddress.setError("Address is too short");
            /*if (registrationBinding.tilFullName.getError() == null) {
                registrationBinding.etAddress.requestFocus();
            }*/
            return false;
        } else {
            //if (!registrationBinding.etAddress.getText().toString().isEmpty() && registrationBinding.etAddress.getText().toString().length() == 10) {
            registrationBinding.tilAddress.setErrorEnabled(false);
            registrationBinding.tilAddress.setError(null);
            return true;
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    private void viewObserverCollection() {

        viewModel.getCustomerResponse.observe(this, customerDetails -> {
            if (customerDetails != null) {
                PreferenceManager.savePref(this, customerDetails.getFirstName(), CommonUtil.USER_F_NAME);
                if (customerDetails.getLastName() != null) {
                    PreferenceManager.savePref(this, customerDetails.getLastName(), CommonUtil.USER_L_NAME);
                }
                PreferenceManager.savePref(this, customerDetails.getMobNo(), CommonUtil.USER_MOBILE);
                /*if (customerDetails.getAddressLine1().trim().length() > 2) {
                    PreferenceManager.savePref(this, customerDetails.getAddressLine1(), CommonUtil.USER_ADDRESS);
                }*/
                PreferenceManager.savePref(this, String.valueOf(customerDetails.getContactId()), CommonUtil.USER_CONTACT_ID);

                new Handler().postDelayed(() -> {
                    Intent intentMain = new Intent(RegistrationActivity.this, MainActivity.class);
                    startActivity(intentMain);
                    RegistrationActivity.this.finish();
                }, 1500);
            } else {
                CommonUtil.showSnackBar(registrationBinding.llRegistrationSnackBar, registrationBinding.llRegistrationSnackBar, "User Registration Fail.");
            }


            //Log.e(TAG, "onChanged: customer is created" + customerDetails.toString());
        });

        viewModel.country.observe(this, countries -> {
            if (countries != null) {
                registrationBinding.tvCountryError.setText("");
                registrationBinding.ivCountryError.setVisibility(View.GONE);
                countries.sort(Comparator.comparing(Country::getCountriesName));
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
                    if (countryNames.get(i).trim().equalsIgnoreCase(tempCountryName.trim())) {
                        tempPos = i;
                    }
                }
                if (tempPos != -1) {
                    selectedCountryCode = tempPos;
                    registrationBinding.spinnerCountry.setSelection(tempPos);
                }
                countryAdapter.notifyDataSetChanged();
            } else {
                selectedCountryCode = -1;
                registrationBinding.tvCountryError.setText("Country list is not available.");
                registrationBinding.ivCountryError.setVisibility(View.VISIBLE);
                CommonUtil.showSnackBar(registrationBinding.llRegistrationSnackBar, registrationBinding.llRegistrationSnackBar, "Country list is not found.");
            }

        });

        viewModel.state.observe(this, states -> {
            Log.e(TAG, "viewObserverCollection: state" + countryNames.get(selectedCountryCode));
            Log.e(TAG, "viewObserverCollection: state" + Arrays.toString(countryNames.toArray()));
            if (states != null) {
                states.sort(Comparator.comparing(State::getStateName));
                stateNames.clear();
                stateCode.clear();
                stateAdapter.clear();
                for (State state : states) {
                    stateNames.add(state.getStateName());
                    stateCode.add(state.getStateName() + "-" + state.getStateCode());
                }

                if (stateNames.size() > 0) {
                    registrationBinding.tvStateError.setText("");
                    registrationBinding.ivStateError.setVisibility(View.GONE);
                    selectedStateCode = -1;
                    stateAdapter.notifyDataSetChanged();


                    //countryAdapter.notifyDataSetChanged();

                } else {
                    registrationBinding.tvStateError.setText("State list is not available.");
                    registrationBinding.ivStateError.setVisibility(View.VISIBLE);
                    selectedStateCode = -1;
                    CommonUtil.showSnackBar(registrationBinding.llRegistrationSnackBar, registrationBinding.llRegistrationSnackBar, "No State Available in " + countryNames.get(selectedCountryCode));
                }
            } else {
                registrationBinding.tvStateError.setText("State list is not available.");
                registrationBinding.ivStateError.setVisibility(View.VISIBLE);
                selectedStateCode = -1;
                CommonUtil.showSnackBar(registrationBinding.llRegistrationSnackBar, registrationBinding.llRegistrationSnackBar, "No State Available in " + countryNames.get(selectedCountryCode));
            }

        });

        viewModel.city.observe(this, cities -> {
            Log.e(TAG, "viewObserverCollection: city" + stateNames.get(selectedStateCode));
            Log.e(TAG, "viewObserverCollection: city" + Arrays.toString(stateNames.toArray()));
            if (cities != null) {
                cities.sort(Comparator.comparing(City::getCityName));
                cityNames.clear();
                cityCode.clear();
                cityAdapter.clear();
                for (City city : cities) {
                    cityNames.add(city.getCityName());
                    cityCode.add(city.getCityName() + "-" + city.getCityCode());
                }

                if (cityNames.size() > 0) {
                    registrationBinding.tvCityError.setText("");
                    registrationBinding.ivCityError.setVisibility(View.GONE);
                    selectedCityCode = -1;
                    cityAdapter.notifyDataSetChanged();
                } else {
                    registrationBinding.tvCityError.setText("City list is not available.");
                    registrationBinding.ivCityError.setVisibility(View.VISIBLE);
                    CommonUtil.showSnackBar(registrationBinding.llRegistrationSnackBar, registrationBinding.llRegistrationSnackBar, "No City Available in " + stateNames.get(selectedStateCode));
                }
            } else {
                registrationBinding.tvCityError.setText("City list is not available.");
                registrationBinding.ivCityError.setVisibility(View.VISIBLE);
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