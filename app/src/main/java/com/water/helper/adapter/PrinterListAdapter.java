package com.water.helper.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.jaydenxiao.common.commonutils.ViewHolderUtil;
import com.water.helper.R;
import com.water.helper.bean.GoodsModel;

import java.util.ArrayList;

/**
 * <p>
 * 打印机列表
 * </p>
 * Created by Administrator on 2017/11/28 0028.
 */
public class PrinterListAdapter extends BaseAdapter {

    private Context mContext;
    private ArrayList<GoodsModel> goodsModels;

    public PrinterListAdapter(Context context, ArrayList<GoodsModel> list) {
        this.mContext = context;
        this.goodsModels = list;
    }

    @Override
    public int getCount() {
        return goodsModels == null ? 0 : goodsModels.size();
    }

    @Override
    public Object getItem(int position) {
        return goodsModels.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_printer_list, parent, false);
        }

        GoodsModel model = goodsModels.get(position);

        TextView mTvtype = ViewHolderUtil.get(convertView, R.id.tv_item_printer_type);
        TextView mTvnum = ViewHolderUtil.get(convertView, R.id.tv_item_printer_num);
        TextView mTvnum_wu = ViewHolderUtil.get(convertView, R.id.tv_item_printer_num_wu);

        mTvtype.setText(model.getTitle());
        mTvnum.setText(String.valueOf(model.getNum()));
        mTvnum_wu.setText(String.valueOf(model.getHuixiNum()));

        return convertView;
    }
}
