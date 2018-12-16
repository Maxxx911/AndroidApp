package com.maxrescuerinc.myandroidapplication.Fragments.Authorization;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.maxrescuerinc.myandroidapplication.Activites.HomePageActivity;
import com.maxrescuerinc.myandroidapplication.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Objects;

import static android.app.Activity.RESULT_OK;


public class SelectedImageFragment extends Fragment {

    private View SelectedImageFragmentView = null;
    private final String APP_PREFERENCES_CURRENT_USER_ID = "current_user_id";
    private final String APP_PREFERENCES = "current_user_setting";
    SharedPreferences mSettings = null;
    private final short MAKE_PHOTO_REQUEST = 122;
    private final short LOAD_PHOTO_REQUEST = 123;
    private Uri selectedImage= null;
    ImageView ImageView = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mSettings = Objects.requireNonNull(getContext()).getSharedPreferences(APP_PREFERENCES,Context.MODE_PRIVATE);
        SelectedImageFragmentView = inflater.inflate(R.layout.fragment_selected_image, container, false);
        ImageView = SelectedImageFragmentView.findViewById(R.id.imageViewSelected);
        signUpClickChoosePhoto();
        signUpClickMakePhoto();
        signUpFinishRegister();
        return SelectedImageFragmentView;
    }

    private File createImageFile() throws IOException {
        Long user_id = mSettings.getLong(APP_PREFERENCES_CURRENT_USER_ID,-1);
        String imageFileName = "Avatar_" + user_id.toString();
        File storageDir = SelectedImageFragmentView.getContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = new File(storageDir +"/" +imageFileName + ".jpg");
        return image;
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(SelectedImageFragmentView.getContext().getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                //REWORK

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
        Button button =SelectedImageFragmentView.findViewById(R.id.buttonSelectedMakePhoto);
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
        Button button = SelectedImageFragmentView.findViewById(R.id.buttonSelectedChosePhoto);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent photoIntent = new Intent(Intent.ACTION_PICK);
                photoIntent.setType("image/*");
                startActivityForResult(photoIntent, LOAD_PHOTO_REQUEST);
            }
        });
    }

    private void signUpFinishRegister(){
        Button button = SelectedImageFragmentView.findViewById(R.id.buttonSelectedFinishRegister);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),HomePageActivity.class);
                startActivity(intent);
            }
        });
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
                    AlertDialog.Builder builder = new AlertDialog.Builder(SelectedImageFragmentView.getContext());
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
                ImageView.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if(requestCode == MAKE_PHOTO_REQUEST && resultCode == RESULT_OK) {
            ImageView.setImageURI(selectedImage);
        }
    }



}
