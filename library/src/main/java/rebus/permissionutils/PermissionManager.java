/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2016 Raphaël Bussa
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

import android.annotation.SuppressLint;
import android.content.Context;
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

    @SuppressLint("StaticFieldLeak")
    private static PermissionManager instance;

    private android.app.Activity activity;
    private android.app.Fragment fragment;
    private android.support.v4.app.Fragment v4fragment;

    private Context context;

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
     * @param activity target activity
     * @return current instance
     */
    public static PermissionManager with(@NonNull android.app.Activity activity) {
        if (instance == null) {
            instance = new PermissionManager();
        }
        instance.init(activity, null, null);
        return instance;
    }

    /**
     * @param v4fragment target v4 fragment
     * @return current instance
     */
    public static PermissionManager with(@NonNull android.support.v4.app.Fragment v4fragment) {
        if (instance == null) {
            instance = new PermissionManager();
        }
        instance.init(null, null, v4fragment);
        return instance;
    }

    /**
     * @param fragment target fragment
     * @return current instance
     */
    public static PermissionManager with(@NonNull android.app.Fragment fragment) {
        if (instance == null) {
            instance = new PermissionManager();
        }
        instance.init(null, fragment, null);
        return instance;
    }

    public static void handleResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (instance == null) return;
        if (requestCode == instance.key) {
            for (int i = 0; i < permissions.length; i++) {
                if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                    instance.permissionsGranted.add(PermissionEnum.fromManifestPermission(permissions[i]));
                } else {
                    boolean permissionsDeniedForever = false;
                    if (instance.activity != null) {
                        permissionsDeniedForever = ActivityCompat.shouldShowRequestPermissionRationale(instance.activity, permissions[i]);
                    } else if (instance.fragment != null) {
                        permissionsDeniedForever = FragmentCompat.shouldShowRequestPermissionRationale(instance.fragment, permissions[i]);
                    } else if (instance.v4fragment != null) {
                        permissionsDeniedForever = instance.v4fragment.shouldShowRequestPermissionRationale(permissions[i]);
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
                                instance.ask();
                            } else {
                                instance.showResult();
                            }
                        }
                    });
                } else {
                    instance.ask();
                }
            } else {
                instance.showResult();
            }
        }
    }

    private void init(android.app.Activity activity, android.app.Fragment fragment, android.support.v4.app.Fragment v4fragment) {
        this.activity = activity;
        this.fragment = fragment;
        this.v4fragment = v4fragment;

        if (activity != null) {
            this.context = activity;
        } else if (fragment != null) {
            this.context = fragment.getActivity();
        } else if (v4fragment != null) {
            this.context = v4fragment.getActivity();
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
     * just start all permission manager
     */
    public void ask() {
        initArray();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            String[] permissionToAsk = permissionToAsk();
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
    private String[] permissionToAsk() {
        ArrayList<String> permissionToAsk = new ArrayList<>();
        for (PermissionEnum permission : permissions) {
            if (!PermissionUtils.isGranted(context, permission)) {
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
    }

}