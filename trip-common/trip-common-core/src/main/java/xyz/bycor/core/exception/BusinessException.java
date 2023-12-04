package xyz.bycor.core.exception;

import lombok.Getter;
import xyz.bycor.core.response.R;

/**
 * 业务异常
 */
@Getter
public class BusinessException extends RuntimeException {

    public BusinessException() {
        super();
    }

    private Integer code= R.DEFAULT_ERROR_CODE;
    public BusinessException(String message) {
        super(message);
    }


    public BusinessException(Integer code, String message) {
        super(message);
        this.code = code;
    }
}
