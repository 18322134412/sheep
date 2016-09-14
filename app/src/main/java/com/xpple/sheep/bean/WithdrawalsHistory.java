package com.xpple.sheep.bean;


import java.util.List;

public class WithdrawalsHistory extends BaseObject {
    // 自己 1-1
    private User selfUser;
    // 手机号orQ币
    private String type;
    // 提现号码（手机号orQ币）
    private String number;
    // 提现前數值
    private Integer before;
    // 提现後數值
    private Integer after;
    // 提现數值
    private Integer change;
    // NOTPAY（未完成）或 SUCCESS（提现成功）
    private String state;

    private List<WithdrawalsHistory> results;

    public List<WithdrawalsHistory> getElements() {
        return results;
    }
}
