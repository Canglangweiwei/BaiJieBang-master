package com.water.helper.bean;

/**
 * <p>
 * 宾馆
 * </p>
 * Created by Administrator on 2017/11/21 0021.
 */
public class HotelBean {

    private int id;
    private String name;
    private int isSelect = 0;// 标记是否选中

    public HotelBean() {
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

    public int getIsSelect() {
        return isSelect;
    }

    public void setIsSelect(int isSelect) {
        this.isSelect = isSelect;
    }
}
