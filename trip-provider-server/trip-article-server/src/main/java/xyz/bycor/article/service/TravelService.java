package xyz.bycor.article.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import xyz.bycor.article.domain.Travel;
import com.baomidou.mybatisplus.extension.service.IService;
import xyz.bycor.article.qo.TravelQuery;

/**
* @author BAI
* @description 针对表【travel】的数据库操作Service
* @createDate 2023-12-12 16:40:25
*/
public interface TravelService extends IService<Travel> {

    Page<Travel> findTravelByDestId(TravelQuery travelQuery);
}
