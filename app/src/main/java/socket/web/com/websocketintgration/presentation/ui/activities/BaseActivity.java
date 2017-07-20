package socket.web.com.websocketintgration.presentation.ui.activities;

import android.support.v7.app.AppCompatActivity;

import socket.web.com.websocketintgration.R;
import socket.web.com.websocketintgration.presentation.FragmentRequestListener;
import socket.web.com.websocketintgration.presentation.ui.fragments.SignInFragment;
import socket.web.com.websocketintgration.presentation.ui.fragments.SignUpFragment;

/**
 * Created by iva on 19.07.17.
 */

public abstract class BaseActivity extends AppCompatActivity implements FragmentRequestListener {

    @Override
    public void startSignUp() {
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.mainContainer,
                        new SignUpFragment(),
                        SignUpFragment.TAG)
                .addToBackStack(SignUpFragment.TAG)
                .commit();
    }

    @Override
    public void startSignIn() {
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.mainContainer,
                        new SignInFragment(),
                        SignInFragment.TAG)
                .addToBackStack(SignInFragment.TAG)
                .commit();
    }

    @Override
    public void startMainActivity() {

    }
}
