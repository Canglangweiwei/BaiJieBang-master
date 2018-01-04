package com.water.helper.adapter;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jaydenxiao.common.commonutils.ViewHolderUtil;
import com.water.helper.R;
import com.water.helper.bean.GoodsModel;
import com.water.helper.util.SimpleTextWather;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("ALL")
public class GoodsAdapter extends BaseAdapter {

    private ArrayList<GoodsModel> goodsModels;
    private GoodsActionCallback callback;

    public GoodsAdapter() {
        this.goodsModels = new ArrayList<>();
    }

    public void resetData(ArrayList<GoodsModel> goodsModels) {
        if (goodsModels == null)
            return;
        this.goodsModels = goodsModels;
        this.notifyDataSetChanged();
    }

    public void setCallback(GoodsActionCallback callback) {
        this.callback = callback;
    }

    public List<GoodsModel> getGoodsModels() {
        return goodsModels;
    }

    @Override
    public int getCount() {
        return goodsModels.size();
    }

    @Override
    public GoodsModel getItem(int position) {
        return goodsModels.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_line_shou, parent, false);
        }
        TextView tvName = ViewHolderUtil.get(convertView, R.id.item_add_goods_name);
        final EditText etLine = ViewHolderUtil.get(convertView, R.id.item_goods_num);
        TextView tvNumHuixi = ViewHolderUtil.get(convertView, R.id.item_huixi_goods_num);
        RelativeLayout item_good_reduce = ViewHolderUtil.get(convertView, R.id.ly_item_goods_reduce);
        RelativeLayout item_good_add = ViewHolderUtil.get(convertView, R.id.ly_item_goods_add);
        RelativeLayout item_good_reduce_wu = ViewHolderUtil.get(convertView, R.id.ly_item_huixi_goods_reduce);
        RelativeLayout item_good_add_wu = ViewHolderUtil.get(convertView, R.id.ly_item_huixi_goods_add);

        final GoodsModel goodsModel = goodsModels.get(position);
        tvName.setText(goodsModel.getTitle());
        tvNumHuixi.setText(String.valueOf(goodsModel.getHuixiNum()));

        if (etLine.getTag() instanceof TextWatcher) {
            etLine.removeTextChangedListener((TextWatcher) (etLine.getTag()));
        }

        if (TextUtils.isEmpty(String.valueOf(goodsModel.getNum()))) {
            etLine.setText("0");
        } else {
            etLine.setText(String.valueOf(goodsModel.getNum()));
        }

        if (goodsModel.isFocus()) {
            if (!etLine.isFocused()) {
                etLine.requestFocus();
            }
            CharSequence text = String.valueOf(goodsModel.getNum());
            etLine.setSelection(TextUtils.isEmpty(text) ? 0 : text.length());
        } else {
            if (etLine.isFocused()) {
                etLine.clearFocus();
            }
        }

        etLine.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(final View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    final boolean focus = goodsModel.isFocus();
                    check(position);
                    if (!focus && !etLine.isFocused()) {
                        etLine.requestFocus();
                        etLine.onWindowFocusChanged(true);
                    }
                }
                return false;
            }
        });

        final TextWatcher watcher = new SimpleTextWather() {

            @Override
            public void afterTextChanged(Editable s) {
                if (callback == null || TextUtils.isEmpty(s)) {
                    return;
                }
                callback.inputGoods(position, String.valueOf(s));
            }
        };
        etLine.addTextChangedListener(watcher);
        etLine.setTag(watcher);

        // 常规加
        item_good_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enFoucs();
                if (callback == null) {
                    return;
                }
                callback.addGoods(v, position);
            }
        });
        // 常规减
        item_good_reduce.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enFoucs();
                if (callback == null || 0 == goodsModel.getNum()) {
                    return;
                }
                callback.reduceGoods(position);
            }
        });
        // 回洗加
        item_good_add_wu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enFoucs();
                if (callback == null) {
                    return;
                }
                callback.addHuixi(v, position);
            }
        });
        // 回洗减
        item_good_reduce_wu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enFoucs();
                if (callback == null || 0 == goodsModel.getHuixiNum()) {
                    return;
                }
                callback.reduceHuixi(position);
            }
        });
        return convertView;
    }

    private void check(int position) {
        for (GoodsModel l : goodsModels) {
            l.setFocus(false);
        }
        goodsModels.get(position).setFocus(true);
    }

    private void enFoucs() {
        for (GoodsModel l : goodsModels) {
            l.setFocus(false);
        }
    }

    public interface GoodsActionCallback {
        void addGoods(View view, int position);

        void inputGoods(int position, String s);

        void reduceGoods(int position);

        void addHuixi(View view, int position);

        void reduceHuixi(int position);
    }

    public void reset() {
        notifyDataSetChanged();
    }
}
