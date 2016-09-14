package com.xpple.sheep.ui.mainFragment;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.sdk.android.feedback.impl.FeedbackAPI;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.core.ImagePipeline;
import com.lenovo.lps.sus.SUS;
import com.xpple.sheep.R;
import com.xpple.sheep.base.BaseFragment;
import com.xpple.sheep.bean.User;
import com.xpple.sheep.proxy.UserProxy;
import com.xpple.sheep.ui.AboutActivity;
import com.xpple.sheep.ui.ActivityActivity;
import com.xpple.sheep.ui.NoticeActivity;
import com.xpple.sheep.ui.PushSettingActivity;
import com.xpple.sheep.util.CommonUtils;
import com.xpple.sheep.util.PhotoPickerUtils;
import com.xpple.sheep.util.SysEnvUtils;
import com.xpple.sheep.view.HeaderLayout;
import com.xpple.sheep.view.ShareDialog;
import com.xpple.sheep.view.dialog.AlertDialog;

import java.io.File;
import java.util.HashMap;
import java.util.Map;


public class MoreFragment extends BaseFragment {
    private View parentView;
    private User user;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        parentView = inflater.inflate(R.layout.fragment_main_more, container, false);
        setUpViews();
        return parentView;
    }

    private void setUpViews() {
        //配合状态栏下移
        setStatusBarHeight(parentView);
        initTopBarForOnlyTitle("更多");
        //显示版本号
        showCurVersion();
        //显示缓存大小
        showCacheSize();
        RelativeLayout active_center = (RelativeLayout) parentView.findViewById(R.id.active_center);

        RelativeLayout rl_setting = (RelativeLayout) parentView.findViewById(R.id.setting_center);
        RelativeLayout help_center = (RelativeLayout) parentView.findViewById(R.id.help_center);
        RelativeLayout about = (RelativeLayout) parentView.findViewById(R.id.about);
        RelativeLayout notice_center = (RelativeLayout) parentView.findViewById(R.id.notice_center);
        RelativeLayout feedback = (RelativeLayout) parentView.findViewById(R.id.feedback);
        RelativeLayout encourage = (RelativeLayout) parentView.findViewById(R.id.encourage);
        RelativeLayout share = (RelativeLayout) parentView.findViewById(R.id.share);
        RelativeLayout update = (RelativeLayout) parentView.findViewById(R.id.update);
        RelativeLayout attention = (RelativeLayout) parentView.findViewById(R.id.attention);
        RelativeLayout clear = (RelativeLayout) parentView.findViewById(R.id.clear);

        active_center.setOnClickListener(v -> getOperation().startActivity(ActivityActivity.class));
        notice_center.setOnClickListener(v -> getOperation().startActivity(NoticeActivity.class));
        help_center.setOnClickListener(v -> {
        });
        rl_setting.setOnClickListener(view -> getOperation().startActivity(PushSettingActivity.class));
        about.setOnClickListener(v -> getOperation().startActivity(AboutActivity.class));
        update.setOnClickListener(v -> {
            if (!SUS.isVersionUpdateStarted()) {
                SUS.AsyncStartVersionUpdate_IgnoreUserSettings(getActivity());//忽略“有新版本再提醒”的用户设置，强制启动应用的版本更新过程
            }
        });
        share.setOnClickListener(v -> new ShareDialog(getActivity()).builder("標題", "主體", "http://www.neday.cn/iis-85.png", "http://www.neday.cn").show());
        feedback.setOnClickListener(v -> {
            /*
            * 可以设置UI自定义参数，如主题色等,具体为：
            * enableAudio(是否开启语音 1：开启 0：关闭)
            * bgColor(消息气泡背景色 "#ffffff")，
            * color(消息内容文字颜色)，
            * avatar(当前登录账号的头像)，
            * toAvatar(客服账号的头像)
            * themeColor(标题栏自定义颜色 "#ffffff")
            */
            UserProxy userProxy = new UserProxy(getActivity());
            user = userProxy.getCurrentUser();
            Map<String, String> map = new HashMap<>();
            map.put("avatar", (user != null) ? user.getAvatar() : null);
            FeedbackAPI.setUICustomInfo(map);
//            //可以设置反馈消息自定义参数，方便在反馈后台查看自定义数据，参数是json对象，里面所有的数据都可以由开发者自定义
//            FeedbackAPI. setAppExtInfo(JSONObject extInfo)
//            /**
//             *设置自定义联系方式
//             * @param customContact  自定义联系方式
//             * @param hideContactView 是否隐藏联系人设置界面
//             */
//            FeedbackAPI.setCustomContact(String customContact, boolean hideContactView)
            FeedbackAPI.openFeedbackActivity(getActivity());
        });
        encourage.setOnClickListener(v -> new AlertDialog(getActivity()).builder().setTitle("给我鼓励").setMsg("袋王亲，如果您觉得我们做的还不错，请给我一些鼓励吧！")
                .setPositiveButton("确定", v1 -> CommonUtils.launchAppDetail(getActivity(), "")).setNegativeButton("取消", v1 -> {
                }).show());
        attention.setOnClickListener(v -> attentionWe());
        clear.setOnClickListener(v -> clearCache());
    }

    private void attentionWe() {
        new AlertDialog(getActivity()).builder().setTitle("关注我们")
                .setMsg("微信—通讯录-添加朋友-查找公众号—搜索\"口袋快爆\"(点击确定跳转微信并可以直接粘贴公众号哦)")
                .setPositiveButton("确定", v1 -> {
                    //复制数据到剪切板
                    ClipboardManager mClipboardManager = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
                    String text = "口袋快爆";
                    ClipData myClip;
                    myClip = ClipData.newPlainText("text", text);
                    mClipboardManager.setPrimaryClip(myClip);
                    try {
                        Intent intent = getActivity().getPackageManager().getLaunchIntentForPackage("com.tencent.mm");
                        startActivity(intent);
                    } catch (Exception ignored) {
                        CommonUtils.showToast("您尚未安装微信APP");
                    }
                }).setNegativeButton("取消", v1 -> {
        }).show();
    }

    private void clearCache() {
        //    清除缓存
        ImagePipeline imagePipeline = Fresco.getImagePipeline();
        //    imagePipeline.clearMemoryCaches();
        //    imagePipeline.clearDiskCaches();
        //    combines above two lines
        imagePipeline.clearCaches();
        PhotoPickerUtils.deleteFolderFile(Environment.getExternalStorageDirectory() + "/Sheep/cache/", false);
        //刷新显示缓存大小
        showCacheSize();
    }

    private void showCurVersion() {
        TextView curVersion = (TextView) parentView.findViewById(R.id.curVersion);
        curVersion.setVisibility(View.VISIBLE);
        String app_version = String.format(getResources().getString(R.string.app_version), SysEnvUtils.getVersion());
        curVersion.setText(app_version);
    }

    private void showCacheSize() {
        File file = new File(Environment.getExternalStorageDirectory() + "/Sheep/cache/");
        TextView cacheSize = (TextView) parentView.findViewById(R.id.cacheSize);
        cacheSize.setText(PhotoPickerUtils.getFormatSize(PhotoPickerUtils.getFolderSize(file)));
    }

    /**
     * 只有title
     */
    public void initTopBarForOnlyTitle(String titleName) {
        HeaderLayout mHeaderLayout = (HeaderLayout) parentView.findViewById(R.id.top_title_bar);
        mHeaderLayout.init(HeaderLayout.HeaderStyle.DEFAULT_TITLE);
        mHeaderLayout.setDefaultTitle(titleName);
    }
}
