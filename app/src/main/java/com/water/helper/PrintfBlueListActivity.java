package com.water.helper;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.AdapterView;

import com.jaydenxiao.common.commonutils.ToastUitl;
import com.jaydenxiao.common.commonwidget.NormalTitleBar;
import com.water.helper.adapter.BlueListViewAdapter;
import com.water.helper.app.AbsAppComponent;
import com.water.helper.base.AbsBaseActivity;
import com.water.helper.base.AbsBaseApplication;
import com.water.helper.manager.BluetoothManager;
import com.water.helper.manager.PrintfManager;
import com.water.helper.util.PermissionUtil;
import com.water.helper.util.Util;
import com.water.helper.webservice.RequestType;
import com.water.helper.widget.ScrollListView;

import java.lang.reflect.Method;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import butterknife.Bind;

/**
 * 打印机列表
 */
public class PrintfBlueListActivity extends AbsBaseActivity {

    @Bind(R.id.ntb)
    NormalTitleBar ntb;

    @Bind(R.id.lv_already_blue_list)
    ScrollListView lv_already_blue_list;
    @Bind(R.id.lv_blue_list)
    ScrollListView lv_blue_list;

    private BluetoothAdapter mBluetoothAdapter;
    private PrintfManager printfManager;

    private static int REQUEST_ENABLE_BT = 2;
    private AbstractList<BluetoothDevice> bluetoothDeviceArrayList;

    private Context context;

    private BlueListViewAdapter adapter;
    private List<BluetoothDevice> alreadyBlueList;
    private boolean isRegister;

    @Override
    protected int initLayoutResID() {
        return R.layout.activity_printf_blue_list;
    }

    @Override
    protected void parseIntent() {

    }

    @Override
    protected void setupComponent(AbsAppComponent component) {

    }

    @Override
    protected void initUi() {
        context = this;
        ntb.setTitleText("打印机列表");
        // 在有些手机上，需要申请位置权限才可以扫描蓝牙
        PermissionUtil.checkLocationPermission(context);
    }

    @Override
    protected void initDatas() {
        printfManager = PrintfManager.getInstance();
        bluetoothDeviceArrayList = new ArrayList<>();
        alreadyBlueList = new ArrayList<>();
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        Set<BluetoothDevice> bondedDevices = mBluetoothAdapter.getBondedDevices();
        for (BluetoothDevice device : bondedDevices) {
            if (judge(device, alreadyBlueList))
                continue;
            alreadyBlueList.add(device);
        }
        adapter = new BlueListViewAdapter(context, bluetoothDeviceArrayList);
        lv_blue_list.setAdapter(adapter);
        lv_already_blue_list.setAdapter(new BlueListViewAdapter(context, alreadyBlueList));
    }

    @Override
    protected void initListener() {
        ntb.setOnBackListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        ntb.setRightTitleVisibility(true);
        ntb.setRightTitle(getString(R.string.printf_blue_list_search));
        ntb.setOnRightTextListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text = ntb.getRightTitleText();
                String stopText = getString(R.string.printf_blue_list_stop);
                String searchText = getString(R.string.printf_blue_list_search);
                if (text.equals(searchText)) {// 点了搜索
                    ntb.setRightTitle(stopText);
                    starSearchBlue();
                } else if (text.equals(stopText)) {// 点击了停止
                    stopSearchBlue();
                    ntb.setRightTitle(searchText);
                }
            }
        });

        lv_already_blue_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                AbsBaseApplication.sApp.getCachedThreadPool().execute(new Runnable() {
                    @Override
                    public void run() {
                        printfManager.openPrinter(alreadyBlueList.get(position));
                    }
                });
            }
        });

        lv_blue_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                ToastUitl.showShort(getString(R.string.connect_now));
                // 先停止搜索
                stopSearchBlue();
                // 进行配对
                AbsBaseApplication.sApp.getCachedThreadPool().execute(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            BluetoothDevice mDevice = mBluetoothAdapter.getRemoteDevice(bluetoothDeviceArrayList.get(position).getAddress());
                            if (mDevice.getBondState() == BluetoothDevice.BOND_NONE) {// 未配对
                                Method createBondMethod = BluetoothDevice.class.getMethod("createBond");
                                boolean success = (Boolean) createBondMethod.invoke(mDevice);
                            } else if (mDevice.getBondState() == BluetoothDevice.BOND_BONDED) {// 配对成功
                                printfManager.openPrinter(mDevice);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PermissionUtil.MY_PERMISSIONS_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // 打开蓝牙设备
                    if (BluetoothManager.isBluetoothSupported()) {
                        if (!BluetoothManager.isBluetoothEnabled()) {
                            BluetoothManager.turnOnBluetooth();
                        }
                    }
                } else {
                    // 权限被拒绝
                    new AlertDialog.Builder(context).setMessage(getString(R.string.permissions_are_rejected_bluetooth))
                            .setPositiveButton(getString(R.string.to_set_up), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent intent = Util.getAppDetailSettingIntent(context);
                                    startActivity(intent);
                                    dialog.dismiss();
                                }
                            }).setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    }).setTitle(getString(R.string.prompt)).show();
                    break;
                }
        }
    }

    /**
     * 停止搜索
     */
    private void stopSearchBlue() {
        if (mReceiver != null && isRegister) {
            try {
                unregisterReceiver(mReceiver);
                ToastUitl.showShort(getString(R.string.stop_search));
            } catch (Exception e) {
            }
        }
        if (mBluetoothAdapter != null) {
            mBluetoothAdapter.cancelDiscovery();
        }
    }

    @Override
    protected void onDestroy() {
        stopSearchBlue();
        super.onDestroy();
    }

    @Override
    public void onLoadSuccessCallBack(String dataJson, RequestType type) {

    }

    @Override
    public Intent registerReceiver(BroadcastReceiver receiver, IntentFilter filter) {
        isRegister = true;
        return super.registerReceiver(receiver, filter);
    }

    @Override
    public void unregisterReceiver(BroadcastReceiver receiver) {
        super.unregisterReceiver(receiver);
        isRegister = false;
    }

    /**
     * 开始搜索设备
     */
    private void starSearchBlue() {
        ToastUitl.showShort(getString(R.string.start_search));
        bluetoothDeviceArrayList.clear();
        IntentFilter filter = new IntentFilter();
        filter.addAction(BluetoothDevice.ACTION_FOUND);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        registerReceiver(mReceiver, filter);
        if (mBluetoothAdapter == null || !mBluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        } else {
            mBluetoothAdapter.startDiscovery();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_ENABLE_BT) {
            if (resultCode == Activity.RESULT_OK) {
                mBluetoothAdapter.startDiscovery();
            }
        }
    }

    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            // 找到设备
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                BluetoothDevice device = intent
                        .getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                if (judge(device, bluetoothDeviceArrayList))
                    return;
                bluetoothDeviceArrayList.add(device);
                adapter.notifyDataSetChanged();
            } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {// 搜索完成
                ntb.setRightTitle(getString(R.string.printf_blue_list_search));
                stopSearchBlue();
            }
        }
    };

    private boolean judge(BluetoothDevice device, List<BluetoothDevice> devices) {
        // 这段代码是只寻找打印机
        int majorDeviceClass = device.getBluetoothClass().getMajorDeviceClass();
        if (majorDeviceClass != 1536) return true;
        if (devices.contains(device)) return true;
        return false;
    }
}
