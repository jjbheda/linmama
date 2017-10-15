package com.linmama.dinning.except;

public interface ErrorType {
//    1xx 消息
//    int CONTINUE = 100;
//    int SWITCHING_PROTOCOLS = 101;
//    int PROCESSING = 102;

//    2xx 成功
//    int OK = 200;
//    int CREATED = 201;
//    int ACCEPTED = 202;
//    int NON_AUTHORITATIVE_INFORMATION = 203;
//    int NO_CONTENT = 204;
//    int RESET_CONTENT = 205;
//    int PARTIAL_CONTENT = 206;
//    int MULTI_STATUS = 207;

//    3xx 重定向
//    int MULTIPLE_CHOICES = 300;
//    int MOVED_PERMANENTLY = 301;
//    int FOUND = 302;
//    int SEE_OTHER = 303;
//    int NOT_MODIFIED = 304;
//    int USE_PROXY = 305;
//    int SWITCH_PROXY = 306;
//    int TEMPORAYR_REDIRECT = 307;

    //    4xx 请求错误
    int BAD_REQUEST = 400;
    int UNAUTHORIZED = 401;
    int PAYMENT_REQUIRED = 402;
    int FORBIDDEN = 403;
    int NOT_FOUND = 404;
    int METHOD_NOT_ALLOWED = 405;
    int NOT_ACCEPTABLE = 406;
    int PROXY_AUTHENTICATION_REQUIRED = 407;
    int REQUEST_TIMEOUT = 408;
    int CONFLICT = 409;
    int GONE = 410;
    int LENGTH_REQUIRED = 411;
    int PRECONDITION_FAILED = 412;
    int REQUEST_ENTITY_TOO_LARGE = 413;
    int REQUEST_URL_TOO_LONG = 414;
    int UNSUPPORTED_MEDIA_TYPE = 415;
    int REQUESTED_RANGE_NOT_SATISFIABLE = 416;
    int EXPECTATION_FAILED = 417;
    int UNPROCESSABLE_ENTITY = 422;
    int LOCKED = 423;
    int FAILED_DEPENDENCY = 424;
    int UNORDERED_COLLECTION = 425;
    int UPGRADE_REQUIRED = 426;
    int RETRY_WITH = 449;

    // 5xx 服务器错误
    int INTERNAL_SERVER_ERROR = 500;
    int NOT_IMPLEMENTED = 501;
    int BAD_GATEWAY = 502;
    int SERVICE_UNAVAILABLE = 503;
    int GATEWAY_TIMEOUT = 504;
    int HTTP_VERSION_NOT_SUPPORTED = 505;
    int VARIANT_ALSO_NEGOTIATES = 506;
    int INSUFFICIENT_STORAGE = 507;
    int BANDWIDTH_LIMIT_EXCEEDED = 509;
    int NOT_EXTENDED = 510;

    int OTHER_ERROR = 1000;
    int EMPTY_BEAN = 1001;
    int EMPTY_DATA = 1002;
    int HTTP_ERROR = 1001;
}
