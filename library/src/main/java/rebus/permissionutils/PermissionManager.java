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

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by raphaelbussa on 22/06/16.
 */
public class PermissionManager {

    private static PermissionManager instance;

    private Context context;

    private FullCallback fullCallback;
    private SimpleCallback simpleCallback;
    private AskagainCallback askagainCallback;

    private boolean askagain = false;

    private ArrayList<PermissionEnum> permissions;
    private ArrayList<PermissionEnum> permissionsGranted;
    private ArrayList<PermissionEnum> permissionsDenied;
    private ArrayList<PermissionEnum> permissionsDeniedForever;
    private ArrayList<PermissionEnum> permissionToAsk;

    /**
     * @param context current context
     * @return current instance
     */
    public static PermissionManager with(Context context) {
        if (instance == null) {
            instance = new PermissionManager();
        }
        instance.init(context);
        return instance;
    }

    public static void handleResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (instance == null) return;
        if (requestCode == PermissionConstant.KEY_PERMISSION) {
            for (int i = 0; i < permissions.length; i++) {
                if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                    instance.permissionsGranted.add(PermissionEnum.fromManifestPermission(permissions[i]));
                } else {
                    boolean permissionsDeniedForever = ActivityCompat.shouldShowRequestPermissionRationale((Activity) instance.context, permissions[i]);
                    if (!permissionsDeniedForever) {
                        instance.permissionsDeniedForever.add(PermissionEnum.fromManifestPermission(permissions[i]));
                    }
                    instance.permissionsDenied.add(PermissionEnum.fromManifestPermission(permissions[i]));
                    instance.permissionToAsk.add(PermissionEnum.fromManifestPermission(permissions[i]));
                }
            }
            if (instance.permissionToAsk.size() != 0 && instance.askagain) {
                instance.askagain = false;
                if (instance.askagainCallback != null && instance.permissionsDeniedForever.size() != instance.permissionsDenied.size()) {
                    instance.askagainCallback.showRequestPermission(new AskagainCallback.UserResponse() {
                        @Override
                        public void result(boolean askagain) {
                            if (askagain) {
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

    private void init(Context context) {
        this.context = context;
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
     * @param askagain ask again when permission not granted
     * @return current instance
     */
    public PermissionManager askagain(boolean askagain) {
        this.askagain = askagain;
        return this;
    }

    public PermissionManager callback(FullCallback fullCallback) {
        this.simpleCallback = null;
        this.fullCallback = fullCallback;
        return this;
    }

    public PermissionManager callback(SimpleCallback simpleCallback) {
        this.fullCallback = null;
        this.simpleCallback = simpleCallback;
        return this;
    }

    public PermissionManager askagainCallback(AskagainCallback askagainCallback) {
        this.askagainCallback = askagainCallback;
        return this;
    }

    public void ask() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            initArray();
            String[] permissionToAsk = permissionToAsk();
            if (permissionToAsk.length == 0) {
                showResult();
            } else {
                ActivityCompat.requestPermissions((Activity) context, permissionToAsk, PermissionConstant.KEY_PERMISSION);
            }
        } else {
            initArray();
            permissionsGranted.addAll(permissions);
            showResult();
        }
    }

    /**
     * @return permission that you realy need to ask
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

    private void showResult() {
        if (simpleCallback != null)
            simpleCallback.result(permissionToAsk.size() == permissionsGranted.size());
        if (fullCallback != null)
            fullCallback.result(permissionsGranted, permissionsDenied, permissionsDeniedForever, permissions);
    }

}