package com.pioneeriot.pioneeriot.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.pioneeriot.pioneeriot.R;
import com.pioneeriot.pioneeriot.bean.Hierarchy;

import java.util.List;


/**
 * Created by 李玉亮 on 2017/4/8 0008.
 * 水司层级查询的适配器
 *
 * @author LiYuliang
 * @version 2017/11/20
 */

public class HierarchyAdapter extends BaseAdapter {

    private List<Hierarchy> list;
    private Context context;

    public HierarchyAdapter(Context c, List<Hierarchy> lv) {
        context = c;
        list = lv;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_list_hierarchy, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        Hierarchy hierarchy = list.get(position);
        if (TextUtils.isEmpty(hierarchy.getText())) {
            viewHolder.tvHierarchy.setText("暂无信息");
        } else {
            viewHolder.tvHierarchy.setText(hierarchy.getText());
        }
        return convertView;
    }


    private class ViewHolder {
        private TextView tvHierarchy;

        private ViewHolder(View view) {
            tvHierarchy = view.findViewById(R.id.tv_hierarchy);
        }
    }
}
