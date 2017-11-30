package com.water.helper.bean;

public class BaseBean {

    private int result;
    private String message;
    private String data;

    public BaseBean() {
        super();
    }

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
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

    @Override
    public String toString() {
        return "BaseBean{" +
                "result=" + result +
                ", message='" + message + '\'' +
                ", data='" + data + '\'' +
                '}';
    }
}
