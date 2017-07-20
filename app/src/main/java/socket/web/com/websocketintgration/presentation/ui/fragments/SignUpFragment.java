package socket.web.com.websocketintgration.presentation.ui.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import socket.web.com.websocketintgration.databinding.FragmentSignupBinding;
import socket.web.com.websocketintgration.domens.repository.base.RestAPICommunicator;
import socket.web.com.websocketintgration.domens.threading.MainThreadImpl;
import socket.web.com.websocketintgration.presentation.presenters.impl.SignUpPresenterImpl;
import socket.web.com.websocketintgration.presentation.presenters.interfaces.SignUpPresenter;

/**
 * Created by iva on 20.07.17.
 */

public class SignUpFragment extends BaseFragment implements SignUpPresenter.View {
    public static final String TAG = "SignUpFragment";

    private FragmentSignupBinding binding;
    private SignUpPresenter presenter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentSignupBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        presenter = new SignUpPresenterImpl(
                MainThreadImpl.getInstance(),
                RestAPICommunicator.getInstance().getCalls(),
                this
        );
    }

    @Override
    protected void initListeners() {

    }

    @Override
    public void startMainActivity() {

    }

    @Override
    public void showProgress() {

    }

    @Override
    public void hideProgress() {

    }

    @Override
    public void showError(String message) {

    }
}
