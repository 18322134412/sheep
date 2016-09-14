package com.xpple.sheep.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Environment;
import android.text.TextUtils;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.sdk.android.AlibabaSDK;
import com.alibaba.sdk.android.media.MediaService;
import com.alibaba.sdk.android.media.upload.UploadListener;
import com.alibaba.sdk.android.media.upload.UploadOptions;
import com.alibaba.sdk.android.media.upload.UploadTask;
import com.alibaba.sdk.android.media.utils.FailReason;
import com.bigkoo.svprogresshud.SVProgressHUD;
import com.facebook.drawee.view.SimpleDraweeView;
import com.orhanobut.logger.Logger;
import com.xpple.sheep.R;
import com.xpple.sheep.StaticConfig;
import com.xpple.sheep.base.BaseOnlineActivity;
import com.xpple.sheep.bean.User;
import com.xpple.sheep.proxy.UserProxy;
import com.xpple.sheep.util.CommonUtils;
import com.xpple.sheep.util.PhotoPickerUtils;
import com.xpple.sheep.view.dialog.ActionSheetDialog;

import java.io.File;

/**
 * 我的账户页
 *
 * @author nEdAy
 */
public class AccountActivity extends BaseOnlineActivity implements UserProxy.IQueryListener, UserProxy.IUpdateListener {
    public static final int PHOTO_ZOOM = 0; // 相册
    public static final int PHOTO_TAKE = 1; // 拍照
    public static final int IMAGE_COMPLETE = 2; // 结果
    private SimpleDraweeView riv_avatar;
    private TextView tv_nickname, tv_account_real;
    private UserProxy userProxy;
    private SVProgressHUD svProgressHUD;
    private String photoSavePath;//保存路径
    private String photoSaveName;//图片名
    private MyBroadcastReceiver receiver = new MyBroadcastReceiver();

    @Override
    public int bindLayout() {
        return R.layout.activity_account_center;
    }

    @Override
    public void initView(View view) {
        setTintManager();
        initTopBarForLeft("我的账户", "返回");
        riv_avatar = (SimpleDraweeView) findViewById(R.id.riv_avatar);
        tv_nickname = (TextView) findViewById(R.id.tv_nickname);

        tv_account_real = (TextView) findViewById(R.id.tv_account_real);
        RelativeLayout rl_key_login = (RelativeLayout) findViewById(R.id.rl_key_login);
        RelativeLayout rl_key_trading = (RelativeLayout) findViewById(R.id.rl_key_trading);
        RelativeLayout rl_change_skin = (RelativeLayout) findViewById(R.id.rl_change_skin);

        userProxy = getUserProxy();
        User user = userProxy.getCurrentUser();
        if (user != null) {
            userProxy.setOnQueryListener(this);
            userProxy.query(user.getObjectId());
            svProgressHUD = new SVProgressHUD(this);
            svProgressHUD.showWithStatus("获取中");
        } else {
            getOperation().startActivity(new Intent(mContext, LoginActivity.class));
            finish();
        }
        rl_key_login.setOnClickListener(v -> getOperation().startActivity(UpdateNewPasswordActivity.class));
        rl_key_trading.setOnClickListener(v -> getOperation().startActivity(UpdateNewTransactionPasswordActivity.class));
        rl_change_skin.setOnClickListener(v -> getOperation().startActivity(ChangeSkinActivity.class));
        // 注册退出广播
        IntentFilter filter = new IntentFilter();
        filter.addAction(StaticConfig.ACTION_UPDATE_NICKNAME_SUCCESS_FINISH);
        registerReceiver(receiver, filter);
    }

    @Override
    public void onQuerySuccess(User user) {
        refreshUser(user);
        svProgressHUD.showSuccessWithStatus("获取成功");
    }

    @Override
    public void onQueryFailure() {
        svProgressHUD.showErrorWithStatus("获取失败");
    }

    private void refreshUser(User user) {
        // 更改
        refreshAvatar(user.getAvatar());
        riv_avatar.setOnClickListener(view -> showActionSheet());
        String nickname = user.getNickname();
        if (TextUtils.isEmpty(nickname) || nickname.equals("口袋爆料人")) {
            tv_nickname.setText("口袋爆料人");
            tv_nickname.setOnClickListener(view -> getOperation().startActivity(UpdateInfoActivity.class));
        } else {
            tv_nickname.setText(nickname);
        }

        String account = user.getUsername();
        tv_account_real.setText(account);
    }

