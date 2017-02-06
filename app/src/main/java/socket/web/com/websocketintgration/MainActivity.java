package socket.web.com.websocketintgration;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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
import socket.web.com.websocketintgration.fragments.ChatFragment;
import socket.web.com.websocketintgration.fragments.LoginFragment;
import socket.web.com.websocketintgration.utils.Constants;
import socket.web.com.websocketintgration.utils.RestAPICommunicator;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private String myUsername = "Johnny";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.mainContainer,
                        new LoginFragment(),
                        LoginFragment.TAG)
                .addToBackStack(LoginFragment.TAG)
                .commit();

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

}