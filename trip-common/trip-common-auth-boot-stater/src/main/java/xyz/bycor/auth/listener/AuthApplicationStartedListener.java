package xyz.bycor.auth.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;
import xyz.bycor.auth.anno.RequiredLogin;
import xyz.bycor.framework.redis.config.RedisJsonService;
import xyz.bycor.framework.redis.config.key.RedisEnum;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Slf4j
public class AuthApplicationStartedListener implements ApplicationListener<ApplicationStartedEvent> {

    private final ApplicationContext applicationContext;

    private final RedisJsonService redisJsonService;

    public AuthApplicationStartedListener(ApplicationContext applicationContext, RedisJsonService redisJsonService) {
        this.applicationContext = applicationContext;
        this.redisJsonService = redisJsonService;
    }

    @Value("${spring.application.name}")
    private String applicationName;




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
        if (blackList.isEmpty()) {
            // 如果黑名单为空,则直接返回
            return;
        }
        log.error("[黑名单扫描]");
        // 获取上一次的黑名单
        Set<String> lastBlackList = redisJsonService.hget(RedisEnum.AUTH_LAST_BLACKLIST, applicationName);
       // lastBlackList2.toString();

        /*log.error("上一次的黑名单:{}", lastBlackList2);

        HashSet<Object> lastBlackList = new HashSet<>();*/

        log.error("上一次的黑名单:{}", lastBlackList);


        if (lastBlackList != null && !lastBlackList.isEmpty()) {
            // 如果上一次的黑名单不为空,则删除上一次的黑名单
            redisJsonService.hRemove(RedisEnum.AUTH_BLACKLIST,lastBlackList.toArray());
        }
        // 将本次的黑名单存入redis
        redisJsonService.hSetAll(RedisEnum.AUTH_LAST_BLACKLIST, applicationName, blackList);

        // 5.将黑名单存入redis
        redisJsonService.sAddAll(RedisEnum.AUTH_BLACKLIST, blackList.toArray());
        log.error("黑名单:{}", blackList);
    }
}
