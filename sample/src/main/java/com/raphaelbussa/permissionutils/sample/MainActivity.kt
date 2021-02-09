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
import android.content.Intent
import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.HtmlCompat
import com.raphaelbussa.permissionutils.AskAgainCallback.UserResponse
import com.raphaelbussa.permissionutils.FullCallback
import com.raphaelbussa.permissionutils.PermissionManager
import com.raphaelbussa.permissionutils.PermissionManager.Companion.Builder
import com.raphaelbussa.permissionutils.PermissionUtils
import com.raphaelbussa.permissionutils.PermissionUtils.openApplicationSettings
import java.util.*

class MainActivity : AppCompatActivity(), FullCallback {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.apply {
            subtitle = "Activity"
        }
        val askOnePermission = findViewById<Button>(R.id.ask_one_permission)
        val askThreePermission = findViewById<Button>(R.id.ask_three_permission)
        val askOnePermissionSimple = findViewById<Button>(R.id.ask_one_permission_simple)
        val askThreePermissionSimple = findViewById<Button>(R.id.ask_three_permission_simple)
        val askOnePermissionSmart = findViewById<Button>(R.id.ask_one_permission_smart)
        val askThreePermissionSmart = findViewById<Button>(R.id.ask_three_permission_smart)
        val checkPermission = findViewById<Button>(R.id.check_permission)
        askOnePermission.setOnClickListener {
            Builder().key(9001)
                    .permission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    .askAgain(true)
                    .askAgainCallback { response -> showDialog(response) }
                    .callback(this@MainActivity)
                    .ask(this@MainActivity)
        }
        askThreePermission.setOnClickListener {
            Builder()
                    .key(801)
                    .permission(Manifest.permission.GET_ACCOUNTS, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.READ_SMS)
                    .askAgain(true)
                    .askAgainCallback { response -> showDialog(response) }
                    .callback(this@MainActivity)
                    .ask(this@MainActivity)
        }
        askOnePermissionSimple.setOnClickListener {
            Builder()
                    .key(701)
                    .permission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    .askAgain(true)
                    .askAgainCallback { response -> showDialog(response) }
                    .callback { allPermissionsGranted -> Toast.makeText(this, "${Manifest.permission.WRITE_EXTERNAL_STORAGE} allPermissionsGranted [$allPermissionsGranted]", Toast.LENGTH_SHORT).show() }
                    .ask(this@MainActivity)
        }
        askThreePermissionSimple.setOnClickListener {
            Builder()
                    .key(601)
                    .permission(Manifest.permission.GET_ACCOUNTS, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.READ_SMS)
                    .askAgain(true)
                    .askAgainCallback { response -> showDialog(response) }
                    .callback { allPermissionsGranted -> Toast.makeText(this, "${Manifest.permission.GET_ACCOUNTS}, ${Manifest.permission.ACCESS_FINE_LOCATION}, ${Manifest.permission.READ_SMS} allPermissionsGranted [$allPermissionsGranted]", Toast.LENGTH_SHORT).show() }
                    .ask(this@MainActivity)
        }
        askOnePermissionSmart.setOnClickListener {
            Builder()
                    .key(2001)
                    .permission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    .askAgain(true)
                    .askAgainCallback { response -> showDialog(response) }
                    .callback { allPermissionsGranted, somePermissionsDeniedForever -> Toast.makeText(this, "${Manifest.permission.WRITE_EXTERNAL_STORAGE} allPermissionsGranted [$allPermissionsGranted] somePermissionsDeniedForever [$somePermissionsDeniedForever]", Toast.LENGTH_SHORT).show() }
                    .ask(this@MainActivity)
        }
        askThreePermissionSmart.setOnClickListener {
            Builder()
                    .key(2101)
                    .permission(Manifest.permission.GET_ACCOUNTS, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.READ_SMS)
                    .askAgain(true)
                    .askAgainCallback { response -> showDialog(response) }
                    .callback { allPermissionsGranted, somePermissionsDeniedForever -> Toast.makeText(this, "${Manifest.permission.GET_ACCOUNTS}, ${Manifest.permission.ACCESS_FINE_LOCATION}, ${Manifest.permission.READ_SMS} allPermissionsGranted [$allPermissionsGranted] somePermissionsDeniedForever [$somePermissionsDeniedForever]", Toast.LENGTH_SHORT).show() }
                    .ask(this@MainActivity)
        }
        checkPermission.setOnClickListener {
            val granted = PermissionUtils.isGranted(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
            Toast.makeText(this, "${Manifest.permission.WRITE_EXTERNAL_STORAGE} isGranted [$granted]", Toast.LENGTH_SHORT).show()
        }
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
        AlertDialog.Builder(this@MainActivity)
                .setTitle("Permission result")
                .setItems(msg.toTypedArray()) { _, _ -> }
                .show()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_settings -> openApplicationSettings(this@MainActivity, "com.raphaelbussa.permissionutils.sample")
            R.id.action_fragment -> {
                val fragment = Intent(this@MainActivity, FragmentActivity::class.java)
                startActivity(fragment)
            }
            R.id.action_info -> {
                val builder = AlertDialog.Builder(this@MainActivity)
                builder.setTitle(getString(R.string.action_info))
                builder.setMessage(HtmlCompat.fromHtml(getString(R.string.info_message), HtmlCompat.FROM_HTML_MODE_COMPACT))
                builder.setPositiveButton(getString(R.string.close), null)
                val dialog = builder.create()
                dialog.show()
                val textView = dialog.findViewById<TextView>(android.R.id.message)
                if (textView != null) {
                    textView.movementMethod = LinkMovementMethod.getInstance()
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun showDialog(response: UserResponse) {
        AlertDialog.Builder(this@MainActivity)
                .setTitle("Permission needed")
                .setMessage("This app realy need to use this permission, you wont to authorize it?")
                .setPositiveButton("OK") { _, _ -> response.result(true) }
                .setNegativeButton("NOT NOW") { _, _ -> response.result(false) }
                .setCancelable(false)
                .show()
    }
}