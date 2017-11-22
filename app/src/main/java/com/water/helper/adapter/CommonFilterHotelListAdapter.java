package com.water.helper.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.jaydenxiao.common.commonutils.ViewHolderUtil;
import com.water.helper.R;
import com.water.helper.bean.HotelBean;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 条件筛选列表
 * </p>
 * Created by Administrator on 2017/11/1 0001.
 */
public class CommonFilterHotelListAdapter extends BaseAdapter {

    private List<HotelBean> data;

    public CommonFilterHotelListAdapter() {
        this.data = new ArrayList<>();
    }

    public void addData(List<HotelBean> list) {
        if (list == null)
            return;
        this.data = list;
        this.notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return data == null ? 0 : data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.channel_gridview_item, parent, false);
        }
        TextView tvName = ViewHolderUtil.get(convertView, R.id.channel_gridview_item);
        tvName.setText(data.get(position).getName());
        tvName.setBackgroundColor(parent.getContext().getColor(R.color.white));
        return convertView;
    }
}
