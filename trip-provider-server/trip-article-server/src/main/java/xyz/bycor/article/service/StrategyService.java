package xyz.bycor.article.service;

import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import xyz.bycor.article.domain.Strategy;
import xyz.bycor.article.domain.StrategyCatalog;
import xyz.bycor.article.domain.StrategyContent;
import xyz.bycor.article.domain.StrategyTheme;
import xyz.bycor.article.qo.StatDataQuery;
import xyz.bycor.article.qo.StrategyQuery;
import xyz.bycor.core.qo.QueryObject;

import java.util.List;
import java.util.Map;

/**
* @author BAI
* @description 针对表【strategy】的数据库操作Service
* @createDate 2023-12-09 11:44:57
*/
public interface StrategyService extends IService<Strategy> {

    Page<Strategy> findPage(QueryObject qo);

    List<StrategyCatalog> findCatalogGroupsByDestId(Long destId);

    StrategyContent findContent(Long id);

    List<Strategy> findViewingTop3(Long destId);

    List<StrategyTheme> getThemesByDestId(Long destId);

    Page<Strategy> queryStrategy(StrategyQuery qo);

    JSONObject findRangs();

    JSONObject findConditions();

    Page<Strategy> queryList(StrategyQuery qo);

    List<Map<String, Object>> findStatByLastSevenDay(StatDataQuery statQuery);

    void thumbnumIncr(Long sid, Long id);

    Map<Object,Object> findstatData(Long id);

    List<Strategy> findStrategiesByDestId(Long destId, int limit);
}
