package socket.web.com.websocketintgration.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.github.nkzawa.emitter.Emitter;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import socket.web.com.websocketintgration.R;
import socket.web.com.websocketintgration.adapters.ChatRecyclerViewAdapter;
import socket.web.com.websocketintgration.models.ChatItem;
import socket.web.com.websocketintgration.utils.Constants;
import socket.web.com.websocketintgration.utils.Utils;

/**
 * Created by root on 06.02.17.
 */

public class ChatFragment extends Fragment {

    public static final String TAG = "ChatFragment";
    @BindView(R.id.addUser) Button addUser;
    @BindView(R.id.sendMessage) Button sendMessage;
    @BindView(R.id.sendFile) Button sendFile;
    @BindView(R.id.messageList) RecyclerView messageList;
    @BindView(R.id.etxt) EditText editText;

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

        initView();
        initRecycler();

        Utils.getSocket().on(Constants.NEW_MESSAGE, onNewMessage);
        Utils.getSocket().on(Constants.TYPING, userIsTyping);
        Utils.getSocket().on(Constants.NEW_FILE, onNewFile);
        Utils.getSocket().connect();

    }

    private void initView() {

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                if(i2 > 0 && !isTyping) {
                    isTyping = true;
                    Utils.getSocket().emit(Constants.TYPING, isTyping);
                }
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                if(i2 == 0) {
                    isTyping = false;
                    Utils.getSocket().emit(Constants.TYPING, isTyping);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private void initRecycler() {
        chatAdapter = new ChatRecyclerViewAdapter(
                getContext(),
                new ArrayList<ChatItem>()
        );

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(
                getContext(),
                LinearLayoutManager.VERTICAL,
                false
        );

        messageList.setLayoutManager(mLayoutManager);

        messageList.setAdapter(chatAdapter);
    }

    private void sendNewMessage() {
        Utils.getSocket().emit(Constants.NEW_MESSAGE, editText.getText());
        chatAdapter.addNewItem(new ChatItem(Utils.getMyUsername(), "", editText.getText().toString()));
        editText.setText("");
    }

    private Emitter.Listener userIsTyping = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
           getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    JSONObject data = (JSONObject) args[0];
                    String username, status;
                    boolean isTyping;

                    try {
                        isTyping = data.getBoolean("isTyping");
                        username = data.getString("username");
                    } catch (JSONException e) {
                        Log.d(TAG, "run: JSONException Exception !!!");
                        return;
                    }

                    if(isTyping) {
                        status = "typing";
                    } else {
                        status = "stoped typing";
                    }

                    // add the message to view

                    Log.d(TAG, "message from server = " + username+": "+status);
                }
            });
        }
    };

    private Emitter.Listener onNewMessage = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    JSONObject data = (JSONObject) args[0];
                    String message, username;
                    try {
                        username = data.getString("username");
                        message = data.getString("message");
                    } catch (JSONException e) {
                        Log.d(TAG, "run: JSONException Exception !!!");
                        return;
                    }

                    // add the message to view
                    chatAdapter.addNewItem(new ChatItem(username, "", message));
                    Log.d(TAG, "message from server = " + message);
                }
            });
        }
    };

    private Emitter.Listener onNewFile = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    JSONObject data = (JSONObject) args[0];
                    String file, username;
                    try {
                        username = data.getString("username");
                        file = data.getString("file");
                    } catch (JSONException e) {
                        Log.d(TAG, "run: JSONException Exception !!!");
                        return;
                    }

                    chatAdapter.addNewItem(new ChatItem(username, file, ""));
                }
            });
        }
    };

    private Emitter.Listener onLogin = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    JSONObject data = (JSONObject) args[0];
                    String message;
                    try {
                        message = data.getString(Constants.NUM_USERS);
                    } catch (JSONException e) {
                        Log.d(TAG, "run: JSONException Exception !!!");
                        return;
                    }

                    // add the message to view
                    Log.d(TAG, "message from server = " + message);
                }
            });
        }
    };

    @OnClick(R.id.sendMessage)
    public void sendMessageButton() {
        sendNewMessage();
    }

    @OnClick(R.id.sendFile)
    public void sendFileButton() {
        Intent intent = new Intent(
                Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        );
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
        getActivity().startActivityForResult(
                intent,
                Constants.SELECT_PICTURE_FILE
        );
    }


    @Override
    public void onDestroy() {
        super.onDestroy();

        Utils.getSocket().disconnect();
        Utils.getSocket().off(Constants.LOGIN, onLogin);
        Utils.getSocket().off(Constants.NEW_MESSAGE, onNewMessage);
        Utils.getSocket().off(Constants.TYPING, userIsTyping);

    }

}
