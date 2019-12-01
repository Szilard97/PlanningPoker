package com.example.planningpoker.LoginAndRegister;


import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.example.planningpoker.AddAndViewRoom.MainFragment;
import com.example.planningpoker.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import static android.content.Context.MODE_PRIVATE;

/**
 * A simple {@link Fragment} subclass.
 */
public class LoginFragment extends Fragment {


    private CheckBox mCheckBoxRemember;
    private SharedPreferences mSharedPreference;
    private static final String PREFS_NAME="PreFsFile";
    private EditText lEmailEditText, lPasswordEditText;
    private Button lLoginButton, lRegisterButton;
    private FirebaseAuth mAuth;
    public static FragmentManager fragmentManager;

    public LoginFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view =  inflater.inflate(R.layout.fragment_login, container, false);

        bindWidget(view);

        getPreferencesData();

        loginButton();

        register();

        return view;

    }

    private void register() {
        lRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("korte", "itt vagyok");
                LoginActivity.fragmentManager.beginTransaction()
                        .replace(R.id.frameLayout, new RegisterFragment(), null).commit();
            }
        });
    }

    private void getPreferencesData() {

        SharedPreferences sp = this.getActivity().getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        if(sp.contains("pref_email")){
            String e = sp.getString("pref_email", "not found");
            lEmailEditText.setText(e.toString());
        }
        if(sp.contains("pref_pass")){
            String p = sp.getString("pref_pass", "not found");
            lPasswordEditText.setText(p.toString());
        }
        if(sp.contains("pref_check")){
            Boolean b = sp.getBoolean("pref_check", false);
            mCheckBoxRemember.setChecked(b);
        }

    }

    private void loginButton() {
        lLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                controlCheckBox();
                login();
            }
        });
    }

    private void login() {
        String email, password;
        email = lEmailEditText.getText().toString().trim();
        password = lPasswordEditText.getText().toString().trim();

        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    FirebaseUser user = mAuth.getCurrentUser();
                    Log.d("alma", user.getUid());

                    LoginActivity.fragmentManager.beginTransaction()
                            .replace(R.id.frameLayout, new MainFragment(), null).commit();
                }else{
                    Toast.makeText(getActivity(),"Login failed", Toast.LENGTH_SHORT).show();
                    Log.d("alma", task.getException().toString());
                }

            }
        });

    }

    private void controlCheckBox() {
        if(mCheckBoxRemember.isChecked()){
            saveElement();
        }else{
            mSharedPreference.edit().clear().apply();
        }
    }

    private void saveElement() {
        Boolean boolIsChecked = mCheckBoxRemember.isChecked();
        SharedPreferences.Editor editor = mSharedPreference.edit();
        editor.putString("pref_email", lEmailEditText.getText().toString());
        editor.putString("pref_pass", lPasswordEditText.getText().toString());
        editor.putBoolean("pref_check", boolIsChecked);
        editor.apply();
        //Toast.makeText(getApplicationContext(), "Email and password has ben saved", Toast.LENGTH_SHORT).show();
    }

    private void bindWidget(View view) {
        lEmailEditText = view.findViewById(R.id.rEmailLabel);
        lPasswordEditText = view.findViewById(R.id.rPasswordLabel);
        lLoginButton = view.findViewById(R.id.lButtonLogin);
        lRegisterButton = view.findViewById(R.id.lButtonRegister);
        mCheckBoxRemember = view.findViewById(R.id.lCheckBoxRememberMe);
        mSharedPreference = this.getActivity().getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        mAuth = FirebaseAuth.getInstance();
    }

}
