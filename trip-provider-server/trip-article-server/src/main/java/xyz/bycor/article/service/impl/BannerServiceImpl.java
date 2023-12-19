package xyz.bycor.article.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import xyz.bycor.article.domain.Banner;
import xyz.bycor.article.mapper.BannerMapper;
import xyz.bycor.article.service.BannerService;

import java.util.List;

/**
* @author BAI
* @description 针对表【Banner】的数据库操作Service实现
* @createDate 2023-12-07 10:43:57
*/
@Service
public class BannerServiceImpl extends ServiceImpl<BannerMapper, Banner>
    implements BannerService{


    @Override
    public List<Banner> findBannerByType(Integer type) {
        QueryWrapper<Banner> wrapper = new QueryWrapper<>();
        wrapper.eq("type",type);
        wrapper.eq("state",Banner.STATE_NORMAL);
        wrapper.orderByAsc("seq");
        return baseMapper.selectList(wrapper);
    }
}




