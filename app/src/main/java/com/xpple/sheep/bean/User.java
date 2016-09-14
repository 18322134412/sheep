package com.xpple.sheep.bean;


public class User extends BaseObject {
    // 账户 唯一键 11位手机号
    private String username;
    // 密码 永远不返回 password字段加密可行方案，客户端提交 md5(password) 密码。
    // 服务端数据库通过 md5(salt+md5(password)) 的规则存储密码，该 salt 仅存储在服务端，且在每次存储密码时都随机生成。
    // 密码被 md5() 提交到服务端之后，可通过 md5(salt + form['password']) 与数据库密码比对。
    // 另防止 replay 攻击（请求被重新发出一次即可能通过验证）的问题，由服务端颁发并验证一个带有时间戳的可信 token （或一次性的）。
    private String password;
    // 交易密码 六位纯数字
    private String transaction_password;
    // Token
    private String sessionToken;
    // 昵称 默认值 口袋爆料人
    private String nickname;
    // 积分 默认值 0 即等级 ！！！必须设默认值0  0=0 1>=100 2>=200 3>=500 4>=1000 5>=2000 6>=5000 7>=15000 8>=50000 9>=100000 10>=200000
    private Integer credit;
    // 收益余额 默认值 0
    private Integer overage;
    // 头像 默认值 null
    private String avatar;
    // 签到 默认值 true
    private Boolean sign_in;
    // 次数 默认值 3
    private Integer shake_times;
    // 第三方授权信息
    private AuthData authData;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getTransaction_password() {
        return transaction_password;
    }

    public void setTransaction_password(String transaction_password) {
        this.transaction_password = transaction_password;
    }

    public String getSessionToken() {
        return sessionToken;
    }

    public void setSessionToken(String sessionToken) {
        this.sessionToken = sessionToken;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public Integer getCredit() {
        return credit;
    }

    public void setCredit(Integer credit) {
        this.credit = credit;
    }

    public Integer getOverage() {
        return overage;
    }

    public void setOverage(Integer overage) {
        this.overage = overage;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public Integer getShake_times() {
        return shake_times;
    }

    public void setShake_times(Integer shake_times) {
        this.shake_times = shake_times;
    }

    public Boolean getSign_in() {
        return sign_in;
    }

    public void setSign_in(Boolean sign_in) {
        this.sign_in = sign_in;
    }

    public AuthData getAuthData() {
        return authData;
    }

    public void setAuthData(AuthData authData) {
        this.authData = authData;
    }

}
