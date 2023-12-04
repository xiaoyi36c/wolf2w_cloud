package xyz.bycor.filter;

import com.alibaba.fastjson2.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import io.jsonwebtoken.Jwts;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Component
public class CheckLoginTokenFilter implements GlobalFilter {
    private static final List<String> WHITE_LIST = Arrays.asList("/userinfo/login", "/userinfo/register", "/ssm", "/userinfo/refreshToken");

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        // 1.获取当前请求的url
        String path = exchange.getRequest().getURI().getPath();
        // 2.判断是否是白名单中的url
        if (WHITE_LIST.contains(path)) {
            // 3.如果是白名单中的url,则直接放行
            return chain.filter(exchange);
        }
        // 4.如果不是白名单中的url,则获取请求头中的token
        String token = exchange.getRequest().getHeaders().getFirst("Authorization");
        try {
            // 2. 解析 token
            Jwts.parser()
                    .setSigningKey("wolfcode")
                    .parseClaimsJws(token)
                    .getBody();
            log.info("[验证token] token 解析成功, 放行请求:{}", path);
            // 3. 解析成功直接放行
            return chain.filter(exchange);
        } catch (Exception e) {
            // 3. 解析失败响应 401
            ServerHttpResponse response = exchange.getResponse();
            //但是一般访问接口应该返回json格式
            JSONObject json = new JSONObject();
            json.put("code", 401);
            json.put("msg", "账号未认证");

            log.warn("[验证token] token 解析失败, 拦截请求:{}", path);

            byte[] bits = json.toJSONString().getBytes(StandardCharsets.UTF_8);

            HttpHeaders httpHeaders = response.getHeaders();
            //返回数据格式
            httpHeaders.setContentType(MediaType.APPLICATION_JSON);

            //把字节数据转换成一个DataBuffer
            DataBuffer buffer = response.bufferFactory().wrap(bits);
            return response.writeWith(Mono.just(buffer));
        }
    }
}
