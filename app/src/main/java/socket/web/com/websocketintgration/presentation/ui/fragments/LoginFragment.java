package socket.web.com.websocketintgration.presentation.ui.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import socket.web.com.websocketintgration.R;
import socket.web.com.websocketintgration.domens.utils.Constants;
import socket.web.com.websocketintgration.domens.utils.Utils;

/**
 * Created by root on 06.02.17.
 */

public class LoginFragment extends Fragment {

    public static final String TAG = "LoginFragment";
    @BindView(R.id.username) EditText username;
    @BindView(R.id.url) EditText url;
    @BindView(R.id.login) Button login;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_login, container, false);

        ButterKnife.bind(this, root);

        return root;
    }

    @OnClick(R.id.login)
    public void loginClick() {
        Utils.connectToServer(url.getText().toString(), username.getText().toString());

        Utils.getSocket().emit(Constants.ADD_USER, username.getText().toString());

        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.mainContainer,
                        new ChatFragment(),
                        ChatFragment.TAG)
                .addToBackStack(ChatFragment.TAG)
                .commit();
    }
}
