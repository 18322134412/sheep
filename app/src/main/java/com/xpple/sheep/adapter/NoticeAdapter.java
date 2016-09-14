package com.xpple.sheep.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.xpple.sheep.R;
import com.xpple.sheep.base.BaseListAdapter;
import com.xpple.sheep.base.ViewHolder;
import com.xpple.sheep.bean.Notice;

import java.util.List;

public class NoticeAdapter extends BaseListAdapter<Notice> {

    public NoticeAdapter(Context context, List<Notice> items) {
        super(context, items);
    }

    @Override
    public View bindView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.adapter_notice, null);
        }
        Notice contract = getList().get(position);
        TextView tv_time = ViewHolder.get(convertView, R.id.tv_time);
        TextView tv_title = ViewHolder.get(convertView, R.id.tv_title);
        TextView tv_content = ViewHolder.get(convertView, R.id.tv_content);

        tv_time.setText(contract.getUpdatedAt().trim());
        tv_title.setText(contract.getTitle().trim());
        tv_content.setText(contract.getContent().trim());
        return convertView;
    }


}
