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
import com.xpple.sheep.bean.Message;
import com.xpple.sheep.bean.User;

import java.util.List;

public class MessageAdapter extends BaseListAdapter<Message> {

    public MessageAdapter(Context context, List<Message> items) {
        super(context, items);
    }

    @Override
    public View bindView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.adapter_message, null);
        }
        Message contract = getList().get(position);
        TextView tv_belong_nickname = ViewHolder.get(convertView, R.id.tv_belong_nickname);
        TextView tv_time = ViewHolder.get(convertView, R.id.tv_time);
        TextView tv_isReaded = ViewHolder.get(convertView, R.id.tv_isReaded);
        TextView tv_content = ViewHolder.get(convertView, R.id.tv_content);
        SimpleDraweeView iv_belong_avatar = ViewHolder.get(convertView, R.id.iv_belong_avatar);
        User BelongId = contract.getBelongId();
        tv_belong_nickname.setText(BelongId.getNickname().trim());
        tv_time.setText(contract.getUpdatedAt().trim());
        tv_isReaded.setText(contract.getIsReaded() ? "已读" : "未读");
        tv_content.setText(contract.getContent().trim());
        String url = BelongId.getAvatar();
        if (url != null && !url.equals("")) {
            Uri uri = Uri.parse(url);
            iv_belong_avatar.setImageURI(uri);
        } else {
            iv_belong_avatar.setImageResource(R.mipmap.avatar_default);
        }
        return convertView;
    }


}
