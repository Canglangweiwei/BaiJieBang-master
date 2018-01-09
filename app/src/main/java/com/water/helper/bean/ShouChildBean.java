package com.water.helper.bean;

/**
 * <p>
 * 收货管理子菜单列表
 * </p>
 * Created by Administrator on 2017/11/2 0002.
 */
public class ShouChildBean {

    private int id;             // id
    private String name;        // 类型
    private int sum;            // 数量
    private int hxNum;          // 回洗数量

    public ShouChildBean() {
        super();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSum() {
        return sum;
    }

    public void setSum(int sum) {
        this.sum = sum;
    }

    public int getHxNum() {
        return hxNum;
    }

    public void setHxNum(int hxNum) {
        this.hxNum = hxNum;
    }
}
