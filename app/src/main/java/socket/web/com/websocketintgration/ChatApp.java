package socket.web.com.websocketintgration;

import android.app.Application;
import android.util.Log;

import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import java.net.URISyntaxException;

import socket.web.com.websocketintgration.utils.Constants;

/**
 * Created by root on 06.02.17.
 */

public class ChatApp extends Application {

    private static final String TAG = "CgatApp";
    private static Socket mSocket;

    @Override
    public void onCreate() {
        super.onCreate();
        try {
            Log.d(TAG, "connecting...");
            IO.Options opts = new IO.Options();
            opts.forceNew = true;
//            opts.query = "token="+ "demo";
            mSocket = IO.socket(Constants.BASE_URL, opts);

        } catch (URISyntaxException e) {
            Log.d(TAG, "URISyntaxException...");
        }
    }

    public static Socket getSocket() {
        return mSocket;
    }
}
