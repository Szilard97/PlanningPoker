package com.example.planningpoker.LoginAndRegister;


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

import com.example.planningpoker.AddAndViewRoom.MainFragment;
import com.example.planningpoker.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

/**
 * A simple {@link Fragment} subclass.
 */
public class RegisterFragment extends Fragment {

    private FirebaseAuth mAuth;
    private EditText rEmailEditText, rPasswordEditText;
    private Button rRegisterButton;

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
                    Toast.makeText(getActivity(), "Please enter your email address and password", Toast.LENGTH_SHORT).show();
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
                            Log.d("alma","Registration is successful");
                            LoginActivity.fragmentManager.beginTransaction()
                                    .replace(R.id.frameLayout, new MainFragment(), null).commit();

                        } else {
                            Log.d("alma", task.getException().toString());
                            Toast.makeText(getActivity(), "Authentication failed."+ task.getException().toString(),
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

}
