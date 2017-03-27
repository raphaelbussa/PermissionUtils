package rebus.permissionutils.sample;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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

public class SecondFragmentV4 extends Fragment implements FullCallback {

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
        askOnePermission = (Button) view.findViewById(R.id.ask_one_permission);
        askThreePermission = (Button) view.findViewById(R.id.ask_three_permission);
        checkPermission = (Button) view.findViewById(R.id.check_permission);
        askOnePermissionSimple = (Button) view.findViewById(R.id.ask_one_permission_simple);
        askThreePermissionSimple = (Button) view.findViewById(R.id.ask_three_permission_simple);
        askOnePermissionSmart = (Button) view.findViewById(R.id.ask_one_permission_smart);
        askThreePermissionSmart = (Button) view.findViewById(R.id.ask_three_permission_smart);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        askOnePermission.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PermissionManager.with(SecondFragmentV4.this)
                        .key(9001)
                        .permission(PermissionEnum.WRITE_EXTERNAL_STORAGE)
                        .askAgain(true)
                        .askAgainCallback(new AskAgainCallback() {
                            @Override
                            public void showRequestPermission(UserResponse response) {
                                showDialog(response);
                            }
                        })
                        .callback(SecondFragmentV4.this)
                        .ask();
            }
        });
        askThreePermission.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PermissionManager.with(getActivity())
                        .key(801)
                        .permission(PermissionEnum.GET_ACCOUNTS, PermissionEnum.ACCESS_FINE_LOCATION, PermissionEnum.READ_SMS)
                        .askAgain(true)
                        .askAgainCallback(new AskAgainCallback() {
                            @Override
                            public void showRequestPermission(UserResponse response) {
                                showDialog(response);
                            }
                        })
                        .callback(SecondFragmentV4.this)
                        .ask();
            }
        });
        askOnePermissionSimple.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PermissionManager.with(getActivity())
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
                        .ask();
            }
        });
        askThreePermissionSimple.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PermissionManager.with(getActivity())
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
                        .ask();
            }
        });
        askOnePermissionSmart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PermissionManager.with(getActivity())
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
                        .ask();
            }
        });
        askThreePermissionSmart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PermissionManager.with(getActivity())
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
                        .ask();
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
