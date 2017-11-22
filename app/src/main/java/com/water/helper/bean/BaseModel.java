package com.water.helper.bean;

/**
 * <p>
 * baseBean
 * </p>
 * Created by Administrator on 2017/11/21 0021.
 */
public class BaseModel {

    private boolean success;
    private int code;
    private String message;
    private String data;

    public BaseModel() {
        super();
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
