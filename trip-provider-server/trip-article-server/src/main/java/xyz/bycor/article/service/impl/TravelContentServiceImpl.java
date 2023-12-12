package xyz.bycor.article.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import xyz.bycor.article.domain.TravelContent;
import xyz.bycor.article.service.TravelContentService;
import xyz.bycor.article.mapper.TravelContentMapper;
import org.springframework.stereotype.Service;

/**
* @author BAI
* @description 针对表【travel_content】的数据库操作Service实现
* @createDate 2023-12-12 16:40:25
*/
@Service
public class TravelContentServiceImpl extends ServiceImpl<TravelContentMapper, TravelContent>
    implements TravelContentService{

}




