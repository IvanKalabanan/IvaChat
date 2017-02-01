package socket.web.com.websocketintgration.interfaces;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import socket.web.com.websocketintgration.utils.Constants;

/**
 * Created by iva on 01.02.17.
 */

public interface APICalls {

    @FormUrlEncoded
    @POST("uploadPhoto")
    Call<Void> sendFile(@Field(Constants.FILE) String file);

}
