package com.water.helper.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.water.helper.R;
import com.water.helper.bean.MemberBean;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("ALL")
public class MemberAdapter extends RecyclerView.Adapter<MemberAdapter.ViewHolder> {

    public interface OnSeletedListener {
        void onSelected(MemberBean memberBean);
    }

    private OnSeletedListener onSeletedListener;

    public List<MemberBean> data;

    public MemberAdapter(OnSeletedListener onSeletedListener) {
        this.onSeletedListener = onSeletedListener;
        this.data = new ArrayList<>();
    }

    public void resetData(List<MemberBean> list) {
        if (list != null) {
            this.data.clear();
            this.data.addAll(list);
            notifyDataSetChanged();
        }
    }

    public void addAllData(List<MemberBean> list) {
        if (list != null) {
            this.data.addAll(list);
            notifyDataSetChanged();
        }
    }

    /**
     * 创建新View，被LayoutManager所调用
     */
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_member, viewGroup, false);
        return new ViewHolder(view);
    }

    /**
     * 将数据与界面进行绑定的操作
     */
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        final MemberBean item = data.get(position);
        viewHolder.mTextView.setText(item.getUsername());
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSeletedListener.onSelected(item);
            }
        });
    }

    /**
     * 获取数据的数量
     */
    @Override
    public int getItemCount() {
        return data.size();
    }

    /**
     * 自定义的ViewHolder，持有每个Item的的所有界面元素
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView mTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            mTextView = (TextView) itemView.findViewById(R.id.item_tv_member);
        }
    }
}
