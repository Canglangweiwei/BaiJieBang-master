package com.water.helper.bean;

import java.util.List;

/**
 * <p>
 * <p>
 * </p>
 * Created by Administrator on 2017/11/2 0002.
 */
public class FaResultBean {

    private List<String> group;
    private List<List<FaChildBean>> child;

    public FaResultBean() {
        super();
    }

    public List<String> getGroup() {
        return group;
    }

    public void setGroup(List<String> group) {
        this.group = group;
    }

    public List<List<FaChildBean>> getChild() {
        return child;
    }

    public void setChild(List<List<FaChildBean>> child) {
        this.child = child;
    }
}
