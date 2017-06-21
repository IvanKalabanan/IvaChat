package socket.web.com.websocketintgration.presentation.presenters.base;


import socket.web.com.websocketintgration.domens.repository.APICalls;
import socket.web.com.websocketintgration.domens.threading.MainThread;

/**
 * This is a base class for all presenters which are communicating with interactors. This base class will hold a
 * reference to the Executor and MainThread objects that are needed for running interactors in a background thread.
 */
public abstract class AbstractPresenter {
    protected MainThread mMainThread;
    protected APICalls calls;

    public AbstractPresenter(MainThread mainThread, APICalls calls) {
        mMainThread = mainThread;
        this.calls = calls;
    }
}
