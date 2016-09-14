package com.xpple.sheep.ui;

import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.sdk.android.AlibabaSDK;
import com.alibaba.sdk.android.trade.TradeConstants;
import com.alibaba.sdk.android.trade.TradeService;
import com.alibaba.sdk.android.trade.callback.TradeProcessCallback;
import com.alibaba.sdk.android.trade.model.TaokeParams;
import com.alibaba.sdk.android.trade.model.TradeResult;
import com.alibaba.sdk.android.trade.page.ItemDetailPage;
import com.bigkoo.svprogresshud.SVProgressHUD;
import com.facebook.drawee.view.SimpleDraweeView;
import com.flyco.banner.anim.select.RotateEnter;
import com.flyco.banner.anim.unselect.NoAnimExist;
import com.kepler.jd.login.KeplerApiManager;
import com.xpple.sheep.R;
import com.xpple.sheep.base.BaseActivity;
import com.xpple.sheep.bean.Item;
import com.xpple.sheep.bean.ItemUser;
import com.xpple.sheep.bean.User;
import com.xpple.sheep.proxy.ItemProxy;
import com.xpple.sheep.util.CommonUtils;
import com.xpple.sheep.view.ItemImageBanner;
import com.xpple.sheep.view.ShareDialog;
import com.xpple.sheep.view.dialog.AlertDialog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ItemDetailsActivity extends BaseActivity implements ItemProxy.IDeleteItemListener, ItemProxy.IQueryItemUserListener, ItemProxy.IAddItemUserListener {
    private LinearLayout ll_disdiggs, ll_diggs, ll_pay, ll_fav, ll_comment, ll_buy;
    private ImageView iv_disdiggs, iv_diggs, iv_fav, iv_diggs_;
    private TextView tv_disdiggs, tv_diggs, tv_fav;
    private boolean isDisdiggs, isDiggs, isFav;
    private SimpleDraweeView iv_belong_avatar;
    private TextView tv_belong_nickname, tv_time, tv_type, tv_title, tv_money, tv_details;
    private TextView tv_diggs_value, tv_hot_value, tv_comment_value;
    private TextView tv_update, tv_delete;
    private List<String> adList;
    private ItemProxy itemProxy;
    private User user, author;//当前用户
    private Item item;
    private String mall_name;
    private SVProgressHUD svProgressHUD;
    public static final int DO_LOVE = 1;
    public static final int DO_DISLOVE = 2;
    public static final int DO_FAV = 3;
    private ItemImageBanner banner;

    @Override
    public int bindLayout() {
        return R.layout.activity_item_details;
    }

    @Override
    public void initView(View view) {
        setTintManager();
        tv_belong_nickname = (TextView) findViewById(R.id.tv_belong_nickname);
        tv_time = (TextView) findViewById(R.id.tv_time);
        tv_type = (TextView) findViewById(R.id.tv_type_change);
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_money = (TextView) findViewById(R.id.tv_money);
        tv_details = (TextView) findViewById(R.id.tv_details);
        iv_diggs_ = (ImageView) findViewById(R.id.iv_diggs_);
        tv_diggs_value = (TextView) findViewById(R.id.tv_diggs_value);
        tv_hot_value = (TextView) findViewById(R.id.tv_hot_value);
        tv_comment_value = (TextView) findViewById(R.id.tv_comment_value);
        iv_belong_avatar = (SimpleDraweeView) findViewById(R.id.iv_belong_avatar);
        iv_disdiggs = (ImageView) findViewById(R.id.iv_disdiggs);
        iv_diggs = (ImageView) findViewById(R.id.iv_diggs);
        iv_fav = (ImageView) findViewById(R.id.iv_fav);
        ll_disdiggs = (LinearLayout) findViewById(R.id.ll_disdiggs);
        ll_diggs = (LinearLayout) findViewById(R.id.ll_diggs);
        ll_pay = (LinearLayout) findViewById(R.id.ll_pay);
        ll_fav = (LinearLayout) findViewById(R.id.ll_fav);
        ll_comment = (LinearLayout) findViewById(R.id.ll_comment);
        ll_buy = (LinearLayout) findViewById(R.id.ll_buy);
        tv_disdiggs = (TextView) findViewById(R.id.tv_disdiggs);
        tv_diggs = (TextView) findViewById(R.id.tv_diggs);
        tv_fav = (TextView) findViewById(R.id.tv_fav);
        tv_update = (TextView) findViewById(R.id.tv_submit);
        tv_delete = (TextView) findViewById(R.id.tv_delete);

        banner = (ItemImageBanner) findViewById(R.id.banner);

        item = (Item) getIntent().getSerializableExtra("item");
        String title = item.getTitle().trim();
        String price = item.getPrice().trim();
        String pic_a = item.getPic_a();
        String url = item.getUrl().trim();
        initTopBarForBoth(title, "返回", "分享", () ->
                new ShareDialog(mContext).builder(title, price, pic_a, url).show()
        );
        itemProxy = new ItemProxy();
        //打开Item详情页，人气+1
        itemProxy.atomicAdd(item.getObjectId());
        mall_name = item.getMall_name();
        author = item.get_User();
        tv_belong_nickname.setText(author.getNickname().trim());
        String avatar_url = author.getAvatar();
        if (avatar_url != null && !avatar_url.equals("")) {
            Uri uri = Uri.parse(avatar_url);
            iv_belong_avatar.setImageURI(uri);
            iv_belong_avatar.setOnClickListener(v -> {
                Intent intent = new Intent(mContext,
                        ImageShowerActivity.class);
                intent.putExtra("img_url", avatar_url);
                startActivity(intent);
            });
        } else {
            iv_belong_avatar.setImageResource(R.mipmap.avatar_default);
        }
        tv_title.setText(title);
        tv_money.setText(price);
        tv_time.setText(item.getCreatedAt().trim());
        tv_type.setText(item.getType().trim());

        String details = item.getDetails();
        if (!TextUtils.isEmpty(details)) {
            tv_details.setText(details.trim());
        }

        tv_hot_value.setText(String.format(mContext.getString(R.string.number_to_string), item.getHot()));
        Integer love = item.getLove();
        tv_diggs_value.setText(String.format(mContext.getString(R.string.number_to_string), item.getLove()));
        if (love < 0) {
            iv_diggs_.setImageResource(R.mipmap.ic_action_disdiggs_normal);
        } else {
            iv_diggs_.setImageResource(R.mipmap.ic_action_diggs_normal);
        }
        tv_comment_value.setText(String.format(mContext.getString(R.string.number_to_string), item.getCommentCount()));

        adList = new ArrayList<>();
        adList.add(pic_a);
        String pic_b = item.getPic_b();
        if (!TextUtils.isEmpty(pic_b)) {
            adList.add(pic_b);
        }
        String pic_c = item.getPic_c();
        if (!TextUtils.isEmpty(pic_c)) {
            adList.add(pic_c);
        }
        addPicData();
    }

    @Override
    public void onResume() {
        super.onResume();
        banner.goOnScroll();
        user = getUserProxy().getCurrentUser();
        if (user != null) {//獲取當前用戶
            String userObjectId = user.getObjectId();
            itemProxy.setQueryItemUserListener(this);
            itemProxy.QueryItemUser(item.getObjectId(), userObjectId);
            if (author.getObjectId().equals(userObjectId)) {
                tv_update.setVisibility(View.VISIBLE);
                tv_delete.setVisibility(View.VISIBLE);
                //编辑更新
                tv_update.setOnClickListener(v -> {

                });
                //删除
                tv_delete.setOnClickListener(v -> {
                    new AlertDialog(mContext).builder().setTitle("提示").setMsg("确定删除该条信息？")
                            .setPositiveButton("刪除", view -> {
                                svProgressHUD = new SVProgressHUD(mContext);
                                svProgressHUD.showWithStatus("删除中...");
                                itemProxy.setDeleteItemListener(this);
                                itemProxy.deleteItem(user.getSessionToken(), item.getObjectId());
                            }).setNegativeButton("取消", view -> {
                            }
                    ).show();
                });
            }
        }
        //感谢
        ll_diggs.setOnClickListener(v -> {
            if (user == null) {
                getOperation().startActivity(LoginActivity.class);
            } else {
                if (isDiggs) {
                    addItemUser(-DO_LOVE);
                } else {
                    addItemUser(DO_LOVE);
                }
            }
        });
        //没有帮助
        ll_disdiggs.setOnClickListener(v -> {
            if (user == null) {
                getOperation().startActivity(LoginActivity.class);
            } else {
                if (isDisdiggs) {
                    addItemUser(-DO_DISLOVE);
                } else {
                    addItemUser(DO_DISLOVE);
                }
            }
        });
        //收藏
        ll_fav.setOnClickListener(v -> {
            if (user == null) {
                getOperation().startActivity(LoginActivity.class);
            } else {
                if (isFav) {
                    addItemUser(-DO_FAV);
                } else {
                    addItemUser(DO_FAV);
                }
            }
        });
        //评论
        ll_comment.setOnClickListener(v -> {
            Intent intent = new Intent(mContext,
                    CommentActivity.class);
            intent.putExtra("itemObjectId", item.getObjectId());
            startActivity(intent);
        });
        //打赏
        ll_pay.setOnClickListener(v -> {
            if (user == null) {
                getOperation().startActivity(LoginActivity.class);
            } else {
                getOperation().startActivity(RewardActivity.class);
                //
            }
        });
        //直达链接--购买
        ll_buy.setOnClickListener(v -> {
            if (user == null) {
                getOperation().startActivity(LoginActivity.class);
            } else {
//                if ()
                KeplerApiManager.getWebViewService().openWebViewPage("{\"type\": \"1\",\"sku\":\"1786149\"}");
//                showItemDetailPage("529330967656", "mm_108668197_0_0");
            }
        });
    }


    @Override
    public void onPause() {
        super.onPause();
        banner.pauseScroll();
    }

    public void showItemDetailPage(String itemId, String pid) {
        TradeService tradeService = AlibabaSDK.getService(TradeService.class);
        //ItemDetailPage(String itemId,  Map<String, String> exParams)
        //itemId  商品id.支持标准的商品id，eg.37196464781；同时支持openItemId，eg.AAHd5d-HAAeGwJedwSnHktBI；必填，不允许为null；
        //exParams 特殊业务扩展字段；选填，允许为null；目前支持3个参数：
        //  1、TradeConstants.ITEM_DETAIL_VIEW_TYPE：启动页面类型，分为TAOBAO_H5_VIEW(以淘宝H5方式打开详情页)、TAOBAO_NATIVE_VIEW(唤起手机淘宝客户端打开详情页)。
        //  2、TradeConstants.ISV_CODE(ISV_CODE用法可参看：http://baichuan.taobao.com/doc2/detail.htm?treeId=30&articleId=102596&docType=1)
        //  3、TradeConstants. TAOBAO_BACK_URL:设置启动手淘native页面后的返回页面
        Map<String, String> exParams = new HashMap<>();
        exParams.put(TradeConstants.ITEM_DETAIL_VIEW_TYPE, TradeConstants.TAOBAO_NATIVE_VIEW);
        ItemDetailPage itemDetailPage = new ItemDetailPage(itemId, exParams);
        TaokeParams taokeParams = new TaokeParams(); //若非淘客taokeParams设置为null即可
        taokeParams.pid = pid;
        tradeService.show(itemDetailPage, null, this, null, new TradeProcessCallback() {
            @Override
            public void onFailure(int code, String msg) {
                CommonUtils.showToast("失败 " + code + msg);
            }

            @Override
            public void onPaySuccess(TradeResult tradeResult) {
                CommonUtils.showToast("成功");
            }
        });
    }

    private void addItemUser(int type) {
        itemProxy.setAddItemUserListener(this);
        ItemUser itemUser = new ItemUser();
        itemUser.setItemObjectId(item.getObjectId());
        itemUser.setUserObjectId(user.getObjectId());
        switch (type) {
            case DO_LOVE:
                itemUser.setLove(true);
                break;
            case DO_DISLOVE:
                itemUser.setDislove(true);
                break;
            case DO_FAV:
                itemUser.setFav(true);
                break;
            case -DO_LOVE:
                itemUser.setLove(false);
                break;
            case -DO_DISLOVE:
                itemUser.setDislove(false);
                break;
            case -DO_FAV:
                itemUser.setFav(false);
                break;
            default:
                return;
        }
        itemProxy.addItemUser(user.getSessionToken(), itemUser);
    }

    private void addPicData() {
        banner.setSelectAnimClass(RotateEnter.class)
                .setUnselectAnimClass(NoAnimExist.class)
                .setSource(adList)
                .startScroll();
        banner.setOnItemClickL(position -> {
            Intent intent = new Intent(mContext,
                    ImageShowerActivity.class);
            intent.putExtra("img_url", adList.get(position));
            startActivity(intent);
        });
    }

    @Override
    public void onQueryItemUserSuccess(List<ItemUser> objects) {
        Boolean love = objects.get(0).getLove();
        Boolean dislove = objects.get(0).getDislove();
        Boolean fav = objects.get(0).getFav();
        updateAction(love, dislove, fav);
    }

    @Override
    public void onQueryItemUserFailure(String message) {
        //
    }

    @Override
    public void onAddItemUserSuccess(ItemUser objects) {
        updateAction(objects.getLove(), objects.getDislove(), objects.getFav());
    }

    @Override
    public void onAddItemUserFailure(String message) {
        //
    }

    @Override
    public void onDeleteItemSuccess() {
        svProgressHUD.showSuccessWithStatus("删除成功");
        finish();
    }

    @Override
    public void onDeleteItemFailure() {
        svProgressHUD.showErrorWithStatus("删除失败");
    }

    private void updateAction(Boolean love, Boolean dislove, Boolean fav) {
        if (love) {
            iv_diggs.setImageResource(R.mipmap.ic_action_diggs_press);
            tv_diggs.setText("已感谢");
            isDiggs = true;
        }
        if (dislove) {
            iv_disdiggs.setImageResource(R.mipmap.ic_action_disdiggs_press);
            tv_disdiggs.setText("撤销操作");
            isDisdiggs = true;
        }
        if (fav) {
            iv_fav.setImageResource(R.mipmap.ic_action_fav_press);
            tv_fav.setText("已收藏");
            isFav = true;
        }
    }


}
