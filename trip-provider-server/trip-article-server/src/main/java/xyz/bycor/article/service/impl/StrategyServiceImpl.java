package xyz.bycor.article.service.impl;

import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;
import xyz.bycor.article.domain.*;
import xyz.bycor.article.mapper.StrategyMapper;
import xyz.bycor.article.qo.StatDataQuery;
import xyz.bycor.article.qo.StrategyQuery;
import xyz.bycor.article.service.StrategyCatalogService;
import xyz.bycor.article.service.StrategyContentService;
import xyz.bycor.article.service.StrategyService;
import xyz.bycor.article.service.StrategyThemeService;
import xyz.bycor.core.qo.QueryObject;
import xyz.bycor.core.utils.OssUtil;

import java.io.Serializable;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

/**
* @author BAI
* @description 针对表【strategy】的数据库操作Service实现
* @createDate 2023-12-09 11:44:57
*/
@Service
@CacheConfig(cacheNames = "wlof2w:trip:article:strategy")
public class StrategyServiceImpl extends ServiceImpl<StrategyMapper, Strategy>
    implements StrategyService{

    private final StrategyThemeService strategyThemeService;
    private final StrategyCatalogService strategyCatalogService;
    private final StrategyContentService strategyContentService;

    private final TransactionTemplate transactionTemplate;


    private final DestinationServiceImpl destinationService;

    public StrategyServiceImpl(StrategyContentService strategyContentService, StrategyCatalogService strategyCatalogService, StrategyThemeService strategyThemeService, DestinationServiceImpl destinationService, TransactionTemplate transactionTemplate) {
        this.strategyContentService = strategyContentService;
        this.strategyCatalogService = strategyCatalogService;
        this.strategyThemeService = strategyThemeService;
        this.destinationService = destinationService;
        this.transactionTemplate = transactionTemplate;
    }


    @Override
    public Page<Strategy> findPage(QueryObject qo) {
        Page<Strategy> strategyPage = new Page<>(qo.getCurrent(), qo.getSize());
        QueryWrapper<Strategy> wrapper = new QueryWrapper<>();
        wrapper.lambda().like(!qo.getKeyword().isEmpty(), Strategy::getThemeName, qo.getKeyword());
        return   super.page(strategyPage, wrapper);

    }

    @Override
    public List<StrategyCatalog> findCatalogGroupsByDestId(Long destId) {
        // 根据目的地id查询所有分类
        List<StrategyCatalog> catalogList = strategyCatalogService.findListByDestId(destId);
        // 遍历分类，查询每个分类下的攻略
        for (StrategyCatalog catalog : catalogList) {
            List<Strategy> strategies = super.list(new QueryWrapper<Strategy>().lambda()
                    .eq(Strategy::getCatalogId, catalog.getId())
                    .eq(Strategy::getState, Strategy.STATE_PUBLISH)
                    .orderByDesc(Strategy::getViewnum)
                    .last("limit 4"));
            catalog.setStrategies(strategies);
        }
        return catalogList;
    }

    @Override
    public StrategyContent findContent(Long id) {
        return strategyContentService.getById(id) != null ? strategyContentService.getById(id) : new StrategyContent();
    }

    @Override
    public List<Strategy> findViewingTop3(Long destId) {
        List<Strategy> strategies = super.list(new QueryWrapper<Strategy>().lambda()
                .eq(Strategy::getDestId, destId)
                .eq(Strategy::getState, Strategy.STATE_PUBLISH)
                .orderByDesc(Strategy::getViewnum)
                .last("limit 3"));
        return strategies;
    }

    @Override
    public List<StrategyTheme> getThemesByDestId(Long destId) {
        List<Strategy> strategies = list(new QueryWrapper<Strategy>()
                .select("DISTINCT theme_id ,theme_name")
                .lambda()
                .eq(Strategy::getDestId, destId)
                .eq(Strategy::getState, Strategy.STATE_PUBLISH)
                .orderByDesc(Strategy::getViewnum));
        return strategies.stream().map(strategy -> {
            StrategyTheme theme = new StrategyTheme();
            theme.setId(strategy.getThemeId());
            theme.setName(strategy.getThemeName());
            return theme;
        }).collect(Collectors.toList());
    }

    @Override
    @Cacheable( key =" 'query:' + #qo.orderBy + ':' + #qo.refid + ':' + #qo.type + ':' + #qo.current + ':' + #qo.size")
    public Page<Strategy> queryStrategy(StrategyQuery qo) {
        Page<Strategy> strategyPage = new Page<>(qo.getCurrent(), qo.getSize());
        QueryWrapper<Strategy> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(qo.getDestId() != null, Strategy::getDestId, qo.getDestId())
                .eq(qo.getThemeId() != null, Strategy::getThemeId, qo.getThemeId())
                .eq(Strategy::getState, Strategy.STATE_PUBLISH)
                .orderByDesc(Strategy::getViewnum);
        return super.page(strategyPage, wrapper);
    }

    @Override
    @Cacheable(key = "'rangs'")
    public JSONObject findRangs() {

        JSONObject json = new JSONObject();
        CompletableFuture<Void> abroadFuture = CompletableFuture.runAsync(() -> {
            QueryWrapper<Strategy> wrapper = new QueryWrapper<>();
            // 海外排行
            List<Strategy> abroad = list(wrapper.lambda()
                    .select(Strategy::getId, Strategy::getTitle,Strategy::getDestId, Strategy::getDestName)
                    .eq(Strategy::getIsabroad, Strategy.ABROAD_YES)
                    .eq(Strategy::getState, Strategy.STATE_PUBLISH)
                    .last("order by thumbsupnum desc , favornum desc limit 10"));
            wrapper.clear();
            json.put("abroadRank", abroad);
        });

        CompletableFuture<Void> chinaFuture = CompletableFuture.runAsync(() -> {
            QueryWrapper<Strategy> wrapper = new QueryWrapper<>();
            // 国内排行
            List<Strategy> china = list(wrapper.lambda()
                    .select(Strategy::getId, Strategy::getTitle,Strategy::getDestId, Strategy::getDestName)
                    .eq(Strategy::getIsabroad, Strategy.ABROAD_NO)
                    .eq(Strategy::getState, Strategy.STATE_PUBLISH)
                    .last("order by thumbsupnum desc , favornum desc limit 10"));
            json.put("chinaRank", china);
        });

        CompletableFuture<Void> hotFuture = CompletableFuture.runAsync(() -> {
            QueryWrapper<Strategy> wrapper = new QueryWrapper<>();
            // 热门排行
            List<Strategy> hot = list(wrapper.lambda()
                    .select(Strategy::getId, Strategy::getTitle,Strategy::getDestId, Strategy::getDestName)
                    .eq(Strategy::getState, Strategy.STATE_PUBLISH)
                    .last("order by viewnum desc , replynum desc limit 10"));
            json.put("hotRank", hot);
        });

        try {
            CompletableFuture.allOf(abroadFuture, chinaFuture, hotFuture).get();
        } catch (Exception e) {
            log.error("查询排行出错", e);
            throw new RuntimeException(e);
        }
        return json;
    }

    @Override
    @Cacheable(key = "'conditions'")
    public JSONObject findConditions() {
        JSONObject json = new JSONObject();
        CompletableFuture<Void> abroadFuture = CompletableFuture.runAsync(() -> {
            List<Map<String, Object>> abroadConditions = getBaseMapper().selectAbroadConditions();
            json.put("abroadCondition", abroadConditions);
        });
        CompletableFuture<Void> themeFuture = CompletableFuture.runAsync(() -> {
            List<Map<String, Object>> themeConditions = getBaseMapper().selectThemeConditions();
            json.put("themeCondition", themeConditions);
        });
        CompletableFuture<Void> chinaFuture = CompletableFuture.runAsync(() -> {
            List<Map<String, Object>> chinaConditions = getBaseMapper().selectChinaConditions();
            json.put("chinaCondition", chinaConditions);
        });
        try {
            CompletableFuture.allOf(abroadFuture, themeFuture, chinaFuture).get();
        } catch (Exception e) {
            log.error("查询条件出错", e);
            throw new RuntimeException(e);
        }
        return json;
    }


    @Override
    @Cacheable( key =" 'list:' + #qo.orderBy + ':' + #qo.refid + ':' + #qo.type + ':' + #qo.current + ':' + #qo.size")
    public Page<Strategy> queryList(StrategyQuery qo) {
        return getBaseMapper().selectListByQuery(new Page<>(qo.getCurrent(), qo.getSize()), qo);
    }

    @Override
    public List<Map<String, Object>> findStatByLastSevenDay(StatDataQuery statQuery) {
        LambdaQueryWrapper<Strategy> lambda = new QueryWrapper<Strategy>().lambda();
        lambda.select(Strategy::getId,Strategy::getViewnum,Strategy::getReplynum,Strategy::getFavornum,Strategy::getSharenum,Strategy::getThumbsupnum);
        lambda.gt(Strategy::getLastUpdateTime, statQuery.getDate());
        lambda.last("limit " + statQuery.getStart() + ", " + statQuery.getSize());
        List<Map<String, Object>> maps = listMaps(lambda);
        return maps;
    }


    @Override
    public Strategy getById(Serializable id) {
        Strategy strategy = super.getById(id);
        StrategyContent content = strategyContentService.getById(strategy.getId());
        strategy.setContent(content);
        return strategy;
    }

    @Override
    @CacheEvict(allEntries = true)
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

        transactionTemplate.execute(transactionStatus -> {
            // 9．保存攻略对象
            super.save(entity);
            //10．将攻略自增id设置到攻略内容对象
            entity.getContent().setId(entity.getId());
            // 11．保存攻略内容
            return strategyContentService.save(entity.getContent());
        });

      return true;

    }

    private void handleEntity(Strategy entity) {
        // 1．处理Url
        if (entity.getCoverUrl() != null) {
            String fileName = UUID.randomUUID().toString().replace("-", "").substring(0, 10);
            String images = OssUtil.uploadImgByBase64("images", fileName, entity.getCoverUrl());
            entity.setCoverUrl("https://"+images);
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
    @CacheEvict(allEntries = true)
    public boolean updateById(Strategy entity) {
        handleEntity(entity);
        super.updateById(entity);
        //10．将攻略自增id设置到攻略内容对象
        entity.getContent().setId(entity.getId());
        // 11．保存攻略内容
        return strategyContentService.updateById(entity.getContent());
    }

    @Override
    @CacheEvict(allEntries = true)
    public boolean removeById(Serializable id) {
        strategyContentService.removeById(id);
        return super.removeById(id);
    }
}




