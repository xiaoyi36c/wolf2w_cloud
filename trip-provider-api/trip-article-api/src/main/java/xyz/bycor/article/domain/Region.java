package xyz.bycor.article.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.springframework.util.StringUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @TableName region
 */
@TableName(value ="region")
@Data
public class Region implements Serializable {

    public static final int STATE_HOT = 1;
    public static final int STATE_NORMAL = 0;

    @TableId(type = IdType.AUTO)
    private Long id; //主键


    private String name; //名称


    private String sn; //编码


    private Integer ishot = STATE_NORMAL; //是否热门


    private Integer seq; //排序

    private String info; //简介


    private String refIds; //关联地区id集合

    public List<Long> parseRefIds(){

        List<Long> ids = new ArrayList<>();
        if(StringUtils.hasLength(refIds)){
            String[] split = refIds.split(",");
            if(split != null && split.length > 0){
                for (int i = 0;i <split.length; i++) {
                    ids.add(Long.parseLong(split[i]));
                }
            }
        }
        return ids;
    }
}