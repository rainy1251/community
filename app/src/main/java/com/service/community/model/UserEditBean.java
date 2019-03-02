package com.service.community.model;

/**
 * Created by Administrator on 2017/5/11.
 */

public class UserEditBean {

  public UserEditBean(String title, String content) {
    this.title = title;
    this.content = content;
  }

  public UserEditBean(String title, String content, String avatar) {
    this.title = title;
    this.content = content;
    this.avatar = avatar;
  }

  public   String title;
  public   String content;
  public   String area;

  public String getArea() {
    return area;
  }

  public void setArea(String area) {
    this.area = area;
  }

  public String getAvatar() {
    return avatar;
  }

  public void setAvatar(String avatar) {
    this.avatar = avatar;
  }

  public   String avatar;//有赞token


}
