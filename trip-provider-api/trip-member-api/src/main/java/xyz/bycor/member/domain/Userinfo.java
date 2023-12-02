package xyz.bycor.member.domain;


import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import lombok.Data;

/**
 *
 * @TableName userinfo
 */
@TableName(value ="userinfo")
@Data
public class Userinfo implements Serializable {
    /**
     * 主键
     */
    @TableId(type = IdType.AUTO)
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
    @TableLogic(value = "0", delval = "1")
    @TableField("is_deleted")
    private Integer deleted;

    /**
     * 乐观锁
     */
    @Version
    private Integer version;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}