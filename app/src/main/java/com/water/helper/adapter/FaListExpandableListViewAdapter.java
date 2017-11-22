package com.water.helper.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.water.helper.R;
import com.water.helper.bean.FaChildBean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * <p>
 * 功能说明：自定义Adapter
 * </p>
 * Created by weiwei On 2017-3-2 上午11:39:58
 */
public class FaListExpandableListViewAdapter extends BaseExpandableListAdapter {

    private List<String> group = new ArrayList<>();
    private List<List<FaChildBean>> gridViewChild = new ArrayList<>();
    private LayoutInflater mInflater;
    private Context context;

    public FaListExpandableListViewAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
        this.context = context;
    }

    public void resetData(List<String> group, List<List<FaChildBean>> gridViewChild) {
        if (group != null)
            this.group = group;
        if (gridViewChild != null)
            this.gridViewChild = gridViewChild;
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
                    mInflater.inflate(R.layout.channel_expandablelistview_fa_item, parent, false);
            mViewChild.listView =
                    (ListView) convertView.findViewById(R.id.channel_item_child_listView);
            convertView.setTag(mViewChild);
        } else {
            mViewChild = (ViewChild) convertView.getTag();
        }

        SimpleAdapter mSimpleAdapter =
                new SimpleAdapter(context, setGridViewData(gridViewChild.get(groupPosition)),
                        R.layout.channel_listview_item,
                        new String[]{"channel_gridview_i_txt", "channel_listview_y_sum", "channel_listview_w_sum", "channel_listview_z_sum"},
                        new int[]{R.id.fa_name, R.id.fa_y_sum, R.id.fa_w_sum, R.id.fa_z_sum});
        mViewChild.listView.setAdapter(mSimpleAdapter);
        return convertView;
    }

    /**
     * 设置gridview数据
     */
    private ArrayList<HashMap<String, Object>> setGridViewData(List<FaChildBean> data) {
        ArrayList<HashMap<String, Object>> gridItem = new ArrayList<>();
        for (FaChildBean aData : data) {
            HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put("channel_gridview_i_txt", aData.getName());
            hashMap.put("channel_listview_y_sum", aData.getY_sum());
            hashMap.put("channel_listview_w_sum", aData.getN_sum());
            hashMap.put("channel_listview_z_sum", aData.getZ_sum());
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
        ListView listView;
    }
}
