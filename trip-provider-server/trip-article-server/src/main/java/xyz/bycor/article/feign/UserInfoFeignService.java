package xyz.bycor.article.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import xyz.bycor.member.dto.UserinfoDTO;

import java.util.List;
import java.util.Set;

@Component
@FeignClient(name = "trip-member-server")
public interface UserInfoFeignService {

    @GetMapping("userinfo/findByPhone")
    UserinfoDTO findByPhone(@RequestParam String phone) ;

    @GetMapping("userinfo/findByIds")
    List<UserinfoDTO> findByIds(@RequestParam Set<Long> authorIds);
}
