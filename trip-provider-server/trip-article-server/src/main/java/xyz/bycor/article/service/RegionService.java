package xyz.bycor.article.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import xyz.bycor.article.domain.Region;
import com.baomidou.mybatisplus.extension.service.IService;
import xyz.bycor.core.qo.QueryObject;

import java.util.List;

/**
* @author BAI
* @description 针对表【region】的数据库操作Service
* @createDate 2023-12-07 10:43:57
*/
public interface RegionService extends IService<Region> {

    Page<Region> findPage(QueryObject qo);

    List<Region> findHotList();
}
