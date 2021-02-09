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
package com.raphaelbussa.permissionutils.sample

import android.Manifest
import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.raphaelbussa.permissionutils.AskAgainCallback.UserResponse
import com.raphaelbussa.permissionutils.FullCallback
import com.raphaelbussa.permissionutils.PermissionManager
import com.raphaelbussa.permissionutils.PermissionManager.Companion.Builder
import com.raphaelbussa.permissionutils.PermissionUtils
import java.util.*

class SecondFragment : Fragment(), FullCallback {

    private lateinit var askOnePermission: Button
    private lateinit var askThreePermission: Button
    private lateinit var checkPermission: Button
    private lateinit var askOnePermissionSimple: Button
    private lateinit var askThreePermissionSimple: Button
    private lateinit var askOnePermissionSmart: Button
    private lateinit var askThreePermissionSmart: Button

    @SuppressLint("InflateParams")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_second, null)
        askOnePermission = view.findViewById(R.id.ask_one_permission)
        askThreePermission = view.findViewById(R.id.ask_three_permission)
        checkPermission = view.findViewById(R.id.check_permission)
        askOnePermissionSimple = view.findViewById(R.id.ask_one_permission_simple)
        askThreePermissionSimple = view.findViewById(R.id.ask_three_permission_simple)
        askOnePermissionSmart = view.findViewById(R.id.ask_one_permission_smart)
        askThreePermissionSmart = view.findViewById(R.id.ask_three_permission_smart)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        askOnePermission.setOnClickListener {
            Builder().key(9001)
                    .permission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    .askAgain(true)
                    .askAgainCallback { response -> showDialog(response) }
                    .callback(this@SecondFragment)
                    .ask(this@SecondFragment)
        }
        askThreePermission.setOnClickListener {
            Builder()
                    .key(801)
                    .permission(Manifest.permission.GET_ACCOUNTS, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.READ_SMS)
                    .askAgain(true)
                    .askAgainCallback { response -> showDialog(response) }
                    .callback(this@SecondFragment)
                    .ask(this@SecondFragment)
        }
        askOnePermissionSimple.setOnClickListener {
            Builder()
                    .key(701)
                    .permission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    .askAgain(true)
                    .askAgainCallback { response -> showDialog(response) }
                    .callback { allPermissionsGranted -> Toast.makeText(requireContext(), "${Manifest.permission.WRITE_EXTERNAL_STORAGE} allPermissionsGranted [$allPermissionsGranted]", Toast.LENGTH_SHORT).show() }
                    .ask(this@SecondFragment)
        }
        askThreePermissionSimple.setOnClickListener {
            Builder()
                    .key(601)
                    .permission(Manifest.permission.GET_ACCOUNTS, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.READ_SMS)
                    .askAgain(true)
                    .askAgainCallback { response -> showDialog(response) }
                    .callback { allPermissionsGranted -> Toast.makeText(requireContext(), "${Manifest.permission.GET_ACCOUNTS}, ${Manifest.permission.ACCESS_FINE_LOCATION}, ${Manifest.permission.READ_SMS} allPermissionsGranted [$allPermissionsGranted]", Toast.LENGTH_SHORT).show() }
                    .ask(this@SecondFragment)
        }
        askOnePermissionSmart.setOnClickListener {
            Builder()
                    .key(2001)
                    .permission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    .askAgain(true)
                    .askAgainCallback { response -> showDialog(response) }
                    .callback { allPermissionsGranted, somePermissionsDeniedForever -> Toast.makeText(requireContext(), "${Manifest.permission.WRITE_EXTERNAL_STORAGE} allPermissionsGranted [$allPermissionsGranted] somePermissionsDeniedForever [$somePermissionsDeniedForever]", Toast.LENGTH_SHORT).show() }
                    .ask(this@SecondFragment)
        }
        askThreePermissionSmart.setOnClickListener {
            Builder()
                    .key(2101)
                    .permission(Manifest.permission.GET_ACCOUNTS, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.READ_SMS)
                    .askAgain(true)
                    .askAgainCallback { response -> showDialog(response) }
                    .callback { allPermissionsGranted, somePermissionsDeniedForever -> Toast.makeText(activity, "${Manifest.permission.GET_ACCOUNTS }, ${Manifest.permission.ACCESS_FINE_LOCATION}, ${Manifest.permission.READ_SMS } allPermissionsGranted [$allPermissionsGranted] somePermissionsDeniedForever [$somePermissionsDeniedForever]", Toast.LENGTH_SHORT).show() }
                    .ask(this@SecondFragment)
        }
        checkPermission.setOnClickListener {
            val granted = PermissionUtils.isGranted(requireContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
            Toast.makeText(activity, "${Manifest.permission.WRITE_EXTERNAL_STORAGE} isGranted [$granted]", Toast.LENGTH_SHORT).show()
        }
    }

    private fun showDialog(response: UserResponse) {
        if (activity == null) return
        AlertDialog.Builder(requireContext())
                .setTitle("Permission needed")
                .setMessage("This app really need to use this permission, you wont to authorize it?")
                .setPositiveButton("OK") { _, _ -> response.result(true) }
                .setNegativeButton("NOT NOW") { _, _ -> response.result(false) }
                .setCancelable(false)
                .show()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        PermissionManager.handleResult(this, requestCode, permissions, grantResults)
    }

    override fun result(permissionsGranted: List<String>, permissionsDenied: List<String>, permissionsDeniedForever: List<String>, permissionsAsked: List<String>) {
        val msg = mutableListOf<String>()
        for (permissionEnum in permissionsGranted) {
            msg.add("$permissionEnum [Granted]")
        }
        for (permissionEnum in permissionsDenied) {
            msg.add("$permissionEnum [Denied]")
        }
        for (permissionEnum in permissionsDeniedForever) {
            msg.add("$permissionEnum [DeniedForever]")
        }
        for (permissionEnum in permissionsAsked) {
            msg.add("$permissionEnum [Asked]")
        }
        AlertDialog.Builder(requireContext())
                .setTitle("Permission result")
                .setItems(msg.toTypedArray()) { _, _ -> }
                .show()
    }
}