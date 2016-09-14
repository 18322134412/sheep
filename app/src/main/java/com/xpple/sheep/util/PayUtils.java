package com.xpple.sheep.util;


import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;

import com.bigkoo.svprogresshud.SVProgressHUD;
import com.orhanobut.logger.Logger;
import com.xpple.sheep.view.dialog.AlertDialog;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Objects;

import c.b.BP;
import c.b.PListener;
import c.b.QListener;

public class PayUtils {
    private static SVProgressHUD svProgressHUD;
    private static String thisOrderId;
    private static String thisStatus;

    // 调用支付宝支付
    public static void payByAli(double price, Context mContext) {
        svProgressHUD = new SVProgressHUD(mContext);
        svProgressHUD.showWithStatus("正在获取订单...");
        BP.pay((Activity) mContext, "充值", "余额充值", price, true, new PListener() {

            // 因为网络等原因,支付结果未知(小概率事件),出于保险起见稍后手动查询
            @Override
            public void unknow() {
                svProgressHUD.showInfoWithStatus("支付结果未知,请稍后手动查询");
//                tv_log.append("充值" + "'s pay status is unknow\n\n");
            }

            // 支付成功,如果金额较大请手动查询确认
            @Override
            public void succeed() {
                if (querySuccess(thisOrderId, mContext)) {
                    svProgressHUD.showSuccessWithStatus("订单支付成功!");
                } else {
                    svProgressHUD.showErrorWithStatus("订单支付异常!");
                }
                svProgressHUD.showSuccessWithStatus("支付成功!");
//                tv_log.append("充值" + "'s pay status is success\n\n");
            }

            // 无论成功与否,返回订单号
            @Override
            public void orderId(String orderId) {
                // 此处应该保存订单号,比如保存进数据库等,以便以后查询
                thisOrderId = orderId;
                svProgressHUD.showWithStatus("获取订单成功!请等待跳转到支付页面");
//                tv_log.append("充值" + "'s orderid is " + orderId + "\n\n");
            }

            // 支付失败,原因可能是用户中断支付操作,也可能是网络原因
            @Override
            public void fail(int code, String reason) {
                svProgressHUD.showErrorWithStatus("支付中断!");
                Logger.e("充值" + "'s pay status is fail, error code is " + code
                        + " ,reason is " + reason + "\n\n");
            }
        });
    }

    // 调用微信支付
    public static void payByWeiXin(double price, Context mContext) {

        svProgressHUD = new SVProgressHUD(mContext);
        svProgressHUD.showWithStatus("正在获取订单...");
        BP.pay((Activity) mContext, "充值", "余额充值", price, false, new PListener() {

            // 因为网络等原因,支付结果未知(小概率事件),出于保险起见稍后手动查询
            @Override
            public void unknow() {
                svProgressHUD.showInfoWithStatus("支付结果未知,请稍后手动查询");
                Logger.e("充值" + "'s pay status is unknow\n\n");
            }

            // 支付成功,如果金额较大请手动查询确认
            @Override
            public void succeed() {
                if (querySuccess(thisOrderId, mContext)) {
                    svProgressHUD.showSuccessWithStatus("订单支付成功!");
                } else {
                    svProgressHUD.showErrorWithStatus("订单支付异常!");
                }
                svProgressHUD.showSuccessWithStatus("支付成功!");
                Logger.e("充值" + "'s pay status is success\n\n");
            }

            // 无论成功与否,返回订单号
            @Override
            public void orderId(String orderId) {
                // 此处应该保存订单号,比如保存进数据库等,以便以后查询
                thisOrderId = orderId;
                svProgressHUD.showWithStatus("获取订单成功!请等待跳转到支付页面");
                Logger.e("充值" + "'s orderid is " + orderId + "\n\n");
            }

            // 支付失败,原因可能是用户中断支付操作,也可能是网络原因
            @Override
            public void fail(int code, String reason) {
                // 当code为-2,意味着用户中断了操作
                // code为-3意味着没有安装BmobPlugin插件
                if (code == -3) {
                    new AlertDialog(mContext).builder().setTitle("是否加载")
                            .setMsg("监测到你尚未加载支付插件,请选择安装插件(已打包在本地,无流量消耗)")
                            .setPositiveButton("加载插件", v ->
                                    installBmobPayPlugin("bp_wx.db", mContext)
                            ).setNegativeButton("支付宝支付", v ->
                            payByAli(price, mContext)
                    ).show();

                } else {
                    svProgressHUD.showErrorWithStatus("支付中断!");
                }
                Logger.e("充值" + "'s pay status is fail, error code is " + code
                        + " ,reason is " + reason + "\n\n");
            }
        });
    }

    // 执行订单查询
    @TargetApi(Build.VERSION_CODES.KITKAT)
    private static boolean querySuccess(String orderId, Context mContext) {
        svProgressHUD.showWithStatus("正在获取订单...");
        BP.query((Activity) mContext, orderId, new QListener() {

            @Override
            public void succeed(String status) {
                thisStatus = status;
                svProgressHUD.showSuccessWithStatus("查询成功!该订单状态为 : " + status);
                Logger.e("pay status of" + orderId + " is " + status + "\n\n");
            }

            @Override
            public void fail(int code, String reason) {
                svProgressHUD.showErrorWithStatus("查询失败");
                Logger.e("query order fail, error code is " + code
                        + " ,reason is " + reason + "\n\n");
                Logger.e("query order fail, error code is " + code
                        + " ,reason is " + reason + "\n\n");
            }
        });
        return Objects.equals(thisStatus, "success");
    }

    private static void installBmobPayPlugin(String fileName, Context mContext) {
        try {
            InputStream is = mContext.getAssets().open(fileName);
            File file = new File(Environment.getExternalStorageDirectory()
                    + File.separator + fileName + ".apk");
            if (file.exists())
                file.delete();
            file.createNewFile();
            FileOutputStream fos = new FileOutputStream(file);
            byte[] temp = new byte[1024];
            int i = 0;
            while ((i = is.read(temp)) > 0) {
                fos.write(temp, 0, i);
            }
            fos.close();
            is.close();

            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setDataAndType(Uri.parse("file://" + file),
                    "application/vnd.android.package-archive");
            mContext.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
