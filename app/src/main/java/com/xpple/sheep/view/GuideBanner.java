package com.xpple.sheep.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.flyco.banner.widget.Banner.BaseIndicatorBanner;
import com.xpple.sheep.R;
import com.xpple.sheep.base.ViewHolder;

public class GuideBanner extends BaseIndicatorBanner<Integer, GuideBanner> {
    public GuideBanner(Context context) {
        this(context, null, 0);
    }

    public GuideBanner(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public GuideBanner(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setBarShowWhenLast(false);
    }

    @Override
    public View onCreateItemView(int position) {
        View inflate = View.inflate(mContext, R.layout.adapter_simple_guide, null);
        ImageView iv = ViewHolder.get(inflate, R.id.iv);
        TextView tv_jump = ViewHolder.get(inflate, R.id.tv_jump);

        final Integer resId = mDatas.get(position);
        tv_jump.setVisibility(position == mDatas.size() - 1 ? VISIBLE : GONE);
        iv.setImageResource(resId);
        tv_jump.setOnClickListener(v -> {
            if (onJumpClickL != null)
                onJumpClickL.onJumpClick();
        });

        return inflate;
    }

    private OnJumpClickL onJumpClickL;

    public interface OnJumpClickL {
        void onJumpClick();
    }

    public void setOnJumpClickL(OnJumpClickL onJumpClickL) {
        this.onJumpClickL = onJumpClickL;
    }
}
