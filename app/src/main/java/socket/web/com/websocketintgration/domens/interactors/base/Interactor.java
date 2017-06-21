package socket.web.com.websocketintgration.domens.interactors.base;


import retrofit2.Call;

/**
 * This is the main interface of an interactor. Each interactor serves a specific use case.
 */
public interface Interactor {

    /**
     * This is the main method that starts an interactor. It will make sure that the interactor operation is done on a
     * background thread.
     */
    <T> void execute(Call<T> call);
}
