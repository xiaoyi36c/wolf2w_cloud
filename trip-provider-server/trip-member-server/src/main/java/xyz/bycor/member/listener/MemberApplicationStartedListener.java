package xyz.bycor.member.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;
import xyz.bycor.framework.redis.config.RedisJsonService;
import xyz.bycor.framework.redis.config.key.RedisEnum;
import xyz.bycor.member.znno.RequiredLogin;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Component
@Slf4j
public class MemberApplicationStartedListener implements ApplicationListener<ApplicationStartedEvent> {

    private final ApplicationContext applicationContext;

    private final RedisJsonService redisJsonService;

    public MemberApplicationStartedListener(ApplicationContext applicationContext, RedisJsonService redisJsonService) {
        this.applicationContext = applicationContext;
        this.redisJsonService = redisJsonService;
    }




    /**
     * 在项目启动时,将所有需要登录的请求路径加入黑名单
     * @param Event 事件
     */

    @Override
    public void onApplicationEvent(ApplicationStartedEvent Event) {
        // 1.声明一个集合,用来存放所有的黑名单
        HashSet<Object> blackList = new HashSet<>();
        // 2.获取所有的RequestMappingHandlerMapping
        RequestMappingHandlerMapping bean = applicationContext.getBean(RequestMappingHandlerMapping.class);
        // 3.获取所有的RequestMappingInfo
        Map<RequestMappingInfo, HandlerMethod> handlerMethodInfos = bean.getHandlerMethods();
        // 4.遍历所有的RequestMappingInfo
        Set<Map.Entry<RequestMappingInfo, HandlerMethod>> entries = handlerMethodInfos.entrySet();
        for (Map.Entry<RequestMappingInfo, HandlerMethod> entry : entries) {
            HandlerMethod handlerMethod = entry.getValue();
            // 获取完整的请求路径
            Set<String> requestURl = entry.getKey().getPatternsCondition().getPatterns();
            // 获取类上的@RequringLogin注解
            Class<?> controllerClass = handlerMethod.getBeanType();
            // 如果类上有@RequringLogin注解,或者方法上有@RequringLogin注解,则将该请求路径加入黑名单
            if (controllerClass.isAnnotationPresent(RequiredLogin.class) || handlerMethod.getMethod().isAnnotationPresent(RequiredLogin.class)) {
                blackList.addAll(requestURl);
            }
        }
        log.error("[黑名单扫描]");
        // 5.将黑名单存入redis
        redisJsonService.sAddAll(RedisEnum.AUTH_BLACKLIST, blackList.toArray());
        log.error("黑名单:{}", blackList);
    }
}
