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
import com.xpple.sheep.bean.Comment;
import com.xpple.sheep.bean.User;

import java.util.List;

public class CommentAdapter extends BaseListAdapter<Comment> {

    public CommentAdapter(Context context, List<Comment> items) {
        super(context, items);
    }

    @Override
    public View bindView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.adapter_comment, null);
        }
        Comment contract = getList().get(position);
        TextView tv_author_nickname = ViewHolder.get(convertView, R.id.tv_author_nickname);
        TextView tv_time = ViewHolder.get(convertView, R.id.tv_time);
        TextView tv_content = ViewHolder.get(convertView, R.id.tv_content);
        SimpleDraweeView iv_author_avatar = ViewHolder.get(convertView, R.id.iv_author_avatar);
        TextView tv_chat = ViewHolder.get(convertView, R.id.tv_chat);
        User author = contract.getAuthor();
        tv_author_nickname.setText(author.getNickname().trim());
        tv_time.setText(contract.getUpdatedAt().trim());
        tv_content.setText(contract.getContent().trim());
        String url = author.getAvatar();
        if (url != null && !url.equals("")) {
            Uri uri = Uri.parse(url);
            iv_author_avatar.setImageURI(uri);
        } else {
            iv_author_avatar.setImageResource(R.mipmap.avatar_default);
        }
//        if (contract.getReplyCount() != 0) {
//            tv_chat.setVisibility(View.VISIBLE);
//            tv_chat.setOnClickListener(view -> {
//                Intent intent = new Intent(mContext,
//                        CommentActivity.class);
//                intent.putExtra("itemObjectId", contract.getObjectId());
//                mContext.startActivity(intent);
//            });
//        }
        return convertView;
    }


}
