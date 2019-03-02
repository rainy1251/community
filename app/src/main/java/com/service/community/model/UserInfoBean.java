package com.service.community.model;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/5/11.
 */

public class UserInfoBean implements Serializable {
    public DatasBean data;

    public class DatasBean implements Serializable {
        public String address;
        public String communityappMobile;
        public String headUrl;
        public String mobile;
        public String mobileOne;
        public String mobileThree;
        public String mobileTwo;
        public String userName;
        public String nickname;
        public String gender;
        public String url;
    }

    public String errmsg;
    public int errno;

}
