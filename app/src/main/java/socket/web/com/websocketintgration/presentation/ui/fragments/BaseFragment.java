package socket.web.com.websocketintgration.presentation.ui.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import socket.web.com.websocketintgration.presentation.FragmentRequestListener;
import socket.web.com.websocketintgration.presentation.ui.activities.BaseActivity;

/**
 * Created by iva on 20.07.17.
 */

public abstract class BaseFragment extends Fragment {
    public static final String TAG = "BaseFragment";

    protected FragmentRequestListener fragmentRequestListener;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        fragmentRequestListener = (BaseActivity) getActivity();
        initListeners();
    }

//    every Fragment init his own listeners so make this method abstract
    protected abstract void initListeners();
}
