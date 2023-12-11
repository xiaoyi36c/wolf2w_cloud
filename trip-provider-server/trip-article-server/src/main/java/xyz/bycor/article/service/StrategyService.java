package xyz.bycor.article.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import xyz.bycor.article.domain.Strategy;
import com.baomidou.mybatisplus.extension.service.IService;
import xyz.bycor.article.domain.StrategyCatalog;
import xyz.bycor.article.domain.StrategyContent;
import xyz.bycor.article.domain.StrategyTheme;
import xyz.bycor.core.qo.QueryObject;

import java.util.List;

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
}
