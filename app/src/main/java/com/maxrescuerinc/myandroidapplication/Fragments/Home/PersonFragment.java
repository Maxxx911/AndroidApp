package com.maxrescuerinc.myandroidapplication.Fragments.Home;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.maxrescuerinc.myandroidapplication.Activites.MainActivity;
import com.maxrescuerinc.myandroidapplication.Models.User;
import com.maxrescuerinc.myandroidapplication.R;
import com.orm.SugarRecord;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

public class PersonFragment extends Fragment{

    private final String APP_PREFERENCES_CURRENT_USER_ID = "current_user_id";
    private final String APP_PREFERENCES = "current_user_setting";
    private TextView LastName = null;
    private TextView Name = null;
    private TextView PhoneNumber = null;
    private TextView Email = null;
    private ImageView PersonImage = null;
    private SharedPreferences mSettings = null;
    private View PersonFragmentView;
    private Uri selectedImage= null;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)  {
        // Inflate the layout for this fragment
        PersonFragmentView = inflater.inflate(R.layout.fragment_person, container, false);
        findAllFields();
        signUpClickEdit();
        singUpClickSignOut();
        UpdateTextView();
        return PersonFragmentView;

    }

    private void signUpClickEdit(){
        Button button = PersonFragmentView.findViewById(R.id.button3);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(PersonFragmentView).navigate(R.id.action_personFragment2_to_editProfileFragment);
            }
        });
    }

    private void singUpClickSignOut(){
        Button button = PersonFragmentView.findViewById(R.id.buttonSignOut);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mSettings.contains(APP_PREFERENCES_CURRENT_USER_ID))
                {
                    SharedPreferences.Editor editor = mSettings.edit();
                    editor.putLong(APP_PREFERENCES_CURRENT_USER_ID,-1);
                    editor.apply();
                    Intent intent = new Intent(getActivity(),MainActivity.class);
                    startActivity(intent);
                    Objects.requireNonNull(getActivity()).finish();

                }
            }
        });
    }

    private File createImageFile() throws IOException {
        Long user_id = mSettings.getLong(APP_PREFERENCES_CURRENT_USER_ID,-1);
        String imageFileName = "Avatar_" + user_id.toString();
        File storageDir = PersonFragmentView.getContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        return new File(storageDir + "/" +imageFileName + ".jpg");
    }

    private void findAllFields(){
        LastName = PersonFragmentView.findViewById(R.id.lastNameHomeView);
        Name = PersonFragmentView.findViewById(R.id.nameHomeView);
        PhoneNumber = PersonFragmentView.findViewById(R.id.phoneNumberHomeView);
        Email = PersonFragmentView.findViewById(R.id.emailHomeView);
        PersonImage = PersonFragmentView.findViewById(R.id.imageViewHomePerson);
        mSettings = Objects.requireNonNull(getContext()).getSharedPreferences(APP_PREFERENCES,Context.MODE_PRIVATE);

    }

    private void UpdateTextView(){
        if(mSettings.contains(APP_PREFERENCES_CURRENT_USER_ID)){
            Long user_id = mSettings.getLong(APP_PREFERENCES_CURRENT_USER_ID,-1);
            User user = SugarRecord.findById(User.class, user_id);
            if(user != null) {
                LastName.setText(user.LastName);
                Name.setText(user.Name);
                Email.setText(user.Email);
                PhoneNumber.setText(user.PhoneNumber);
                File file = null;
                try {
                    file = createImageFile();
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
                if(file.exists())
                {
                    selectedImage = Uri.fromFile(file);
                    PersonImage.setImageURI(selectedImage);
                }else
                    PersonImage.setImageResource(R.drawable.ic_cake_black_24dp);
            }
        }
    }
}
