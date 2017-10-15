package com.linmama.dinning.except;

public class ApiException extends RuntimeException {
    private static final long serialVersionUID = 1374263463374055037L;
    private int code;
    private String message;

    public ApiException(Throwable throwable, int code, String message) {
        super(throwable);
        this.code = code;
        this.message = message;
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
}
