package com.water.helper.adapter;

import android.content.Context;
import android.graphics.Color;
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
 * 宾馆标签
 */
public class TagAdapter extends BaseAdapter {

    private Context mContext;
    private List<HotelBean> data;

    public TagAdapter(Context context) {
        this.mContext = context;
        this.data = new ArrayList<>();
    }

    public void resetData(List<HotelBean> list) {
        if (list == null || list.size() == 0)
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_button_tag, parent, false);
        }
        HotelBean hotelBean = data.get(position);
        TextView btnTag = ViewHolderUtil.get(convertView, R.id.common_btn_tag);
        btnTag.setText(hotelBean.getName());

        // 判断当前item是否选中，并设置相对应的颜色背景
        switch (getTheListSelect(position)) {
            case 0:
                btnTag.setBackgroundResource(R.drawable.windows_bg_radius);
                btnTag.setTextColor(Color.GRAY);
                break;
            case 1:
                btnTag.setBackgroundResource(R.drawable.windows_red_bg);
                btnTag.setTextColor(Color.WHITE);
                break;
            default:
                break;
        }
        return convertView;
    }

    /**
     * 获取当前item的颜色类别
     */
    public int getTheListSelect(int position) {
        return data.get(position).getIsSelect();
    }

    /**
     * 设置当前item的颜色类别
     *
     * @param position 当前item第几项
     * @param isSelect 是否属于选中的类别 0 - 未选中；1 - 选中
     */
    public void setTheListSelect(int position, int isSelect) {
        this.data.get(position).setIsSelect(isSelect);
        notifyDataSetChanged();
    }
}
