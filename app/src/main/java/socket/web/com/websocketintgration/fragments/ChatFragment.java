package socket.web.com.websocketintgration.fragments;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.github.nkzawa.emitter.Emitter;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import jp.wasabeef.recyclerview.animators.SlideInUpAnimator;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import socket.web.com.websocketintgration.MainActivity;
import socket.web.com.websocketintgration.R;
import socket.web.com.websocketintgration.adapters.ChatRecyclerViewAdapter;
import socket.web.com.websocketintgration.models.ChatItem;
import socket.web.com.websocketintgration.utils.Constants;
import socket.web.com.websocketintgration.utils.RestAPICommunicator;
import socket.web.com.websocketintgration.utils.Utils;

/**
 * Created by root on 06.02.17.
 */

public class ChatFragment extends Fragment {

    public static final String TAG = "ChatFragment";
    @BindView(R.id.sendMessage) ImageView sendMessage;
    @BindView(R.id.sendFile) ImageView sendFile;
    @BindView(R.id.uploadPhoto) ImageView uploadPhoto;
    @BindView(R.id.messageList) RecyclerView messageList;
    @BindView(R.id.etxt) EditText editText;

    private ChatRecyclerViewAdapter chatAdapter;

    private boolean isTyping;
    private String encodedImage = "";

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

                if (i2 > 0 && !isTyping) {
                    isTyping = true;
                    Utils.getSocket().emit(Constants.TYPING, isTyping);
                }
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                if (i2 == 0) {
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

        messageList.setItemAnimator(new SlideInUpAnimator());

        chatAdapter = new ChatRecyclerViewAdapter(
                getContext(),
                new ArrayList<ChatItem>()
        );

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(
                getContext()
        );

        messageList.setLayoutManager(mLayoutManager);

        messageList.setAdapter(chatAdapter);
    }

    private void sendNewMessage() {
        Utils.getSocket().emit(Constants.NEW_MESSAGE, editText.getText());
        chatAdapter.addNewItem(new ChatItem(Utils.getMyUsername(), "", editText.getText().toString()));
        messageList.getLayoutManager().smoothScrollToPosition(messageList, null, chatAdapter.getItemCount() - 1);
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

                    if (isTyping) {
                        status = "typing";
                    } else {
                        status = "stoped typing";
                    }

                    // add the message to view

                    Log.d(TAG, "message from server = " + username + ": " + status);
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
                    messageList.getLayoutManager().smoothScrollToPosition(messageList, null, chatAdapter.getItemCount() - 1);
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
                    messageList.getLayoutManager().smoothScrollToPosition(messageList, null, chatAdapter.getItemCount() - 1);
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
        if (!editText.getText().toString().isEmpty()) {
            sendNewMessage();
        }
        if (!encodedImage.isEmpty()) {
            RestAPICommunicator.getInstance().getCalls().sendFile(encodedImage).enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    Toast.makeText(getContext(), "FILE SEND", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {

                }
            });
            encodedImage = "";
            uploadPhoto.setVisibility(View.GONE);
        }
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null && requestCode == Constants.SELECT_PICTURE_FILE) {
            Uri pickPictureFromPhone = data.getData();

            final InputStream imageStream;
            try {
                imageStream = getActivity().getContentResolver().openInputStream(pickPictureFromPhone);
                Bitmap decodeStreamBitmap = BitmapFactory.decodeStream(imageStream);

                uploadPhoto.setVisibility(View.VISIBLE);
                uploadPhoto.setImageBitmap(decodeStreamBitmap);

                String[] orientationColumn = {MediaStore.Images.Media.ORIENTATION};
                Cursor cur = getActivity().getContentResolver().query(pickPictureFromPhone, orientationColumn, null, null, null);
                int orientation = -1;
                if (cur != null && cur.moveToFirst()) {
                    orientation = cur.getInt(cur.getColumnIndex(orientationColumn[0]));
                    cur.close();
                }
                decodeStreamBitmap = Utils.rotateImage(decodeStreamBitmap, orientation);

                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                decodeStreamBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos); //bm is the bitmap object
                byte[] byteArrayImage = baos.toByteArray();
                encodedImage = Base64.encodeToString(byteArrayImage, Base64.DEFAULT);
                // tempAvatar = "data:image/png;base64," + encodedImage;
                // mSocket.emit(Constants.NEW_FILE, encodedImage);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
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
