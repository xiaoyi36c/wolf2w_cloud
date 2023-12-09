package xyz.bycor.article.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import xyz.bycor.article.domain.Strategy;
import xyz.bycor.article.mapper.StrategyMapper;
import xyz.bycor.article.service.StrategyService;
import xyz.bycor.core.qo.QueryObject;

/**
* @author BAI
* @description 针对表【strategy】的数据库操作Service实现
* @createDate 2023-12-09 11:44:57
*/
@Service
public class StrategyServiceImpl extends ServiceImpl<StrategyMapper, Strategy>
    implements StrategyService{

    @Override
    public Page<Strategy> findPage(QueryObject qo) {
        Page<Strategy> strategyPage = new Page<>(qo.getCurrent(), qo.getSize());
        QueryWrapper<Strategy> wrapper = new QueryWrapper<>();
        wrapper.lambda().like(!qo.getKeyword().isEmpty(), Strategy::getThemeName, qo.getKeyword());
        return   super.page(strategyPage, wrapper);

    }
}




