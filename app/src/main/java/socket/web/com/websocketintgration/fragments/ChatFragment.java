package socket.web.com.websocketintgration.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import butterknife.BindView;
import butterknife.ButterKnife;
import socket.web.com.websocketintgration.R;
import socket.web.com.websocketintgration.adapters.ChatRecyclerViewAdapter;

/**
 * Created by root on 06.02.17.
 */

public class ChatFragment extends BaseFragment {

    @BindView(R.id.addUser) Button addUser;
    @BindView(R.id.sendMessage) Button sendMessage;
    @BindView(R.id.sendFile) Button sendFile;
    @BindView(R.id.messageList) RecyclerView messageList;

    private EditText editText;
    private ChatRecyclerViewAdapter chatAdapter;

    private boolean isTyping;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_chat, container, false);

        ButterKnife.bind(this, root);

        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }
}
