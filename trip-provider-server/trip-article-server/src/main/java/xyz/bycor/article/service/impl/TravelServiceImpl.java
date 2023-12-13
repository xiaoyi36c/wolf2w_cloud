package xyz.bycor.article.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import xyz.bycor.article.domain.Travel;
import xyz.bycor.article.domain.TravelContent;
import xyz.bycor.article.feign.UserInfoFeignService;
import xyz.bycor.article.mapper.TravelMapper;
import xyz.bycor.article.qo.TravelQuery;
import xyz.bycor.article.service.TravelContentService;
import xyz.bycor.article.service.TravelService;
import xyz.bycor.article.utils.RangParam;
import xyz.bycor.core.exception.BusinessException;
import xyz.bycor.core.utils.JwtUtils;
import xyz.bycor.member.dto.UserinfoDTO;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

/**
 * @author BAI
 * @description 针对表【travel】的数据库操作Service实现
 * @createDate 2023-12-12 16:40:25
 */
@Service
public class TravelServiceImpl extends ServiceImpl<TravelMapper, Travel>
        implements TravelService {

    private final UserInfoFeignService userInfoFeignService;

    public TravelServiceImpl(UserInfoFeignService userInfoFeignService, TravelContentService travelContentService) {
        this.userInfoFeignService = userInfoFeignService;
        this.travelContentService = travelContentService;
    }

    private final TravelContentService travelContentService;


    @Override
    public Page<Travel> findTravelByDestId(TravelQuery travelQuery) {
        QueryWrapper<Travel> wrapper = new QueryWrapper<>();
        String phone = JwtUtils.getSubject();

        // 如果登录了 查询登录用户的游记
        if (!StringUtils.isEmpty(phone)) {
            // feign 发送的请求
            UserinfoDTO userinfo = userInfoFeignService.findByPhone(phone);
            // 登录用户
            wrapper.nested(w ->
                    w.lambda().eq(userinfo.getId() != null, Travel::getAuthorId, userinfo.getId())
                            .eq(travelQuery.getDestId() != null, Travel::getDestId, travelQuery.getDestId())
            );

            // 时间范围
            RangParam travelTime = travelQuery.getTravelTime();
            if (travelTime != null) {
                wrapper.between("MONTH(create_time)", travelTime.getMin(), travelTime.getMax());
            }
            // 人均消费范围
            RangParam travelConsume = travelQuery.getTravelConsume();
            if (travelConsume != null) {
                wrapper.lambda().between(Travel::getAvgConsume, travelConsume.getMin(), travelConsume.getMax());
            }
            // 出行天数范围
            RangParam travelDays = travelQuery.getTravelDays();
            if (travelDays != null) {
                wrapper.lambda().between(Travel::getDay, travelDays.getMin(), travelDays.getMax());
            }
        }
        //  公开 和 审核通过 的游记
        wrapper.or(w ->
                // 公开 和 审核通过
                w.lambda().eq(Travel::getIspublic, Travel.ISPUBLIC_YES)
                        .eq(Travel::getState, Travel.STATE_RELEASE)
                        // 目的地下
                        .eq(travelQuery.getDestId() != null, Travel::getDestId, travelQuery.getDestId())
        );

        // 时间范围
        RangParam travelTime = travelQuery.getTravelTime();
        if (travelTime != null) {
            wrapper.between("MONTH(create_time)", travelTime.getMin(), travelTime.getMax());
        }
        // 人均消费范围
        RangParam travelConsume = travelQuery.getTravelConsume();
        if (travelConsume != null) {
            wrapper.lambda().between(Travel::getAvgConsume, travelConsume.getMin(), travelConsume.getMax());
        }
        // 出行天数范围
        RangParam travelDays = travelQuery.getTravelDays();
        if (travelDays != null) {
            wrapper.lambda().between(Travel::getDay, travelDays.getMin(), travelDays.getMax());
        }
        // 排序
        wrapper.orderByDesc(StringUtils.isEmpty(travelQuery.getOrderBy()), travelQuery.getOrderBy());


        Page<Travel> page = super.page(new Page<>(travelQuery.getCurrent(), travelQuery.getSize()), wrapper);
        List<Travel> records = page.getRecords();

        // 获取每篇游记的作者信息
        Set<Long> authorIds = records.stream().map(Travel::getAuthorId).collect(Collectors.toSet());


        // 如果没有作者信息 直接返回
        if (authorIds.isEmpty()) {
            return page;
        }

        List<UserinfoDTO> userinfoDTOS = userInfoFeignService.findByIds(authorIds);

        // 将作者信息设置到游记中
        records.forEach(travel -> {
            userinfoDTOS.forEach(userinfoDTO -> {
                if (travel.getAuthorId().equals(userinfoDTO.getId())) {
                    travel.setAuthor(userinfoDTO);
                }
            });
        });

        return page;
    }

    @Override
    public List<Travel> findTravelTop3(Long destId) {
        // 查询目的地下的游记
        QueryWrapper<Travel> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(Travel::getDestId, destId)
                .eq(Travel::getIspublic, Travel.ISPUBLIC_YES)
                .eq(Travel::getState, Travel.STATE_RELEASE)
                .orderByDesc(Travel::getViewnum)
                .last("limit 3");
        return super.list(wrapper);
    }

    @Override
    public Travel findById(Long id) {
        //查询游记
        CompletableFuture<Travel> travelFuture = CompletableFuture.supplyAsync(() -> super.getById(id)).whenComplete((travel, throwable) -> {
            // 查询作者信息
            Set<Long> ids = new HashSet<>(Collections.singletonList(travel.getAuthorId()));
            userInfoFeignService.findByIds(ids).forEach(userinfoDTO -> {
                if (travel.getAuthorId().equals(userinfoDTO.getId())) {
                    travel.setAuthor(userinfoDTO);
                }
            });
        });


        // 查询游记内容
        CompletableFuture<TravelContent> contentFuture = CompletableFuture.supplyAsync(() -> travelContentService.getById(id));

        CompletableFuture.allOf(travelFuture, contentFuture).whenCompleteAsync((v, t) -> {
            Travel travel = travelFuture.join();
            travel.setContentHtml(contentFuture.join());
        });
        try {
            return travelFuture.get();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    @Transactional
    public boolean saveOrUpdate(Travel entity) {
        // 判断是否有id
        if (entity.getId() == null) {
            // 新增的逻辑
            // 获取 登录用户的信息
            String phone = JwtUtils.getSubject();
            UserinfoDTO userinfo = userInfoFeignService.findByPhone(phone);
            // 设置作者信息
            entity.setAuthorId(userinfo.getId());
            // 设置保存状态
            if (entity.getIspublic() == null) {
                entity.setIspublic(Travel.ISPUBLIC_NO);
            }
            // 设置 审核状态
            if (entity.getState() == null || entity.getState() == Travel.STATE_NORMAL) {
                entity.setState(Travel.STATE_NORMAL);
            } else {
                entity.setReleaseTime(new Date());
                entity.setState(Travel.STATE_WAITING);
            }
            //初始化数据
            entity.setViewnum(0);
            entity.setReplynum(0);
            entity.setFavornum(0);
            entity.setSharenum(0);
            entity.setThumbsupnum(0);
            // 设置创建时间
            entity.setCreateTime(new Date());
            entity.setLastUpdateTime(new Date());
            // 新增游记
            super.save(entity);
            // 新增游记内容
            TravelContent content = new TravelContent(entity.getId(), entity.getContent());
            return travelContentService.saveOrUpdate(content);
        } else {
            // 修改的逻辑
            Travel byId = getById(entity.getId());
            try {
                Assert.notNull(byId, "游记不存在");
            } catch (Exception e) {
                throw new BusinessException(404, "访问的游记不存在");
            }
            // 获取 登录用户的信息
            String phone = JwtUtils.getSubject();
            UserinfoDTO userinfo = userInfoFeignService.findByPhone(phone);
            if (!entity.getAuthorId().equals(userinfo.getId())) {
                throw new BusinessException(403, "不能修改别人的游记");
            }
            // 判断是否私密
            if (entity.getState() == null || entity.getState() == Travel.STATE_NORMAL) {
                // 如果是私密的 状态改为 草稿
                entity.setState(Travel.STATE_NORMAL);
            } else {
                // 设置最新更新时间
                entity.setLastUpdateTime(new Date());
                // 待发布
                entity.setState(Travel.STATE_WAITING);
            }
            // 修改游记
            super.updateById(entity);
            // 修改游记内容
            TravelContent content = new TravelContent(entity.getId(), entity.getContent());
            return travelContentService.saveOrUpdate(content);
        }
    }
}




