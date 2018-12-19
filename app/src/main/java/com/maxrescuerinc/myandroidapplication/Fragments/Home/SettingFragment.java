package com.maxrescuerinc.myandroidapplication.Fragments.Home;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.maxrescuerinc.myandroidapplication.Functions.UserFunction;
import com.maxrescuerinc.myandroidapplication.R;

import java.util.Objects;

public class SettingFragment extends Fragment {

    private View settingFragmentView;
    private Button buttonRss;
    private TextView textViewRssLink;
    private final String APP_PREFERENCES_CURRENT_USER_ID = "current_user_id";
    private final String APP_PREFERENCES = "current_user_setting";
    private SharedPreferences mSettings = null;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        settingFragmentView = inflater.inflate(R.layout.fragment_setting, container, false);
        buttonRss = settingFragmentView.findViewById(R.id.buttonSettingFragment);
        textViewRssLink = settingFragmentView.findViewById(R.id.editTextSettingFragment);
        mSettings = Objects.requireNonNull(getContext()).getSharedPreferences(APP_PREFERENCES,Context.MODE_PRIVATE);
        signUpClickSetRssLinkButton();
        return settingFragmentView;
    }

    public void signUpClickSetRssLinkButton(){
        buttonRss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Long user_id = mSettings.getLong(APP_PREFERENCES_CURRENT_USER_ID,-1);
                UserFunction.UpdateUserRssLink(user_id,textViewRssLink.getText().toString());
            }
        });
    }

}
