package xyz.bycor.article.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import xyz.bycor.article.domain.Travel;
import xyz.bycor.article.feign.UserInfoFeignService;
import xyz.bycor.article.mapper.TravelMapper;
import xyz.bycor.article.qo.TravelQuery;
import xyz.bycor.article.service.TravelService;
import xyz.bycor.core.utils.JwtUtils;
import xyz.bycor.member.dto.UserinfoDTO;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
* @author BAI
* @description 针对表【travel】的数据库操作Service实现
* @createDate 2023-12-12 16:40:25
*/
@Service
public class TravelServiceImpl extends ServiceImpl<TravelMapper, Travel>
    implements TravelService{

    private final UserInfoFeignService userInfoFeignService;

    public TravelServiceImpl(UserInfoFeignService userInfoFeignService) {
        this.userInfoFeignService = userInfoFeignService;
    }


    @Override
    public Page<Travel> findTravelByDestId(TravelQuery travelQuery) {
        QueryWrapper<Travel> wrapper = new QueryWrapper<>();
        String phone = JwtUtils.getSubject();


        // 如果登录了 查询登录用户的游记
        if (!StringUtils.isEmpty(phone)) {
            // feign 发送的请求
            UserinfoDTO userinfo = userInfoFeignService.findByPhone(phone);
            // 登录用户
            wrapper.lambda().eq(userinfo.getId() !=null, Travel::getAuthorId,userinfo.getId());
        }



        //  公开 和 审核通过 的游记
        wrapper.lambda().or( w ->
                // 公开 和 审核通过
                w.eq(Travel::getIspublic,Travel.ISPUBLIC_YES)
                  .eq(Travel::getState,Travel.STATE_RELEASE)
        );

        // 目的地下
        wrapper.lambda().eq(travelQuery.getDestId() !=null, Travel::getDestId,travelQuery.getDestId());



        Page<Travel> page = super.page(new Page<>(travelQuery.getCurrent(), travelQuery.getSize()), wrapper);
        List<Travel> records = page.getRecords();

        // 获取每篇游记的作者信息
        Set<Long> authorIds = records.stream().map(Travel::getAuthorId).collect(Collectors.toSet());
        List<UserinfoDTO> userinfoDTOS = userInfoFeignService.findByIds(authorIds);

        // 将作者信息设置到游记中
        records.forEach(travel -> {
            userinfoDTOS.forEach(userinfoDTO -> {
                if (travel.getAuthorId().equals(userinfoDTO.getId())) {
                    travel.setAuthor(userinfoDTO);
                }
            });
        });

        return  page;
    }
}




