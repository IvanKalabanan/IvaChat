package socket.web.com.websocketintgration.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import socket.web.com.websocketintgration.R;
import socket.web.com.websocketintgration.utils.Constants;

/**
 * Created by root on 06.02.17.
 */

public class LoginFragment extends BaseFragment {

    @BindView(R.id.username) EditText username;
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
        mSocket.emit(Constants.ADD_USER, username.getText().toString());

        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.mainContainer,
                        LikedFragment.newInstance(id),
                        LikedFragment.TAG)
                .addToBackStack(LikedFragment.TAG)
                .commit();
    }
}
