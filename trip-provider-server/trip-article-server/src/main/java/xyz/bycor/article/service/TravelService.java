package xyz.bycor.article.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import xyz.bycor.article.domain.Travel;
import xyz.bycor.article.qo.TravelQuery;

import java.util.List;

/**
* @author BAI
* @description 针对表【travel】的数据库操作Service
* @createDate 2023-12-12 16:40:25
*/
public interface TravelService extends IService<Travel> {

    Page<Travel> findTravelByDestId(TravelQuery travelQuery);

    List<Travel> findTravelTop3(Long destId);

    Travel findById(Long id);

}
