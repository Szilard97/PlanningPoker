package com.example.myplanningpokeruser.LoginAndRegister;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.myplanningpokeruser.R;
import com.example.myplanningpokeruser.ViewQuestion.MainActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    EditText rEmailEditText, rPasswordEditText;
    Button rRegisterButton;
    private DatabaseReference myRef;
    private static Integer numberOfUser= 0;

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
                    Toast.makeText(getApplicationContext(), "Please enter your email addren and password", Toast.LENGTH_SHORT).show();
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
                            addNewUser();
                            Log.d("alma","Registration is successful");
                            Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                            finish();
                            startActivity(intent);

                        } else {
                            Log.d("alma", task.getException().toString());
                            Toast.makeText(RegisterActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void addNewUser() {


        String email = rEmailEditText.getText().toString();

        String [] user = email.split("@");

        ++numberOfUser;

        myRef = FirebaseDatabase.getInstance().getReference();

        myRef.child("Users").child(user[0]).setValue("");

        

        myRef.child("Users").setValue(numberOfUser);

    }
}
