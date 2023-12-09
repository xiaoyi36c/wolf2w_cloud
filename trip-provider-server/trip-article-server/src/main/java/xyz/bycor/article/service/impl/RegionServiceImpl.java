package xyz.bycor.article.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import xyz.bycor.article.domain.Region;
import xyz.bycor.article.mapper.RegionMapper;
import xyz.bycor.article.service.RegionService;
import xyz.bycor.core.qo.QueryObject;

import java.util.List;

/**
* @author BAI
* @description 针对表【region】的数据库操作Service实现
* @createDate 2023-12-07 10:43:57
*/
@Service
public class RegionServiceImpl extends ServiceImpl<RegionMapper, Region>
    implements RegionService{

    @Override
    public Page<Region> findPage(QueryObject qo) {
        Page<Region> regionPage = new Page<>(qo.getCurrent(), qo.getSize());
        QueryWrapper<Region> wrapper = new QueryWrapper<>();
        wrapper.lambda().like(!qo.getKeyword().isEmpty(), Region::getName, qo.getKeyword());
         return   super.page(regionPage, wrapper);

    }

    @Override
    public List<Region> findHotList() {
        LambdaQueryWrapper<Region> wrapper = new QueryWrapper<Region>().lambda()
                .eq(Region::getIshot, Region.STATE_HOT)
                .orderByAsc(Region::getSeq);
        return super.list(wrapper);
    }
}




