package socket.web.com.websocketintgration;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.Socket;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import socket.web.com.websocketintgration.adapters.ChatRecyclerViewAdapter;
import socket.web.com.websocketintgration.models.ChatItem;
import socket.web.com.websocketintgration.utils.Constants;
import socket.web.com.websocketintgration.utils.RestAPICommunicator;

public class MainActivity extends Activity {

    private static final String TAG = "MainActivity";

    private String myUsername = "Johnny";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        initView();
        initRecycler();

        mSocket.on(Constants.LOGIN, onLogin);
        mSocket.on(Constants.NEW_MESSAGE, onNewMessage);
        mSocket.on(Constants.TYPING, userIsTyping);
        mSocket.on(Constants.NEW_FILE, onNewFile);
        mSocket.connect();
    }

    private void initRecycler() {
        chatAdapter = new ChatRecyclerViewAdapter(
                getApplicationContext(),
                new ArrayList<ChatItem>()
        );

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(
                getApplicationContext(),
                LinearLayoutManager.VERTICAL,
                true
        );

        messageList.setLayoutManager(mLayoutManager);

        messageList.setAdapter(chatAdapter);
    }

    private void initView() {

        editText = (EditText) findViewById(R.id.etxt);

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
    }

    private void addUser() {
        mSocket.emit(Constants.ADD_USER, myUsername);
    }

    private void sendNewMessage() {
        mSocket.emit(Constants.NEW_MESSAGE, editText.getText());
        chatAdapter.addNewItem(new ChatItem("", "", editText.getText().toString()));
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
                    chatAdapter.addNewItem(new ChatItem(username, "", message));
                    Log.d(TAG, "message from server = " + message);
                }
            });
        }
    };

    private Emitter.Listener onNewFile = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null && requestCode == Constants.SELECT_PICTURE_FILE) {
            final Uri pickPictureFromPhone = data.getData();

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    final InputStream imageStream;
                    try {
                        imageStream = getContentResolver().openInputStream(pickPictureFromPhone);
                        final Bitmap pictureBitmap = BitmapFactory.decodeStream(imageStream);
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        pictureBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                        byte[] byteArrayImage = baos.toByteArray();
                        String encodedImage = Base64.encodeToString(byteArrayImage, Base64.DEFAULT);
                        RestAPICommunicator.getInstance().getCalls().sendFile(encodedImage).enqueue(new Callback<Void>() {
                            @Override
                            public void onResponse(Call<Void> call, Response<Void> response) {
                                Toast.makeText(MainActivity.this, "FILE SEND", Toast.LENGTH_SHORT).show();
                                //chatAdapter.addNewItem(new ChatItem("", ));
                            }

                            @Override
                            public void onFailure(Call<Void> call, Throwable t) {

                            }
                        });
                        // tempAvatar = "data:image/png;base64," + encodedImage;
                        // mSocket.emit(Constants.NEW_FILE, encodedImage);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            });
        }

    }

    @OnClick(R.id.addUser)
    public void addUserButton() {
        addUser();
    }

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
        startActivityForResult(
                intent,
                Constants.SELECT_PICTURE_FILE
        );
    }

}