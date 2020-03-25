package com.qianhong.sflive.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by cxf on 2017/8/14.
 */

public class UserBean implements Parcelable {
    private String id;
    private String user_nicename;
    private String avatar;
    private String avatar_thumb;
    private int sex;
    private String signature;
    private int coin;
    private String province;
    private String city;
    private String area;
    private String birthday;
    private int follows;
    private int fans;
    private String age;
    private String praise;
    private String mobile;
    private int workVideos;//作品数量
    private int likeVideos;//喜欢别人的视频数量
    private String addtime;//拉黑的时间，黑名单用
    public String user_login;
    public String activation;
    private String qrcode;
    public String alipay_name;
    public String alipay_num;
    public String name;
    public String id_card;
    public String real_order;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUser_nicename() {
        return user_nicename;
    }

    public void setUser_nicename(String user_nicename) {
        this.user_nicename = user_nicename;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getAvatar_thumb() {
        return avatar_thumb;
    }

    public void setAvatar_thumb(String avatar_thumb) {
        this.avatar_thumb = avatar_thumb;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public int getCoin() {
        return coin;
    }

    public void setCoin(int coin) {
        this.coin = coin;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public int getFollows() {
        return follows;
    }

    public void setFollows(int follows) {
        this.follows = follows;
    }

    public int getFans() {
        return fans;
    }

    public void setFans(int fans) {
        this.fans = fans;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getPraise() {
        return praise;
    }

    public void setPraise(String praise) {
        this.praise = praise;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public int getWorkVideos() {
        return workVideos;
    }

    public void setWorkVideos(int workVideos) {
        this.workVideos = workVideos;
    }

    public int getLikeVideos() {
        return likeVideos;
    }

    public void setLikeVideos(int likeVideos) {
        this.likeVideos = likeVideos;
    }

    public String getAddtime() {
        return addtime;
    }

    public void setAddtime(String addtime) {
        this.addtime = addtime;
    }

    public String getUser_login() {
        return user_login;
    }

    public void setUser_login(String user_login) {
        this.user_login = user_login;
    }

    public String getActivation() {
        return activation;
    }

    public void setActivation(String activation) {
        this.activation = activation;
    }

    public String getQrcode() {
        return qrcode;
    }

    public void setQrcode(String qrcode) {
        this.qrcode = qrcode;
    }

    public String getAlipay_name() {
        return alipay_name;
    }

    public void setAlipay_name(String alipay_name) {
        this.alipay_name = alipay_name;
    }

    public String getAlipay_num() {
        return alipay_num;
    }

    public void setAlipay_num(String alipay_num) {
        this.alipay_num = alipay_num;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId_card() {
        return id_card;
    }

    public void setId_card(String id_card) {
        this.id_card = id_card;
    }

    public String getReal_order() {
        return real_order;
    }

    public void setReal_order(String real_order) {
        this.real_order = real_order;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.user_nicename);
        dest.writeString(this.avatar);
        dest.writeString(this.avatar_thumb);
        dest.writeInt(this.sex);
        dest.writeString(this.signature);
        dest.writeInt(this.coin);
        dest.writeString(this.province);
        dest.writeString(this.city);
        dest.writeString(this.area);
        dest.writeString(this.birthday);
        dest.writeInt(this.follows);
        dest.writeInt(this.fans);
        dest.writeString(this.age);
        dest.writeString(this.praise);
        dest.writeString(this.mobile);
        dest.writeInt(this.workVideos);
        dest.writeInt(this.likeVideos);
        dest.writeString(this.addtime);
        dest.writeString(this.user_login);
        dest.writeString(this.activation);
        dest.writeString(this.qrcode);
        dest.writeString(this.alipay_name);
        dest.writeString(this.alipay_num);
        dest.writeString(this.name);
        dest.writeString(this.id_card);
        dest.writeString(this.real_order);
    }

    public UserBean() {
    }

    protected UserBean(Parcel in) {
        this.id = in.readString();
        this.user_nicename = in.readString();
        this.avatar = in.readString();
        this.avatar_thumb = in.readString();
        this.sex = in.readInt();
        this.signature = in.readString();
        this.coin = in.readInt();
        this.province = in.readString();
        this.city = in.readString();
        this.area = in.readString();
        this.birthday = in.readString();
        this.follows = in.readInt();
        this.fans = in.readInt();
        this.age = in.readString();
        this.praise = in.readString();
        this.mobile = in.readString();
        this.workVideos = in.readInt();
        this.likeVideos = in.readInt();
        this.addtime = in.readString();
        this.user_login = in.readString();
        this.activation = in.readString();
        this.qrcode = in.readString();
        this.alipay_name = in.readString();
        this.alipay_num = in.readString();
        this.name = in.readString();
        this.id_card = in.readString();
        this.real_order = in.readString();
    }

    public static final Creator<UserBean> CREATOR = new Creator<UserBean>() {
        @Override
        public UserBean createFromParcel(Parcel source) {
            return new UserBean(source);
        }

        @Override
        public UserBean[] newArray(int size) {
            return new UserBean[size];
        }
    };
}
