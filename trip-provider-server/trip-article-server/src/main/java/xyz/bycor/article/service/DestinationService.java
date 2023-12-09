package xyz.bycor.article.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import xyz.bycor.article.domain.Destination;
import xyz.bycor.article.qo.DestinationQueryObject;

import java.util.List;

/**
* @author BAI
* @description 针对表【destination】的数据库操作Service
* @createDate 2023-12-07 10:53:35
*/
public interface DestinationService extends IService<Destination> {

    Page<Destination> findPage(DestinationQueryObject qo);

    List<Destination> findByRegionId(Long id);

    List<Destination> findToastList(Long parentId);

    List<Destination> findHotList(Long rid);
}
