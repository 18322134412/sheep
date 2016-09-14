package com.xpple.sheep.ui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.view.MotionEvent;
import android.view.View;

import com.facebook.common.executors.CallerThreadExecutor;
import com.facebook.common.references.CloseableReference;
import com.facebook.datasource.DataSource;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.core.ImagePipeline;
import com.facebook.imagepipeline.datasource.BaseBitmapDataSubscriber;
import com.facebook.imagepipeline.image.CloseableImage;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.xpple.sheep.R;
import com.xpple.sheep.base.BaseActivity;
import com.xpple.sheep.util.CommonUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class ImageShowerActivity extends BaseActivity {
    private Uri uri;

    @Override
    public int bindLayout() {
        return R.layout.activity_image_shower;
    }

    @Override
    public void initView(View view) {
        initTopBarForBoth("显示大图", "返回", "保存", () ->
                saveImageToGallery(uri)
        );
        SimpleDraweeView iv_img_shower = (SimpleDraweeView) findViewById(R.id.iv_img_shower);
        String img_url = getIntent().getStringExtra("img_url");
        if (img_url != null && !img_url.equals("")) {
            uri = Uri.parse(img_url);
            iv_img_shower.setImageURI(uri);
        } else {
            iv_img_shower.setImageResource(R.mipmap.avatar_default);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // TODO Auto-generated method stub
        finish();
        return true;
    }

    private void saveImageToGallery(Uri url) {
        ImageRequest imageRequest = ImageRequestBuilder
                .newBuilderWithSource(url)
                .setProgressiveRenderingEnabled(true)
                .build();
        ImagePipeline imagePipeline = Fresco.getImagePipeline();
        DataSource<CloseableReference<CloseableImage>>
                dataSource = imagePipeline.fetchDecodedImage(imageRequest, mContext);
        dataSource.subscribe(new BaseBitmapDataSubscriber() {
                                 @Override
                                 public void onNewResultImpl(Bitmap bitmap) {
                                     // You can use the bitmap in only limited ways
                                     // No need to do any cleanup.
                                     if (bitmap == null) {
                                         CommonUtils.showToast("保存图片失败，图片并不存在");
                                     }
                                     File appDir = new File(Environment.getExternalStorageDirectory(), "/Sheep/cache/");
                                     if (!appDir.exists()) {
                                         appDir.mkdir();
                                     }
                                     String fileName = System.currentTimeMillis() + ".png";
                                     File file = new File(appDir, fileName);
                                     try {
                                         FileOutputStream fos = new FileOutputStream(file);
                                         assert bitmap != null;
                                         bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
                                         fos.flush();
                                         fos.close();
                                     } catch (IOException e) {
                                         e.printStackTrace();
                                     }
                                     Uri uri = Uri.fromFile(file);
                                     // 通知图库更新
                                     Intent scannerIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri);
                                     mContext.sendBroadcast(scannerIntent);
                                     String msg = String.format(getString(R.string.picture_has_save_to),
                                             appDir.getAbsolutePath());
                                     CommonUtils.showToast(msg);
                                 }

                                 @Override
                                 public void onFailureImpl(DataSource dataSource) {
                                     CommonUtils.showToast("保存图片失败，再试试");
                                 }
                             },
                CallerThreadExecutor.getInstance());
    }
}
