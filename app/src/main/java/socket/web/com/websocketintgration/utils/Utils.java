package socket.web.com.websocketintgration.utils;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import java.net.URISyntaxException;

/**
 * Created by iva on 31.01.17.
 */

public class Utils {

    private static Socket mSocket;

    private static String myUsername;

    private static final String TAG = "Utils";

    public static boolean connectToServer(String url, String username) {
        myUsername = username;
        try {
            Log.d(TAG, "connecting...");
            IO.Options opts = new IO.Options();
            opts.forceNew = true;
//            opts.query = "token="+ "demo";
            mSocket = IO.socket(url, opts);
            return true;
        } catch (URISyntaxException e) {
            Log.d(TAG, "URISyntaxException...");
            return false;
        }
    }

    public static Socket getSocket() {
        return mSocket;
    }

    public static String getMyUsername() {
        return myUsername;
    }

}
