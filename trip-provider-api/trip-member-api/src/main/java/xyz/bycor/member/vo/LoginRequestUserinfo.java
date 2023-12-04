package xyz.bycor.member.vo;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class LoginRequestUserinfo {
    @NotNull(message = "手机号不能为空")
    private String phone;
    @NotNull(message = "密码不能为空")
    private String password;
}
