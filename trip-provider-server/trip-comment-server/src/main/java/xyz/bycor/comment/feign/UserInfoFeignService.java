package xyz.bycor.comment.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import xyz.bycor.member.dto.UserinfoDTO;


@FeignClient(name ="trip-member-server")
public interface UserInfoFeignService {

    @GetMapping("userinfo/findByPhone")
    UserinfoDTO findByPhone(@RequestParam String phone) ;


}
