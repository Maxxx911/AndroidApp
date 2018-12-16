package com.maxrescuerinc.myandroidapplication.Fragments.Home;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.maxrescuerinc.myandroidapplication.R;

import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

public class AboutFragment extends Fragment {

    private TextView textView = null;
    private final short READ_PHONE_STATE_REQUEST = 100;
    private View AboutFragmentView;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        AboutFragmentView = inflater.inflate(R.layout.fragment_about, container, false);
        textView = AboutFragmentView.findViewById(R.id.textIMEI);
        if (ContextCompat.checkSelfPermission(AboutFragmentView.getContext(), Manifest.permission.READ_PHONE_STATE)
                == PackageManager.PERMISSION_GRANTED) {
            ShowIMEI();
        }
        else {
            requestPermissions(new String[]{Manifest.permission.CAMERA},READ_PHONE_STATE_REQUEST );
        }
        return AboutFragmentView;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case(READ_PHONE_STATE_REQUEST): {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    ShowIMEI();
                }
            }
        }
    }

    private void ShowIMEI() {
        if (ContextCompat.checkSelfPermission(AboutFragmentView.getContext(), Manifest.permission.READ_PHONE_STATE)
                != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.READ_PHONE_STATE},  READ_PHONE_STATE_REQUEST);
        } else
        {
            TelephonyManager telephonyManager = (TelephonyManager) Objects.requireNonNull(getContext()).getSystemService(Context.TELEPHONY_SERVICE);
            @SuppressLint("HardwareIds") String imei = telephonyManager.getDeviceId();
            textView.setText(imei);
        }
    }


}