    private void showActionSheet() {
        new ActionSheetDialog(mContext).builder()
                .setCancelable(true)
                .setCanceledOnTouchOutside(true)
                .addSheetItem("拍照", ActionSheetDialog.SheetItemColor.Blue,
                        which -> {
                            //拍照
                            if (CommonUtils.checkSdCard()) {
                                File file = new File(Environment.getExternalStorageDirectory(), "Sheep/cache");
                                if (!file.exists())
                                    file.mkdirs();
                                photoSavePath = Environment.getExternalStorageDirectory() + "/Sheep/cache/";
                                photoSaveName = String.valueOf(System.currentTimeMillis()) + ".png";
                                Uri imageUri = Uri.fromFile(new File(photoSavePath, photoSaveName));
                                PhotoPickerUtils.launchCamera(AccountActivity.this, PHOTO_TAKE, imageUri);
                            } else {
                                CommonUtils.showToast("SD卡不存在");
                            }
                        })
                .addSheetItem("从相册选择", ActionSheetDialog.SheetItemColor.Blue,
                        which -> {
                            //从相册选择
                            PhotoPickerUtils.launchGallery(AccountActivity.this, PHOTO_ZOOM);
                        }).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) {
            return;
        }
        Intent intent = new Intent(AccountActivity.this, ClipActivity.class);
        switch (requestCode) {
            case PHOTO_ZOOM://相册
                if (data == null) {
                    return;
                }
                String path = PhotoPickerUtils.getPhotoPathByLocalUri(mContext, data);
                intent.putExtra("path", path);
                startActivityForResult(intent, IMAGE_COMPLETE);
                break;
            case PHOTO_TAKE://拍照
                path = photoSavePath + photoSaveName;
                intent.putExtra("path", path);
                startActivityForResult(intent, IMAGE_COMPLETE);
                break;
            case IMAGE_COMPLETE:
                svProgressHUD = new SVProgressHUD(this);
                svProgressHUD.showWithStatus("上传&更新中...");
                // 上传头像
                uploadAvatar(data.getStringExtra("path"));
                break;
            default:
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    private void uploadAvatar(String path) {
        MediaService mediaService = AlibabaSDK.getService(MediaService.class);
        //带上传选项
        UploadOptions options = new UploadOptions.Builder().dir("Avatar").build();
        UploadListener listener = new UploadListener() {
            @Override
            public void onUploading(UploadTask uploadTask) {
                Logger.d("---上传中---已上传大小：" + uploadTask.getCurrent()
                        + " 总文件大小：" + uploadTask.getTotal());
            }

            @Override
            public void onUploadFailed(UploadTask uploadTask, FailReason failReason) {
                Logger.e("---上传失败---" + failReason.getMessage());
                svProgressHUD.showErrorWithStatus("更新失败");
            }

            @Override
            public void onUploadComplete(UploadTask uploadTask) {
                Logger.d("---上传成功---URL:" + uploadTask.getResult().uri);
                // 更新BmobUser对象
                updateUserAvatar(uploadTask.getResult().url);
            }

            @Override
            public void onUploadCancelled(UploadTask uploadTask) {
                Logger.d("---上传取消---");
            }
        };
        //上传
        mediaService.upload(new File(path), "sheep", options, listener);
    }


    private void updateUserAvatar(String url) {
        userProxy.setOnUpdateListener(this);
        User user = new User();
        user.setAvatar(url);
        userProxy.update(user);
    }


    @Override
    public void onUpdateSuccess(User user) {
        svProgressHUD.showSuccessWithStatus("更新成功");
        refreshAvatar(user.getAvatar());
    }

    @Override
    public void onUpdateFailure(String msg) {
        svProgressHUD.showErrorWithStatus("更新失败");
        Logger.e(msg);
    }

    /**
     * 更新头像 refreshAvatar
     */
    private void refreshAvatar(String avatar) {
        if (avatar != null && !avatar.equals("")) {
            Uri uri = Uri.parse(avatar + "@!avatar");
            riv_avatar.setImageURI(uri);
        } else {
            riv_avatar.setImageResource(R.mipmap.avatar_default);
        }
    }


    public class MyBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent != null
                    && StaticConfig.ACTION_UPDATE_NICKNAME_SUCCESS_FINISH
                    .equals(intent.getAction())) {
                tv_nickname.setText(intent.getStringExtra("nickname"));
                tv_nickname.setEnabled(false);
            }
        }
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        unregisterReceiver(receiver);
    }
}
