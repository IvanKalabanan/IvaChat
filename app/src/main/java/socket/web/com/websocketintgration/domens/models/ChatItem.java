package socket.web.com.websocketintgration.domens.models;

/**
 * Created by iva on 02.02.17.
 */

public class ChatItem {

    private String username;
    private String photoUrl;
    private String message;

    public ChatItem(){}

    public ChatItem(String username, String photoUrl, String message) {
        this.username = username;
        this.photoUrl = photoUrl;
        this.message = message;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
