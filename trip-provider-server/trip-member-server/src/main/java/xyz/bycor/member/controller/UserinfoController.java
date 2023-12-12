package xyz.bycor.member.controller;


import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import xyz.bycor.core.response.R;
import xyz.bycor.member.domain.Userinfo;
import xyz.bycor.member.service.UserinfoService;
import xyz.bycor.member.vo.LoginRequestUserinfo;
import xyz.bycor.member.vo.LoginResponse;
import xyz.bycor.member.vo.RegisterRequestUserinfo;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author baishuai
 * @data: 2023-11-27 18:53:23
 * @Description: 针对表【userinfo】的数据库操作Controller
 *
 */

@RestController
@RequestMapping("/userinfo")
@Api(tags = "用户信息接口")
@Slf4j
@Validated
@RefreshScope
public class UserinfoController {

    private final UserinfoService userinfoService;

    public UserinfoController(UserinfoService userinfoService) {
        this.userinfoService = userinfoService;
    }

    /**
     * 查询所有用户信息
     * @return
     */

    @ApiOperation(value = "查询所有用户信息")
    @GetMapping("/list")
    public R<List<Userinfo>> list() {
        log.info("查询所有用户信息");
        return R.ok(userinfoService.list());
    }

    /**
     * 分页查询所有用户信息
     * @return 分页对象
     */
    @ApiOperation(value = "分页查询所有用户信息")
    @GetMapping("/listPage/{page}/{size}")
    public R<Page<Userinfo>> listPage(@PathVariable("page") Integer page, @PathVariable("size") Integer size) {
        log.info("查询所有用户信息");
        Page<Userinfo> userinfoPage = new Page<>(page, size);
        return R.ok(userinfoService.page(userinfoPage));
    }

    /**
     * 根据id查询用户信息
     * @param id
     * @return
     */
    @ApiOperation(value = "根据id查询用户信息")
    @GetMapping("/getById/{id}")
    public R<Userinfo> get(@PathVariable("id") Long id) {
        log.info("根据id查询用户信息");
        return R.ok(userinfoService.getById(id));
    }


    /**
     * 根据id删除用户信息
     * @param id
     * @return
     */
    @ApiOperation(value = "根据id删除用户信息")
    @DeleteMapping("/deleteById/{id}")
    public R delete(@PathVariable("id") Long id) {
        log.info("根据id删除用户信息");
        userinfoService.removeById(id);
        return R.ok();
    }


    /**
     * 根据id更新用户信息
     * @param id
     * @param userinfo
     * @return
     */
    @ApiOperation(value = "根据id更新用户信息")
    @PutMapping("/{id}")
    public R update(@PathVariable("id") Long id, @RequestBody Userinfo userinfo) {
        Userinfo byId = userinfoService.getById(id);
        log.info("根据id更新用户信息");
        userinfo.setId(id);
        userinfoService.updateById(userinfo);
        return  R.ok();
    }

    /**
     * 新增用户信息
     * @param userinfo
     * @return
     */
    @ApiOperation(value = "新增用户信息")
    @PostMapping
    public R save(@RequestBody Userinfo userinfo) {
        log.info("新增用户信息");
        userinfoService.save(userinfo);
        return R.ok();
    }

    /**
     * 根据手机号查询用户信息
     * @param phone
     * @return true 存在 false 不存在
     * 请求方法: GET
     */
    @GetMapping("/phone/exists")
    @ApiOperation(value = "根据手机号查询用户信息")
    public R<Boolean> exists(String phone) {
        log.info("根据手机号查询用户信息");
        Userinfo userinfo = userinfoService.getPhone(phone);
        return R.ok(userinfo != null);
    }


    /**
     *  注册用户
     * @param userinfo
     * @return R<>
     * 请求方法: POST
     * 请求参数: phone nickname password verifyCode
     */

    /**
     * Po => persistent object 持久化对象 => entity 实体 ,从数据库中查询出来的对象
     * Vo => view object 值对象 => 封装数据库中查询出来的对象,超出数据库字段的属性
     * Dto => data transfer object 数据传输对象 => 封装请求参数 ,用户服务内部传递的对象
     * Bo => business object 业务对象 => 封装业务逻辑的对象,针对特殊业务逻辑的对象
     */

    @PostMapping("/register")
    @ApiOperation(value = "注册用户")
    public R<?> doRegister(@Validated RegisterRequestUserinfo userinfo) {
        log.info("注册用户");
        userinfoService.doRegister(userinfo);
        return R.ok();
    }


    /**
     *  根据手机号登录
     * @param userinfo
     * @return R<>
     * 请求方法: Post
     * 请求参数: phone
     *         password
     * 请求地址: /userinfo/login
     */
    @PostMapping("/login")
    @ApiOperation(value = "根据手机号登录")
    public R<LoginResponse> doLogin(@Validated LoginRequestUserinfo userinfo) {
        log.info("根据手机号登录");
        return userinfoService.doLogin(userinfo);
    }

    @PostMapping("/refreshToken")
    @ApiOperation(value = "刷新token")
    public R<JSONObject> refreshToken(@NotNull(message = "登录过期") String refreshToken) {
        log.info("刷新token");
        return userinfoService.refreshToken(refreshToken);
    }

    @GetMapping("/findByPhone")
    @ApiOperation(value = "根据手机号查询用户信息")
    public Userinfo findByPhone(String phone) {
        log.info("根据手机号查询用户信息");
        return userinfoService.getPhone(phone);
    }
}
