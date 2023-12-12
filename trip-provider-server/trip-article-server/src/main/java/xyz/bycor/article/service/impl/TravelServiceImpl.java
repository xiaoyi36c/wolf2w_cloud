package xyz.bycor.article.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import xyz.bycor.article.domain.Travel;
import xyz.bycor.article.service.TravelService;
import xyz.bycor.article.mapper.TravelMapper;
import org.springframework.stereotype.Service;

/**
* @author BAI
* @description 针对表【travel】的数据库操作Service实现
* @createDate 2023-12-12 16:40:25
*/
@Service
public class TravelServiceImpl extends ServiceImpl<TravelMapper, Travel>
    implements TravelService{

}




