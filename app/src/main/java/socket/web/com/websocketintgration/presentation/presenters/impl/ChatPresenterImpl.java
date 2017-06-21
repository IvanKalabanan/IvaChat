package socket.web.com.websocketintgration.presentation.presenters.impl;

import android.content.ContentResolver;

import socket.web.com.websocketintgration.domens.repository.APICalls;
import socket.web.com.websocketintgration.domens.threading.MainThread;
import socket.web.com.websocketintgration.presentation.presenters.base.AbstractPresenter;
import socket.web.com.websocketintgration.presentation.presenters.interfaces.ChatPresenter;

/**
 * Created by iva on 31.05.17.
 */

public class ChatPresenterImpl extends AbstractPresenter implements ChatPresenter {

    private View view;

    public ChatPresenterImpl(
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

}
