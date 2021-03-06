package com.xpple.sheep.view;

/**
 * 增长的数字接口
 */
public interface IRiseNumber {
    /**
     * 开始播放动画的方法
     */
    void start();

    /**
     * 设置小数
     */
    void withNumber(float number);

    /**
     * 设置整数
     */
    void withNumber(int number);

    /**
     * 设置动画播放时长
     */
    void setDuration(long duration);

    /**
     * 设置动画结束监听器
     */
    void setOnEndListener(RiseNumberTextView.EndListener callback);
}
