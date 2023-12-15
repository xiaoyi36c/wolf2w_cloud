package xyz.bycor.auth.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import xyz.bycor.auth.resolver.GetRequestUserMethodArgumentResolver;

import java.util.List;

@Configuration
public class MyResolverMvcConfig implements WebMvcConfigurer {
    @Autowired
    private GetRequestUserMethodArgumentResolver getRequestUserMethodArgumentResolver;

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(getRequestUserMethodArgumentResolver);
    }
}
