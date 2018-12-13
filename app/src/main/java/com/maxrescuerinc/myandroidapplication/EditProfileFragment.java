package com.maxrescuerinc.myandroidapplication;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Camera;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import static android.app.Activity.RESULT_OK;


public class EditProfileFragment extends Fragment {

    private final short MAKE_PHOTO_REQUEST = 122;
    private final short LOAD_PHOTO_REQUEST = 123;
    private final short MY_PERMISSION_ACCESS = 111;
    final int CAMERA_ID = 0;
    final boolean FULL_SCREEN = true;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View inf = inflater.inflate(R.layout.fragment_edit_profile, container, false);
//        ImageView image = inf.findViewById(R.id.imageViewEdit);
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
       return inf;
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
                                                MY_PERMISSION_ACCESS);
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
            Uri selectedImage = data.getData();
            ImageView imageView =  getActivity().findViewById(R.id.imageViewEdit);
            imageView.setImageURI(selectedImage);

        }
        if(requestCode == MAKE_PHOTO_REQUEST && resultCode == RESULT_OK)
        {
            Bitmap thumbnailBitmap = (Bitmap) data.getExtras().get("data");
            ImageView imageView =  getActivity().findViewById(R.id.imageViewEdit);
            imageView.setImageBitmap(thumbnailBitmap);
        }
    }

}
