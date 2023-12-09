package xyz.bycor.article.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import xyz.bycor.article.domain.Destination;
import xyz.bycor.article.domain.StrategyCatalog;
import xyz.bycor.article.mapper.StrategyCatalogMapper;
import xyz.bycor.article.service.DestinationService;
import xyz.bycor.article.service.StrategyCatalogService;
import xyz.bycor.article.vo.CatalogGroup;
import xyz.bycor.core.qo.QueryObject;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
* @author BAI
* @description 针对表【strategy_catalog】的数据库操作Service实现
* @createDate 2023-12-09 11:44:57
*/
@Service
public class StrategyCatalogServiceImpl extends ServiceImpl<StrategyCatalogMapper, StrategyCatalog>
    implements StrategyCatalogService{

    private final DestinationService destinationService;

    public StrategyCatalogServiceImpl(DestinationService destinationService) {
        this.destinationService = destinationService;
    }

    @Override
    public Page<StrategyCatalog> findPage(QueryObject qo) {
        Page<StrategyCatalog> strategyCatalogPage = new Page<>(qo.getCurrent(), qo.getSize());
        QueryWrapper<StrategyCatalog> wrapper = new QueryWrapper<>();
        wrapper.lambda().like(!qo.getKeyword().isEmpty(), StrategyCatalog::getName, qo.getKeyword());
        return   super.page(strategyCatalogPage, wrapper);
    }

    @Override
    public List<CatalogGroup> findGroups() {
        List<StrategyCatalog> catalogs = super.list();
        Map<String, List<StrategyCatalog>> collect = catalogs.stream().collect(Collectors.groupingBy(StrategyCatalog::getDestName));
        return collect.entrySet().stream()
                .map(entry ->  new CatalogGroup(entry.getKey(), entry.getValue())).collect(Collectors.toList());
    }

    @Override
    public boolean saveOrUpdate(StrategyCatalog entity) {
        if (entity != null) {
            if (entity.getDestId() !=null) {
                Destination destination = destinationService.getById(entity.getDestId());
                entity.setDestName(destination.getName());
            }
        }
        return super.saveOrUpdate(entity);
    }
}




