package socket.web.com.websocketintgration.presentation.ui.fragments;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import socket.web.com.websocketintgration.R;
import socket.web.com.websocketintgration.databinding.FragmentLoginBinding;
import socket.web.com.websocketintgration.domens.repository.base.RestAPICommunicator;

/**
 * Created by root on 06.02.17.
 */

public class LoginFragment extends Fragment {

    public static final String TAG = "LoginFragment";
    private FragmentLoginBinding binding;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_login, container, false);

        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        binding.login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RestAPICommunicator.getInstance().getCalls().getRooms().enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        Log.d(TAG, "onResponse: ");
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        Log.d(TAG, "onFailure: ");
                    }
                });
            }
        });
        binding.login2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RestAPICommunicator.getInstance().getCalls().createRoom("watafaka room").enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        Log.d(TAG, "onResponse: ");
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        Log.d(TAG, "onFailure: ");
                    }
                });
            }
        });
//        Utils.connectToServer(binding.url.getText().toString(), binding.username.getText().toString());
//
//        Utils.getSocket().emit(Constants.ADD_USER, binding.username.getText().toString());
//
//        getActivity().getSupportFragmentManager()
//                .beginTransaction()
//                .replace(R.id.mainContainer,
//                        new ChatFragment(),
//                        ChatFragment.TAG)
//                .addToBackStack(ChatFragment.TAG)
//                .commit();
    }
}
