package com.linmama.dinning.base;

/**
 * Created by jingkang on 2017/2/28
 */
public class BaseHttpResult<T> {
    private String status;
    private Object errors_info;
    public int per_page = 0;
    public int current_page = 0;
    public int last_page = 0;
    private T data;

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

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "BaseHttpResult{" +
                "status='" + status + '\'' +
                ", errors_info='" + errors_info + '\'' +
                ", data=" + data +
                '}';
    }
}
