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

import com.water.helper.R;
import com.water.helper.bean.GoodsModel;
import com.water.helper.util.SimpleTextWather;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

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
        final ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_line_shou, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        final GoodsModel goodsModel = goodsModels.get(position);
        viewHolder.tvName.setText(goodsModel.getTitle());
        viewHolder.tvNumWu.setText(String.valueOf(goodsModel.getNum_wu()));

        if (viewHolder.etLine.getTag() instanceof TextWatcher) {
            viewHolder.etLine.removeTextChangedListener((TextWatcher) (viewHolder.etLine.getTag()));
        }

        if (TextUtils.isEmpty(String.valueOf(goodsModel.getNum()))) {
            viewHolder.etLine.setText("0");
        } else {
            viewHolder.etLine.setText(String.valueOf(goodsModel.getNum()));
        }

        if (goodsModel.isFocus()) {
            if (!viewHolder.etLine.isFocused()) {
                viewHolder.etLine.requestFocus();
            }
            CharSequence text = String.valueOf(goodsModel.getNum());
            viewHolder.etLine.setSelection(TextUtils.isEmpty(text) ? 0 : text.length());
        } else {
            if (viewHolder.etLine.isFocused()) {
                viewHolder.etLine.clearFocus();
            }
        }

        viewHolder.etLine.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(final View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    final boolean focus = goodsModel.isFocus();
                    check(position);
                    if (!focus && !viewHolder.etLine.isFocused()) {
                        viewHolder.etLine.requestFocus();
                        viewHolder.etLine.onWindowFocusChanged(true);
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
        viewHolder.etLine.addTextChangedListener(watcher);
        viewHolder.etLine.setTag(watcher);

        // 常规加
        viewHolder.item_good_add.setOnClickListener(new View.OnClickListener() {
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
        viewHolder.item_good_reduce.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enFoucs();
                if (callback == null || 0 == goodsModel.getNum()) {
                    return;
                }
                callback.reduceGoods(position);
            }
        });
        // 重污加
        viewHolder.item_good_add_wu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enFoucs();
                if (callback == null) {
                    return;
                }
                callback.addWu(v, position);
            }
        });
        // 重污减
        viewHolder.item_good_reduce_wu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enFoucs();
                if (callback == null || 0 == goodsModel.getNum_wu()) {
                    return;
                }
                callback.reduceWu(position);
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

    class ViewHolder {
        @Bind(R.id.item_add_goods_name)
        TextView tvName;
        @Bind(R.id.item_goods_num)
        EditText etLine;
        @Bind(R.id.item_wu_goods_num)
        TextView tvNumWu;
        @Bind(R.id.ly_item_goods_reduce)
        RelativeLayout item_good_reduce;
        @Bind(R.id.ly_item_goods_add)
        RelativeLayout item_good_add;

        @Bind(R.id.ly_item_wu_goods_reduce)
        RelativeLayout item_good_reduce_wu;
        @Bind(R.id.ly_item_wu_goods_add)
        RelativeLayout item_good_add_wu;

        ViewHolder(View itemView) {
            ButterKnife.bind(this, itemView);
        }
    }

    public interface GoodsActionCallback {
        void addGoods(View view, int position);

        void inputGoods(int position, String s);

        void reduceGoods(int position);

        void addWu(View view, int position);

        void reduceWu(int position);
    }

    public void reset() {
        notifyDataSetChanged();
    }
}
