package xyz.bycor.member.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import xyz.bycor.member.domain.Userinfo;
import xyz.bycor.member.service.UserinfoService;
import xyz.bycor.core.response.R;


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
}
