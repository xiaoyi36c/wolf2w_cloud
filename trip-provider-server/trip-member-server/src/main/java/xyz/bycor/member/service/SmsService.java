package xyz.bycor.member.service;

import com.baomidou.mybatisplus.extension.service.IService;
import xyz.bycor.member.domain.Userinfo;

public interface SmsService {
    void registerSend(String phone);
}
