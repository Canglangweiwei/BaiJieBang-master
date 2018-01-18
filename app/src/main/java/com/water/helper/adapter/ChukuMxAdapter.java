package com.water.helper.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.water.helper.bean.ChuKuMxBean;
import com.water.helper.config.presenter.BackWashPresenter;
import com.water.helper.viewholder.ChukuMxHolder;

/**
 * des: 出库明细的adapter
 * Created by xsf
 * on 2016.08.14:19
 */
@SuppressWarnings("ALL")
public class ChukuMxAdapter extends BaseReclyerViewAdapter<ChuKuMxBean> {

    private BackWashPresenter mPresenter;

    public ChukuMxAdapter(Context context, BackWashPresenter presenter) {
        super(context);
        this.mContext = context;
        this.mPresenter = presenter;
    }

    @Override
    public int getItemViewType(int position) {
        return 0;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return ChukuMxHolder.create(mContext);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        if (holder instanceof ChukuMxHolder) {
            ((ChukuMxHolder) holder).setData(get(position), mPresenter);
        }
    }

    /**
     * 显示回洗按钮
     */
    public void startOper() {
        for (ChuKuMxBean bean : data) {
            bean.setMode(1);
        }
        notifyDataSetChanged();
    }

    /**
     * 隐藏回洗按钮
     */
    public void cancelOper() {
        for (ChuKuMxBean bean : data) {
            bean.setMode(0);
        }
        notifyDataSetChanged();
    }
}
