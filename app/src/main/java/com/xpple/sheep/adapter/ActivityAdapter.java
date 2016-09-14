package com.xpple.sheep.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.xpple.sheep.R;
import com.xpple.sheep.base.BaseListAdapter;
import com.xpple.sheep.base.ViewHolder;
import com.xpple.sheep.bean.Activity;

import java.util.List;

public class ActivityAdapter extends BaseListAdapter<Activity> {

    public ActivityAdapter(Context context, List<Activity> items) {
        super(context, items);
    }

    @Override
    public View bindView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.adapter_activity, null);
        }
        Activity contract = getList().get(position);
        TextView tv_time = ViewHolder.get(convertView, R.id.tv_title);
        TextView tv_title = ViewHolder.get(convertView, R.id.tv_title);
        SimpleDraweeView riv_img = ViewHolder.get(convertView, R.id.riv_img);

        tv_time.setText(contract.getUpdatedAt().trim());
        tv_title.setText(contract.getTitle().trim());
        String url = contract.getPic();
        if (url != null && !url.equals("")) {
            Uri uri = Uri.parse(url);
            riv_img.setImageURI(uri);
        } else {
            riv_img.setImageResource(R.drawable.icon_error);
        }
        return convertView;
    }


}
