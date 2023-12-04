package xyz.bycor.member.vo;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

/**
 * @Description: Vo类
 *
 */


@Data
public class RegisterRequestUserinfo {
    @NotNull(message = "手机号不能为空")
    @Pattern(regexp = "^1[3456789]\\d{9}$", message = "手机号格式不正确")
    private String phone;
    @NotNull(message = "密码不能为空")
    private String password;
    @NotNull(message = "验证码不能为空")
    @Length(min = 6, max = 6, message = "验证码长度为6位")
    private String verifyCode;
    @NotNull(message = "昵称不能为空")
    private String nickname;
}
