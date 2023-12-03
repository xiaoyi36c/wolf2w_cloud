package xyz.bycor.core.response;

import lombok.Getter;
import lombok.Setter;

/**
 * 统一返回结果
 *
 * @param <T>
 */

@Setter
@Getter
public class R<T> {

    public static final int DEFAULT_SUCCESS_CODE = 200;
    public static final String DEFAULT_SUCCESS_MSG = "操作成功";
    public static final int DEFAULT_ERROR_CODE = 500;
    public static final String DEFAULT_ERROR_MSG = "服务繁忙";

    private String msg;
    private Integer code;
    private T data;

    public R(Integer code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public R(Integer code, String msg) {
        this(code, msg, null);
    }

    public static <T> R<T> ok() {
        return ok(null);
    }

    public static <T> R<T> ok(T data) {
        return new R<>(DEFAULT_SUCCESS_CODE, DEFAULT_SUCCESS_MSG, data);
    }

    public static <T> R<T> error() {
        return error(DEFAULT_ERROR_CODE, DEFAULT_ERROR_MSG);
    }

    public static <T> R<T> error(Integer code, String msg) {
        return new R<>(code, msg);
    }


}

