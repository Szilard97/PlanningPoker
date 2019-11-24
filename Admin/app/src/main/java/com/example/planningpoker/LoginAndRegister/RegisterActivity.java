package com.example.planningpoker.LoginAndRegister;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.planningpoker.AddAndViewRoom.MainActivity;
import com.example.planningpoker.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegisterActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private EditText rEmailEditText, rPasswordEditText;
    private Button rRegisterButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        bindWidget();

        register();

    }

    private void bindWidget() {
        rEmailEditText = findViewById(R.id.rEmailLabel);
        rPasswordEditText = findViewById(R.id.rPasswordLabel);
        rRegisterButton = findViewById(R.id.rButtonRegister);
        mAuth = FirebaseAuth.getInstance();
    }

    private void register() {

        rRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(readDataForRegistration()){
                    createNewUser();
                }
                else
                    Toast.makeText(getApplicationContext(), "Please enter your email address and password", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private Boolean readDataForRegistration() {

        if(!rEmailEditText.getText().toString().isEmpty() && !rPasswordEditText.getText().toString().isEmpty()){
            return  true;
        }
        return false;
    }

    private void createNewUser() {
        String email, password;

        email = rEmailEditText.getText().toString();
        password = rPasswordEditText.getText().toString();

        Log.d("alma", email);
        Log.d("alma",password);
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d("alma","Registration is successful");
                            Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                            finish();
                            startActivity(intent);

                        } else {
                            Log.d("alma", task.getException().toString());
                            Toast.makeText(RegisterActivity.this, "Authentication failed."+ task.getException().toString(),
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
