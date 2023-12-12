package xyz.bycor.member.dto;


import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;

/**
 *
 * UserinfoDTO 实体的DTO对象
 */
@Data
public class UserinfoDTO implements Serializable {
    public static final int GENDER_SECRET = 0; //保密
    public static final int GENDER_MALE = 1;   //男
    public static final int GENDER_FEMALE = 2;  //女
    public static final int STATE_NORMAL = 0;  //正常
    public static final int STATE_DISABLE = 1;  //冻结


    /**
     * 主键
     */
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
     * 密码
     */
    private String password;

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

    /**
     * 状态
     */
    private Integer state;

    /**
     * 逻辑删除
     */
    private Integer deleted;

    /**
     * 乐观锁
     */
    private Integer version;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}