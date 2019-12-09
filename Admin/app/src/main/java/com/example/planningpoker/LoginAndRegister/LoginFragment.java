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

public class LoginFragment extends Fragment {


    private CheckBox mCheckBoxRemember;
    private SharedPreferences mSharedPreference;
    private static final String PREFS_NAME="PreFsFile";
    private EditText lEmailEditText, lPasswordEditText;
    private Button lLoginButton, lRegisterButton;
    private FirebaseAuth mAuth;
    public static FragmentManager fragmentManager;


    public LoginFragment() {
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

    //register button OnCliCkListener call Register fragment
    private void register() {
        lRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginActivity.fragmentManager.beginTransaction()
                        .replace(R.id.frameLayout,
                                new RegisterFragment(),
                                //onBackPressed hogy ha a RegisterFragmentben visszalepek akkor a LoginFragmentbe lepjen vissza ne lepjen ki
                                null).addToBackStack(null)
                        .commit();
            }
        });
    }

    //get data from SharedPreferences
    private void getPreferencesData() {

        //email lekeres ha el volt mentve
        SharedPreferences sp = this.getActivity().getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        if(sp.contains("pref_email")){
            String e = sp.getString("pref_email", "not found");
            lEmailEditText.setText(e.toString());
        }
        //password le keres ha el volt mentve
        if(sp.contains("pref_pass")){
            String p = sp.getString("pref_pass", "not found");
            lPasswordEditText.setText(p.toString());
        }
        //ha ki volt pippalva akkor maradjon ki pippalva
        if(sp.contains("pref_check")){
            Boolean b = sp.getBoolean("pref_check", false);
            mCheckBoxRemember.setChecked(b);
        }

    }

    //if checkBox is checked call save element methods
    // call login
    private void loginButton() {
        lLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                controlCheckBox();
                login();
            }
        });
    }

    //login to the application
    private void login() {
        //lekerem a EditText-bol a beirt adatokat es elkuldom a Firebasenek
        String email, password;
        email = lEmailEditText.getText().toString().trim();
        password = lPasswordEditText.getText().toString().trim();

        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                // ha sikeres akkor atlepik a MainFragmentbe
                if(task.isSuccessful()){
                    LoginActivity.fragmentManager.beginTransaction()
                            .replace(R.id.frameLayout, new MainFragment(), null).commit();
                }else{
                    //ha nem akkor Toast es Logban a hibat
                    Toast.makeText(getActivity(),"Login failed", Toast.LENGTH_SHORT).show();
                    Log.d("alma", task.getException().toString());
                }

            }
        });

    }

    //remember me check box is checked
    private void controlCheckBox() {
        if(mCheckBoxRemember.isChecked()){
            //ha igen akkor meghiv egy fugvenyt ami menti az adatokat
            saveElement();
        }else{
            //ha nincs mentve akkor akkor torli a beirt adatokat
            mSharedPreference.edit().clear().apply();
        }
    }

    //save elements
    private void saveElement() {
        Boolean boolIsChecked = mCheckBoxRemember.isChecked();
        SharedPreferences.Editor editor = mSharedPreference.edit();
        editor.putString("pref_email", lEmailEditText.getText().toString());
        editor.putString("pref_pass", lPasswordEditText.getText().toString());
        editor.putBoolean("pref_check", boolIsChecked);
        editor.apply();
        Toast.makeText(getActivity(), "Email and password has ben saved", Toast.LENGTH_SHORT).show();
    }

    //initialization
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
