package com.xpple.sheep.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.xpple.sheep.R;
import com.xpple.sheep.base.BaseListAdapter;
import com.xpple.sheep.base.ViewHolder;
import com.xpple.sheep.bean.Item;
import com.xpple.sheep.bean.User;
import com.xpple.sheep.util.TimeUtils;

import java.util.List;

public class ItemAdapter extends BaseListAdapter<Item> {

    public ItemAdapter(Context context, List<Item> items) {
        super(context, items);
    }

    @Override
    public View bindView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.adapter_item, null);
        }
        Item contract = getList().get(position);
        TextView tv_belong_nickname = ViewHolder.get(convertView, R.id.tv_belong_nickname);
        SimpleDraweeView iv_belong_avatar = ViewHolder.get(convertView, R.id.iv_belong_avatar);
        TextView tv_title = ViewHolder.get(convertView, R.id.tv_title);
        TextView tv_money = ViewHolder.get(convertView, R.id.tv_money);
        TextView tv_time = ViewHolder.get(convertView, R.id.tv_time);
        TextView tv_type = ViewHolder.get(convertView, R.id.tv_type_change);
        SimpleDraweeView iv_img = ViewHolder.get(convertView, R.id.iv_img_shower);
        TextView tv_hot = ViewHolder.get(convertView, R.id.tv_hot);
        TextView tv_diggs = ViewHolder.get(convertView, R.id.tv_diggs);
        ImageView iv_diggs = ViewHolder.get(convertView, R.id.iv_diggs);
        TextView tv_comment = ViewHolder.get(convertView, R.id.tv_comment);

        User author = contract.get_User();
        tv_belong_nickname.setText(author.getNickname().trim());
        String avatar_url = author.getAvatar();
        if (avatar_url != null && !avatar_url.equals("")) {
            Uri uri = Uri.parse(avatar_url);
            iv_belong_avatar.setImageURI(uri);
        } else {
            iv_belong_avatar.setImageResource(R.mipmap.avatar_default);
        }
        tv_title.setText(contract.getTitle().trim());
        tv_money.setText(contract.getPrice().trim());
        tv_time.setText(TimeUtils.getCurrentTime(TimeUtils.FORMAT_DATE_TIME));
        tv_type.setText(contract.getType().trim());

        String img_url = contract.getPic_a();
        if (img_url != null && !img_url.equals("")) {
            Uri uri = Uri.parse(img_url);
            iv_img.setImageURI(uri);
        } else {
            iv_img.setImageResource(R.mipmap.avatar_default);
        }

        tv_hot.setText(String.format(mContext.getString(R.string.number_to_string), contract.getHot()));
        Integer love = contract.getLove();
        tv_diggs.setText(String.format(mContext.getString(R.string.number_to_string), contract.getLove()));
        if (love < 0) {
            iv_diggs.setImageResource(R.mipmap.ic_action_disdiggs_normal);
        } else {
            iv_diggs.setImageResource(R.mipmap.ic_action_diggs_normal);
        }
//        tv_comment.setText(contract.getComment().getCount());
        return convertView;
    }


}
