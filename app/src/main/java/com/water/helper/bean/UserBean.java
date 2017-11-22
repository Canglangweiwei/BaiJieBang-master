package com.water.helper.bean;

import org.litepal.crud.DataSupport;

import java.io.Serializable;

/**
 * <p>
 * 用户信息
 * </p>
 * Created by Administrator on 2017/10/13 0013.
 */
@SuppressWarnings("ALL")
public class UserBean extends DataSupport implements Serializable {


    /**
     * userid : 2
     * username : weiwei
     * password : ac59075b964b0715
     * roleID : null
     * addtime : 2017-11-21 14:16:02
     * addperson : null
     * lasttime : 2017-11-21 14:20:56
     * a_id : null
     * zhaohu : 下午好, weiwei
     */

    private int userid;
    private String username;
    private String password;
    private Object roleID;
    private String addtime;
    private Object addperson;
    private String lasttime;
    private Object a_id;
    private String zhaohu;

    public UserBean() {
        super();
    }

    public int getUserid() {
        return userid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }

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

    public Object getRoleID() {
        return roleID;
    }

    public void setRoleID(Object roleID) {
        this.roleID = roleID;
    }

    public String getAddtime() {
        return addtime;
    }

    public void setAddtime(String addtime) {
        this.addtime = addtime;
    }

    public Object getAddperson() {
        return addperson;
    }

    public void setAddperson(Object addperson) {
        this.addperson = addperson;
    }

    public String getLasttime() {
        return lasttime;
    }

    public void setLasttime(String lasttime) {
        this.lasttime = lasttime;
    }

    public Object getA_id() {
        return a_id;
    }

    public void setA_id(Object a_id) {
        this.a_id = a_id;
    }

    public String getZhaohu() {
        return zhaohu;
    }

    public void setZhaohu(String zhaohu) {
        this.zhaohu = zhaohu;
    }
}
