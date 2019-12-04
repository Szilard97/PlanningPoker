package com.example.myplanningpokeruser.ViewQuestion;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import android.os.Bundle;
import com.example.myplanningpokeruser.LoginAndRegister.LoginFragment;
import com.example.myplanningpokeruser.R;
public class MainActivity extends AppCompatActivity {

    public static FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initFragment();

    }

    private void initFragment() {
        if (findViewById(R.id.frameLayout) != null) {
            fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            LoginFragment loginFragment = new LoginFragment();
            fragmentTransaction.add(R.id.frameLayout, loginFragment, null);
            fragmentTransaction.commit();
        }
    }
}