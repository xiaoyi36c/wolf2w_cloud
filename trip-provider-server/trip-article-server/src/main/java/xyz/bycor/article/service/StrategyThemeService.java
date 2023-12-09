package xyz.bycor.article.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import xyz.bycor.article.domain.StrategyTheme;
import com.baomidou.mybatisplus.extension.service.IService;
import xyz.bycor.core.qo.QueryObject;

/**
* @author BAI
* @description 针对表【strategy_theme】的数据库操作Service
* @createDate 2023-12-09 11:44:57
*/
public interface StrategyThemeService extends IService<StrategyTheme> {

    Page<StrategyTheme> findPage(QueryObject qo);
}
