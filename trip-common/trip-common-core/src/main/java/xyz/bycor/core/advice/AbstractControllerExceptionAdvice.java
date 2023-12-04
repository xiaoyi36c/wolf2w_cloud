package xyz.bycor.core.advice;

import org.springframework.web.bind.annotation.ExceptionHandler;
import xyz.bycor.core.exception.BusinessException;
import xyz.bycor.core.response.R;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;


/**
 * 统一异常处理的父类
 *
 */
public abstract class  AbstractControllerExceptionAdvice {


    @ExceptionHandler(BusinessException.class)
    public R<?> handleException(BusinessException e){
        return R.error(e.getCode(), e.getMessage());
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public R<?> handleException(ConstraintViolationException e){
        ConstraintViolation<?> next = e.getConstraintViolations().iterator().next();
        return R.error(400, next.getMessage());
    }
}
