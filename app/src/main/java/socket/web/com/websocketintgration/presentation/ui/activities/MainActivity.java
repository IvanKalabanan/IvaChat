package socket.web.com.websocketintgration.presentation.ui.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import butterknife.ButterKnife;
import socket.web.com.websocketintgration.R;
import socket.web.com.websocketintgration.presentation.ui.fragments.LoginFragment;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = "MainActivity";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.mainContainer,
                        new LoginFragment(),
                        LoginFragment.TAG)
                .addToBackStack(LoginFragment.TAG)
                .commit();

    }
}
