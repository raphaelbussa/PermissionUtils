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
package com.raphaelbussa.permissionutils

import android.app.Activity
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import java.util.*

/**
 * Created by com.raphaelbussa on 22/06/16.
 */
class PermissionManager {

    private var fullCallback: FullCallback? = null
    private var simpleCallback: SimpleCallback? = null
    private var askAgainCallback: AskAgainCallback? = null
    private var smartCallback: SmartCallback? = null

    private var askAgain = false

    private val permissions = mutableListOf<String>()
    private val permissionsGranted = mutableListOf<String>()
    private val permissionsDenied = mutableListOf<String>()
    private val permissionsDeniedForever = mutableListOf<String>()
    private val permissionToAsk = mutableListOf<String>()

    private var key = PermissionConstant.KEY_PERMISSION

    /**
     * @param permissions an array of permission that you need to ask
     * @return current instance
     */
    fun permissions(permissions: List<String>): PermissionManager {
        this.permissions.clear()
        this.permissions.addAll(permissions)
        return this
    }

    /**
     * @param permission permission you need to ask
     * @return current instance
     */
    fun permission(permission: String): PermissionManager {
        permissions.clear()
        permissions.add(permission)
        return this
    }

    /**
     * @param permissions permission you need to ask
     * @return current instance
     */
    fun permission(vararg permissions: String): PermissionManager {
        this.permissions.clear()
        this.permissions.addAll(permissions.toList())
        return this
    }

    /**
     * @param askAgain ask again when permission not granted
     * @return current instance
     */
    fun askAgain(askAgain: Boolean): PermissionManager {
        this.askAgain = askAgain
        return this
    }

    /**
     * @param fullCallback set fullCallback for the request
     * @return current instance
     */
    fun callback(fullCallback: FullCallback): PermissionManager {
        this.simpleCallback = null
        this.smartCallback = null
        this.fullCallback = fullCallback
        return this
    }

    /**
     * @param simpleCallback set simpleCallback for the request
     * @return current instance
     */
    fun callback(simpleCallback: SimpleCallback): PermissionManager {
        this.fullCallback = null
        this.smartCallback = null
        this.simpleCallback = simpleCallback
        return this
    }

    /**
     * @param smartCallback set smartCallback for the request
     * @return current instance
     */
    fun callback(smartCallback: SmartCallback): PermissionManager {
        fullCallback = null
        simpleCallback = null
        this.smartCallback = smartCallback
        return this
    }

    /**
     * @param askAgainCallback set askAgainCallback for the request
     * @return current instance
     */
    fun askAgainCallback(askAgainCallback: AskAgainCallback): PermissionManager {
        this.askAgainCallback = askAgainCallback
        return this
    }

    /**
     * @param key set a custom request code
     * @return current instance
     */
    fun key(key: Int): PermissionManager {
        this.key = key
        return this
    }

    /**
     * @param activity target activity
     * just start all permission manager
     */
    fun ask(activity: Activity) {
        ask(activity, null)
    }

    /**
     * @param fragmentX target v4 fragment
     * just start all permission manager
     */
    fun ask(fragment: Fragment) {
        ask(null, fragment)
    }

    private fun ask(activity: Activity?, fragment: Fragment?) {
        initArray()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val permissionToAsk = permissionToAsk(activity, fragment)
            if (permissionToAsk.isEmpty()) {
                showResult()
            } else {
                if (activity != null) {
                    ActivityCompat.requestPermissions(activity, permissionToAsk, key)
                } else fragment?.requestPermissions(permissionToAsk, key)
            }
        } else {
            permissionsGranted.addAll(permissions)
            showResult()
        }
    }

    /**
     * @return permission that you really need to ask
     */
    private fun permissionToAsk(
        activity: Activity?,
        fragment: Fragment?
    ): Array<String> {
        val permissionToAsk = ArrayList<String>()
        for (permission in permissions) {
            var isGranted = false
            if (activity != null) {
                isGranted = PermissionUtils.isGranted(activity, permission)
            } else if (fragment != null) {
                isGranted = PermissionUtils.isGranted(fragment.requireContext(), permission)
            }
            if (!isGranted) {
                permissionToAsk.add(permission)
            } else {
                permissionsGranted.add(permission)
            }
        }
        return permissionToAsk.toTypedArray()
    }

    /**
     * init permissions ArrayList
     */
    private fun initArray() {
        this.permissionsGranted.clear()
        this.permissionsDenied.clear()
        this.permissionsDeniedForever.clear()
        this.permissionToAsk.clear()
    }

    /**
     * check if one of three types of callback are not null and pass data
     */
    private fun showResult() {
        simpleCallback?.result(permissionToAsk.size == 0 || permissionToAsk.size == permissionsGranted.size)
        fullCallback?.result(
            permissionsGranted,
            permissionsDenied,
            permissionsDeniedForever,
            permissions
        )
        smartCallback?.result(
            permissionToAsk.size == 0 || permissionToAsk.size == permissionsGranted.size,
            permissionsDeniedForever.isNotEmpty()
        )
        instance = null
    }

    companion object {
        private var instance: PermissionManager? = null

        /**
         * @return current instance
         */
        @Suppress("FunctionName")
        @JvmStatic
        fun Builder(): PermissionManager {
            return PermissionManager().also { instance = it }
        }

        /**
         * @param activity     target activity
         * @param requestCode  requestCode
         * @param permissions  permissions
         * @param grantResults grantResults
         */
        fun handleResult(
            activity: Activity,
            requestCode: Int,
            permissions: Array<String>,
            grantResults: IntArray
        ) {
            handleResult(activity, null, requestCode, permissions, grantResults)
        }

        /**
         * @param fragment     target fragment
         * @param requestCode  requestCode
         * @param permissions  permissions
         * @param grantResults grantResults
         */
        fun handleResult(
            fragment: Fragment,
            requestCode: Int,
            permissions: Array<String>,
            grantResults: IntArray
        ) {
            handleResult(null, fragment, requestCode, permissions, grantResults)
        }

        private fun handleResult(
            activity: Activity?,
            fragment: Fragment?,
            requestCode: Int,
            permissions: Array<String>,
            grantResults: IntArray
        ) {
            val validInstance = instance ?: return

            if (requestCode == validInstance.key) {
                for (i in permissions.indices) {
                    if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                        validInstance.permissionsGranted.add(permissions[i])
                    } else {
                        var permissionsDeniedForever = false
                        if (activity != null) {
                            permissionsDeniedForever =
                                ActivityCompat.shouldShowRequestPermissionRationale(
                                    activity,
                                    permissions[i]
                                )
                        } else if (fragment != null) {
                            permissionsDeniedForever =
                                fragment.shouldShowRequestPermissionRationale(permissions[i])
                        }
                        if (!permissionsDeniedForever) {
                            validInstance.permissionsDeniedForever.add(permissions[i])
                        }
                        validInstance.permissionsDenied.add(permissions[i])
                        validInstance.permissionToAsk.add(permissions[i])
                    }
                }
                if (validInstance.permissionToAsk.size != 0 && validInstance.askAgain) {
                    validInstance.askAgain = false
                    if (validInstance.askAgainCallback != null && validInstance.permissionsDeniedForever.size != validInstance.permissionsDenied.size) {
                        validInstance.askAgainCallback?.showRequestPermission { askAgain ->
                            if (askAgain) {
                                validInstance.ask(activity, fragment)
                            } else {
                                validInstance.showResult()
                            }
                        }
                    } else {
                        validInstance.ask(activity, fragment)
                    }
                } else {
                    validInstance.showResult()
                }
            }
        }
    }
}