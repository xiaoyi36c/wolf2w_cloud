package xyz.bycor.article.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 
 * @author BAI
 * @TableName destination
 */
@Data
public class DestinationDTO implements Serializable {

    private Long id;


    private String name;


    private String english;


    private String coverUrl;


    private String info;


    private String parentName;


    private Long parentId;
}