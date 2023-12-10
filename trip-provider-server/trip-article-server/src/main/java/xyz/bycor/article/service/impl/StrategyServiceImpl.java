package xyz.bycor.article.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.bycor.article.domain.*;
import xyz.bycor.article.mapper.StrategyMapper;
import xyz.bycor.article.service.StrategyCatalogService;
import xyz.bycor.article.service.StrategyContentService;
import xyz.bycor.article.service.StrategyService;
import xyz.bycor.article.service.StrategyThemeService;
import xyz.bycor.core.qo.QueryObject;
import xyz.bycor.core.utils.OssUtil;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
* @author BAI
* @description 针对表【strategy】的数据库操作Service实现
* @createDate 2023-12-09 11:44:57
*/
@Service
public class StrategyServiceImpl extends ServiceImpl<StrategyMapper, Strategy>
    implements StrategyService{

    private final StrategyThemeService strategyThemeService;
    private final StrategyCatalogService strategyCatalogService;
    private final StrategyContentService strategyContentService;


    private final DestinationServiceImpl destinationService;

    public StrategyServiceImpl(StrategyContentService strategyContentService, StrategyCatalogService strategyCatalogService, StrategyThemeService strategyThemeService, DestinationServiceImpl destinationService) {
        this.strategyContentService = strategyContentService;
        this.strategyCatalogService = strategyCatalogService;
        this.strategyThemeService = strategyThemeService;
        this.destinationService = destinationService;
    }


    @Override
    public Page<Strategy> findPage(QueryObject qo) {
        Page<Strategy> strategyPage = new Page<>(qo.getCurrent(), qo.getSize());
        QueryWrapper<Strategy> wrapper = new QueryWrapper<>();
        wrapper.lambda().like(!qo.getKeyword().isEmpty(), Strategy::getThemeName, qo.getKeyword());
        return   super.page(strategyPage, wrapper);

    }


    @Override
    public Strategy getById(Serializable id) {
        Strategy strategy = super.getById(id);
        StrategyContent content = strategyContentService.getById(strategy.getId());
        strategy.setContent(content);
        return strategy;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean save(Strategy entity) {
        handleEntity(entity);

        // 6．没置创建时间
        entity.setCreateTime(new Date());
        // 7．设置各种数量初始值
        entity.setViewnum(0);
        entity.setReplynum(0);
        entity.setFavornum(0);
        entity.setSharenum(0);
        entity.setThumbsupnum(0);
        // 8．设置攻略状态
        entity.setState(Strategy.STATE_NORMAL);
        // 9．保存攻略对象
        super.save(entity);
        //10．将攻略自增id设置到攻略内容对象
        entity.getContent().setId(entity.getId());
        // 11．保存攻略内容
        return strategyContentService.save(entity.getContent());
    }

    private void handleEntity(Strategy entity) {
        // 1．处理Url
        if (entity.getCoverUrl() != null) {
            String fileName = UUID.randomUUID().toString().replace("-", "").substring(0, 10);
            String images = OssUtil.uploadImgByBase64("images", fileName, entity.getCoverUrl());
            entity.setCoverUrl(images);
        }
        // 2．查询主题名称
        if (entity.getThemeId() != null) {
            StrategyTheme theme = strategyThemeService.getById(entity.getThemeId());
            entity.setThemeName(theme.getName());
        }

        // 3．查询分类名称
        if (entity.getCatalogId() != null) {
            StrategyCatalog catalog = strategyCatalogService.getById(entity.getCatalogId());
            if (catalog != null) {
                entity.setCatalogName(catalog.getName());
                // 4．查询目的地名称
                entity.setDestId(catalog.getDestId());
                entity.setDestName(catalog.getDestName());
                // 5．设置是否国外
                List<Destination> toastList = destinationService.findToastList(catalog.getDestId());
                if (toastList.get(0).getId() == 1L) {
                    entity.setIsabroad(Strategy.ABROAD_YES);
                } else {
                    entity.setIsabroad(Strategy.ABROAD_NO);
                }
            }
        }
    }

    @Override
    public boolean updateById(Strategy entity) {
        handleEntity(entity);
        super.updateById(entity);
        //10．将攻略自增id设置到攻略内容对象
        entity.getContent().setId(entity.getId());
        // 11．保存攻略内容
        return strategyContentService.updateById(entity.getContent());
    }

    @Override
    public boolean removeById(Serializable id) {
        strategyContentService.removeById(id);
        return super.removeById(id);
    }
}




