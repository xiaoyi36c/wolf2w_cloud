package xyz.bycor.member.vo;

import lombok.Data;


@Data
public class LoginResponseUser {

    private Long id;


    /**
     * 昵称
     */
    private String nickname;

    /**
     * 电话号码
     */
    private String phone;

    /**
     * 电子邮箱
     */
    private String email;

    /**
     * 性别
     */
    private Integer gender;

    /**
     * 生日
     */
    private Integer level;

    /**
     * 省份
     */
    private String city;

    /**
     * 头像
     */
    private String headImgUrl;

    /**
     * 个性签名
     */
    private String info;


}
