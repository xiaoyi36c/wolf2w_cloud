package xyz.bycor.article.mapper;

import xyz.bycor.article.domain.Strategy;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;
import java.util.Map;

/**
* @author BAI
* @description 针对表【strategy】的数据库操作Mapper
* @createDate 2023-12-09 11:44:57
* @Entity xyz.bycor.article.domain.Strategy
*/
public interface StrategyMapper extends BaseMapper<Strategy> {

    List<Map<String,Object>> selectThemeConditions();
    List<Map<String,Object>> selectAbroadConditions();
    List<Map<String,Object>>selectChinaConditions();

}




