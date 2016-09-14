package com.xpple.sheep.bean;

//第三方授权信息
public class AuthData {
    private Weibo weibo;//新浪微博
    private Qq qq;//腾讯QQ
    private Alibaba alibaba;//阿里系
    public Weibo getWeibo() {
        return weibo;

    }

    public void setWeibo(Weibo weibo) {
        this.weibo = weibo;
    }

    public Qq getQq() {
        return qq;
    }

    public void setQq(Qq qq) {
        this.qq = qq;
    }

    public Alibaba getAlibaba() {
        return alibaba;
    }

    public void setAlibaba(Alibaba alibaba) {
        this.alibaba = alibaba;
    }

    private class Weibo {
        private String uid;//"123456789"
        private String access_token;//"2.00ed6eMCV9DWcBcb79e8108f8m1HdE"
        private Long expires_in;//1564469423540

        public String getUid() {
            return uid;
        }

        public void setUid(String uid) {
            this.uid = uid;
        }

        public String getAccess_token() {
            return access_token;
        }

        public void setAccess_token(String access_token) {
            this.access_token = access_token;
        }

        public Long getExpires_in() {
            return expires_in;
        }

        public void setExpires_in(Long expires_in) {
            this.expires_in = expires_in;
        }
    }

    private class Qq {
        private String openid;//"2345CA18A5CD6255E5BA185E7BECD222"
        private String access_token;//"12345678-SM3m2avZxh5cjJmIrAfx4ZYyamdofM7IjU"
        private Long expires_in;//1382686496

        public String getOpenid() {
            return openid;
        }

        public void setOpenid(String openid) {
            this.openid = openid;
        }

        public String getAccess_token() {
            return access_token;
        }

        public void setAccess_token(String access_token) {
            this.access_token = access_token;
        }

        public Long getExpires_in() {
            return expires_in;
        }

        public void setExpires_in(Long expires_in) {
            this.expires_in = expires_in;
        }
    }

    private class Alibaba {
        private String userId;//AAFBwAg6ACXcfdsTiIVhA3po
        private String authorizationCode;//"8HbqCs4RDZ7VpnsVf76vNHOl298308"
        private Long loginTime;//1460940821699

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getAuthorizationCode() {
            return authorizationCode;
        }

        public void setAuthorizationCode(String authorizationCode) {
            this.authorizationCode = authorizationCode;
        }

        public Long getLoginTime() {
            return loginTime;
        }

        public void setLoginTime(Long loginTime) {
            this.loginTime = loginTime;
        }
    }
}
