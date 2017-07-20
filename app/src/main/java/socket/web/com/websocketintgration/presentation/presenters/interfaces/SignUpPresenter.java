package socket.web.com.websocketintgration.presentation.presenters.interfaces;

import socket.web.com.websocketintgration.presentation.presenters.base.BasePresenter;
import socket.web.com.websocketintgration.presentation.ui.BaseView;

/**
 * Created by iva on 31.05.17.
 */

public interface SignUpPresenter extends BasePresenter {

    interface View extends BaseView {
        void startMainActivity();
    }

    void registration(String name, String email, String password);

}
