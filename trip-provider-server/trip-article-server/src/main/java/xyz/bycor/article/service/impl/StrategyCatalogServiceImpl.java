package xyz.bycor.article.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import xyz.bycor.article.domain.StrategyCatalog;
import xyz.bycor.article.mapper.StrategyCatalogMapper;
import xyz.bycor.article.service.StrategyCatalogService;
import xyz.bycor.core.qo.QueryObject;

/**
* @author BAI
* @description 针对表【strategy_catalog】的数据库操作Service实现
* @createDate 2023-12-09 11:44:57
*/
@Service
public class StrategyCatalogServiceImpl extends ServiceImpl<StrategyCatalogMapper, StrategyCatalog>
    implements StrategyCatalogService{

    @Override
    public Page<StrategyCatalog> findPage(QueryObject qo) {
        Page<StrategyCatalog> strategyCatalogPage = new Page<>(qo.getCurrent(), qo.getSize());
        QueryWrapper<StrategyCatalog> wrapper = new QueryWrapper<>();
        wrapper.lambda().like(!qo.getKeyword().isEmpty(), StrategyCatalog::getName, qo.getKeyword());
        return   super.page(strategyCatalogPage, wrapper);
    }
}




