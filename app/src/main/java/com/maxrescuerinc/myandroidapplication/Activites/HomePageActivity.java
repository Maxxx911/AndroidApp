package com.maxrescuerinc.myandroidapplication.Activites;

import android.Manifest;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.maxrescuerinc.myandroidapplication.Interfaces.EditProfileFragmentListener;
import com.maxrescuerinc.myandroidapplication.R;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.util.Objects;

public class HomePageActivity extends AppCompatActivity  {

    public EditProfileFragmentListener editProfileFragmentListener;
    private NavController navController = null;
    private BottomNavigationView bottomNavigationView = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        navController = Navigation.findNavController(HomePageActivity.this,R.id.my_nav_host_fragment);
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        Toolbar toolbar =  findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        NavigationUI.setupWithNavController(bottomNavigationView, navController);
        if (!getResources().getBoolean(R.bool.dev_mode)) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }

    }

    public void setActivityListener(EditProfileFragmentListener activityListener) {
        this.editProfileFragmentListener= activityListener;
    }

    public void HideBottomNavogation(){
        if(editProfileFragmentListener != null){
           bottomNavigationView.setVisibility(View.GONE);
        }
    }

    public void ShowBottomNavigation(){
        if(editProfileFragmentListener != null){
            bottomNavigationView.setVisibility(View.VISIBLE);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.right_menu, menu);


        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.aboutFragment) {
            AlertDialog.Builder builder = new AlertDialog.Builder(HomePageActivity.this);
            builder.setTitle(R.string.titleWarning)
                    .setMessage(R.string.go_to_about)
                    .setCancelable(false)
                    .setNegativeButton(R.string.no,
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            })
                    .setPositiveButton(R.string.yes,
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    navController.navigate(R.id.aboutFragment);
                                    dialog.cancel();
                                }
                            });
            AlertDialog alert = builder.create();
            alert.show();


        }
        return true;

    }
}
