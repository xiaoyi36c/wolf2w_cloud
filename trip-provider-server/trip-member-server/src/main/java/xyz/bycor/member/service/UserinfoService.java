package xyz.bycor.member.service;



import com.baomidou.mybatisplus.extension.service.IService;
import xyz.bycor.core.response.R;
import xyz.bycor.member.domain.Userinfo;
import xyz.bycor.member.vo.LoginRequestUserinfo;
import xyz.bycor.member.vo.LoginResponse;
import xyz.bycor.member.vo.RegisterRequestUserinfo;

/**
* @author BAI
* @description 针对表【userinfo】的数据库操作Service
* @createDate 2023-12-02 15:57:08
*/
public interface UserinfoService extends IService<Userinfo> {

    /**
     *
     * @param phone
     * @return
     */
    Userinfo getPhone(String phone);

    void doRegister(RegisterRequestUserinfo userinfo);

    R<LoginResponse> doLogin(LoginRequestUserinfo userinfo);
}
