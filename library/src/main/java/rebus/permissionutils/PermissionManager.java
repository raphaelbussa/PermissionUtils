/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2017 Raphaël Bussa
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package rebus.permissionutils;

import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v13.app.FragmentCompat;
import android.support.v4.app.ActivityCompat;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by raphaelbussa on 22/06/16.
 */
public class PermissionManager {

    private static PermissionManager instance;

    private FullCallback fullCallback;
    private SimpleCallback simpleCallback;
    private AskAgainCallback askAgainCallback;
    private SmartCallback smartCallback;

    private boolean askAgain = false;

    private ArrayList<PermissionEnum> permissions;
    private ArrayList<PermissionEnum> permissionsGranted;
    private ArrayList<PermissionEnum> permissionsDenied;
    private ArrayList<PermissionEnum> permissionsDeniedForever;
    private ArrayList<PermissionEnum> permissionToAsk;

    private int key = PermissionConstant.KEY_PERMISSION;

    /**
     * @return current instance
     */
    public static PermissionManager Builder() {
        if (instance == null) {
            instance = new PermissionManager();
        }
        return instance;
    }

    /**
     * @param activity target activity
     * @param requestCode requestCode
     * @param permissions permissions
     * @param grantResults grantResults
     */
    public static void handleResult(@NonNull android.app.Activity activity, int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        handleResult(activity, null, null, requestCode, permissions, grantResults);
    }

    /**
     * @param v4fragment target v4 fragment
     * @param requestCode requestCode
     * @param permissions permissions
     * @param grantResults grantResults
     */
    public static void handleResult(@NonNull android.support.v4.app.Fragment v4fragment, int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        handleResult(null, v4fragment, null, requestCode, permissions, grantResults);
    }

    /**
     * @param fragment target fragment
     * @param requestCode requestCode
     * @param permissions permissions
     * @param grantResults grantResults
     */
    public static void handleResult(@NonNull android.app.Fragment fragment, int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        handleResult(null, null, fragment, requestCode, permissions, grantResults);
    }

