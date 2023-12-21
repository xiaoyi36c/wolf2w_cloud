package xyz.bycor.search.service.impl;

import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.stereotype.Service;
import xyz.bycor.member.dto.UserinfoDTO;
import xyz.bycor.search.service.UserInfoESService;

/**
 * 用户信息搜索
 */
@Service
public class UserInfoESServiceImpl extends SearchServiceImpl<UserinfoDTO>  implements UserInfoESService {

    public UserInfoESServiceImpl(ElasticsearchRestTemplate template) {
        super(template);
    }
}
