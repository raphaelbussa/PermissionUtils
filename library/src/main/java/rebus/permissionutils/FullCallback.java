package rebus.permissionutils;

import java.util.ArrayList;

/**
 * Created by raphaelbussa on 22/06/16.
 */
public interface FullCallback {

    void result(ArrayList<PermissionEnum> permissionsGranted, ArrayList<PermissionEnum> permissionsDenied, ArrayList<PermissionEnum> permissionsDeniedForever, ArrayList<PermissionEnum> permissionsAsked);

}
