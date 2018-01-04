package com.water.helper.bean;

/**
 * <p>
 * 出库明细
 * </p>
 * Created by Administrator on 2018/1/3 0003.
 */
public class ChuKuMxBean {

    /**
     * id : 5
     * keyid : 2018010317093764
     * cpid : 9
     * cpname : 浴袍
     * shuLiang : 7
     * beizhu : 正常
     * lc : 1
     * hid : 1
     * riqi : 2018-01-03 17:09:37
     */

    private int id;
    private String keyid;
    private int cpid;
    private String cpname;
    private int shuLiang;
    private String beizhu;
    private String lc;
    private int hid;
    private String riqi;

    public ChuKuMxBean() {
        super();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getKeyid() {
        return keyid;
    }

    public void setKeyid(String keyid) {
        this.keyid = keyid;
    }

    public int getCpid() {
        return cpid;
    }

    public void setCpid(int cpid) {
        this.cpid = cpid;
    }

    public String getCpname() {
        return cpname;
    }

    public void setCpname(String cpname) {
        this.cpname = cpname;
    }

    public int getShuLiang() {
        return shuLiang;
    }

    public void setShuLiang(int shuLiang) {
        this.shuLiang = shuLiang;
    }

    public String getBeizhu() {
        return beizhu;
    }

    public void setBeizhu(String beizhu) {
        this.beizhu = beizhu;
    }

    public String getLc() {
        return lc;
    }

    public void setLc(String lc) {
        this.lc = lc;
    }

    public int getHid() {
        return hid;
    }

    public void setHid(int hid) {
        this.hid = hid;
    }

    public String getRiqi() {
        return riqi;
    }

    public void setRiqi(String riqi) {
        this.riqi = riqi;
    }
}
