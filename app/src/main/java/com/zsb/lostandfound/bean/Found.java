package com.zsb.lostandfound.bean;

import cn.bmob.v3.BmobObject;

/**
 * Created by zsb on 2015/3/22.
 */
public class Found extends BmobObject {
    private String describe;
    private String phone;
    private String title;

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getDescribe() {

        return describe;
    }

    public String getPhone() {
        return phone;
    }

    public String getTitle() {
        return title;
    }
}
