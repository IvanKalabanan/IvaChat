package socket.web.com.websocketintgration;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;

import socket.web.com.websocketintgration.utils.Constants;

public class MainActivity extends Activity {

    private static final String TAG = "MainActivity";
    private Socket mSocket;
    private TextView textView;
    private EditText editText;

    private boolean isTyping;

    private String myUsername = "Johnny";

    {
        try {
            Log.d(TAG, "connecting...");
            IO.Options opts = new IO.Options();
            opts.forceNew = true;
//            opts.query = "token="+ "demo";
            mSocket = IO.socket("http://192.168.1.18:80", opts);

        } catch (URISyntaxException e) {
            Log.d(TAG, "URISyntaxException...");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        Button addUser = (Button) findViewById(R.id.addUser);
        Button sendMessage = (Button) findViewById(R.id.sendMessage);

        textView = (TextView) findViewById(R.id.txt);
        editText = (EditText) findViewById(R.id.etxt);

        mSocket.on(Constants.LOGIN, onLogin);
        mSocket.on(Constants.NEW_MESSAGE, onNewMessage);
        mSocket.on(Constants.TYPING, userIsTyping);
        mSocket.connect();

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                if(i2 > 0 && !isTyping) {
                    isTyping = true;
                    mSocket.emit(Constants.TYPING, isTyping);
                }
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                if(i2 == 0) {
                    isTyping = false;
                    mSocket.emit(Constants.TYPING, isTyping);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


        addUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addUser();
            }
        });

        sendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendNewMessage();
            }
        });


    }

    private void addUser() {
        mSocket.emit(Constants.ADD_USER, myUsername);
    }

    private void sendNewMessage() {
        mSocket.emit(Constants.NEW_MESSAGE, editText.getText());
        editText.setText("");
    }

    private Emitter.Listener userIsTyping = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {
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
                    textView.setText(textView.getText()+"\n"+ username+": "+status);
                    Log.d(TAG, "message from server = " + username+": "+status);
                }
            });
        }
    };

    private Emitter.Listener onNewMessage = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {
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
                    textView.setText(textView.getText()+"\n"+ username+": "+message);
                    Log.d(TAG, "message from server = " + message);
                }
            });
        }
    };


    private Emitter.Listener onLogin = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {
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
                    textView.setText(textView.getText()+"\n current amount users in chat: "+ message);
                    Log.d(TAG, "message from server = " + message);
                }
            });
        }
    };


    @Override
    public void onDestroy() {
        super.onDestroy();

        mSocket.disconnect();
        mSocket.off(Constants.LOGIN, onLogin);
        mSocket.off(Constants.NEW_MESSAGE, onNewMessage);
        mSocket.off(Constants.TYPING, userIsTyping);

    }
}