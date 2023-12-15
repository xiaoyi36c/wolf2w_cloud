package xyz.bycor.auth.resolver;

import org.springframework.core.MethodParameter;
import org.springframework.util.Assert;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import xyz.bycor.auth.anno.RequestUser;
import xyz.bycor.auth.vo.LoginResponseUser;
import xyz.bycor.core.exception.BusinessException;
import xyz.bycor.core.utils.JwtUtils;
import xyz.bycor.framework.redis.config.RedisJsonService;
import xyz.bycor.framework.redis.config.key.RedisEnum;


public class GetRequestUserMethodArgumentResolver implements HandlerMethodArgumentResolver {
    private final RedisJsonService redisJsonService;

    public GetRequestUserMethodArgumentResolver(RedisJsonService redisJsonService) {
        this.redisJsonService = redisJsonService;
    }


    @Override
    public boolean supportsParameter(MethodParameter methodParameter) {
        return methodParameter.hasParameterAnnotation(RequestUser.class) && methodParameter.getParameterType() == LoginResponseUser.class;
    }

    @Override
    public LoginResponseUser resolveArgument(MethodParameter methodParameter, ModelAndViewContainer modelAndViewContainer, NativeWebRequest nativeWebRequest, WebDataBinderFactory webDataBinderFactory) throws Exception {
        // 获取注解的required属性
        boolean required = methodParameter.getParameterAnnotation(RequestUser.class).required();
        String phone = JwtUtils.getSubject();
        LoginResponseUser user = redisJsonService.get(RedisEnum.USER_LOGIN_INFO_KEY, phone);
        // 判断是否必须登录
        if (required){
            try {
                Assert.hasText(phone,"用户未登录");
                Assert.notNull(user,"用户未登录");
            } catch (Exception e) {
                throw new BusinessException(401,e.getMessage());
            }
        }
        return user;
    }
}
