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

    private void initFragment() {
        if(findViewById(R.id.frameLayout)!=null){
            fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            LoginFragment loginFragment = new LoginFragment();
            fragmentTransaction.add(R.id.frameLayout, loginFragment, null);
            fragmentTransaction.commit();
        }
    }

    /*@Override
    public void onBackPressed() {
        new AlertDialog.Builder(getApplicationContext())
                .setMessage("Are you sure you want to exit?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        LoginActivity.super.onBackPressed();
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }*/

}
