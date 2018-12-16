package com.maxrescuerinc.myandroidapplication.Fragments.Home;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.maxrescuerinc.myandroidapplication.Models.User;
import com.maxrescuerinc.myandroidapplication.R;
import com.orm.SugarRecord;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
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
    String mCurrentPhotoPath;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        EditProfileFragmaentView = inflater.inflate(R.layout.fragment_edit_profile, container, false);
        findAllFields();
        UpdateTextView();
        final Button makePhotoButton = inf.findViewById(R.id.buttonMakePhoto);
        makePhotoButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if(ContextCompat.checkSelfPermission(getContext(),Manifest.permission.CAMERA)
                        == PackageManager.PERMISSION_GRANTED)
                {
                    Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(cameraIntent, MAKE_PHOTO_REQUEST);
                }else
                {
                    requestPermissions(new String[]{Manifest.permission.CAMERA},MAKE_PHOTO_REQUEST);
                }
            }
        });
        final Button chooseButton = inf.findViewById(R.id.buttonChosePhoto);
        chooseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent photoIntent = new Intent(Intent.ACTION_PICK);
                photoIntent.setType("image/*");
                startActivityForResult(photoIntent, LOAD_PHOTO_REQUEST);
            }
        });
        Button saveButton = inf.findViewById(R.id.buttonHomePersonEditSave);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            UpdeteUserFields();
            Navigation.findNavController(v).navigate(R.id.action_editProfileFragment_to_personFragment2);
            }
        });

       return inf;
    }

    private void findAllFields(){
        mSettings = Objects.requireNonNull(getContext()).getSharedPreferences(APP_PREFERENCES,Context.MODE_PRIVATE);
        LastName = EditProfileFragmaentView.findViewById(R.id.editTextLastNameHomeEditPerson);
        Name = EditProfileFragmaentView.findViewById(R.id.editTextNameHomeEditPerson);
        Email = EditProfileFragmaentView.findViewById(R.id.editTextEmailHomeEditPerson);
        PhoneNumber = EditProfileFragmaentView.findViewById(R.id.editTextPhoneNumberHomeEditPerson);
        PersonImage = EditProfileFragmaentView.findViewById(R.id.imageViewHomeEditPerson);
        PersonImage.setImageResource(R.drawable.ic_cake_black_24dp);
    }

    private void signUpClickMakePhoto(){
        Button button = EditProfileFragmaentView.findViewById(R.id.buttonMakePhoto);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ContextCompat.checkSelfPermission(getContext(),Manifest.permission.CAMERA)
                        == PackageManager.PERMISSION_GRANTED)
                {
                    Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(cameraIntent, MAKE_PHOTO_REQUEST);
                }else
                {
                    requestPermissions(new String[]{Manifest.permission.CAMERA},MAKE_PHOTO_REQUEST);
                }
            }
        });
    }

    private void UpdeteUserFields(){
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
                if(!user.URI.equals(selectedImage.toString()))
                {
                    user.URI = selectedImage.toString();
                }
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
                 }else
                 {
                     AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle(R.string.titleWarning)
                    .setMessage(R.string.CameraPermission)
                        .setCancelable(false)
                        .setNegativeButton(R.string.ok,
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        ActivityCompat.requestPermissions(getActivity(),
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
    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == LOAD_PHOTO_REQUEST && resultCode == RESULT_OK && null != data) {
            selectedImage = data.getData();
            PersonImage.setImageURI(selectedImage);


        }
        if(requestCode == MAKE_PHOTO_REQUEST && resultCode == RESULT_OK)
        {
            Bitmap thumbnailBitmap = (Bitmap) data.getExtras().get("data");
            PersonImage.setImageBitmap(thumbnailBitmap);
            File file = new File("image");
            try (OutputStream out = new FileOutputStream(file)){
                thumbnailBitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
                //file = createImageFile();
            } catch (IOException e) {
                e.printStackTrace();
            }

//            selectedImage = Uri.fromFile(file);
//            data.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));

//            ImageView imageView =  getActivity().findViewById(R.id.imageViewEdit);
//            imageView.setImageBitmap(thumbnailBitmap);
        }
    }


}
