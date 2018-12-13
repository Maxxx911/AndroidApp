package com.maxrescuerinc.myandroidapplication;

import android.content.Context;
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
import android.widget.ImageView;

public class PersonFragment extends Fragment implements View.OnClickListener {



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)  {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_person, container, false);
        Button b = (Button) v.findViewById(R.id.button3);
        ImageView imageView = v.findViewById(R.id.personImageView);

        b.setOnClickListener(this);
        return v;

    }

    @Override
    public void onClick(View view)
    {
        Navigation.findNavController(view).navigate(R.id.action_personFragment2_to_editProfileFragment);
    }
}
