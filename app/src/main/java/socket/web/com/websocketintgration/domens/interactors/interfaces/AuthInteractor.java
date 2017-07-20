package socket.web.com.websocketintgration.domens.interactors.interfaces;

/**
 * Created by iva on 20.07.17.
 */

public interface AuthInteractor {

    void login(String name, String password);

    void registration(String name, String email, String password);

    public interface UserRetrieveListeners{

    }
}
