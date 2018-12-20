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
import android.os.StrictMode;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.maxrescuerinc.myandroidapplication.Activites.HomePageActivity;
import com.maxrescuerinc.myandroidapplication.Functions.UserFunction;
import com.maxrescuerinc.myandroidapplication.Interfaces.EditProfileFragmentListener;
import com.maxrescuerinc.myandroidapplication.Models.User;
import com.maxrescuerinc.myandroidapplication.R;
import com.orm.SugarRecord;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import static android.app.Activity.RESULT_OK;


public class EditProfileFragment extends Fragment implements EditProfileFragmentListener {

    private final short MAKE_PHOTO_REQUEST = 122;
    private final short LOAD_PHOTO_REQUEST = 123;
    private SharedPreferences mSettings = null;
    private TextView LastName = null;
    private TextView Name = null;
    private TextView PhoneNumber = null;
    private TextView Email = null;
    private ImageView PersonImage = null;
    private Uri selectedImage= null;
    private View EditProfileFragmentView;

    public void onCreate(Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        EditProfileFragmentView = inflater.inflate(R.layout.fragment_edit_profile, container, false);
        findAllFields();
        UpdateTextView();
        signUpClickMakePhoto();
        signUpClickChoosePhoto();
        signUpClickSave();
        ((HomePageActivity) getActivity()).setActivityListener(EditProfileFragment.this);
        ((HomePageActivity) getActivity()).HideBottomNavogation();
        return EditProfileFragmentView;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.aboutFragment) {
            AlertDialog.Builder builder = new AlertDialog.Builder(EditProfileFragmentView.getContext());
            builder.setTitle(R.string.titleWarning)
                    .setMessage(R.string.leave_page)
                    .setCancelable(false)
                    .setNegativeButton(R.string.no,
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            })
                    .setPositiveButton(R.string.ok,
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                    Navigation.findNavController(EditProfileFragmentView).navigate(R.id.action_editProfileFragment_to_personFragment2);
                                }
                            });

            AlertDialog alert = builder.create();
            alert.show();

        }
      return super.onOptionsItemSelected(item);

    }



    private void showToast(Integer text) {
        Toast toast = Toast.makeText(getContext(),text, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER,0,0);
        toast.show();
    }

    private void findAllFields(){
        String APP_PREFERENCES = "current_user_setting";
        mSettings = Objects.requireNonNull(getContext()).getSharedPreferences(APP_PREFERENCES,Context.MODE_PRIVATE);
        LastName = EditProfileFragmentView.findViewById(R.id.editTextLastNameHomeEditPerson);
        Name = EditProfileFragmentView.findViewById(R.id.editTextNameHomeEditPerson);
        Email = EditProfileFragmentView.findViewById(R.id.editTextEmailHomeEditPerson);
        PhoneNumber = EditProfileFragmentView.findViewById(R.id.editTextPhoneNumberHomeEditPerson);
        PersonImage = EditProfileFragmentView.findViewById(R.id.imageViewHomeEditPerson);
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
            PersonImage.setImageResource(R.drawable.default_photo);
    }

    private File createImageFile() throws IOException {
        String APP_PREFERENCES_CURRENT_USER_ID = "current_user_id";
        Long user_id = mSettings.getLong(APP_PREFERENCES_CURRENT_USER_ID,-1);
        String imageFileName = "Avatar_" + user_id.toString();
        File storageDir = EditProfileFragmentView.getContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        return new File(storageDir +"/" +imageFileName + ".jpg");
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(EditProfileFragmentView.getContext().getPackageManager()) != null) {
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
        Button button = EditProfileFragmentView.findViewById(R.id.buttonMakePhoto);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ContextCompat.checkSelfPermission(getContext(),Manifest.permission.CAMERA)
                        == PackageManager.PERMISSION_GRANTED)
                {
                    dispatchTakePictureIntent();

                }else
                {
                    requestPermissions(new String[]{Manifest.permission.CAMERA},MAKE_PHOTO_REQUEST);
                }
            }
        });
    }

    private void signUpClickChoosePhoto(){
        Button button = EditProfileFragmentView.findViewById(R.id.buttonChosePhoto);
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
        Button button = EditProfileFragmentView.findViewById(R.id.buttonHomePersonEditSave);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UpdateUserFields();
                showToast(R.string.edit_successful);
                Navigation.findNavController(v).navigate(R.id.action_editProfileFragment_to_personFragment2);
                ((HomePageActivity) getActivity()).ShowBottomNavigation();
            }
        });
    }

    private void UpdateUserFields(){
        Long user_id = UserFunction.getIdCurrentUser(mSettings);
        UserFunction.UpdateUser(user_id,Name.getText().toString(),LastName.getText().toString(),
                PhoneNumber.getText().toString(),null );
    }

    private void UpdateTextView()  {
        Long user_id = UserFunction.getIdCurrentUser(mSettings);
        User user = UserFunction.getCurrentUser(user_id);
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
                     AlertDialog.Builder builder = new AlertDialog.Builder(EditProfileFragmentView.getContext());
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


    @Override
    public void onBackPressed() {

    }

    @Override
    public void showBottomNavigation() {

    }

    @Override
    public void hideBottomNavigation() {

    }
}
