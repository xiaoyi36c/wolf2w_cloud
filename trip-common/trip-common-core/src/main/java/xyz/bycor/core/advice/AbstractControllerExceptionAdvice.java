package xyz.bycor.core.advice;

import org.springframework.web.bind.annotation.ExceptionHandler;
import xyz.bycor.core.response.R;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;


/**
 * 统一异常处理的父类
 *
 */
public abstract class  AbstractControllerExceptionAdvice {

    @ExceptionHandler(ConstraintViolationException.class)
    public R<?> handleException(ConstraintViolationException e){
        ConstraintViolation<?> next = e.getConstraintViolations().iterator().next();
        return R.error(400, next.getMessage());
    }
}
