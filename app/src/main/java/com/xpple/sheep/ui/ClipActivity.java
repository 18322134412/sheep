package com.xpple.sheep.ui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Environment;
import android.text.TextUtils;
import android.view.View;

import com.xpple.sheep.R;
import com.xpple.sheep.base.BaseActivity;
import com.xpple.sheep.util.CommonUtils;
import com.xpple.sheep.util.PhotoPickerUtils;
import com.xpple.sheep.view.clip.ClipImageLayout;

import java.io.File;

public class ClipActivity extends BaseActivity {

    @Override
    public int bindLayout() {
        return R.layout.activity_clip;
    }

    @Override
    public void initView(View view) {
        setTintManager();
        initTopBarForLeft("移动与缩放", "返回");
        String path = getIntent().getStringExtra("path");
        if (TextUtils.isEmpty(path) || !(new File(path).exists())) {
            CommonUtils.showToast("图片加载失败");
            return;
        }

        Bitmap bitmap = PhotoPickerUtils.convertToBitmap(path, 600, 600);

        if (bitmap == null) {
            CommonUtils.showToast("图片加载失败");
            return;
        }
        ClipImageLayout mClipImageLayout = (ClipImageLayout) findViewById(R.id.id_clipImageLayout);
        mClipImageLayout.setBitmap(bitmap);
        findViewById(R.id.id_action_clip).setOnClickListener(arg0 -> new Thread(() -> {
            Bitmap bitmap1 = mClipImageLayout.clip();
            String path1 = Environment.getExternalStorageDirectory() + "/Sheep/cache/" + System.currentTimeMillis() + ".png";
            PhotoPickerUtils.savePhotoToSDCard(bitmap1, path1);
            Intent intent = new Intent();
            intent.putExtra("path", path1);
            setResult(RESULT_OK, intent);
            finish();
        }).start());
    }


}
