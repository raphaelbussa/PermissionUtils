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

package rebus.permissionutils.sample;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import rebus.permissionutils.AskAgainCallback;
import rebus.permissionutils.FullCallback;
import rebus.permissionutils.PermissionEnum;
import rebus.permissionutils.PermissionManager;
import rebus.permissionutils.PermissionUtils;
import rebus.permissionutils.SimpleCallback;
import rebus.permissionutils.SmartCallback;

public class SecondFragment extends Fragment implements FullCallback {

    private Button askOnePermission;
    private Button askThreePermission;
    private Button checkPermission;
    private Button askOnePermissionSimple;
    private Button askThreePermissionSimple;
    private Button askOnePermissionSmart;
    private Button askThreePermissionSmart;

    @SuppressLint("InflateParams")
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_second, null);
        askOnePermission = view.findViewById(R.id.ask_one_permission);
        askThreePermission = view.findViewById(R.id.ask_three_permission);
        checkPermission = view.findViewById(R.id.check_permission);
        askOnePermissionSimple = view.findViewById(R.id.ask_one_permission_simple);
        askThreePermissionSimple = view.findViewById(R.id.ask_three_permission_simple);
        askOnePermissionSmart = view.findViewById(R.id.ask_one_permission_smart);
        askThreePermissionSmart = view.findViewById(R.id.ask_three_permission_smart);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        askOnePermission.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PermissionManager.Builder()
                        .key(9001)
                        .permission(PermissionEnum.WRITE_EXTERNAL_STORAGE)
                        .askAgain(true)
                        .askAgainCallback(new AskAgainCallback() {
                            @Override
                            public void showRequestPermission(UserResponse response) {
                                showDialog(response);
                            }
                        })
                        .callback(SecondFragment.this)
                        .ask(SecondFragment.this);
            }
        });
        askThreePermission.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PermissionManager.Builder()
                        .key(801)
                        .permission(PermissionEnum.GET_ACCOUNTS, PermissionEnum.ACCESS_FINE_LOCATION, PermissionEnum.READ_SMS)
                        .askAgain(true)
                        .askAgainCallback(new AskAgainCallback() {
                            @Override
                            public void showRequestPermission(UserResponse response) {
                                showDialog(response);
                            }
                        })
                        .callback(SecondFragment.this)
                        .ask(SecondFragment.this);
            }
        });
        askOnePermissionSimple.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PermissionManager.Builder()
                        .key(701)
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
                                Toast.makeText(getActivity(), PermissionEnum.WRITE_EXTERNAL_STORAGE.toString() + " allPermissionsGranted [" + allPermissionsGranted + "]", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .ask(SecondFragment.this);
            }
        });
        askThreePermissionSimple.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PermissionManager.Builder()
                        .key(601)
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
                                Toast.makeText(getActivity(), PermissionEnum.GET_ACCOUNTS.toString() + ", " + PermissionEnum.ACCESS_FINE_LOCATION.toString() + ", " + PermissionEnum.READ_SMS.toString() + " allPermissionsGranted [" + allPermissionsGranted + "]", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .ask(SecondFragment.this);
            }
        });
        askOnePermissionSmart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PermissionManager.Builder()
                        .key(2001)
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
                                Toast.makeText(getActivity(), PermissionEnum.WRITE_EXTERNAL_STORAGE.toString() + " allPermissionsGranted [" + allPermissionsGranted + "] somePermissionsDeniedForever [" + somePermissionsDeniedForever + "]", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .ask(SecondFragment.this);
            }
        });
        askThreePermissionSmart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PermissionManager.Builder()
                        .key(2101)
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
                                Toast.makeText(getActivity(), PermissionEnum.GET_ACCOUNTS.toString() + ", " + PermissionEnum.ACCESS_FINE_LOCATION.toString() + ", " + PermissionEnum.READ_SMS.toString() + " allPermissionsGranted [" + allPermissionsGranted + "] somePermissionsDeniedForever [" + somePermissionsDeniedForever + "]", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .ask(SecondFragment.this);
            }
        });
        checkPermission.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PermissionEnum permissionEnum = PermissionEnum.WRITE_EXTERNAL_STORAGE;
                boolean granted = PermissionUtils.isGranted(getActivity(), PermissionEnum.WRITE_EXTERNAL_STORAGE);
                Toast.makeText(getActivity(), permissionEnum.toString() + " isGranted [" + granted + "]", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showDialog(final AskAgainCallback.UserResponse response) {
        new AlertDialog.Builder(getActivity())
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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        PermissionManager.handleResult(this, requestCode, permissions, grantResults);
    }

    @SuppressWarnings("ToArrayCallWithZeroLengthArrayArgument")
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
        new AlertDialog.Builder(getActivity())
                .setTitle("Permission result")
                .setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .show();
    }
}
