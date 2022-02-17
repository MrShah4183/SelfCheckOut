package com.vasyerp.selfcheckout.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkRequest;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

public class ConnectivityStatus extends LiveData<Boolean> {
    private Context context;
    private ConnectivityManager.NetworkCallback callback;
    private Network network;
    private ConnectivityManager manager;

    public ConnectivityStatus(Context context) {
        this.context = context;
        manager = (ConnectivityManager) this.context.getSystemService(Context.CONNECTIVITY_SERVICE);
    }

    private ConnectivityManager.NetworkCallback getCallback() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            callback = new ConnectivityManager.NetworkCallback() {
                @Override
                public void onAvailable(@NonNull Network network) {
                    super.onAvailable(network);
                    postValue(true);
                }

                @Override
                public void onLost(@NonNull Network network) {
                    super.onLost(network);
                    postValue(false);
                }

                @Override
                public void onUnavailable() {
                    super.onUnavailable();
                    postValue(false);
                }
            };
        }
        return callback;
    }

    private void checkInternet() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            network = manager.getActiveNetwork();
            if (network == null) {
                postValue(false);
            }
            NetworkRequest.Builder request = new NetworkRequest.Builder();
            request.addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
                    .addCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)
                    .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
                    .addTransportType(NetworkCapabilities.TRANSPORT_ETHERNET)
                    .addTransportType(NetworkCapabilities.TRANSPORT_BLUETOOTH)
                    .addTransportType(NetworkCapabilities.TRANSPORT_WIFI);

            manager.registerNetworkCallback(request.build(), getCallback());
        }
    }

    @Override
    protected void onActive() {
        super.onActive();
        checkInternet();
    }

    @Override
    protected void onInactive() {
        super.onInactive();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            manager.unregisterNetworkCallback(callback);
        }
    }
}
