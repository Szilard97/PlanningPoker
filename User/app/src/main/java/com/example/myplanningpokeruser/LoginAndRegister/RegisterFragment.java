package com.example.myplanningpokeruser.LoginAndRegister;


import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.myplanningpokeruser.R;
import com.example.myplanningpokeruser.ViewQuestion.MainActivity;
import com.example.myplanningpokeruser.ViewQuestion.MainFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * A simple {@link Fragment} subclass.
 */
public class RegisterFragment extends Fragment {

    private FirebaseAuth mAuth;
    EditText rEmailEditText, rPasswordEditText;
    Button rRegisterButton;
    private DatabaseReference myRef;

    public RegisterFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_register, container, false);

        bindWidget(view);

        register();

        return view;
    }

    private void bindWidget(View view) {
        rEmailEditText = view.findViewById(R.id.rEmailLabel);
        rPasswordEditText = view.findViewById(R.id.rPasswordLabel);
        rRegisterButton = view.findViewById(R.id.rButtonRegister);
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
                    Toast.makeText(getActivity(), "Please enter your email addren and password", Toast.LENGTH_SHORT).show();
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
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            addNewUser();
                            Log.d("alma","Registration is successful");

                            MainActivity.fragmentManager.beginTransaction()
                                    .replace(R.id.frameLayout, new MainFragment(), null).commit();

                        } else {
                            Log.d("alma", task.getException().toString());
                            Toast.makeText(getActivity(), "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void addNewUser() {


        String emailNickName = rEmailEditText.getText().toString();

        String [] user = emailNickName.split("@");

        myRef = FirebaseDatabase.getInstance().getReference();

        myRef.child("Users").child(user[0]).setValue("");
    }

}
