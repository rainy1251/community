package com.service.community.model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Administrator on 2017/5/11.
 */

public class AdListBean implements Serializable {
    public DatasBean data;

    public class DatasBean implements Serializable {
        public ItemBean items;

        public class ItemBean implements Serializable {
            public int currPage;
            public int pageSize;
            public int totalCount;
            public int totalPage;
            public ArrayList<ListBean> list;

            public class ListBean implements Serializable {
                public String addTime;
                public String content;
                public boolean deleted;
                public boolean enabled;
                public int id;
                public int orderNum;
                public int position;
                public int type;
                public int version;
                public String link;
                public String name;
                public String url;

                public int communityId;
                public int isenable;
                public String communityName;
                public String serverName;
                public String linkName;
                public String linkTel;

                public String title;
                public String sendTime;
                public String attachName;
                public String attachUrl;
                public String message;
                public String createTime;
                public int sendAll;
                public int status;
                public int sendType;
                public int sendResultTotal;
                public int sendResultSuccess;
                public int sendResultFail;
                public int createUser;
                public int noticeType;
//                public ArrayList<UrlBean> attachUrl;
//                public class UrlBean implements Serializable {
//
//                }
            }

        }
    }

    public String errmsg;
    public int errno;

}
