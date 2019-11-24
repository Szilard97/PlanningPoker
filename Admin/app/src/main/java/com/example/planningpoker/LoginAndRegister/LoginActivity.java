package com.example.planningpoker.LoginAndRegister;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.example.planningpoker.AddAndViewRoom.MainActivity;
import com.example.planningpoker.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    private CheckBox mCheckBoxRemember;
    private SharedPreferences mSharedPreference;
    private static final String PREFS_NAME="PreFsFile";
    private EditText lEmailEditText, lPasswordEditText;
    private Button lLoginButton, lRegisterButton;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        bindWidget();

        getPreferencesData();

        loginButton();

        register();

    }

    private void register() {
        lRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
    }

    private void getPreferencesData() {

        SharedPreferences sp = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
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

                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    finish();
                    startActivity(intent);
                }else{
                    Toast.makeText(getApplicationContext(),"Login failed", Toast.LENGTH_SHORT).show();
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
        Toast.makeText(getApplicationContext(), "Email and password has ben saved", Toast.LENGTH_SHORT).show();
    }

    private void bindWidget() {
        lEmailEditText = findViewById(R.id.rEmailLabel);
        lPasswordEditText = findViewById(R.id.rPasswordLabel);
        lLoginButton = findViewById(R.id.lButtonLogin);
        lRegisterButton = findViewById(R.id.lButtonRegister);
        mCheckBoxRemember = findViewById(R.id.lCheckBoxRememberMe);
        mSharedPreference = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        mAuth = FirebaseAuth.getInstance();
    }
}
