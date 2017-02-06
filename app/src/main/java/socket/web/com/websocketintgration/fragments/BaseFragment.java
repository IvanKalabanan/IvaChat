package socket.web.com.websocketintgration.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.github.nkzawa.socketio.client.Socket;

import socket.web.com.websocketintgration.ChatApp;

/**
 * Created by root on 06.02.17.
 */

public class BaseFragment extends Fragment {

    private static final String TAG = "BaseFragment";

    protected Socket mSocket;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mSocket = ChatApp.getSocket();
    }
}
