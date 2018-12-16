package com.maxrescuerinc.myandroidapplication.Fragments.Authorization;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.maxrescuerinc.myandroidapplication.Models.User;
import com.maxrescuerinc.myandroidapplication.R;
import com.orm.SugarRecord;

import java.util.List;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import static android.app.Activity.RESULT_OK;


public class RegisterFragment extends Fragment {

    private final short MAKE_PHOTO_REQUEST = 122;
    private final short LOAD_PHOTO_REQUEST = 123;
    private EditText LastName = null;
    private EditText Name = null;
    private EditText Email = null;
    private EditText PhoneNumber = null;
    private EditText Password = null;
    private EditText PasswordConfirm = null;
    private ImageView PersonImage = null;
    private Uri selectedImage= null;
    private String ImagePath = null;
    private View RegisterFormView = null;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        RegisterFormView = inflater.inflate(R.layout.fragment_register, container, false);
        findAllFields();
        PersonImage.setImageResource(R.drawable.ic_cake_black_24dp);
        signUpMakePhoto();
        signUpChoosePhoto();
        signUpRegister();
        return RegisterFormView;
    }

    private void signUpRegister(){
        Button button = RegisterFormView.findViewById(R.id.buttonRegisterRegister);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(inputFormValidate(LastName.getText().toString(),Name.getText().toString(),
                        Email.getText().toString(),PhoneNumber.getText().toString(),
                        Password.getText().toString(), PasswordConfirm.getText().toString())){
                    List<User> users = SugarRecord.find(User.class,"Email = ?",Email.getText().toString());
                    if(users.isEmpty()){
                        if(ImagePath != null){
                            User userModel = new User(LastName.getText().toString(),
                                    Name.getText().toString(), Email.getText().toString(),
                                    PhoneNumber.getText().toString(), Password.getText().toString(), ImagePath);
                            userModel.save();
                            Navigation.findNavController(RegisterFormView).navigate(R.id.action_registerFragment_to_logInFragment);
                            showToast(R.string.register_successful);
                        }
                        else {
                            User userModel = new User(LastName.getText().toString(),
                                    Name.getText().toString(), Email.getText().toString(),
                                    PhoneNumber.getText().toString(), Password.getText().toString());
                            userModel.save();
                            Navigation.findNavController(RegisterFormView).navigate(R.id.action_registerFragment_to_logInFragment);
                            showToast(R.string.register_successful);
                        }
                    }
                    else{
                        showToast(R.string.user_error);
                        Email.setText("");
                        Password.setText("");
                        PasswordConfirm.setText("");
                    }
                }
            }
        });
    }

    private void signUpChoosePhoto(){
        Button button = RegisterFormView.findViewById(R.id.buttonRegisterChosePhoto);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent photoIntent = new Intent(Intent.ACTION_PICK);
                photoIntent.setType("image/*");
                startActivityForResult(photoIntent, LOAD_PHOTO_REQUEST);
            }
        });
    }

    private void signUpMakePhoto(){
        Button button = RegisterFormView.findViewById(R.id.buttonRegisterMakePhoto);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ContextCompat.checkSelfPermission(Objects.requireNonNull(getContext()),Manifest.permission.CAMERA)
                        == PackageManager.PERMISSION_GRANTED) {
                    Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(cameraIntent, MAKE_PHOTO_REQUEST);
                }
                else {
                    requestPermissions(new String[]{Manifest.permission.CAMERA},MAKE_PHOTO_REQUEST);
                }
            }
        });
    }

    private void findAllFields(){
        LastName = RegisterFormView.findViewById(R.id.editRegisterLastName);
        Name = RegisterFormView.findViewById(R.id.editRegisterName);
        Email = RegisterFormView.findViewById(R.id.editRegisterEmail);
        PhoneNumber = RegisterFormView.findViewById(R.id.editRegisterPhoneNumber);
        Password = RegisterFormView.findViewById(R.id.editRegisterPassword);
        PasswordConfirm = RegisterFormView.findViewById(R.id.editRegisterPasswordConfirm);
        PersonImage = RegisterFormView.findViewById(R.id.imageViewRegisterEdit);
    }

    private void showToast(Integer text) {
        Toast toast = Toast.makeText(getContext(),text, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER,0,0);
        toast.show();
    }

    private boolean inputFormValidate(String lastName, String name, String email,
                                      String phoneNumber, String password, String passwordConfirm){
        if(lastName.equals("") || name.equals("") || email.equals("") || phoneNumber.equals("")
                || password.equals("") || passwordConfirm.equals("")) {
           showToast(R.string.empty_field_error);
            return false;
        }
        if(!email.contains("@")) {
            showToast(R.string.email_error);
            Email.setText("");
            return false;
        }
        if(!email.contains(".")) {
            showToast(R.string.email_point_error);
            Email.setText("");
            return false;
        }
        if(!password.equals(passwordConfirm)) {
            PasswordConfirm.setText("");
            Password.setText("");
            showToast(R.string.confirm_password_error);
            return false;
        }
        if(password.length()<7 ) {
            showToast(R.string.password_length_error);
            Password.setText("");
            PasswordConfirm.setText("");
            return false;
        }
        if(!phoneNumber.startsWith("+")) {
            showToast(R.string.phone_plus_error);
            PhoneNumber.setText("");
            return false;
        }
        if(phoneNumber.length()<13 || phoneNumber.length()>13) {
            PhoneNumber.setText("");
            showToast(R.string.phone_length_error);
            return false;
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case(MAKE_PHOTO_REQUEST): {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(cameraIntent, MAKE_PHOTO_REQUEST);
                }
                else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(RegisterFormView.getContext());
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
           if(selectedImage != null)
            ImagePath =  selectedImage.toString();
           PersonImage.setImageURI(selectedImage);
        }
        if(requestCode == MAKE_PHOTO_REQUEST && resultCode == RESULT_OK) {
            Bitmap thumbnailBitmap = (Bitmap) Objects.requireNonNull(data.getExtras()).get("data");
            PersonImage.setImageBitmap(thumbnailBitmap);
//            ImageView imageView =  getActivity().findViewById(R.id.imageViewEdit);
//            imageView.setImageBitmap(thumbnailBitmap);
        }
    }
}
