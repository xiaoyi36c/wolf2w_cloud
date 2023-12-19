package xyz.bycor.article.service;

import com.baomidou.mybatisplus.extension.service.IService;
import xyz.bycor.article.domain.Banner;

import java.util.List;

/**
* @author BAI
* @description 针对表【Banner】的数据库操作Service
* @createDate 2023-12-07 10:43:57
*/
public interface BannerService extends IService<Banner> {
    List<Banner> findBannerByType(Integer type);
}
