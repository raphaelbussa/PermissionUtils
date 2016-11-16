package rebus.permissionutils;

/**
 * Created by raphaelbussa on 16/11/16.
 */

public interface SmartCallback {

    void result(boolean allPermissionsGranted, boolean somePermissionsDeniedForever);

}
