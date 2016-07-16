package rebus.permissionutils;

/**
 * Created by raphaelbussa on 22/06/16.
 */
public interface AskagainCallback {

    void showRequestPermission(UserResponse response);

    interface UserResponse {
        void result(boolean askagain);
    }

}
