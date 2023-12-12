package xyz.bycor.article.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import xyz.bycor.member.dto.UserinfoDTO;

@FeignClient(name = "trip-user-server")
public interface UserInfoFeignService {

    @GetMapping("/findByPhone")
    public UserinfoDTO findByPhone(@RequestParam String phone) ;
}
