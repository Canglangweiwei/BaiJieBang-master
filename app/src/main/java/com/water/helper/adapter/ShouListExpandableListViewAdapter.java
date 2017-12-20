package com.water.helper.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseExpandableListAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.jaydenxiao.common.commonutils.ToastUitl;
import com.water.helper.R;
import com.water.helper.bean.ShouChildBean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * <p>
 * 功能说明：自定义Adapter
 * </p>
 * Created by weiwei On 2017-3-2 上午11:39:58
 */
public class ShouListExpandableListViewAdapter extends BaseExpandableListAdapter {

    private List<String> group = new ArrayList<>();
    private List<List<ShouChildBean>> gridViewChild = new ArrayList<>();
    private LayoutInflater mInflater;
    private Context context;

    public ShouListExpandableListViewAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
        this.context = context;
    }

    public void resetData(List<String> group, List<List<ShouChildBean>> gridViewChild) {
        if (group != null)
            this.group = group;
        if (gridViewChild != null)
            this.gridViewChild = gridViewChild;
        notifyDataSetChanged();
    }

    public void clear() {
        group.clear();
        gridViewChild.clear();
        notifyDataSetChanged();
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return gridViewChild.get(groupPosition).get(childPosition);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    /**
     * child视图生成器
     */
    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild,
                             View convertView, ViewGroup parent) {
        if (convertView == null) {
            mViewChild = new ViewChild();
            convertView =
                    mInflater.inflate(R.layout.channel_expandablelistview_shou_item, parent, false);
            mViewChild.gridView =
                    (GridView) convertView.findViewById(R.id.channel_item_child_gridView);
            convertView.setTag(mViewChild);
        } else {
            mViewChild = (ViewChild) convertView.getTag();
        }

        SimpleAdapter mSimpleAdapter =
                new SimpleAdapter(context, setGridViewData(gridViewChild.get(groupPosition)),
                        R.layout.channel_gridview_item, new String[]{"channel_gridview_item"},
                        new int[]{R.id.channel_gridview_item});
        mViewChild.gridView.setAdapter(mSimpleAdapter);
        setGridViewListener(mViewChild.gridView);
        return convertView;
    }

    /**
     * 设置gridview点击事件监听
     */
    private void setGridViewListener(final GridView gridView) {
        gridView.setOnItemClickListener(new GridView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (view instanceof TextView) {
                    // 如果想要获取到哪一行，则自定义gridview的adapter，item设置2个textview一个隐藏设置id，显示哪一行
                    TextView tv = (TextView) view;
                    ToastUitl.showShort(tv.getText().toString().trim());
                }
            }
        });
    }

    /**
     * 设置gridview数据
     */
    private ArrayList<HashMap<String, Object>> setGridViewData(List<ShouChildBean> data) {
        ArrayList<HashMap<String, Object>> gridItem = new ArrayList<>();
        for (ShouChildBean aData : data) {
            HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put("channel_gridview_item", aData.getName() + ":" + aData.getSum());
            gridItem.add(hashMap);
        }
        return gridItem;
    }

    // getChildrenCount返回1 因为用一个Item 来加载GridView
    @Override
    public int getChildrenCount(int groupPosition) {
        return 1;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return group.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return group == null ? 0 : group.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    /**
     * group视图生成器
     */
    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView,
                             ViewGroup parent) {
        if (convertView == null) {
            mViewChild = new ViewChild();
            convertView = mInflater.inflate(R.layout.channel_expandablelistview, parent, false);
            mViewChild.textView = (TextView) convertView.findViewById(R.id.channel_group_name);
            mViewChild.imageView =
                    (ImageView) convertView.findViewById(R.id.channel_imageview_orientation);
            convertView.setTag(mViewChild);
        } else {
            mViewChild = (ViewChild) convertView.getTag();
        }

        /**
         * 展开图标
         */
        if (isExpanded) {// 展开
            mViewChild.imageView.setImageResource(R.drawable.channel_expandablelistview_top_icon);
        } else {// 折叠
            mViewChild.imageView
                    .setImageResource(R.drawable.channel_expandablelistview_bottom_icon);
        }
        mViewChild.textView.setText(getGroup(groupPosition).toString());
        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    private ViewChild mViewChild;

    private static class ViewChild {
        ImageView imageView;
        TextView textView;
        GridView gridView;
    }
}
