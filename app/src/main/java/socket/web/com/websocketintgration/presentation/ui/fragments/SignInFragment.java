package socket.web.com.websocketintgration.presentation.ui.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import socket.web.com.websocketintgration.databinding.FragmentSigninBinding;

/**
 * Created by iva on 20.07.17.
 */

public class SignInFragment extends BaseFragment {
    public static final String TAG = "SignInFragment";

    private FragmentSigninBinding binding;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentSigninBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    protected void initListeners() {
        binding.createAccountBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentRequestListener.startSignUp();
            }
        });
    }
}
