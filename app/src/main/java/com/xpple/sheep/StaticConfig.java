package com.xpple.sheep;

import android.content.Context;

import com.facebook.cache.disk.DiskCacheConfig;
import com.facebook.common.internal.Sets;
import com.facebook.common.util.ByteConstants;
import com.facebook.imagepipeline.backends.okhttp.OkHttpImagePipelineConfigFactory;
import com.facebook.imagepipeline.cache.MemoryCacheParams;
import com.facebook.imagepipeline.core.ImagePipelineConfig;
import com.facebook.imagepipeline.listener.RequestListener;
import com.facebook.imagepipeline.listener.RequestLoggingListener;
import com.squareup.okhttp.OkHttpClient;

/**
 * 系统变量
 *
 * @author nEdAy
 */
public final class StaticConfig {
    private static ImagePipelineConfig sOkHttpImagePipelineConfig;
    public static final int MAX_DISK_CACHE_SIZE = 40 * ByteConstants.MB;
    private static final String IMAGE_PIPELINE_CACHE_DIR = "image_cache";
    private static final int MAX_HEAP_SIZE = (int) Runtime.getRuntime().maxMemory();
    public static final int MAX_MEMORY_CACHE_SIZE = MAX_HEAP_SIZE / 4;

    public static final String ACTION_REGISTER_SUCCESS_FINISH = "register.success.finish";// 注册成功之后登陆页面退出

    public static final String ACTION_UPDATE_NICKNAME_SUCCESS_FINISH = "updateNickname.success.finish";// 更新昵称之后账户页面刷新
    // 这是Bmob的Application ID,用于初始化操作
    public static final String BMOB_KEY = "417ad80af2de7cc79d976ed980d308a0";
    public static final String BMOB_REST_API_Key = "ce5bf94363345371af655621a8d57c41";
    //这是mob的App Key,用于初始化操作
    public static final String MOB_APP_KEY = "ed3fea7a8a28";
    //这是mob的App Secret,用于初始化操作
    public static final String MOB_APP_SECRET = "bc40908421902a36afbb9fcb696f7417";
    //阿里百川的App Key
    public static final String ALIBABA_APP_KEY = "23346374";
    //淘客PID
    public static final String DEFAULT_TAOKE_PID = "mm_108668197_0_0";
    //JD appKey 还有keySecret
    public static final String JD_APP_KEY = "51ddbe320143441d832d6d824b2b9fb6";
    public static final String JD_KEY_SECRET = "054a6706ddae44ff8ef7e6fac1cd9ee4";

    /**
     * Creates config using OkHttp as network backed.
     */
    public static ImagePipelineConfig getOkHttpImagePipelineConfig(Context context) {
        if (sOkHttpImagePipelineConfig == null) {
            OkHttpClient okHttpClient = new OkHttpClient();
            ImagePipelineConfig.Builder configBuilder =
                    OkHttpImagePipelineConfigFactory.newBuilder(context, okHttpClient);
            configureCaches(configBuilder, context);
            configureLoggingListeners(configBuilder);
            sOkHttpImagePipelineConfig = configBuilder.build();
        }
        return sOkHttpImagePipelineConfig;
    }

    /**
     * Configures disk and memory cache not to exceed common limits
     */
    private static void configureCaches(
            ImagePipelineConfig.Builder configBuilder,
            Context context) {
        final MemoryCacheParams bitmapCacheParams = new MemoryCacheParams(
                MAX_MEMORY_CACHE_SIZE, // Max total size of elements in the cache
                Integer.MAX_VALUE,                     // Max entries in the cache
                MAX_MEMORY_CACHE_SIZE, // Max total size of elements in eviction queue
                Integer.MAX_VALUE,                     // Max length of eviction queue
                Integer.MAX_VALUE);                    // Max cache entry size
        configBuilder
                .setBitmapMemoryCacheParamsSupplier(
                        () -> bitmapCacheParams)
                .setMainDiskCacheConfig(
                        DiskCacheConfig.newBuilder(context)
                                .setBaseDirectoryPath(context.getApplicationContext().getCacheDir())
                                .setBaseDirectoryName(IMAGE_PIPELINE_CACHE_DIR)
                                .setMaxCacheSize(MAX_DISK_CACHE_SIZE)
                                .build());
    }

    private static void configureLoggingListeners(ImagePipelineConfig.Builder configBuilder) {
        configBuilder.setRequestListeners(
                Sets.newHashSet((RequestListener) new RequestLoggingListener()));
    }

}
