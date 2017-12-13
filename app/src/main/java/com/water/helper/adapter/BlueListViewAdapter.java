package com.water.helper.adapter;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.jaydenxiao.common.commonutils.ViewHolderUtil;
import com.water.helper.R;

import java.util.List;

/**
 * 设备列表
 */
public class BlueListViewAdapter extends BaseAdapter {

    private List<BluetoothDevice> deviceList = null;
    private Context context;

    public BlueListViewAdapter(Context context, List<BluetoothDevice> deviceList) {
        this.context = context;
        this.deviceList = deviceList;
    }

    @Override
    public int getCount() {
        return deviceList.size();
    }

    @Override
    public BluetoothDevice getItem(int position) {
        return deviceList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.blue_list_item, parent, false);
        }

        TextView tv_blue_list_name = ViewHolderUtil.get(convertView, R.id.tv_blue_list_name);
        TextView tv_blue_list_address = ViewHolderUtil.get(convertView, R.id.tv_blue_list_address);

        BluetoothDevice device = getItem(position);
        tv_blue_list_name.setText(device.getName());
        tv_blue_list_address.setText(device.getAddress());
        return convertView;
    }
}
