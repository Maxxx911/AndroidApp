package com.maxrescuerinc.myandroidapplication.Fragments.Authorization;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.maxrescuerinc.myandroidapplication.Activites.HomePageActivity;
import com.maxrescuerinc.myandroidapplication.Models.User;
import com.maxrescuerinc.myandroidapplication.R;
import com.orm.SugarRecord;

import java.util.List;
import java.util.Objects;


public class LogInFragment extends Fragment {

    private EditText Email = null;
    private EditText Password = null;
    private final String APP_PREFERENCES_CURRENT_USER_ID = "current_user_id";
    private SharedPreferences setting;
    private View LoginFragmentView;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        LoginFragmentView = inflater.inflate(R.layout.fragment_log_in, container, false);
        String APP_PREFERENCES = "current_user_setting";
        setting = Objects.requireNonNull(getContext()).getSharedPreferences(APP_PREFERENCES,Context.MODE_PRIVATE);
        Email = LoginFragmentView.findViewById(R.id.LoginEmail);
        Password = LoginFragmentView.findViewById(R.id.editLoginPassword);
        signUpClickLogIn();
        signUpClickRegister();
        return LoginFragmentView;
    }

    private void signUpClickLogIn()
    {
        Button button = LoginFragmentView.findViewById(R.id.buttonLoginLogin);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<User> users = SugarRecord.find(User.class,"Email = ?",Email.getText().toString());
                SharedPreferences.Editor editor = setting.edit();
                if(users.isEmpty()) {
                    showToast(R.string.no_such_email_error);
                    Email.setText("");
                    Password.setText("");
                }
                else {
                    User current_user = users.get(0);
                    if(!current_user.Password.equals(Password.getText().toString())) {
                        Password.setText("");
                            showToast(R.string.password_wrong);
                    }
                    else{
                        editor.putLong(APP_PREFERENCES_CURRENT_USER_ID,current_user.getId());
                        editor.apply();
                        Intent intent = new Intent(getActivity(),HomePageActivity.class);
                        startActivity(intent);
                    }
                }
            }
        });
    }

    private void signUpClickRegister() {
        Button button = LoginFragmentView.findViewById(R.id.buttonLoginRegister);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.action_logInFragment_to_registerFragment);
            }
        });
    }

    private void showToast(Integer text) {
        Toast toast = Toast.makeText(getContext(),text, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER,0,0);
        toast.show();
    }


}
