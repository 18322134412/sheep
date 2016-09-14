package com.xpple.sheep.view;

import android.content.Context;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.flyco.banner.widget.Banner.BaseIndicatorBanner;
import com.xpple.sheep.R;
import com.xpple.sheep.base.ViewHolder;
import com.xpple.sheep.bean.Advertising;

public class AdImageBanner extends BaseIndicatorBanner<Advertising, AdImageBanner> {

    public AdImageBanner(Context context) {
        this(context, null, 0);
    }

    public AdImageBanner(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AdImageBanner(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void onTitleSlect(TextView tv, int position) {
        final Advertising item = mDatas.get(position);
        tv.setText(item.getTitle());
    }

    @Override
    public View onCreateItemView(int position) {
        View inflate = View.inflate(mContext, R.layout.adapter_simple_image, null);
        SimpleDraweeView iv = ViewHolder.get(inflate, R.id.iv);

        final Advertising item = mDatas.get(position);
        int itemWidth = mDisplayMetrics.widthPixels;
        int itemHeight = (int) (itemWidth * 360 * 1.0f / 640);
        iv.setScaleType(ImageView.ScaleType.CENTER_CROP);
        iv.setLayoutParams(new LinearLayout.LayoutParams(itemWidth, itemHeight));
        iv.setImageURI(Uri.parse(item.getPic()));
        return inflate;
    }


}
