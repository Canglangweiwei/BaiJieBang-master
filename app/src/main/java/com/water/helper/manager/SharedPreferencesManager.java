package com.water.helper.manager;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.SharedPreferences;

@SuppressWarnings("ALL")
public class SharedPreferencesManager {

    // 先保存名字，后面最好保存地址，通过地址来过滤
    public static void updateBluetoothName(Context context, BluetoothDevice device) {
        SharedPreferences mht = getSharedPreferences(context);
        mht.edit().putString("blueName", device.getName()).commit();
    }

    public static String getBluetoothName(Context context) {
        SharedPreferences mht = getSharedPreferences(context);
        return mht.getString("blueName", null);
    }

    private static SharedPreferences getSharedPreferences(Context context) {
        return context.getSharedPreferences("MHT", Context.MODE_PRIVATE);
    }
}
