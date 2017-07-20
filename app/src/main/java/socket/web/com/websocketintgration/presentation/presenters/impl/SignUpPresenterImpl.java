package socket.web.com.websocketintgration.presentation.presenters.impl;

import socket.web.com.websocketintgration.domens.repository.APICalls;
import socket.web.com.websocketintgration.domens.threading.MainThread;
import socket.web.com.websocketintgration.presentation.presenters.base.AbstractPresenter;
import socket.web.com.websocketintgration.presentation.presenters.interfaces.SignUpPresenter;

/**
 * Created by iva on 31.05.17.
 */

public class SignUpPresenterImpl extends AbstractPresenter implements SignUpPresenter {

    private View view;

    public SignUpPresenterImpl(
            MainThread mainThread,
            APICalls calls,
            View view) {
        super(mainThread, calls);
        this.view = view;
    }

    @Override
    public void resume() {

    }

    @Override
    public void pause() {

    }

    @Override
    public void stop() {

    }

    @Override
    public void destroy() {

    }

    @Override
    public void onError(String message) {

    }

    @Override
    public void registration(String name, String email, String password) {

    }
}
