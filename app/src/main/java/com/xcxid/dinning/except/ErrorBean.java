package com.xcxid.dinning.except;

/**
 * Created by jingkang on 2017/3/10
 */
public class ErrorBean {
    private String status;
    private Object errors_info;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Object getErrors_info() {
        return errors_info;
    }

    public void setErrors_info(Object errors_info) {
        this.errors_info = errors_info;
    }

    @Override
    public String toString() {
        return "ErrorBean{" +
                "status='" + status + '\'' +
                ", errors_info='" + errors_info + '\'' +
                '}';
    }
}
