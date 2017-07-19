package socket.web.com.websocketintgration.domens.repository;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import socket.web.com.websocketintgration.domens.utils.Constants;

/**
 * Created by iva on 01.02.17.
 */

public interface APICalls {

    @FormUrlEncoded
    @POST("uploadPhoto")
    Call<Void> sendFile(@Field(Constants.FILE) String file);

    @FormUrlEncoded
    @POST("rooms")
    Call<Void> createRoom(@Field(Constants.NAME) String name);

    @GET("rooms")
    Call<Void> getRooms();

}
