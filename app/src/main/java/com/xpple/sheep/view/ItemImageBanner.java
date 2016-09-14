package com.xpple.sheep.view;

import android.content.Context;
import android.net.Uri;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.flyco.banner.widget.Banner.BaseIndicatorBanner;
import com.xpple.sheep.R;
import com.xpple.sheep.base.ViewHolder;

public class ItemImageBanner extends BaseIndicatorBanner<String, ItemImageBanner> {

    public ItemImageBanner(Context context) {
        this(context, null, 0);
    }

    public ItemImageBanner(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ItemImageBanner(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void onTitleSlect(TextView tv, int position) {
    }

    @Override
    public View onCreateItemView(int position) {
        View inflate = View.inflate(mContext, R.layout.adapter_simple_image, null);
        SimpleDraweeView iv = ViewHolder.get(inflate, R.id.iv);

        int itemWidth = mDisplayMetrics.widthPixels;
        int itemHeight = (int) (itemWidth * 360 * 1.0f / 640);
        iv.setScaleType(ImageView.ScaleType.CENTER_CROP);
        iv.setLayoutParams(new LinearLayout.LayoutParams(itemWidth, itemHeight));
        String img_url = mDatas.get(position);
        if (!TextUtils.isEmpty(img_url)) {
            iv.setImageURI(Uri.parse(img_url));
        }

        return inflate;
    }


}
