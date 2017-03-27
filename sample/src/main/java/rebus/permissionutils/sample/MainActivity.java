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

package rebus.permissionutils.sample;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import rebus.permissionutils.AskAgainCallback;
import rebus.permissionutils.FullCallback;
import rebus.permissionutils.PermissionEnum;
import rebus.permissionutils.PermissionManager;
import rebus.permissionutils.PermissionUtils;
import rebus.permissionutils.SimpleCallback;
import rebus.permissionutils.SmartCallback;

public class MainActivity extends AppCompatActivity implements FullCallback {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setSubtitle("Activity");
        }

        Button askOnePermission = (Button) findViewById(R.id.ask_one_permission);
        Button askThreePermission = (Button) findViewById(R.id.ask_three_permission);
        Button askOnePermissionSimple = (Button) findViewById(R.id.ask_one_permission_simple);
        Button askThreePermissionSimple = (Button) findViewById(R.id.ask_three_permission_simple);
        Button askOnePermissionSmart = (Button) findViewById(R.id.ask_one_permission_smart);
        Button askThreePermissionSmart = (Button) findViewById(R.id.ask_three_permission_smart);

        Button checkPermission = (Button) findViewById(R.id.check_permission);

        askOnePermission.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PermissionManager.with(MainActivity.this)
                        .key(9000)
                        .permission(PermissionEnum.WRITE_EXTERNAL_STORAGE)
                        .askAgain(true)
                        .askAgainCallback(new AskAgainCallback() {
                            @Override
                            public void showRequestPermission(UserResponse response) {
                                showDialog(response);
                            }
                        })
                        .callback(MainActivity.this)
                        .ask();
            }
        });
        askThreePermission.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PermissionManager.with(MainActivity.this)
                        .key(800)
                        .permission(PermissionEnum.GET_ACCOUNTS, PermissionEnum.ACCESS_FINE_LOCATION, PermissionEnum.READ_SMS)
                        .askAgain(true)
                        .askAgainCallback(new AskAgainCallback() {
                            @Override
                            public void showRequestPermission(UserResponse response) {
                                showDialog(response);
                            }
                        })
                        .callback(MainActivity.this)
                        .ask();
            }
        });
        askOnePermissionSimple.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PermissionManager.with(MainActivity.this)
                        .key(700)
                        .permission(PermissionEnum.WRITE_EXTERNAL_STORAGE)
                        .askAgain(true)
                        .askAgainCallback(new AskAgainCallback() {
                            @Override
                            public void showRequestPermission(UserResponse response) {
                                showDialog(response);
                            }
                        })
                        .callback(new SimpleCallback() {
                            @Override
                            public void result(boolean allPermissionsGranted) {
                                Toast.makeText(MainActivity.this, PermissionEnum.WRITE_EXTERNAL_STORAGE.toString() + " allPermissionsGranted [" + allPermissionsGranted + "]", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .ask();
            }
        });
        askThreePermissionSimple.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PermissionManager.with(MainActivity.this)
                        .key(600)
                        .permission(PermissionEnum.GET_ACCOUNTS, PermissionEnum.ACCESS_FINE_LOCATION, PermissionEnum.READ_SMS)
                        .askAgain(true)
                        .askAgainCallback(new AskAgainCallback() {
                            @Override
                            public void showRequestPermission(UserResponse response) {
                                showDialog(response);
                            }
                        })
                        .callback(new SimpleCallback() {
                            @Override
                            public void result(boolean allPermissionsGranted) {
                                Toast.makeText(MainActivity.this, PermissionEnum.GET_ACCOUNTS.toString() + ", " + PermissionEnum.ACCESS_FINE_LOCATION.toString() + ", " + PermissionEnum.READ_SMS.toString() + " allPermissionsGranted [" + allPermissionsGranted + "]", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .ask();
            }
        });
        askOnePermissionSmart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PermissionManager.with(MainActivity.this)
                        .key(2000)
                        .permission(PermissionEnum.WRITE_EXTERNAL_STORAGE)
                        .askAgain(true)
                        .askAgainCallback(new AskAgainCallback() {
                            @Override
                            public void showRequestPermission(UserResponse response) {
                                showDialog(response);
                            }
                        })
                        .callback(new SmartCallback() {
                            @Override
                            public void result(boolean allPermissionsGranted, boolean somePermissionsDeniedForever) {
                                Toast.makeText(MainActivity.this, PermissionEnum.WRITE_EXTERNAL_STORAGE.toString() + " allPermissionsGranted [" + allPermissionsGranted + "] somePermissionsDeniedForever [" + somePermissionsDeniedForever + "]", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .ask();
            }
        });
        askThreePermissionSmart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PermissionManager.with(MainActivity.this)
                        .key(2100)
                        .permission(PermissionEnum.GET_ACCOUNTS, PermissionEnum.ACCESS_FINE_LOCATION, PermissionEnum.READ_SMS)
                        .askAgain(true)
                        .askAgainCallback(new AskAgainCallback() {
                            @Override
                            public void showRequestPermission(UserResponse response) {
                                showDialog(response);
                            }
                        })
                        .callback(new SmartCallback() {
                            @Override
                            public void result(boolean allPermissionsGranted, boolean somePermissionsDeniedForever) {
                                Toast.makeText(MainActivity.this, PermissionEnum.GET_ACCOUNTS.toString() + ", " + PermissionEnum.ACCESS_FINE_LOCATION.toString() + ", " + PermissionEnum.READ_SMS.toString() + " allPermissionsGranted [" + allPermissionsGranted + "] somePermissionsDeniedForever [" + somePermissionsDeniedForever + "]", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .ask();
            }
        });
        checkPermission.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PermissionEnum permissionEnum = PermissionEnum.WRITE_EXTERNAL_STORAGE;
                boolean granted = PermissionUtils.isGranted(MainActivity.this, PermissionEnum.WRITE_EXTERNAL_STORAGE);
                Toast.makeText(MainActivity.this, permissionEnum.toString() + " isGranted [" + granted + "]", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        PermissionManager.handleResult(requestCode, permissions, grantResults);
    }

    @Override
    public void result(ArrayList<PermissionEnum> permissionsGranted, ArrayList<PermissionEnum> permissionsDenied, ArrayList<PermissionEnum> permissionsDeniedForever, ArrayList<PermissionEnum> permissionsAsked) {
        List<String> msg = new ArrayList<>();
        for (PermissionEnum permissionEnum : permissionsGranted) {
            msg.add(permissionEnum.toString() + " [Granted]");
        }
        for (PermissionEnum permissionEnum : permissionsDenied) {
            msg.add(permissionEnum.toString() + " [Denied]");
        }
        for (PermissionEnum permissionEnum : permissionsDeniedForever) {
            msg.add(permissionEnum.toString() + " [DeniedForever]");
        }
        for (PermissionEnum permissionEnum : permissionsAsked) {
            msg.add(permissionEnum.toString() + " [Asked]");
        }
        String[] items = msg.toArray(new String[msg.size()]);
        new AlertDialog.Builder(MainActivity.this)
                .setTitle("Permission result")
                .setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                PermissionUtils.openApplicationSettings(MainActivity.this, R.class.getPackage().getName());
                break;
            case R.id.action_fragment_v4:
                Intent fragmentV4 = new Intent(MainActivity.this, FragmentActivity.class);
                fragmentV4.putExtra("IS_FRAGMENT_V4", true);
                startActivity(fragmentV4);
                break;
            case R.id.action_fragment:
                Intent fragment = new Intent(MainActivity.this, FragmentActivity.class);
                fragment.putExtra("IS_FRAGMENT_V4", false);
                startActivity(fragment);
                break;
            case R.id.action_info:
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle(getString(R.string.action_info));
                builder.setMessage(fromHtml(getString(R.string.info_message)));
                builder.setPositiveButton(getString(R.string.close), null);
                AlertDialog dialog = builder.create();
                dialog.show();
                TextView textView = (TextView) dialog.findViewById(android.R.id.message);
                if (textView != null) {
                    textView.setMovementMethod(LinkMovementMethod.getInstance());
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showDialog(final AskAgainCallback.UserResponse response) {
        new AlertDialog.Builder(MainActivity.this)
                .setTitle("Permission needed")
                .setMessage("This app realy need to use this permission, you wont to authorize it?")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        response.result(true);
                    }
                })
                .setNegativeButton("NOT NOW", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        response.result(false);
                    }
                })
                .setCancelable(false)
                .show();
    }

    private Spanned fromHtml(String value) {
        if (value == null) {
            value = "";
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return Html.fromHtml(value, Html.FROM_HTML_MODE_LEGACY);
        } else {
            return Html.fromHtml(value);
        }
    }

}
