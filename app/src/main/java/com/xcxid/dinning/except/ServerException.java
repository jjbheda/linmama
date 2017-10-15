package com.xcxid.dinning.except;

public class ServerException extends RuntimeException {
    private static final long serialVersionUID = -6049058902104637525L;
    public int code;
    public String message;

    public ServerException(int code, String message) {
        super(message);
        this.code = code;
        this.message = message;
    }
}
