package xyz.bycor.article.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import xyz.bycor.article.domain.StrategyTheme;
import xyz.bycor.article.mapper.StrategyThemeMapper;
import xyz.bycor.article.service.StrategyThemeService;
import xyz.bycor.core.qo.QueryObject;

/**
* @author BAI
* @description 针对表【strategyTheme_theme】的数据库操作Service实现
* @createDate 2023-12-09 11:44:57
*/
@Service
public class StrategyThemeServiceImpl extends ServiceImpl<StrategyThemeMapper, StrategyTheme>
    implements StrategyThemeService {

    @Override
    public Page<StrategyTheme> findPage(QueryObject qo) {
        Page<StrategyTheme> strategyThemePage = new Page<>(qo.getCurrent(), qo.getSize());
        QueryWrapper<StrategyTheme> wrapper = new QueryWrapper<>();
        wrapper.lambda().like(!qo.getKeyword().isEmpty(), StrategyTheme::getName, qo.getKeyword());
        return   super.page(strategyThemePage, wrapper);
    }
}




