package com.example.planningpoker.LoginAndRegister;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import com.example.planningpoker.AddAndViewRoom.MainFragment;
import com.example.planningpoker.R;

public class LoginActivity extends AppCompatActivity {

    public static FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initFragment();

    }

    //add fragment to the FrameLayout
    private void initFragment() {
        //if frame layout does exist
        if(findViewById(R.id.frameLayout)!=null){
            fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            LoginFragment loginFragment = new LoginFragment();
            fragmentTransaction.add(R.id.frameLayout, loginFragment, null);
            fragmentTransaction.commit();
        }
    }

}
