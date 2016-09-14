package com.xpple.sheep.bean;


import java.util.List;

public class RechargeHistory extends BaseObject {
    //自己 1-1
    private User selfUser;
    //订单或商品名称
    private String name;
    // 商品详情
    private String body;
    //调起支付的时间
    private String create_time;
    //Bmob系统的订单号
    private String out_trade_no;
    //微信或支付宝的系统订单号
    private String transaction_id;
    //WECHATPAY（微信支付）或ALIPAY（支付宝支付）
    private String pay_type;
    //订单总金额
    private String total_fee;
    //NOTPAY（未支付）或 SUCCESS（支付成功）
    private String trade_state;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public String getOut_trade_no() {
        return out_trade_no;
    }

    public void setOut_trade_no(String out_trade_no) {
        this.out_trade_no = out_trade_no;
    }

    public String getTransaction_id() {
        return transaction_id;
    }

    public void setTransaction_id(String transaction_id) {
        this.transaction_id = transaction_id;
    }

    public String getPay_type() {
        return pay_type;
    }

    public void setPay_type(String pay_type) {
        this.pay_type = pay_type;
    }

    public String getTotal_fee() {
        return total_fee;
    }

    public void setTotal_fee(String total_fee) {
        this.total_fee = total_fee;
    }

    public String getTrade_state() {
        return trade_state;
    }

    public void setTrade_state(String trade_state) {
        this.trade_state = trade_state;
    }

    private List<RechargeHistory> results;

    public List<RechargeHistory> getElements() {
        return results;
    }
}
