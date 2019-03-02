package com.service.community.model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Administrator on 2017/5/11.
 */

public class StateBean implements Serializable {
    public DatasBean data;

    public class DatasBean implements Serializable {
        public String token;
        public String tokenExpire;
        public String imToken;
        public UserBean userInfo;

        public class UserBean implements Serializable {
            public String avatarUrl;
            public String userId;
            public int type;

        }
    }

    public String errmsg;
    public int errno;

}
