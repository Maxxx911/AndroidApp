package com.maxrescuerinc.myandroidapplication.Fragments.Home;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.NavHost;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.maxrescuerinc.myandroidapplication.Activites.HomePageActivity;
import com.maxrescuerinc.myandroidapplication.Activites.MainActivity;
import com.maxrescuerinc.myandroidapplication.Models.User;
import com.maxrescuerinc.myandroidapplication.R;
import com.orm.SugarRecord;

import org.w3c.dom.Text;

import java.util.List;

public class PersonFragment extends Fragment implements View.OnClickListener {

    private final String APP_PREFERENCES_CURRENT_USER_ID = "current_user_id";
    private final String APP_PREFERENCES = "current_user_setting";
    private TextView LastName = null;
    private TextView Name = null;
    private TextView PhoneNumber = null;
    private TextView Email = null;
    private ImageView PersonImage = null;
    SharedPreferences mSettings = null;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)  {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_person, container, false);
        Button b = (Button) v.findViewById(R.id.button3);
        LastName = v.findViewById(R.id.lastNameHomeView);
        Name = v.findViewById(R.id.nameHomeView);
        PhoneNumber = v.findViewById(R.id.phoneNumberHomeView);
        Email = v.findViewById(R.id.emailHomeView);
        PersonImage = v.findViewById(R.id.imageViewHomePerson);
        mSettings = getContext().getSharedPreferences(APP_PREFERENCES,Context.MODE_PRIVATE);
        Button signOutbutton = v.findViewById(R.id.buttonSignOut);
        signOutbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mSettings.contains(APP_PREFERENCES_CURRENT_USER_ID))
                {
                    SharedPreferences.Editor editor = mSettings.edit();
                    editor.putLong(APP_PREFERENCES_CURRENT_USER_ID,-1);
                    editor.apply();
                    Intent intent = new Intent(getActivity(),MainActivity.class);
                    startActivity(intent);
                    getActivity().finish();

                }
            }
        });
        UpdateTextView();
        b.setOnClickListener(this);
        return v;

    }

    private void UpdateTextView(){
        if(mSettings.contains(APP_PREFERENCES_CURRENT_USER_ID))
        {
            Long user_id = mSettings.getLong(APP_PREFERENCES_CURRENT_USER_ID,-1);
            User user = SugarRecord.findById(User.class, user_id);
            if(user != null)
            {
            LastName.setText(user.LastName);
            Name.setText(user.Name);
            Email.setText(user.Email);
            PhoneNumber.setText(user.PhoneNumber);
            PersonImage.setImageURI(Uri.parse(user.URI));
            }
        }
    }

    @Override
    public void onClick(View view)
    {
        Navigation.findNavController(view).navigate(R.id.action_personFragment2_to_editProfileFragment);
    }
}