    private static void handleResult(final android.app.Activity activity, final android.support.v4.app.Fragment v4fragment, final android.app.Fragment fragment, int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (instance == null) return;
        if (requestCode == instance.key) {
            for (int i = 0; i < permissions.length; i++) {
                if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                    instance.permissionsGranted.add(PermissionEnum.fromManifestPermission(permissions[i]));
                } else {
                    boolean permissionsDeniedForever = false;
                    if (activity != null) {
                        permissionsDeniedForever = ActivityCompat.shouldShowRequestPermissionRationale(activity, permissions[i]);
                    } else if (fragment != null) {
                        permissionsDeniedForever = FragmentCompat.shouldShowRequestPermissionRationale(fragment, permissions[i]);
                    } else if (v4fragment != null) {
                        permissionsDeniedForever = v4fragment.shouldShowRequestPermissionRationale(permissions[i]);
                    }
                    if (!permissionsDeniedForever) {
                        instance.permissionsDeniedForever.add(PermissionEnum.fromManifestPermission(permissions[i]));
                    }
                    instance.permissionsDenied.add(PermissionEnum.fromManifestPermission(permissions[i]));
                    instance.permissionToAsk.add(PermissionEnum.fromManifestPermission(permissions[i]));
                }
            }
            if (instance.permissionToAsk.size() != 0 && instance.askAgain) {
                instance.askAgain = false;
                if (instance.askAgainCallback != null && instance.permissionsDeniedForever.size() != instance.permissionsDenied.size()) {
                    instance.askAgainCallback.showRequestPermission(new AskAgainCallback.UserResponse() {
                        @Override
                        public void result(boolean askAgain) {
                            if (askAgain) {
                                instance.ask(activity, v4fragment, fragment);
                            } else {
                                instance.showResult();
                            }
                        }
                    });
                } else {
                    instance.ask(activity, v4fragment, fragment);
                }
            } else {
                instance.showResult();
            }
        }
    }

    /**
     * @param permissions an array of permission that you need to ask
     * @return current instance
     */
    public PermissionManager permissions(ArrayList<PermissionEnum> permissions) {
        this.permissions = new ArrayList<>();
        this.permissions.addAll(permissions);
        return this;
    }

    /**
     * @param permission permission you need to ask
     * @return current instance
     */
    public PermissionManager permission(PermissionEnum permission) {
        this.permissions = new ArrayList<>();
        this.permissions.add(permission);
        return this;
    }

    /**
     * @param permissions permission you need to ask
     * @return current instance
     */
    public PermissionManager permission(PermissionEnum... permissions) {
        this.permissions = new ArrayList<>();
        Collections.addAll(this.permissions, permissions);
        return this;
    }

    /**
     * @param askAgain ask again when permission not granted
     * @return current instance
     */
    public PermissionManager askAgain(boolean askAgain) {
        this.askAgain = askAgain;
        return this;
    }

    /**
     * @param fullCallback set fullCallback for the request
     * @return current instance
     */
    public PermissionManager callback(FullCallback fullCallback) {
        this.simpleCallback = null;
        this.smartCallback = null;
        this.fullCallback = fullCallback;
        return this;
    }

    /**
     * @param simpleCallback set simpleCallback for the request
     * @return current instance
     */
    public PermissionManager callback(SimpleCallback simpleCallback) {
        this.fullCallback = null;
        this.smartCallback = null;
        this.simpleCallback = simpleCallback;
        return this;
    }

    /**
     * @param smartCallback set smartCallback for the request
     * @return current instance
     */
    public PermissionManager callback(SmartCallback smartCallback) {
        this.fullCallback = null;
        this.simpleCallback = null;
        this.smartCallback = smartCallback;
        return this;
    }

    /**
     * @param askAgainCallback set askAgainCallback for the request
     * @return current instance
     */
    public PermissionManager askAgainCallback(AskAgainCallback askAgainCallback) {
        this.askAgainCallback = askAgainCallback;
        return this;
    }

    /**
     * @param key set a custom request code
     * @return current instance
     */
    public PermissionManager key(int key) {
        this.key = key;
        return this;
    }

    /**
     * @param activity target activity
     * just start all permission manager
     */
    public void ask(android.app.Activity activity) {
        ask(activity, null, null);
    }

    /**
     * @param v4fragment target v4 fragment
     * just start all permission manager
     */
    public void ask(android.support.v4.app.Fragment v4fragment) {
        ask(null, v4fragment, null);
    }

    /**
     * @param fragment target fragment
     * just start all permission manager
     */
    public void ask(android.app.Fragment fragment) {
        ask(null, null, fragment);
    }

    private void ask(android.app.Activity activity, android.support.v4.app.Fragment v4fragment, android.app.Fragment fragment) {
        initArray();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            String[] permissionToAsk = permissionToAsk(activity, v4fragment, fragment);
            if (permissionToAsk.length == 0) {
                showResult();
            } else {
                if (activity != null) {
                    ActivityCompat.requestPermissions(activity, permissionToAsk, key);
                } else if (fragment != null) {
                    FragmentCompat.requestPermissions(fragment, permissionToAsk, key);
                } else if (v4fragment != null) {
                    v4fragment.requestPermissions(permissionToAsk, key);
                }
            }
        } else {
            permissionsGranted.addAll(permissions);
            showResult();
        }
    }

    /**
     * @return permission that you really need to ask
     */
    @NonNull
    private String[] permissionToAsk(android.app.Activity activity, android.support.v4.app.Fragment v4fragment, android.app.Fragment fragment) {
        ArrayList<String> permissionToAsk = new ArrayList<>();
        for (PermissionEnum permission : permissions) {
            boolean isGranted = false;
            if (activity != null) {
                isGranted = PermissionUtils.isGranted(activity, permission);
            } else if (fragment != null) {
                isGranted = PermissionUtils.isGranted(fragment.getActivity(), permission);
            } else if (v4fragment != null) {
                isGranted = PermissionUtils.isGranted(v4fragment.getActivity(), permission);
            }
            if (!isGranted) {
                permissionToAsk.add(permission.toString());
            } else {
                permissionsGranted.add(permission);
            }
        }
        return permissionToAsk.toArray(new String[permissionToAsk.size()]);
    }

    /**
     * init permissions ArrayList
     */
    private void initArray() {
        this.permissionsGranted = new ArrayList<>();
        this.permissionsDenied = new ArrayList<>();
        this.permissionsDeniedForever = new ArrayList<>();
        this.permissionToAsk = new ArrayList<>();
    }

    /**
     * check if one of three types of callback are not null and pass data
     */
    private void showResult() {
        if (simpleCallback != null)
            simpleCallback.result(permissionToAsk.size() == 0 || permissionToAsk.size() == permissionsGranted.size());
        if (fullCallback != null)
            fullCallback.result(permissionsGranted, permissionsDenied, permissionsDeniedForever, permissions);
        if (smartCallback != null)
            smartCallback.result(permissionToAsk.size() == 0 || permissionToAsk.size() == permissionsGranted.size(), !permissionsDeniedForever.isEmpty());
        instance = null;
    }

}