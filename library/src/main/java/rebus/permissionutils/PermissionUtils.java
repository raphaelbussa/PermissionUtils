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

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.content.ContextCompat;

/**
 * Created by raphaelbussa on 22/06/16.
 */
public class PermissionUtils {

    /**
     * @param context    current context
     * @param permission permission to check
     * @return if permission is granted return true
     */
    public static boolean isGranted(Context context, PermissionEnum permission) {
        return Build.VERSION.SDK_INT < Build.VERSION_CODES.M || ContextCompat.checkSelfPermission(context, permission.toString()) == PackageManager.PERMISSION_GRANTED;
    }

    /**
     * @param context    current context
     * @param permission all permission you need to check
     * @return if one of permission is not granted return false
     */
    public static boolean isGranted(Context context, PermissionEnum... permission) {
        for (PermissionEnum permissionEnum : permission) {
            if (!isGranted(context, permissionEnum)) {
                return false;
            }
        }
        return true;
    }

    /**
     * @param packageName package name of your app
     * @return an intent to start for open settings app
     */
    public static Intent openApplicationSettings(String packageName) {
        Intent intent = new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.parse("package:" + packageName));
        return intent;
    }

    /**
     * @param context     current context
     * @param packageName package name of your app
     */
    public static void openApplicationSettings(Context context, String packageName) {
        context.startActivity(openApplicationSettings(packageName));
    }

}
