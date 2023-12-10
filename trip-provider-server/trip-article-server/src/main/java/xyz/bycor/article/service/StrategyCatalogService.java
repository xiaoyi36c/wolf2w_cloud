package xyz.bycor.article.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import xyz.bycor.article.domain.StrategyCatalog;
import com.baomidou.mybatisplus.extension.service.IService;
import xyz.bycor.article.vo.CatalogGroup;
import xyz.bycor.core.qo.QueryObject;

import java.util.List;

/**
* @author BAI
* @description 针对表【strategy_catalog】的数据库操作Service
* @createDate 2023-12-09 11:44:57
*/
public interface StrategyCatalogService extends IService<StrategyCatalog> {

    Page<StrategyCatalog> findPage(QueryObject qo);

    List<CatalogGroup> findGroups();

    List<StrategyCatalog> findListByDestId(Long destId);
}
