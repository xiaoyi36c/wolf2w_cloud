package xyz.bycor.article.controller.backend;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import xyz.bycor.article.domain.Destination;
import xyz.bycor.article.service.DestinationService;
import xyz.bycor.core.response.R;

import java.util.List;

/**
 * @author baishuai
 * @data: 2023-11-27 18:53:23
 * @Description: 针对表【destination】的数据库操作Controller
 *
 */

@RestController
@RequestMapping("/destination")
@Api(tags = "目地址接口")
@Slf4j
@Validated
@RefreshScope
public class DestinationBackendController {

    private final DestinationService destinationService;

    public DestinationBackendController(DestinationService destinationService) {
        this.destinationService = destinationService;
    }

    /**
     * 查询所有目地址
     * @return
     */

    @ApiOperation(value = "查询所有目地址")
    @GetMapping("/list")
    public R<List<Destination>> list() {
        log.info("查询所有目地址");
        return R.ok(destinationService.list());
    }

    /**
     * 分页查询所有目地址
     * @return 分页对象
     */
    @ApiOperation(value = "分页查询所有目地址")
    @GetMapping("/listPage/{page}/{size}")
    public R<Page<Destination>> listPage(@PathVariable("page") Integer page, @PathVariable("size") Integer size) {
        log.info("查询所有目地址");
        Page<Destination> destinationPage = new Page<>(page, size);
        return R.ok(destinationService.page(destinationPage));
    }

    /**
     * 根据id查询目地址
     * @param id
     * @return
     */
    @ApiOperation(value = "根据id查询目地址")
    @GetMapping("/getById/{id}")
    public R<Destination> get(@PathVariable("id") Long id) {
        log.info("根据id查询目地址");
        return R.ok(destinationService.getById(id));
    }


    /**
     * 根据id删除目地址
     * @param id
     * @return
     */
    @ApiOperation(value = "根据id删除目地址")
    @DeleteMapping("/deleteById/{id}")
    public R delete(@PathVariable("id") Long id) {
        log.info("根据id删除目地址");
        destinationService.removeById(id);
        return R.ok();
    }


    /**
     * 根据id更新目地址
     * @param id
     * @param destination
     * @return
     */
    @ApiOperation(value = "根据id更新目地址")
    @PutMapping("/{id}")
    public R update(@PathVariable("id") Long id, @RequestBody Destination destination) {
        Destination byId = destinationService.getById(id);
        log.info("根据id更新目地址");
        destination.setId(id);
        destinationService.updateById(destination);
        return  R.ok();
    }

    /**
     * 新增目地址
     * @param destination
     * @return
     */
    @ApiOperation(value = "新增目地址")
    @PostMapping
    public R save(@RequestBody Destination destination) {
        log.info("新增目地址");
        destinationService.save(destination);
        return R.ok();
    }

}
