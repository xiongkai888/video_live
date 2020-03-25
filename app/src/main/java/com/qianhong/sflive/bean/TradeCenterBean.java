package com.qianhong.sflive.bean;

import java.io.Serializable;
import java.util.List;

public class TradeCenterBean {

    public DayData dayData;
    public List<TradeInfo> list;

    public static class DayData {
        public String id;
        public String highprice;
        public String lowprice;
        public String tradenum;
        public String addtime;
        public String price;
        public String totalNum;
        public String rise;
        public String candy;
    }

    public static class TradeInfo implements Serializable {
        public String id;
        public String uid;
        public String gid;
        public String pid;
        public int type;
        public String price;
        public String totalprice;
        public String number;
        public String status;
        public String pay_status;
        public String pay_number;
        public String addtime;
        public String updatetime;
        public String ordernum;
        public String dealnum;
        public String user_nicename;
        public String avatar_thumb;

        public String img_url;
        public String uid_alipay_num;
        public String uid_name;
        public String gid_alipay_num;
        public String gid_name;

    }

}
