package com.xpple.sheep.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.xpple.sheep.R;
import com.xpple.sheep.base.BaseListAdapter;
import com.xpple.sheep.base.ViewHolder;
import com.xpple.sheep.bean.Item;

import java.util.List;

public class ItemPassAdapter extends BaseListAdapter<Item> {

    public ItemPassAdapter(Context context, List<Item> items) {
        super(context, items);
    }

    @Override
    public View bindView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.adapter_item_pass, null);
        }
        Item contract = getList().get(position);
        TextView tv_title = ViewHolder.get(convertView, R.id.tv_title);
        TextView tv_reason = ViewHolder.get(convertView, R.id.tv_reason);
        TextView tv_state = ViewHolder.get(convertView, R.id.tv_state);
        TextView tv_time = ViewHolder.get(convertView, R.id.tv_time);

//        tv_title.setText(contract.getTitle().trim());
//        tv_reason.setText(contract.getTitle().trim());
//        tv_time.setText(TimeUtils.getCurrentTime(TimeUtils.FORMAT_DATE_TIME));
//        tv_state.setText(contract.getType().trim());


        return convertView;
    }


}
