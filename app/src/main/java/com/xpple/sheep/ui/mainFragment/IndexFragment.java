package com.xpple.sheep.ui.mainFragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.generic.RoundingParams;
import com.facebook.drawee.view.SimpleDraweeView;
import com.flyco.banner.anim.select.ZoomInEnter;
import com.xpple.sheep.R;
import com.xpple.sheep.base.BaseFragment;
import com.xpple.sheep.bean.Advertising;
import com.xpple.sheep.bean.Item;
import com.xpple.sheep.proxy.AdvertisingProxy;
import com.xpple.sheep.proxy.IntegrationShopProxy;
import com.xpple.sheep.proxy.ItemProxy;
import com.xpple.sheep.ui.ActivityActivity;
import com.xpple.sheep.ui.InvitationActivity;
import com.xpple.sheep.ui.ItemDetailsActivity;
import com.xpple.sheep.ui.NoticeActivity;
import com.xpple.sheep.ui.OverageActivity;
import com.xpple.sheep.ui.ShakeActivity;
import com.xpple.sheep.ui.VipActivity;
import com.xpple.sheep.util.CommonUtils;
import com.xpple.sheep.view.AdImageBanner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class IndexFragment extends BaseFragment implements AdvertisingProxy.IQueryAdvertisingListener, ItemProxy.IQueryItemListener {
    private SwipeRefreshLayout mSwipeLayout;
    private View parentView;
    private RelativeLayout rl_top_click;
    private List<Advertising> adList;
    private ItemProxy queryProxy;
    private AdImageBanner banner;
    private LinearLayout ll_index_item_top;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        parentView = inflater.inflate(R.layout.fragment_main_index, container, false);
        setUpViews();
        return parentView;
    }

    private void setUpViews() {
        mSwipeLayout = (SwipeRefreshLayout) parentView.findViewById(R.id.swipe_container);
        mSwipeLayout.setColorSchemeResources(R.color.global_blue_color,
                R.color.global_green_color, R.color.global_orange_color,
                R.color.global_red_color);
        mSwipeLayout.setOnRefreshListener(this::reDynamicView);
        rl_top_click = (RelativeLayout) parentView.findViewById(R.id.rl_bottom_0);
        rl_top_click.setOnClickListener(view -> {
        });

        ll_index_item_top = (LinearLayout) parentView.findViewById(R.id.ll_index_item_top);

        setUpIcon();
        initAd();
        initTop();
    }


    private void initAd() {
        banner = (AdImageBanner) parentView.findViewById(R.id.banner);
        banner.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                enableDisableSwipeRefresh(state == ViewPager.SCROLL_STATE_IDLE);
            }

            private void enableDisableSwipeRefresh(boolean b) {
                if (mSwipeLayout != null) {
                    mSwipeLayout.setEnabled(b);
                }
            }
        });
        adList = new ArrayList<>();
        addAdData();
    }

    private void addAdData() {
        AdvertisingProxy queryAdvertisingProxy = new AdvertisingProxy();
        queryAdvertisingProxy.setQueryAdvertisingListener(this);
        queryAdvertisingProxy.QueryAdvertising();
    }

    private void setUpIcon() {
        RelativeLayout rl_shake = (RelativeLayout) parentView.findViewById(R.id.rl_shake);
        rl_shake.setOnClickListener(view -> getOperation().startActivity(ShakeActivity.class));
        RelativeLayout rl_community = (RelativeLayout) parentView.findViewById(R.id.rl_community);
        rl_community.setOnClickListener(view -> getOperation().startWebActivity("http://neday.kuaizhan.com/", "社区"));
        RelativeLayout rl_integral = (RelativeLayout) parentView.findViewById(R.id.rl_integral);
        rl_integral.setOnClickListener(view -> getOperation().startActivity(VipActivity.class));
        RelativeLayout rl_integration_shop = (RelativeLayout) parentView.findViewById(R.id.rl_integration_shop);
        rl_integration_shop.setOnClickListener(view -> {
            IntegrationShopProxy integrationShopProxy = new IntegrationShopProxy(getActivity(), getActivity());
            integrationShopProxy.showIntegrationShop();
        });
        RelativeLayout rl_invitation = (RelativeLayout) parentView.findViewById(R.id.rl_invitation);
        rl_invitation.setOnClickListener(view -> getOperation().startActivity(InvitationActivity.class));
        RelativeLayout rl_overage = (RelativeLayout) parentView.findViewById(R.id.rl_overage);
        rl_overage.setOnClickListener(view -> getOperation().startActivity(OverageActivity.class));

        RelativeLayout rl_activity = (RelativeLayout) parentView.findViewById(R.id.rl_activity);
        rl_activity.setOnClickListener(view -> getOperation().startActivity(ActivityActivity.class));
        RelativeLayout rl_notice = (RelativeLayout) parentView.findViewById(R.id.rl_notice);
        rl_notice.setOnClickListener(view -> getOperation().startActivity(NoticeActivity.class));

    }

    private void initTop() {
        queryProxy = new ItemProxy();
        queryProxy.setQueryItemListener(this);
        queryTopItem();
    }

    private void queryTopItem() {
        Map<String, Object> queryMap = new HashMap<>();
        queryMap.put("include", "_User[objectId|nickname|avatar]");
        // TODO: 2016/5/11 限制返回三条以内数目 用where条件每个字段
        queryProxy.QueryItem(true, queryMap);
    }

    //重新获取
    private void reDynamicView() {
        queryTopItem();
        addAdData();
    }


    @Override
    public void onQueryAdvertisingSuccess(List<Advertising> object) {
        adList = object;// 广告数据
        banner.setSelectAnimClass(ZoomInEnter.class)
                .setSource(adList)
                .startScroll();
        banner.setOnItemClickL(position -> getOperation().startWebActivity(adList.get(position).getUrl(), adList.get(position).getTitle()));
        stopRefresh();
    }

    @Override
    public void onQueryAdvertisingFailure() {
        stopRefresh();
    }

    @Override
    public void onQueryItemSuccess(List<Item> object, boolean isUpdate) {
        rl_top_click.setVisibility(View.VISIBLE);
        ll_index_item_top.setVisibility(View.VISIBLE);
        ll_index_item_top.removeAllViews();
        for (Item item : object) {
            addTopView(item);
        }
        stopRefresh();
    }

    @Override
    public void onQueryItemFailure() {
        rl_top_click.setVisibility(View.GONE);
        ll_index_item_top.setVisibility(View.GONE);
        stopRefresh();
    }

    private void stopRefresh() {
        if (mSwipeLayout.isRefreshing()) {
            mSwipeLayout.setRefreshing(false);
        }
    }

    private void addTopView(Item item) {
        LinearLayout.LayoutParams layoutParam_text = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParam_text.setMargins(0, CommonUtils.dip2px(getActivity(), 10), 0, 0);

        TextView tv_title = new TextView(getActivity());
        tv_title.setText(item.getTitle());
        tv_title.setTextColor(getResources().getColor(R.color.global_black_color));
        tv_title.setLayoutParams(layoutParam_text);

        TextView tv_money = new TextView(getActivity());
        tv_money.setText(item.getPrice());
        tv_money.setTextColor(getResources().getColor(R.color.global_red_color));
        tv_money.setLayoutParams(layoutParam_text);

        LinearLayout.LayoutParams layoutParam_l = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParam_l.weight = 1;
        LinearLayout linearLayout_l = new LinearLayout(getActivity());
        linearLayout_l.setLayoutParams(layoutParam_l);
        linearLayout_l.setOrientation(LinearLayout.VERTICAL);
        linearLayout_l.setPadding(CommonUtils.dip2px(getActivity(), 10), 0, CommonUtils.dip2px(getActivity(), 10), 0);
        linearLayout_l.addView(tv_title);
        linearLayout_l.addView(tv_money);

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(CommonUtils.dip2px(getActivity(), 80), CommonUtils.dip2px(getActivity(), 80));
        SimpleDraweeView img = new SimpleDraweeView(getActivity());
        img.setScaleType(SimpleDraweeView.ScaleType.CENTER_CROP);
        img.setLayoutParams(layoutParams);
        GenericDraweeHierarchy hierarchy = img.getHierarchy();
        hierarchy.setFailureImage(getResources().getDrawable(R.drawable.icon_error));
        hierarchy.setPlaceholderImage(R.drawable.icon_stub);
        RoundingParams roundingParams = RoundingParams.fromCornersRadius(CommonUtils.dip2px(getActivity(), 5));
        hierarchy.setRoundingParams(roundingParams);
        img.setHierarchy(hierarchy);
        String img_url = item.getPic_a();
        if (img_url != null && !img_url.equals("")) {
            Uri uri = Uri.parse(img_url);
            img.setImageURI(uri);
        } else {
            img.setImageResource(R.mipmap.avatar_default);
        }

        LinearLayout.LayoutParams layoutParam_r = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        LinearLayout linearLayout_r = new LinearLayout(getActivity());
        linearLayout_r.setLayoutParams(layoutParam_r);
        linearLayout_r.setOrientation(LinearLayout.VERTICAL);
        linearLayout_r.setPadding(0, CommonUtils.dip2px(getActivity(), 10), CommonUtils.dip2px(getActivity(), 10), CommonUtils.dip2px(getActivity(), 10));
        layoutParam_r.gravity = Gravity.END;

        linearLayout_r.addView(img);

        LinearLayout linearLayout_root = new LinearLayout(getActivity());
        linearLayout_root.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        linearLayout_root.setBackgroundColor(getResources().getColor(android.R.color.white));
        linearLayout_root.setOrientation(LinearLayout.HORIZONTAL);

        linearLayout_root.addView(linearLayout_l);
        linearLayout_root.addView(linearLayout_r);

        linearLayout_root.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(),
                    ItemDetailsActivity.class);
            intent.putExtra("item", item);
            startActivity(intent);
        });
        View line = new View(getActivity());
        line.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, CommonUtils.dip2px(getActivity(), 0.3F)));
        line.setBackgroundColor(getResources().getColor(R.color.divide_line_color));

        ll_index_item_top.addView(linearLayout_root);
        ll_index_item_top.addView(line);
    }

    @Override
    public void onResume() {
        super.onResume();
        banner.goOnScroll();
    }

    @Override
    public void onPause() {
        super.onPause();
        banner.pauseScroll();
    }
}
