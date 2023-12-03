package xyz.bycor.member.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import xyz.bycor.core.response.R;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@RestController
@RequestMapping("/sms")
@Api(tags = "短信验证 接口")
@Slf4j
@Validated
public class SMSController {

    /**
     * 发送短信验证码
     * @param phone 手机号
     * @return
     */

    @PostMapping("/register")
    @ApiOperation(value = "发送短信验证码")
    public R<String> sendSMS(
            @NotNull(message = "手机号不能为空")
            @Pattern(regexp = "^1[3456789]\\d{9}$", message = "手机号格式不正确")
            String phone) {
        return R.ok("发送成功");
    }

}
