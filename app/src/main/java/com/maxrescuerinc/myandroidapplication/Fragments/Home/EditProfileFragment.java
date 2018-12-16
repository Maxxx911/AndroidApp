package com.maxrescuerinc.myandroidapplication.Fragments.Home;

import android.Manifest;
import android.app.ActionBar;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.maxrescuerinc.myandroidapplication.Models.User;
import com.maxrescuerinc.myandroidapplication.R;
import com.orm.SugarRecord;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.ThemedSpinnerAdapter;
import androidx.constraintlayout.solver.widgets.Helper;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.core.graphics.PathUtils;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import static android.app.Activity.RESULT_OK;


public class EditProfileFragment extends Fragment {

    private final String APP_PREFERENCES_CURRENT_USER_ID = "current_user_id";
    private final String APP_PREFERENCES = "current_user_setting";
    private final short MAKE_PHOTO_REQUEST = 122;
    private final short LOAD_PHOTO_REQUEST = 123;
    private final short MY_PERMISSION_ACCESS = 111;
    final int CAMERA_ID = 0;
    final boolean FULL_SCREEN = true;
    SharedPreferences mSettings = null;
    private TextView LastName = null;
    private TextView Name = null;
    private TextView PhoneNumber = null;
    private TextView Email = null;
    private ImageView PersonImage = null;
    private Uri selectedImage= null;
    private View EditProfileFragmaentView;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        EditProfileFragmaentView = inflater.inflate(R.layout.fragment_edit_profile, container, false);
        findAllFields();
        UpdateTextView();
        signUpClickMakePhoto();
        signUpClickChoosePhoto();
        signUpClickSave();
       return EditProfileFragmaentView;
    }

    private void showToast(Integer text) {
        Toast toast = Toast.makeText(getContext(),text, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER,0,0);
        toast.show();
    }

    private void findAllFields(){
        mSettings = Objects.requireNonNull(getContext()).getSharedPreferences(APP_PREFERENCES,Context.MODE_PRIVATE);
        LastName = EditProfileFragmaentView.findViewById(R.id.editTextLastNameHomeEditPerson);
        Name = EditProfileFragmaentView.findViewById(R.id.editTextNameHomeEditPerson);
        Email = EditProfileFragmaentView.findViewById(R.id.editTextEmailHomeEditPerson);
        PhoneNumber = EditProfileFragmaentView.findViewById(R.id.editTextPhoneNumberHomeEditPerson);
        PersonImage = EditProfileFragmaentView.findViewById(R.id.imageViewHomeEditPerson);
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

    private File createImageFile() throws IOException {
        Long user_id = mSettings.getLong(APP_PREFERENCES_CURRENT_USER_ID,-1);
        String imageFileName = "Avatar_" + user_id.toString();
        File storageDir = EditProfileFragmaentView.getContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = new File(storageDir +"/" +imageFileName + ".jpg");
        return image;
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(EditProfileFragmaentView.getContext().getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                //REWORK
                showToast(R.string.email_error);
            }
            if (photoFile != null) {
                selectedImage = Uri.fromFile(photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, selectedImage);
                startActivityForResult(takePictureIntent, MAKE_PHOTO_REQUEST);

            }
        }
    }

    private void signUpClickMakePhoto(){
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        Button button = EditProfileFragmaentView.findViewById(R.id.buttonMakePhoto);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ContextCompat.checkSelfPermission(getContext(),Manifest.permission.CAMERA)
                        == PackageManager.PERMISSION_GRANTED)
                {
                    dispatchTakePictureIntent();
//                    Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                    startActivityForResult(cameraIntent, MAKE_PHOTO_REQUEST);
                }else
                {
                    requestPermissions(new String[]{Manifest.permission.CAMERA},MAKE_PHOTO_REQUEST);
                }
            }
        });
    }

    private void signUpClickChoosePhoto(){
        Button button = EditProfileFragmaentView.findViewById(R.id.buttonChosePhoto);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent photoIntent = new Intent(Intent.ACTION_PICK);
                photoIntent.setType("image/*");
                startActivityForResult(photoIntent, LOAD_PHOTO_REQUEST);
            }
        });
    }

    private void signUpClickSave(){
        Button button = EditProfileFragmaentView.findViewById(R.id.buttonHomePersonEditSave);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UpdateUserFields();
                showToast(R.string.edit_successful);
                Navigation.findNavController(v).navigate(R.id.action_editProfileFragment_to_personFragment2);
            }
        });
    }

    private void UpdateUserFields(){
        if(mSettings.contains(APP_PREFERENCES_CURRENT_USER_ID))
        {
            Long user_id = mSettings.getLong(APP_PREFERENCES_CURRENT_USER_ID,-1);
            User user = SugarRecord.findById(User.class, user_id);
            if(user != null)
            {
                user.LastName = LastName.getText().toString();
                user.Email = Email.getText().toString();
                user.Name = Name.getText().toString();
                user.PhoneNumber = PhoneNumber.getText().toString();
                user.save();
            }
        }
    }

    private void UpdateTextView()  {
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
                if(user.URI!=null)
                    PersonImage.setImageURI(Uri.parse(user.URI));
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case(MAKE_PHOTO_REQUEST): {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(cameraIntent, MAKE_PHOTO_REQUEST);
                 }
                 else {
                     AlertDialog.Builder builder = new AlertDialog.Builder(EditProfileFragmaentView.getContext());
                builder.setTitle(R.string.titleWarning)
                    .setMessage(R.string.CameraPermission)
                        .setCancelable(false)
                        .setNegativeButton(R.string.ok,
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        ActivityCompat.requestPermissions(Objects.requireNonNull(getActivity()),
                                                new String[]{Manifest.permission.CAMERA},
                                                MAKE_PHOTO_REQUEST);
                                        dialog.cancel();
                                    }
                                });
                AlertDialog alert = builder.create();
                alert.show();
                 }

            }
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == LOAD_PHOTO_REQUEST && resultCode == RESULT_OK && null != data) {
            selectedImage = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(Objects.requireNonNull(getActivity()).getContentResolver(), selectedImage);
                File photoFile = createImageFile();
                FileOutputStream fileOutputStream = new FileOutputStream(photoFile);
                bitmap.compress(Bitmap.CompressFormat.PNG,100, fileOutputStream);
                PersonImage.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if(requestCode == MAKE_PHOTO_REQUEST && resultCode == RESULT_OK) {
            PersonImage.setImageURI(selectedImage);
        }
    }


}
