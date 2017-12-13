package com.water.helper.receiver;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.android.print.sdk.PrinterConstants;
import com.water.helper.manager.PrintfManager;

@SuppressWarnings("ALL")
public class NameOfBroadcastReceiverClass extends BroadcastReceiver {

    private String TAG = "NameOfBroadcastReceiverClass";

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (action.equals(BluetoothDevice.ACTION_BOND_STATE_CHANGED)) {//连接状态改变
            BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
            if (device == null) {
                return;
            }
            switch (device.getBondState()) {
                case BluetoothDevice.BOND_BONDING://正在配对
                    break;
                case BluetoothDevice.BOND_BONDED://配对结束
                    PrintfManager.getInstance().openPrinter(device);
                    break;
                case BluetoothDevice.BOND_NONE://取消配对/未配对
                default:
                    break;
            }
        } else if (BluetoothDevice.ACTION_ACL_DISCONNECTED.equals(action)) { //蓝牙连接已经断开
            PrintfManager.getInstance().mHandler.sendEmptyMessage(PrinterConstants.Connect.CLOSED);
        } else if (BluetoothAdapter.ACTION_STATE_CHANGED.equals(action)) {
            if (intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, -1) == BluetoothAdapter.STATE_OFF) {//蓝牙关闭
                PrintfManager.getInstance().mHandler.sendEmptyMessage(PrinterConstants.Connect.CLOSED);
            }
        }
    }
}
