package com.maxrescuerinc.myandroidapplication.Activites;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.maxrescuerinc.myandroidapplication.R;
import com.orm.SugarContext;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

public class MainActivity extends AppCompatActivity {

//    final private int MY_PERMISSION_ACCESS = 123;
//    private BottomNavigationView BottomNavigationView = null;
    private NavController navController = null;
    SharedPreferences mSettings = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        navController = Navigation.findNavController(MainActivity.this,R.id.my_fragment);
        Toolbar toolbar =  findViewById(R.id.toolbar1);
        SugarContext.init(MainActivity.this);
        setSupportActionBar(toolbar);
        String APP_PREFERENCES = "current_user_setting";
        mSettings = getSharedPreferences(APP_PREFERENCES,Context.MODE_PRIVATE);
        String APP_PREFERENCES_CURRENT_USER_ID = "current_user_id";
        if(mSettings.contains(APP_PREFERENCES_CURRENT_USER_ID))
        {
            Long user_id = mSettings.getLong(APP_PREFERENCES_CURRENT_USER_ID,-1);
            if(user_id != -1)
            {
                Intent intent = new Intent(this,HomePageActivity.class);
                startActivity(intent);
                finish();
            }
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
            navController.navigate(R.id.aboutFragment2);
//            Intent intent = new Intent(this, HomePageActivity.class);
//            startActivity(intent);
        }
        return true;
    }
}
