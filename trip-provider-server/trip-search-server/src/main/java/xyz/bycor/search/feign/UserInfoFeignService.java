package xyz.bycor.search.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import xyz.bycor.member.dto.UserinfoDTO;

import java.io.Serializable;
import java.util.List;

/**
 * @author BAI
 */

@FeignClient("trip-member-server")
public interface UserInfoFeignService {

    @GetMapping("/userinfo/findUserByDestId")
    List<UserinfoDTO> findUserByDestName(@RequestParam String destName);

    @GetMapping("/userinfo/findById")
    UserinfoDTO findById(@RequestParam Serializable id);
}
