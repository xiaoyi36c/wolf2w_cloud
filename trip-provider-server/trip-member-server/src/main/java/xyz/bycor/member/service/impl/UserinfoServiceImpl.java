package xyz.bycor.member.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import xyz.bycor.member.domain.Userinfo;
import xyz.bycor.member.mapper.UserinfoMapper;
import xyz.bycor.member.service.UserinfoService;

/**
* @author BAI
* @description 针对表【userinfo】的数据库操作Service实现
* @createDate 2023-12-02 15:57:08
*/
@Service
public class UserinfoServiceImpl extends ServiceImpl<UserinfoMapper, Userinfo>
    implements UserinfoService {

    @Override
    public Userinfo getPhone(String phone) {
        return super.getOne(new QueryWrapper<Userinfo>().lambda().eq(Userinfo::getPhone, phone));
    }
}




